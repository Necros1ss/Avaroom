package com.avaroom.app.ui.student;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.avaroom.app.data.model.request.SendMessageRequest;
import com.avaroom.app.data.model.response.ApiResponse;
import com.avaroom.app.data.model.response.ConversationResponse;
import com.avaroom.app.data.model.response.MessageListResponse;
import com.avaroom.app.data.model.response.MessageResponse;
import com.avaroom.app.data.model.response.ReadResponse;
import com.avaroom.app.data.remote.ChatSocketManager;
import com.avaroom.app.data.remote.RetrofitClient;
import com.avaroom.app.databinding.FragmentChatBinding;
import com.avaroom.app.utils.TokenManager;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Màn hình chi tiết cuộc trò chuyện và lịch sử tin nhắn giữa Sinh viên và Chủ trọ.
 * Tích hợp Socket.IO thời gian thực kết hợp REST API lấy lịch sử tin nhắn.
 */
public class ChatFragment extends Fragment {

    private static final String TAG = "ChatFragment";

    private FragmentChatBinding binding;
    private MessageAdapter adapter;
    private ChatSocketManager socketManager;
    private long conversationId = -1L;
    private long currentUserId = -1L;
    private boolean markReadInFlight = false;
    private boolean pendingMarkReadAfterInFlight = false;
    private boolean needsHistoryRefreshOnStart = false;
    private boolean hasConnectedOnce = false;
    private boolean wasDisconnectedWhileActive = false;

    // Listener nhận sự kiện Socket.IO thời gian thực
    private final ChatSocketManager.ChatEventListener socketListener = new ChatSocketManager.SimpleChatEventListener() {
        @Override
        public void onConnectionStateChanged(ChatSocketManager.ConnectionState state) {
            if (binding == null || !isAdded()) return;
            updateConnectionStateUI(state);

            if (state == ChatSocketManager.ConnectionState.DISCONNECTED
                    || state == ChatSocketManager.ConnectionState.RECONNECTING
                    || state == ChatSocketManager.ConnectionState.ERROR) {
                if (hasConnectedOnce) {
                    wasDisconnectedWhileActive = true;
                }
                return;
            }

            // Khi Socket kết nối thành công, tự động tham gia room của cuộc trò chuyện hiện tại
            if (state == ChatSocketManager.ConnectionState.CONNECTED && conversationId > 0) {
                hasConnectedOnce = true;
                joinCurrentConversationIfNeeded();
                if (wasDisconnectedWhileActive) {
                    wasDisconnectedWhileActive = false;
                    refreshMessageHistorySilently();
                } else {
                    markIncomingMessagesReadIfNeeded();
                }
            }
        }

        @Override
        public void onNewMessage(MessageResponse message) {
            if (binding == null || !isAdded() || message == null || message.getId() <= 0) return;

            // Chỉ xử lý tin nhắn thuộc đúng cuộc trò chuyện đang mở
            if (message.getConversationId() == conversationId) {
                boolean isMine = message.isMine();
                if (currentUserId > 0) {
                    isMine = (message.getSenderId() == currentUserId);
                    message.setIsMine(isMine);
                }

                // Thêm tin nhắn mới kèm chống trùng lặp qua message ID
                boolean isNew = adapter.addOrUpdateMessage(message);
                if (adapter.getItemCount() > 0) {
                    showContentState();
                    if (isNew) {
                        binding.rvMessages.smoothScrollToPosition(adapter.getItemCount() - 1);
                    }
                }

                // Nếu là tin nhắn đến chưa đọc từ đối phương khi đang mở màn hình -> đồng bộ đã đọc
                if (!isMine && !message.isRead()) {
                    markIncomingMessagesReadIfNeeded();
                }
            }
        }

        @Override
        public void onMessagesRead(long convId, long readBy, String readAt) {
            if (binding == null || !isAdded() || convId != conversationId) return;

            if (currentUserId > 0 && readBy > 0) {
                if (readBy != currentUserId) {
                    // Đối phương (Chủ trọ) đã đọc tin nhắn của Sinh viên -> cập nhật ✓✓ cho outgoing messages
                    adapter.markOutgoingMessagesAsRead(readAt);
                } else {
                    // Chính Sinh viên đã đánh dấu đọc -> đồng bộ cục bộ tin nhắn đến
                    adapter.markIncomingMessagesAsRead();
                }
            }
        }

        @Override
        public void onError(String code, String message) {
            if (binding == null || !isAdded()) return;
            Log.w(TAG, "Lỗi từ Socket.IO: [" + code + "] " + message);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Nhận conversationId an toàn từ Navigation arguments
        if (getArguments() != null) {
            conversationId = getArguments().getLong("conversationId", -1L);
        }

        // 2. Nút quay lại danh sách
        if (binding.btnBackChat != null) {
            binding.btnBackChat.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());
        }

        // 3. Kiểm tra tính hợp lệ của conversationId
        if (conversationId <= 0) {
            Log.e(TAG, "conversationId không hợp lệ: " + conversationId);
            showErrorState("ID cuộc trò chuyện không hợp lệ");
            binding.layoutBottomInput.setVisibility(View.GONE);
            return;
        }

        // 4. Khởi tạo RecyclerView và MessageAdapter
        currentUserId = TokenManager.getUserId(requireContext());
        adapter = new MessageAdapter(currentUserId);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setStackFromEnd(true); // Giữ cuộn ở cuối danh sách
        binding.rvMessages.setLayoutManager(layoutManager);
        binding.rvMessages.setAdapter(adapter);

        // 5. Nút Thử lại khi gặp lỗi
        binding.btnRetryChat.setOnClickListener(v -> {
            if (socketManager != null && !socketManager.isConnected()) {
                ChatSocketManager.ConnectionState state = socketManager.getConnectionState();
                if (state != ChatSocketManager.ConnectionState.CONNECTING
                        && state != ChatSocketManager.ConnectionState.RECONNECTING
                        && getContext() != null) {
                    socketManager.connect(requireContext());
                }
            }
            loadChatData();
        });

        // 6. Nút Gửi tin nhắn
        binding.btnSendChat.setOnClickListener(v -> sendMessage());

        // 7. Khởi tạo quản lý Socket.IO (đăng ký listener và join room được quản lý đồng bộ tại onStart/onStop)
        needsHistoryRefreshOnStart = false;
        socketManager = ChatSocketManager.getInstance();
        updateConnectionStateUI(socketManager.getConnectionState());

        // 8. Tải dữ liệu cuộc trò chuyện và lịch sử tin nhắn qua REST API
        loadChatData();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (socketManager != null && conversationId > 0) {
            socketManager.addListener(socketListener);
            ChatSocketManager.ConnectionState currentState = socketManager.getConnectionState();
            updateConnectionStateUI(currentState);

            if (socketManager.isConnected()) {
                hasConnectedOnce = true;
                joinCurrentConversationIfNeeded();
            } else if (currentState != ChatSocketManager.ConnectionState.CONNECTING
                    && currentState != ChatSocketManager.ConnectionState.RECONNECTING
                    && getContext() != null) {
                socketManager.connect(requireContext());
            }

            if (needsHistoryRefreshOnStart) {
                needsHistoryRefreshOnStart = false;
                refreshMessageHistorySilently();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        needsHistoryRefreshOnStart = true;
        if (socketManager != null) {
            socketManager.removeListener(socketListener);
            if (conversationId > 0) {
                socketManager.leaveConversation(conversationId, null);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (socketManager != null) {
            socketManager.removeListener(socketListener);
            if (conversationId > 0) {
                socketManager.leaveConversation(conversationId, null);
            }
        }
        binding = null;
    }

    private void joinCurrentConversationIfNeeded() {
        if (socketManager == null || conversationId <= 0 || !socketManager.isConnected()) return;
        if (socketManager.getActiveConversationId() == conversationId) return;

        Log.d(TAG, "Tham gia phòng trò chuyện " + conversationId);
        socketManager.joinConversation(conversationId, new ChatSocketManager.ChatCallback<Long>() {
            @Override
            public void onSuccess(Long data) {
                Log.d(TAG, "Đã tham gia phòng trò chuyện " + data);
            }

            @Override
            public void onError(String code, String message) {
                Log.w(TAG, "Không thể tham gia phòng trò chuyện " + conversationId + ": [" + code + "] " + message);
            }
        });
    }

    private void refreshMessageHistorySilently() {
        if (!isAdded() || getContext() == null || conversationId <= 0) return;
        String token = TokenManager.getToken(requireContext());
        if (token != null && !token.isEmpty()) {
            loadMessageHistory(token);
        }
    }

    /**
     * Cập nhật trạng thái kết nối hiển thị trên thanh Header
     */
    private void updateConnectionStateUI(ChatSocketManager.ConnectionState state) {
        if (binding == null || !isAdded()) return;

        switch (state) {
            case CONNECTED:
                binding.tvLandlordStatus.setText("Trực tuyến");
                binding.tvLandlordStatus.setTextColor(0xFF047857); // Xanh lá đậm
                break;
            case CONNECTING:
            case RECONNECTING:
                binding.tvLandlordStatus.setText("Đang kết nối lại...");
                binding.tvLandlordStatus.setTextColor(0xFFD97706); // Vàng cam
                break;
            case ERROR:
            case DISCONNECTED:
            default:
                binding.tvLandlordStatus.setText("Ngoại tuyến");
                binding.tvLandlordStatus.setTextColor(0xFF64748B); // Xám
                break;
        }
    }

    /**
     * Tải thông tin chi tiết cuộc trò chuyện và lịch sử tin nhắn từ Backend REST API
     */
    private void loadChatData() {
        if (!isAdded() || getContext() == null) return;

        String token = TokenManager.getToken(requireContext());
        if (token == null || token.isEmpty()) {
            showErrorState("Vui lòng đăng nhập để xem tin nhắn");
            return;
        }

        // Hiển thị trạng thái đang tải
        binding.progressLoadingMessages.setVisibility(View.VISIBLE);
        binding.layoutEmptyMessages.setVisibility(View.GONE);
        binding.layoutErrorMessages.setVisibility(View.GONE);

        // 1. Tải thông tin chi tiết cuộc trò chuyện (Chủ trọ và phòng trọ)
        loadConversationDetail(token);

        // 2. Tải lịch sử tin nhắn
        loadMessageHistory(token);
    }

    /**
     * Gọi API GET /api/chat/conversations/:conversationId
     */
    private void loadConversationDetail(String token) {
        RetrofitClient.getApiService().getConversationDetail(token, conversationId)
                .enqueue(new Callback<ApiResponse<ConversationResponse>>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse<ConversationResponse>> call,
                                           @NonNull Response<ApiResponse<ConversationResponse>> response) {
                        if (binding == null || !isAdded()) return;

                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            ConversationResponse detail = response.body().getData();
                            if (detail != null) {
                                bindConversationDetail(detail);
                            }
                        } else {
                            Log.w(TAG, "Không thể tải chi tiết cuộc hội thoại: HTTP " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse<ConversationResponse>> call, @NonNull Throwable t) {
                        Log.w(TAG, "Lỗi mạng khi tải chi tiết cuộc hội thoại: " + t.getMessage());
                    }
                });
    }

    /**
     * Hiển thị thông tin đối phương (Chủ trọ) và thông tin phòng trọ đính kèm
     */
    private void bindConversationDetail(ConversationResponse detail) {
        if (binding == null || !isAdded()) return;

        // 1. Thông tin Chủ trọ
        ConversationResponse.PartnerInfo partner = detail.getPartner();
        if (partner != null) {
            String name = (partner.getFullName() != null && !partner.getFullName().trim().isEmpty())
                    ? partner.getFullName()
                    : "Chủ trọ";
            binding.tvLandlordName.setText(name);

            // Cập nhật thông tin đối phương cho Adapter để hiển thị tên và avatar ở các tin nhắn nhận vào
            adapter.setPartnerInfo(name, partner.getAvatarUrl());

            // Avatar trên thanh Header
            if (partner.getAvatarUrl() != null && !partner.getAvatarUrl().trim().isEmpty()) {
                binding.ivHeaderAvatar.setVisibility(View.VISIBLE);
                binding.tvHeaderAvatarPlaceholder.setVisibility(View.GONE);
                Glide.with(this)
                        .load(partner.getAvatarUrl())
                        .circleCrop()
                        .into(binding.ivHeaderAvatar);
            } else {
                binding.ivHeaderAvatar.setVisibility(View.GONE);
                binding.tvHeaderAvatarPlaceholder.setVisibility(View.VISIBLE);
                binding.tvHeaderAvatarPlaceholder.setText("👩‍💼");
            }

            // Nút gọi điện cho chủ trọ
            String phone = partner.getPhoneNumber();
            if (phone != null && !phone.trim().isEmpty()) {
                binding.btnCallLandlord.setVisibility(View.VISIBLE);
                binding.btnCallLandlord.setOnClickListener(v -> {
                    Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                    startActivity(dialIntent);
                });
            } else {
                binding.btnCallLandlord.setVisibility(View.GONE);
            }
        }

        // 2. Thông tin phòng trọ đính kèm (Pinned Room Listing Card)
        ConversationResponse.RoomInfo room = detail.getRoom();
        if (room != null && room.getTitle() != null && !room.getTitle().trim().isEmpty()) {
            binding.cardPinnedListing.setVisibility(View.VISIBLE);
            binding.tvRoomTitle.setText(room.getTitle());
            String roomCode = (room.getId() > 0 ? "#AVR-" + room.getId() : "#AVR") +
                    (room.getDistrict() != null ? " • " + room.getDistrict() : "");
            binding.tvRoomCode.setText(roomCode);

            if (room.getPrice() > 0) {
                binding.tvRoomPrice.setText(String.format(Locale.getDefault(), "%,d đ/tháng", (long) room.getPrice()));
            }

            if (room.getThumbnailUrl() != null && !room.getThumbnailUrl().trim().isEmpty()) {
                Glide.with(this)
                        .load(room.getThumbnailUrl())
                        .centerCrop()
                        .into(binding.ivRoomThumb);
            }
        } else {
            binding.cardPinnedListing.setVisibility(View.GONE);
        }
    }

    /**
     * Gọi API GET /api/chat/conversations/:conversationId/messages?page=1&limit=50
     */
    private void loadMessageHistory(String token) {
        RetrofitClient.getApiService().getMessages(token, conversationId, 1, 50)
                .enqueue(new Callback<ApiResponse<MessageListResponse>>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse<MessageListResponse>> call,
                                           @NonNull Response<ApiResponse<MessageListResponse>> response) {
                        if (binding == null || !isAdded()) return;

                        binding.progressLoadingMessages.setVisibility(View.GONE);

                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            MessageListResponse data = response.body().getData();
                            List<MessageResponse> list = (data != null) ? data.getMessages() : null;

                            if (list == null || list.isEmpty()) {
                                if (adapter.getItemCount() == 0) {
                                    showEmptyState();
                                }
                            } else {
                                // Backend sắp xếp giảm dần (newest-first) -> Client đảo ngược để hiển thị cũ ở trên, mới ở dưới
                                List<MessageResponse> chronological = new ArrayList<>(list);
                                Collections.reverse(chronological);

                                adapter.setMessages(chronological);
                                if (adapter.getItemCount() > 0) {
                                    showContentState();
                                    binding.rvMessages.scrollToPosition(adapter.getItemCount() - 1);
                                } else {
                                    showEmptyState();
                                }

                                // Chỉ đánh dấu đã đọc khi thực sự có tin nhắn đến chưa đọc
                                markIncomingMessagesReadIfNeeded();
                            }
                        } else {
                            Log.e(TAG, "Lỗi API getMessages: HTTP " + response.code());
                            if (adapter.getItemCount() == 0) {
                                showErrorState("Không thể tải lịch sử tin nhắn (HTTP " + response.code() + ")");
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse<MessageListResponse>> call, @NonNull Throwable t) {
                        if (binding == null || !isAdded()) return;

                        binding.progressLoadingMessages.setVisibility(View.GONE);
                        Log.e(TAG, "Lỗi mạng khi tải tin nhắn: " + t.getMessage(), t);
                        if (adapter.getItemCount() == 0) {
                            showErrorState("Lỗi kết nối mạng, vui lòng thử lại");
                        }
                    }
                });
    }

    /**
     * Gửi tin nhắn mới: Ưu tiên Socket.IO realtime với ACK xác nhận, tự động fallback REST nếu Socket chưa kết nối
     */
    private void sendMessage() {
        if (!isAdded() || getContext() == null) return;

        String token = TokenManager.getToken(requireContext());
        if (token == null || token.isEmpty()) {
            Toast.makeText(requireContext(), "Vui lòng đăng nhập lại để gửi tin nhắn", Toast.LENGTH_SHORT).show();
            return;
        }

        String content = binding.etChatInput.getText() != null
                ? binding.etChatInput.getText().toString().trim()
                : "";

        // 1. Kiểm tra nội dung rỗng
        if (content.isEmpty()) {
            Toast.makeText(requireContext(), "Vui lòng nhập nội dung tin nhắn", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Kiểm tra độ dài tối đa (Backend giới hạn 2000 ký tự)
        if (content.length() > 2000) {
            Toast.makeText(requireContext(), "Nội dung tin nhắn tối đa 2000 ký tự", Toast.LENGTH_SHORT).show();
            return;
        }

        // 3. Khóa nút gửi tránh gửi lặp khi request đang xử lý
        binding.btnSendChat.setEnabled(false);
        binding.btnSendChat.setAlpha(0.5f);

        // 4. Ưu tiên gửi qua Socket.IO Realtime nếu đang kết nối
        if (socketManager != null && socketManager.isConnected()) {
            Log.d(TAG, "Gửi tin nhắn qua Socket.IO Realtime: conversation=" + conversationId);
            socketManager.sendMessage(conversationId, content, new ChatSocketManager.ChatCallback<MessageResponse>() {
                @Override
                public void onSuccess(MessageResponse data) {
                    if (binding == null || !isAdded()) return;

                    // Mở khóa nút gửi
                    binding.btnSendChat.setEnabled(true);
                    binding.btnSendChat.setAlpha(1.0f);

                    if (data == null || data.getId() <= 0) {
                        Log.w(TAG, "ACK send_message thành công nhưng dữ liệu tin nhắn không hợp lệ");
                        if (getContext() != null) {
                            Toast.makeText(getContext(), "Phản hồi gửi tin không hợp lệ, vui lòng kiểm tra lại", Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }

                    // Xóa ô nhập chỉ khi nội dung chưa bị chỉnh sửa sau khi xác nhận gửi thành công
                    if (binding.etChatInput.getText() != null
                            && binding.etChatInput.getText().toString().trim().equals(content)) {
                        binding.etChatInput.setText("");
                    }

                    data.setIsMine(true);
                    boolean isNew = adapter.addOrUpdateMessage(data);
                    if (adapter.getItemCount() > 0) {
                        showContentState();
                        if (isNew) {
                            binding.rvMessages.smoothScrollToPosition(adapter.getItemCount() - 1);
                        }
                    }
                }

                @Override
                public void onError(String code, String message) {
                    if (binding == null || !isAdded()) return;

                    binding.btnSendChat.setEnabled(true);
                    binding.btnSendChat.setAlpha(1.0f);
                    Log.e(TAG, "Lỗi gửi tin qua Socket.IO: [" + code + "] " + message);
                    if (getContext() != null) {
                        String toastMsg = ("ACK_TIMEOUT".equals(code) || "ACK_UNCERTAIN".equals(code))
                                ? message
                                : "Không thể gửi tin: " + message;
                        Toast.makeText(getContext(), toastMsg, Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            // Fallback gửi qua REST API nếu Socket tạm thời ngắt kết nối
            Log.d(TAG, "Socket chưa kết nối, fallback gửi tin qua REST API");
            sendViaRest(token, content);
        }
    }

    /**
     * Gửi tin nhắn qua REST API (Dùng làm fallback khi Socket ngắt kết nối)
     */
    private void sendViaRest(String token, String content) {
        RetrofitClient.getApiService().sendMessage(token, conversationId, new SendMessageRequest(content))
                .enqueue(new Callback<ApiResponse<MessageResponse>>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse<MessageResponse>> call,
                                           @NonNull Response<ApiResponse<MessageResponse>> response) {
                        if (binding == null || !isAdded()) return;

                        binding.btnSendChat.setEnabled(true);
                        binding.btnSendChat.setAlpha(1.0f);

                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            MessageResponse sentMsg = response.body().getData();
                            if (sentMsg == null || sentMsg.getId() <= 0) {
                                Log.e(TAG, "REST sendMessage thành công nhưng data null/không hợp lệ");
                                if (getContext() != null) {
                                    Toast.makeText(getContext(), "Phản hồi gửi tin không hợp lệ, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                                }
                                return;
                            }

                            if (binding.etChatInput.getText() != null
                                    && binding.etChatInput.getText().toString().trim().equals(content)) {
                                binding.etChatInput.setText("");
                            }

                            sentMsg.setIsMine(true);
                            boolean isNew = adapter.addOrUpdateMessage(sentMsg);
                            if (adapter.getItemCount() > 0) {
                                showContentState();
                                if (isNew) {
                                    binding.rvMessages.smoothScrollToPosition(adapter.getItemCount() - 1);
                                }
                            }
                        } else {
                            Log.e(TAG, "Lỗi gửi tin qua REST: HTTP " + response.code());
                            if (getContext() != null) {
                                Toast.makeText(getContext(), "Không thể gửi tin nhắn, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse<MessageResponse>> call, @NonNull Throwable t) {
                        if (binding == null || !isAdded()) return;

                        binding.btnSendChat.setEnabled(true);
                        binding.btnSendChat.setAlpha(1.0f);
                        Log.e(TAG, "Lỗi mạng khi gửi tin nhắn REST: " + t.getMessage(), t);
                        if (getContext() != null) {
                            Toast.makeText(getContext(), "Lỗi kết nối, không thể gửi tin nhắn", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * Đồng bộ trạng thái đã đọc cho các tin nhắn đến chưa đọc (chỉ gửi khi cần thiết).
     * Ưu tiên Socket mark_read khi đang kết nối, fallback REST PATCH khi ngắt kết nối.
     */
    private void markIncomingMessagesReadIfNeeded() {
        if (!isAdded() || getContext() == null || conversationId <= 0 || adapter == null) return;
        if (!adapter.hasUnreadIncomingMessages()) return;

        if (markReadInFlight) {
            pendingMarkReadAfterInFlight = true;
            return;
        }

        String token = TokenManager.getToken(requireContext());
        if (token == null || token.isEmpty()) return;

        markReadInFlight = true;
        pendingMarkReadAfterInFlight = false;

        if (socketManager != null && socketManager.isConnected()) {
            socketManager.markRead(conversationId, new ChatSocketManager.ChatCallback<ReadResponse>() {
                @Override
                public void onSuccess(ReadResponse data) {
                    if (binding == null || !isAdded()) {
                        markReadInFlight = false;
                        return;
                    }
                    Log.d(TAG, "Socket mark_read thành công cho conversation=" + conversationId);
                    onMarkReadCompleted(true);
                }

                @Override
                public void onError(String code, String message) {
                    Log.w(TAG, "Socket mark_read lỗi [" + code + "]: " + message);
                    if (binding == null || !isAdded() || getContext() == null) {
                        markReadInFlight = false;
                        return;
                    }
                    if ("NOT_CONNECTED".equals(code) || "ACK_TIMEOUT".equals(code) || "ACK_UNCERTAIN".equals(code)) {
                        String latestToken = TokenManager.getToken(getContext());
                        if (latestToken != null && !latestToken.isEmpty()) {
                            markMessagesAsReadViaRest(latestToken);
                            return;
                        }
                    }
                    onMarkReadCompleted(false);
                }
            });
        } else {
            markMessagesAsReadViaRest(token);
        }
    }

    /**
     * Gọi API PATCH /api/chat/conversations/:conversationId/read để đánh dấu đã đọc
     */
    private void markMessagesAsReadViaRest(String token) {
        RetrofitClient.getApiService().markMessagesAsRead(token, conversationId)
                .enqueue(new Callback<ApiResponse<ReadResponse>>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse<ReadResponse>> call,
                                           @NonNull Response<ApiResponse<ReadResponse>> response) {
                        if (binding == null || !isAdded()) {
                            markReadInFlight = false;
                            return;
                        }
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            Log.d(TAG, "REST markMessagesAsRead thành công");
                            onMarkReadCompleted(true);
                        } else {
                            Log.w(TAG, "REST markMessagesAsRead thất bại: HTTP " + response.code());
                            onMarkReadCompleted(false);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse<ReadResponse>> call, @NonNull Throwable t) {
                        Log.w(TAG, "Không thể cập nhật trạng thái đã đọc qua REST: " + t.getMessage());
                        if (binding == null || !isAdded()) {
                            markReadInFlight = false;
                            return;
                        }
                        onMarkReadCompleted(false);
                    }
                });
    }

    private void onMarkReadCompleted(boolean success) {
        markReadInFlight = false;
        if (success && adapter != null) {
            adapter.markIncomingMessagesAsRead();
        }
        if (pendingMarkReadAfterInFlight) {
            pendingMarkReadAfterInFlight = false;
            if (success && adapter != null && adapter.hasUnreadIncomingMessages()) {
                markIncomingMessagesReadIfNeeded();
            }
        }
    }

    private void showContentState() {
        if (binding == null) return;
        binding.rvMessages.setVisibility(View.VISIBLE);
        binding.layoutEmptyMessages.setVisibility(View.GONE);
        binding.layoutErrorMessages.setVisibility(View.GONE);
    }

    private void showEmptyState() {
        if (binding == null) return;
        binding.rvMessages.setVisibility(View.GONE);
        binding.layoutEmptyMessages.setVisibility(View.VISIBLE);
        binding.layoutErrorMessages.setVisibility(View.GONE);
    }

    private void showErrorState(String message) {
        if (binding == null) return;
        binding.rvMessages.setVisibility(View.GONE);
        binding.layoutEmptyMessages.setVisibility(View.GONE);
        binding.layoutErrorMessages.setVisibility(View.VISIBLE);
        binding.tvErrorMessage.setText(message);
    }
}
