package com.avaroom.app.data.remote;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.avaroom.app.BuildConfig;
import com.avaroom.app.data.model.response.MessageResponse;
import com.avaroom.app.data.model.response.ReadResponse;
import com.avaroom.app.utils.TokenManager;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Quản lý kết nối Socket.IO Client tập trung cho module Student Chat của ứng dụng Android Avaroom.
 * Tuân thủ đúng kiến trúc realtime backend và hợp đồng sự kiện Task 5.2.
 */
public class ChatSocketManager {

    private static final String TAG = "ChatSocketManager";
    private static final long ACK_TIMEOUT_MS = 10000L;

    // Trạng thái kết nối Socket
    public enum ConnectionState {
        DISCONNECTED,
        CONNECTING,
        CONNECTED,
        RECONNECTING,
        ERROR
    }

    // Callback chung cho các tác vụ emit qua Socket có xác nhận (Ack)
    public interface ChatCallback<T> {
        void onSuccess(T data);
        void onError(String code, String message);
    }

    // Interface lắng nghe các sự kiện Chat từ Socket.IO
    public interface ChatEventListener {
        void onConnectionStateChanged(ConnectionState state);
        void onNewMessage(MessageResponse message);
        void onConversationUpdated(long conversationId, MessageResponse lastMessage);
        void onMessagesRead(long conversationId, long readBy, String readAt);
        void onUnreadCountUpdated(long conversationId, long readBy, int updatedCount);
        void onError(String code, String message);
    }

    // Adapter rỗng hỗ trợ đăng ký lắng nghe có chọn lọc
    public static class SimpleChatEventListener implements ChatEventListener {
        @Override public void onConnectionStateChanged(ConnectionState state) {}
        @Override public void onNewMessage(MessageResponse message) {}
        @Override public void onConversationUpdated(long conversationId, MessageResponse lastMessage) {}
        @Override public void onMessagesRead(long conversationId, long readBy, String readAt) {}
        @Override public void onUnreadCountUpdated(long conversationId, long readBy, int updatedCount) {}
        @Override public void onError(String code, String message) {}
    }

    // Singleton instance
    private static volatile ChatSocketManager instance;

    // Biến quản lý trạng thái nội bộ
    private Socket mSocket;
    private Context appContext;
    private final Gson gson = new Gson();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final List<ChatEventListener> listeners = new CopyOnWriteArrayList<>();
    private final List<Runnable> pendingDisconnectCallbacks = new CopyOnWriteArrayList<>();

    private volatile ConnectionState connectionState = ConnectionState.DISCONNECTED;
    private volatile long activeConversationId = -1L;
    private volatile String connectedToken = null;

    private ChatSocketManager() {
        // Private constructor cho Singleton
    }

    public static ChatSocketManager getInstance() {
        if (instance == null) {
            synchronized (ChatSocketManager.class) {
                if (instance == null) {
                    instance = new ChatSocketManager();
                }
            }
        }
        return instance;
    }

    // --- QUẢN LÝ LISTENER ---

    public void addListener(@NonNull ChatEventListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(@NonNull ChatEventListener listener) {
        listeners.remove(listener);
    }

    // --- TRẠNG THÁI KẾT NỐI ---

    public ConnectionState getConnectionState() {
        return connectionState;
    }

    public boolean isConnected() {
        return mSocket != null && mSocket.connected() && connectionState == ConnectionState.CONNECTED;
    }

    public long getActiveConversationId() {
        return activeConversationId;
    }

    public void setActiveConversationId(long conversationId) {
        this.activeConversationId = conversationId;
    }

    private synchronized void setConnectionState(ConnectionState newState) {
        if (this.connectionState != newState) {
            this.connectionState = newState;
            mainHandler.post(() -> {
                for (ChatEventListener listener : listeners) {
                    try {
                        listener.onConnectionStateChanged(newState);
                    } catch (Exception e) {
                        Log.e(TAG, "Error dispatching connection state change", e);
                    }
                }
            });
        }
    }

    private void notifyError(String code, String message) {
        mainHandler.post(() -> {
            for (ChatEventListener listener : listeners) {
                try {
                    listener.onError(code, message);
                } catch (Exception e) {
                    Log.e(TAG, "Error dispatching error event", e);
                }
            }
        });
    }

    // --- VÒNG ĐỜI KẾT NỐI (CONNECTION LIFECYCLE) ---

    /**
     * Khởi tạo kết nối Socket.IO với JWT access token từ TokenManager
     * @param context Context ứng dụng (sẽ tự lấy getApplicationContext để chống rò rỉ bộ nhớ)
     */
    public synchronized void connect(@NonNull Context context) {
        this.appContext = context.getApplicationContext();

        // Lấy access token từ TokenManager
        String token = TokenManager.getAccessToken(appContext);
        if (token == null || token.trim().isEmpty()) {
            Log.e(TAG, "Cannot connect Socket: Missing JWT access token");
            connectedToken = null;
            cleanupSocket();
            setConnectionState(ConnectionState.ERROR);
            notifyError("UNAUTHORIZED", "Vui lòng đăng nhập để sử dụng tính năng Chat");
            return;
        }

        // Nếu đã kết nối hoặc đang kết nối/tái kết nối với cùng token, không tạo mới
        boolean sameToken = token.equals(connectedToken);
        if (sameToken && mSocket != null && (mSocket.connected()
                || connectionState == ConnectionState.CONNECTING
                || connectionState == ConnectionState.RECONNECTING)) {
            Log.d(TAG, "Socket is already connected, connecting, or reconnecting");
            return;
        }

        if (!sameToken && mSocket != null) {
            Log.d(TAG, "Access token changed; recreating Socket.IO connection");
            activeConversationId = -1L;
            cleanupSocket();
        }

        try {
            // Xác định base URL từ BuildConfig (chuẩn hóa loại bỏ dấu gạch chéo cuối nếu có)
            String baseUrl = BuildConfig.BASE_URL;
            if (baseUrl.endsWith("/")) {
                baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
            }

            // Cấu hình IO.Options tương thích với backend Socket.IO v4
            IO.Options options = new IO.Options();
            options.auth = Collections.singletonMap("token", token);
            options.transports = new String[]{"websocket", "polling"};
            options.reconnection = true;
            options.reconnectionAttempts = 5;
            options.reconnectionDelay = 1000L;
            options.reconnectionDelayMax = 5000L;
            options.timeout = 10000L;

            // Dọn dẹp socket cũ nếu còn tồn tại
            if (mSocket != null) {
                cleanupSocket();
            }

            mSocket = IO.socket(baseUrl, options);
            connectedToken = token;
            registerSocketEvents();

            setConnectionState(ConnectionState.CONNECTING);
            mSocket.connect();
            Log.d(TAG, "Initiated Socket.IO connection to " + baseUrl);

        } catch (URISyntaxException e) {
            Log.e(TAG, "Invalid server URL for Socket.IO", e);
            setConnectionState(ConnectionState.ERROR);
            notifyError("CONFIG_ERROR", "URL máy chủ không hợp lệ: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Unexpected error initializing Socket.IO", e);
            setConnectionState(ConnectionState.ERROR);
            notifyError("INIT_ERROR", "Không thể khởi tạo kết nối Socket: " + e.getMessage());
        }
    }

    /**
     * Ngắt kết nối Socket.IO và giải phóng tài nguyên lắng nghe
     */
    public synchronized void disconnect() {
        activeConversationId = -1L;
        connectedToken = null;
        cleanupSocket();
        setConnectionState(ConnectionState.DISCONNECTED);
        Log.d(TAG, "Socket disconnected and cleaned up");
    }

    private void abortPendingSendAcks() {
        for (Runnable abort : pendingDisconnectCallbacks) {
            try {
                abort.run();
            } catch (Exception e) {
                Log.e(TAG, "Error aborting pending send ACK", e);
            }
        }
        pendingDisconnectCallbacks.clear();
    }

    private void cleanupSocket() {
        abortPendingSendAcks();
        if (mSocket != null) {
            try {
                mSocket.off(Socket.EVENT_CONNECT, onConnect);
                mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
                mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
                mSocket.off("new_message", onNewMessage);
                mSocket.off("conversation_updated", onConversationUpdated);
                mSocket.off("messages_read", onMessagesRead);
                mSocket.off("unread_count_updated", onUnreadCountUpdated);

                if (mSocket.io() != null) {
                    mSocket.io().off(Manager.EVENT_RECONNECT, onReconnect);
                    mSocket.io().off(Manager.EVENT_RECONNECT_ATTEMPT, onReconnectAttempt);
                    mSocket.io().off(Manager.EVENT_RECONNECT_FAILED, onReconnectFailed);
                }

                mSocket.disconnect();
                mSocket.close();
            } catch (Exception e) {
                Log.e(TAG, "Error cleaning up socket", e);
            } finally {
                mSocket = null;
            }
        }
    }

    // --- ĐĂNG KÝ SỰ KIỆN NỘI BỘ VÀ REALTIME BACKEND ---

    private void registerSocketEvents() {
        if (mSocket == null) return;

        // Sự kiện vòng đời kết nối
        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);

        // Sự kiện Manager tái kết nối
        if (mSocket.io() != null) {
            mSocket.io().on(Manager.EVENT_RECONNECT, onReconnect);
            mSocket.io().on(Manager.EVENT_RECONNECT_ATTEMPT, onReconnectAttempt);
            mSocket.io().on(Manager.EVENT_RECONNECT_FAILED, onReconnectFailed);
        }

        // Sự kiện nghiệp vụ Chat từ Backend
        mSocket.on("new_message", onNewMessage);
        mSocket.on("conversation_updated", onConversationUpdated);
        mSocket.on("messages_read", onMessagesRead);
        mSocket.on("unread_count_updated", onUnreadCountUpdated);
    }

    // --- CÁC LISTENER NỘI BỘ XỬ LÝ SỰ KIỆN SOCKET ---

    private final Emitter.Listener onConnect = args -> {
        Log.d(TAG, "Socket connected successfully: id=" + (mSocket != null ? mSocket.id() : "null"));
        setConnectionState(ConnectionState.CONNECTED);
    };

    private final Emitter.Listener onDisconnect = args -> {
        Log.d(TAG, "Socket disconnected");
        activeConversationId = -1L;
        abortPendingSendAcks();
        setConnectionState(ConnectionState.DISCONNECTED);
    };

    private final Emitter.Listener onConnectError = args -> {
        String errorMsg = "Connect error";
        String errorCode = "CONNECT_ERROR";
        if (args != null && args.length > 0 && args[0] != null) {
            errorMsg = args[0].toString();
        }
        Log.e(TAG, "Socket connect error: " + errorMsg);
        setConnectionState(ConnectionState.ERROR);

        // Kiểm tra nếu lỗi xác thực unrecoverable (token hết hạn, không hợp lệ, không có quyền)
        if (errorMsg.contains("Authentication error") || errorMsg.contains("Authorization error")
                || errorMsg.contains("UNAUTHORIZED") || errorMsg.contains("FORBIDDEN")
                || errorMsg.contains("TOKEN_EXPIRED") || errorMsg.contains("INVALID_TOKEN")) {
            errorCode = "AUTH_ERROR";
            Log.w(TAG, "Unrecoverable authentication/authorization failure. Stopping reconnect attempts.");
            disconnect();
        }

        notifyError(errorCode, errorMsg);
    };

    private final Emitter.Listener onReconnectAttempt = args -> {
        Log.d(TAG, "Socket reconnecting attempt...");
        setConnectionState(ConnectionState.RECONNECTING);
    };

    private final Emitter.Listener onReconnect = args -> {
        Log.d(TAG, "Socket transport reconnected; awaiting Socket.EVENT_CONNECT");
    };

    private final Emitter.Listener onReconnectFailed = args -> {
        Log.e(TAG, "Socket reconnect failed after maximum attempts");
        setConnectionState(ConnectionState.ERROR);
        notifyError("RECONNECT_FAILED", "Không thể kết nối lại tới máy chủ Chat");
    };

    // Sự kiện 1: new_message (nhận tin nhắn mới trong phòng chat)
    private final Emitter.Listener onNewMessage = args -> {
        if (args != null && args.length > 0 && args[0] instanceof JSONObject) {
            try {
                JSONObject obj = (JSONObject) args[0];
                MessageResponse message = gson.fromJson(obj.toString(), MessageResponse.class);
                if (message != null && message.getId() > 0) {
                    // Backend không gửi is_mine qua Socket event -> Client tự tính toán theo userId hiện tại
                    long currentUserId = (appContext != null) ? TokenManager.getUserId(appContext) : -1L;
                    if (currentUserId > 0) {
                        message.setIsMine(message.getSenderId() == currentUserId);
                    }

                    mainHandler.post(() -> {
                        for (ChatEventListener listener : listeners) {
                            try {
                                listener.onNewMessage(message);
                            } catch (Exception e) {
                                Log.e(TAG, "Error in listener onNewMessage", e);
                            }
                        }
                    });
                }
            } catch (Exception e) {
                Log.e(TAG, "Error deserializing new_message event", e);
            }
        }
    };

    // Sự kiện 2: conversation_updated (cập nhật tin nhắn cuối trong danh sách hội thoại)
    private final Emitter.Listener onConversationUpdated = args -> {
        if (args != null && args.length > 0 && args[0] instanceof JSONObject) {
            try {
                JSONObject obj = (JSONObject) args[0];
                long conversationId = obj.optLong("conversation_id", -1L);
                JSONObject lastMsgObj = obj.optJSONObject("last_message");
                MessageResponse lastMessage = null;
                if (lastMsgObj != null) {
                    lastMessage = gson.fromJson(lastMsgObj.toString(), MessageResponse.class);
                    if (lastMessage != null) {
                        lastMessage.setConversationId(conversationId);
                        long currentUserId = (appContext != null) ? TokenManager.getUserId(appContext) : -1L;
                        if (currentUserId > 0) {
                            lastMessage.setIsMine(lastMessage.getSenderId() == currentUserId);
                        }
                    }
                }

                final MessageResponse finalLastMessage = lastMessage;
                mainHandler.post(() -> {
                    for (ChatEventListener listener : listeners) {
                        try {
                            listener.onConversationUpdated(conversationId, finalLastMessage);
                        } catch (Exception e) {
                            Log.e(TAG, "Error in listener onConversationUpdated", e);
                        }
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "Error deserializing conversation_updated event", e);
            }
        }
    };

    // Sự kiện 3: messages_read (đối phương đã đọc tin nhắn trong phòng hội thoại)
    private final Emitter.Listener onMessagesRead = args -> {
        if (args != null && args.length > 0 && args[0] instanceof JSONObject) {
            try {
                JSONObject obj = (JSONObject) args[0];
                long conversationId = obj.optLong("conversation_id", -1L);
                long readBy = obj.optLong("read_by", -1L);
                String readAt = obj.isNull("read_at") ? null : obj.optString("read_at", null);

                mainHandler.post(() -> {
                    for (ChatEventListener listener : listeners) {
                        try {
                            listener.onMessagesRead(conversationId, readBy, readAt);
                        } catch (Exception e) {
                            Log.e(TAG, "Error in listener onMessagesRead", e);
                        }
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "Error deserializing messages_read event", e);
            }
        }
    };

    // Sự kiện 4: unread_count_updated (số tin chưa đọc đã được cập nhật)
    private final Emitter.Listener onUnreadCountUpdated = args -> {
        if (args != null && args.length > 0 && args[0] instanceof JSONObject) {
            try {
                JSONObject obj = (JSONObject) args[0];
                long conversationId = obj.optLong("conversation_id", -1L);
                long readBy = obj.optLong("read_by", -1L);
                int updatedCount = obj.optInt("updated_count", 0);

                mainHandler.post(() -> {
                    for (ChatEventListener listener : listeners) {
                        try {
                            listener.onUnreadCountUpdated(conversationId, readBy, updatedCount);
                        } catch (Exception e) {
                            Log.e(TAG, "Error in listener onUnreadCountUpdated", e);
                        }
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "Error deserializing unread_count_updated event", e);
            }
        }
    };

    // --- CÁC PHƯƠNG THỨC GỬI SỰ KIỆN (EMIT CLIENT EVENTS) ---

    /**
     * Tham gia vào phòng chat của một cuộc hội thoại cụ thể
     * Payload backend: { conversation_id: Long }
     */
    public void joinConversation(long conversationId) {
        joinConversation(conversationId, null);
    }

    public void joinConversation(long conversationId, @Nullable ChatCallback<Long> callback) {
        if (conversationId <= 0) {
            if (callback != null) {
                mainHandler.post(() -> callback.onError("BAD_REQUEST", "ID cuộc trò chuyện không hợp lệ"));
            }
            return;
        }

        if (!isConnected()) {
            if (callback != null) {
                mainHandler.post(() -> callback.onError("NOT_CONNECTED", "Chưa kết nối Socket"));
            }
            return;
        }

        try {
            JSONObject payload = new JSONObject();
            payload.put("conversation_id", conversationId);

            mSocket.emit("join_conversation", new Object[]{payload}, new Ack() {
                @Override
                public void call(Object... args) {
                    if (args != null && args.length > 0 && args[0] instanceof JSONObject) {
                        JSONObject res = (JSONObject) args[0];
                        boolean success = res.optBoolean("success", false);
                        if (success) {
                            activeConversationId = conversationId;
                            if (callback != null) {
                                mainHandler.post(() -> callback.onSuccess(conversationId));
                            }
                        } else {
                            String err = res.optString("error", "UNKNOWN_ERROR");
                            String msg = res.optString("message", "Không thể tham gia phòng trò chuyện");
                            if (callback != null) {
                                mainHandler.post(() -> callback.onError(err, msg));
                            }
                        }
                    } else if (callback != null) {
                        mainHandler.post(() -> callback.onError("INVALID_RESPONSE", "Phản hồi xác nhận không hợp lệ"));
                    }
                }
            });
        } catch (JSONException e) {
            Log.e(TAG, "Error creating join_conversation payload", e);
            if (callback != null) {
                mainHandler.post(() -> callback.onError("PAYLOAD_ERROR", e.getMessage()));
            }
        }
    }

    /**
     * Rời khỏi phòng chat của một cuộc hội thoại
     * Payload backend: { conversation_id: Long }
     */
    public void leaveConversation(long conversationId) {
        leaveConversation(conversationId, null);
    }

    public void leaveConversation(long conversationId, @Nullable ChatCallback<Long> callback) {
        if (conversationId <= 0) {
            if (callback != null) {
                mainHandler.post(() -> callback.onError("BAD_REQUEST", "ID cuộc trò chuyện không hợp lệ"));
            }
            return;
        }

        if (activeConversationId == conversationId) {
            activeConversationId = -1L;
        }

        if (!isConnected()) {
            if (callback != null) {
                mainHandler.post(() -> callback.onSuccess(conversationId));
            }
            return;
        }

        try {
            JSONObject payload = new JSONObject();
            payload.put("conversation_id", conversationId);

            mSocket.emit("leave_conversation", new Object[]{payload}, new Ack() {
                @Override
                public void call(Object... args) {
                    if (callback != null) {
                        if (args != null && args.length > 0 && args[0] instanceof JSONObject) {
                            JSONObject res = (JSONObject) args[0];
                            boolean success = res.optBoolean("success", false);
                            if (success) {
                                mainHandler.post(() -> callback.onSuccess(conversationId));
                            } else {
                                String err = res.optString("error", "UNKNOWN_ERROR");
                                String msg = res.optString("message", "Lỗi khi rời phòng trò chuyện");
                                mainHandler.post(() -> callback.onError(err, msg));
                            }
                        } else {
                            mainHandler.post(() -> callback.onSuccess(conversationId));
                        }
                    }
                }
            });
        } catch (JSONException e) {
            Log.e(TAG, "Error creating leave_conversation payload", e);
            if (callback != null) {
                mainHandler.post(() -> callback.onError("PAYLOAD_ERROR", e.getMessage()));
            }
        }
    }

    /**
     * Gửi tin nhắn qua Socket.IO có xác nhận Acknowledgement (Authoritative Write Path)
     * Payload backend: { conversation_id: Long, content: String }
     */
    public void sendMessage(long conversationId, @NonNull String content) {
        sendMessage(conversationId, content, null);
    }

    public void sendMessage(long conversationId, @NonNull String content, @Nullable ChatCallback<MessageResponse> callback) {
        if (conversationId <= 0) {
            if (callback != null) {
                mainHandler.post(() -> callback.onError("BAD_REQUEST", "ID cuộc trò chuyện không hợp lệ"));
            }
            return;
        }

        if (content == null || content.trim().isEmpty()) {
            if (callback != null) {
                mainHandler.post(() -> callback.onError("BAD_REQUEST", "Nội dung tin nhắn không được để trống"));
            }
            return;
        }

        String trimmed = content.trim();
        if (trimmed.length() > 2000) {
            if (callback != null) {
                mainHandler.post(() -> callback.onError("BAD_REQUEST", "Nội dung tin nhắn không được vượt quá 2000 ký tự"));
            }
            return;
        }

        if (!isConnected()) {
            if (callback != null) {
                mainHandler.post(() -> callback.onError("NOT_CONNECTED", "Chưa kết nối Socket"));
            }
            return;
        }

        try {
            JSONObject payload = new JSONObject();
            payload.put("conversation_id", conversationId);
            payload.put("content", trimmed);

            final AtomicBoolean settled = new AtomicBoolean(false);
            final Runnable[] timeoutTaskRef = new Runnable[1];
            final Runnable[] disconnectAbortRef = new Runnable[1];

            if (callback != null) {
                timeoutTaskRef[0] = () -> {
                    if (!settled.compareAndSet(false, true)) return;
                    if (disconnectAbortRef[0] != null) {
                        pendingDisconnectCallbacks.remove(disconnectAbortRef[0]);
                    }
                    callback.onError("ACK_TIMEOUT", "Chưa nhận được xác nhận từ máy chủ. Vui lòng kiểm tra trước khi gửi lại.");
                };
                disconnectAbortRef[0] = () -> {
                    if (!settled.compareAndSet(false, true)) return;
                    if (timeoutTaskRef[0] != null) {
                        mainHandler.removeCallbacks(timeoutTaskRef[0]);
                    }
                    mainHandler.post(() -> callback.onError("ACK_UNCERTAIN", "Mất kết nối trong lúc chờ xác nhận gửi tin. Vui lòng kiểm tra trước khi gửi lại."));
                };
                pendingDisconnectCallbacks.add(disconnectAbortRef[0]);
                mainHandler.postDelayed(timeoutTaskRef[0], ACK_TIMEOUT_MS);
            }

            mSocket.emit("send_message", new Object[]{payload}, new Ack() {
                @Override
                public void call(Object... args) {
                    if (!settled.compareAndSet(false, true)) return;
                    if (timeoutTaskRef[0] != null) {
                        mainHandler.removeCallbacks(timeoutTaskRef[0]);
                    }
                    if (disconnectAbortRef[0] != null) {
                        pendingDisconnectCallbacks.remove(disconnectAbortRef[0]);
                    }

                    if (args != null && args.length > 0 && args[0] instanceof JSONObject) {
                        JSONObject res = (JSONObject) args[0];
                        boolean success = res.optBoolean("success", false);
                        if (success) {
                            JSONObject data = res.optJSONObject("data");
                            MessageResponse sentMessage = null;
                            if (data != null) {
                                sentMessage = gson.fromJson(data.toString(), MessageResponse.class);
                            }
                            if (sentMessage == null || sentMessage.getId() <= 0) {
                                if (callback != null) {
                                    mainHandler.post(() -> callback.onError("INVALID_RESPONSE", "Dữ liệu tin nhắn xác nhận không hợp lệ"));
                                }
                                return;
                            }
                            sentMessage.setIsMine(true);
                            final MessageResponse finalMessage = sentMessage;
                            if (callback != null) {
                                mainHandler.post(() -> callback.onSuccess(finalMessage));
                            }
                        } else {
                            String err = res.optString("error", "UNKNOWN_ERROR");
                            String msg = res.optString("message", "Gửi tin nhắn thất bại");
                            if (callback != null) {
                                mainHandler.post(() -> callback.onError(err, msg));
                            }
                        }
                    } else if (callback != null) {
                        mainHandler.post(() -> callback.onError("INVALID_RESPONSE", "Phản hồi xác nhận gửi tin không hợp lệ"));
                    }
                }
            });
        } catch (JSONException e) {
            Log.e(TAG, "Error creating send_message payload", e);
            if (callback != null) {
                mainHandler.post(() -> callback.onError("PAYLOAD_ERROR", e.getMessage()));
            }
        }
    }

    /**
     * Đánh dấu đã đọc các tin nhắn trong cuộc hội thoại qua Socket.IO
     * Payload backend: { conversation_id: Long }
     */
    public void markRead(long conversationId) {
        markRead(conversationId, null);
    }

    public void markRead(long conversationId, @Nullable ChatCallback<ReadResponse> callback) {
        if (conversationId <= 0) {
            if (callback != null) {
                mainHandler.post(() -> callback.onError("BAD_REQUEST", "ID cuộc trò chuyện không hợp lệ"));
            }
            return;
        }

        if (!isConnected()) {
            if (callback != null) {
                mainHandler.post(() -> callback.onError("NOT_CONNECTED", "Chưa kết nối Socket"));
            }
            return;
        }

        try {
            JSONObject payload = new JSONObject();
            payload.put("conversation_id", conversationId);

            final AtomicBoolean settled = new AtomicBoolean(false);
            final Runnable[] timeoutTaskRef = new Runnable[1];
            final Runnable[] disconnectAbortRef = new Runnable[1];

            if (callback != null) {
                timeoutTaskRef[0] = () -> {
                    if (!settled.compareAndSet(false, true)) return;
                    if (disconnectAbortRef[0] != null) {
                        pendingDisconnectCallbacks.remove(disconnectAbortRef[0]);
                    }
                    callback.onError("ACK_TIMEOUT", "Hết thời gian chờ xác nhận đánh dấu đã đọc");
                };
                disconnectAbortRef[0] = () -> {
                    if (!settled.compareAndSet(false, true)) return;
                    if (timeoutTaskRef[0] != null) {
                        mainHandler.removeCallbacks(timeoutTaskRef[0]);
                    }
                    mainHandler.post(() -> callback.onError("ACK_UNCERTAIN", "Mất kết nối khi đang chờ xác nhận đánh dấu đã đọc"));
                };
                pendingDisconnectCallbacks.add(disconnectAbortRef[0]);
                mainHandler.postDelayed(timeoutTaskRef[0], ACK_TIMEOUT_MS);
            }

            mSocket.emit("mark_read", new Object[]{payload}, new Ack() {
                @Override
                public void call(Object... args) {
                    if (!settled.compareAndSet(false, true)) return;
                    if (timeoutTaskRef[0] != null) {
                        mainHandler.removeCallbacks(timeoutTaskRef[0]);
                    }
                    if (disconnectAbortRef[0] != null) {
                        pendingDisconnectCallbacks.remove(disconnectAbortRef[0]);
                    }

                    if (args != null && args.length > 0 && args[0] instanceof JSONObject) {
                        JSONObject res = (JSONObject) args[0];
                        boolean success = res.optBoolean("success", false);
                        if (success) {
                            JSONObject data = res.optJSONObject("data");
                            ReadResponse readResponse = null;
                            if (data != null) {
                                readResponse = new ReadResponse();
                                readResponse.setConversationId(data.optLong("conversation_id", conversationId));
                                readResponse.setUpdatedCount(data.optInt("updated_count", 0));
                            }
                            final ReadResponse finalResponse = readResponse;
                            if (callback != null) {
                                mainHandler.post(() -> callback.onSuccess(finalResponse));
                            }
                        } else {
                            String err = res.optString("error", "UNKNOWN_ERROR");
                            String msg = res.optString("message", "Đánh dấu đã đọc thất bại");
                            if (callback != null) {
                                mainHandler.post(() -> callback.onError(err, msg));
                            }
                        }
                    } else if (callback != null) {
                        mainHandler.post(() -> callback.onError("INVALID_RESPONSE", "Phản hồi đánh dấu đã đọc không hợp lệ"));
                    }
                }
            });
        } catch (JSONException e) {
            Log.e(TAG, "Error creating mark_read payload", e);
            if (callback != null) {
                mainHandler.post(() -> callback.onError("PAYLOAD_ERROR", e.getMessage()));
            }
        }
    }
}
