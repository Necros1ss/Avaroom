package com.avaroom.app.ui.student;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.avaroom.app.R;
import com.avaroom.app.data.model.response.ApiResponse;
import com.avaroom.app.data.model.response.ConversationListResponse;
import com.avaroom.app.data.model.response.ConversationResponse;
import com.avaroom.app.data.model.response.MessageResponse;
import com.avaroom.app.data.model.response.UnreadCountResponse;
import com.avaroom.app.data.remote.ChatSocketManager;
import com.avaroom.app.data.remote.RetrofitClient;
import com.avaroom.app.databinding.FragmentChatListBinding;
import com.avaroom.app.utils.TokenManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragment hiển thị danh sách cuộc hội thoại thực tế từ Backend REST API cho Sinh viên.
 */
public class ChatListFragment extends Fragment implements ConversationAdapter.OnConversationClickListener {

    private static final String TAG = "ChatListFragment";

    private FragmentChatListBinding binding;
    private ConversationAdapter adapter;
    private ChatSocketManager socketManager;
    private long currentUserId = -1L;
    private final List<ConversationResponse> fullList = new ArrayList<>();
    private boolean needsRefreshOnStart = false;
    private boolean hasConnectedOnce = false;
    private boolean wasDisconnectedWhileActive = false;
    private boolean isLoadingConversations = false;

    private final ChatSocketManager.ChatEventListener socketListener = new ChatSocketManager.SimpleChatEventListener() {
        @Override
        public void onConnectionStateChanged(ChatSocketManager.ConnectionState state) {
            if (binding == null || !isAdded()) return;

            if (state == ChatSocketManager.ConnectionState.DISCONNECTED
                    || state == ChatSocketManager.ConnectionState.RECONNECTING
                    || state == ChatSocketManager.ConnectionState.ERROR) {
                if (hasConnectedOnce) {
                    wasDisconnectedWhileActive = true;
                }
                return;
            }

            if (state == ChatSocketManager.ConnectionState.CONNECTED) {
                hasConnectedOnce = true;
                if (wasDisconnectedWhileActive) {
                    wasDisconnectedWhileActive = false;
                    reconcileFromRest();
                }
            }
        }

        @Override
        public void onConversationUpdated(long conversationId, MessageResponse lastMessage) {
            if (binding == null || !isAdded() || conversationId <= 0) return;

            boolean found = updateConversationPreviewLocally(conversationId, lastMessage);
            if (found) {
                applyCurrentFilter();
                fetchSingleConversationDetail(conversationId);
            } else {
                loadConversationsSilently();
            }
            loadTotalUnreadCount();
        }

        @Override
        public void onUnreadCountUpdated(long conversationId, long readBy, int updatedCount) {
            if (binding == null || !isAdded() || conversationId <= 0) return;

            if (currentUserId <= 0 || readBy == currentUserId) {
                if (updateConversationUnreadLocally(conversationId, 0)) {
                    applyCurrentFilter();
                }
                loadTotalUnreadCount();
            }
        }

        @Override
        public void onMessagesRead(long conversationId, long readBy, String readAt) {
            if (binding == null || !isAdded() || conversationId <= 0) return;

            if (currentUserId > 0 && readBy == currentUserId) {
                if (updateConversationUnreadLocally(conversationId, 0)) {
                    applyCurrentFilter();
                }
                loadTotalUnreadCount();
            } else if (currentUserId > 0 && readBy > 0 && readBy != currentUserId) {
                markConversationLastMessageReadLocally(conversationId);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentChatListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Khởi tạo RecyclerView và Adapter
        adapter = new ConversationAdapter(this);
        binding.rvConversations.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvConversations.setAdapter(adapter);

        // 2. Thiết lập Pull-to-Refresh
        binding.swipeRefreshChatList.setColorSchemeResources(R.color.student_accent_red);
        binding.swipeRefreshChatList.setOnRefreshListener(() -> {
            loadConversations();
            loadTotalUnreadCount();
        });

        // 3. Nút Thử lại ở trạng thái lỗi
        binding.btnRetryChatList.setOnClickListener(v -> {
            loadConversations();
            loadTotalUnreadCount();
        });

        // 4. Tìm kiếm cuộc trò chuyện theo tên chủ trọ hoặc tên phòng trọ
        binding.etSearchChat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterConversations(s != null ? s.toString() : "");
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // 5. Điều hướng chuông thông báo & hồ sơ người dùng
        if (binding.btnNotificationChatList != null) {
            binding.btnNotificationChatList.setOnClickListener(v -> {
                Navigation.findNavController(v).navigate(R.id.action_chatList_to_notifications);
            });
        }

        if (binding.btnProfileChatList != null) {
            binding.btnProfileChatList.setOnClickListener(v -> {
                Navigation.findNavController(v).navigate(R.id.action_chatList_to_ekyc);
            });
        }

        // 6. Khởi tạo SocketManager và tải dữ liệu cuộc hội thoại thực tế từ Backend REST API
        needsRefreshOnStart = false;
        currentUserId = TokenManager.getUserId(requireContext());
        socketManager = ChatSocketManager.getInstance();
        loadConversations();
        loadTotalUnreadCount();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (socketManager != null) {
            socketManager.addListener(socketListener);
            ChatSocketManager.ConnectionState state = socketManager.getConnectionState();
            if (socketManager.isConnected()) {
                hasConnectedOnce = true;
            } else if (state != ChatSocketManager.ConnectionState.CONNECTING
                    && state != ChatSocketManager.ConnectionState.RECONNECTING
                    && getContext() != null) {
                socketManager.connect(requireContext());
            }
        }

        if (needsRefreshOnStart) {
            needsRefreshOnStart = false;
            reconcileFromRest();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        needsRefreshOnStart = true;
        if (socketManager != null) {
            socketManager.removeListener(socketListener);
        }
    }

    /**
     * Tải danh sách cuộc trò chuyện từ Backend: GET /api/chat/conversations
     */
    private void loadConversations() {
        loadConversationsInternal(true);
    }

    private void loadConversationsSilently() {
        loadConversationsInternal(false);
    }

    private void reconcileFromRest() {
        loadConversationsSilently();
        loadTotalUnreadCount();
    }

    private void loadConversationsInternal(boolean showSpinnerIfEmpty) {
        if (!isAdded() || getContext() == null) return;
        if (isLoadingConversations) return;

        String token = TokenManager.getToken(requireContext());
        if (token == null || token.isEmpty()) {
            showError("Vui lòng đăng nhập để xem danh sách tin nhắn");
            if (binding != null) {
                binding.swipeRefreshChatList.setRefreshing(false);
            }
            return;
        }

        isLoadingConversations = true;

        // Hiển thị loading indicator nếu đang load lần đầu/chưa có dữ liệu và không phải swipe refresh
        if (showSpinnerIfEmpty && binding != null
                && !binding.swipeRefreshChatList.isRefreshing()
                && fullList.isEmpty()) {
            binding.progressLoadingChatList.setVisibility(View.VISIBLE);
            binding.layoutEmptyState.setVisibility(View.GONE);
            binding.layoutErrorState.setVisibility(View.GONE);
        }

        RetrofitClient.getApiService().getConversations(token, 1, 50)
                .enqueue(new Callback<ApiResponse<ConversationListResponse>>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse<ConversationListResponse>> call,
                                           @NonNull Response<ApiResponse<ConversationListResponse>> response) {
                        isLoadingConversations = false;
                        if (binding == null || !isAdded()) return;

                        binding.progressLoadingChatList.setVisibility(View.GONE);
                        binding.swipeRefreshChatList.setRefreshing(false);

                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            ConversationListResponse data = response.body().getData();
                            List<ConversationResponse> conversations = (data != null) ? data.getConversations() : null;

                            replaceFullListDeduplicated(conversations);
                            applyCurrentFilter();
                        } else {
                            Log.e(TAG, "Lỗi phản hồi API: HTTP " + response.code());
                            if (fullList.isEmpty()) {
                                showError("Không thể tải danh sách cuộc trò chuyện (HTTP " + response.code() + ")");
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse<ConversationListResponse>> call, @NonNull Throwable t) {
                        isLoadingConversations = false;
                        if (binding == null || !isAdded()) return;

                        binding.progressLoadingChatList.setVisibility(View.GONE);
                        binding.swipeRefreshChatList.setRefreshing(false);
                        Log.e(TAG, "Lỗi kết nối khi tải cuộc trò chuyện: " + t.getMessage(), t);
                        if (fullList.isEmpty()) {
                            showError("Lỗi kết nối mạng, vui lòng kiểm tra và thử lại");
                        }
                    }
                });
    }

    /**
     * Đồng bộ tổng số tin chưa đọc từ Backend: GET /api/chat/unread-count
     */
    private void loadTotalUnreadCount() {
        if (!isAdded() || getContext() == null) return;
        String token = TokenManager.getToken(requireContext());
        if (token == null || token.isEmpty()) return;

        RetrofitClient.getApiService().getUnreadCount(token)
                .enqueue(new Callback<ApiResponse<UnreadCountResponse>>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse<UnreadCountResponse>> call,
                                           @NonNull Response<ApiResponse<UnreadCountResponse>> response) {
                        if (binding == null || !isAdded()) return;
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            UnreadCountResponse data = response.body().getData();
                            if (data != null) {
                                updateTitleUnreadCount(data.getUnreadCount());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse<UnreadCountResponse>> call, @NonNull Throwable t) {
                        Log.w(TAG, "Không thể tải tổng số tin chưa đọc: " + t.getMessage());
                    }
                });
    }

    /**
     * Lấy chi tiết một cuộc trò chuyện từ Backend để đồng bộ chính xác unread_count và last_message
     */
    private void fetchSingleConversationDetail(long conversationId) {
        if (!isAdded() || getContext() == null || conversationId <= 0) return;
        String token = TokenManager.getToken(requireContext());
        if (token == null || token.isEmpty()) return;

        RetrofitClient.getApiService().getConversationDetail(token, conversationId)
                .enqueue(new Callback<ApiResponse<ConversationResponse>>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse<ConversationResponse>> call,
                                           @NonNull Response<ApiResponse<ConversationResponse>> response) {
                        if (binding == null || !isAdded()) return;
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            ConversationResponse detail = response.body().getData();
                            if (detail != null && detail.getId() > 0) {
                                upsertConversationAtTop(detail);
                                applyCurrentFilter();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse<ConversationResponse>> call, @NonNull Throwable t) {
                        Log.w(TAG, "Không thể đồng bộ chi tiết cuộc trò chuyện " + conversationId + ": " + t.getMessage());
                    }
                });
    }

    private void updateTitleUnreadCount(int totalUnread) {
        if (binding == null) return;
        if (totalUnread > 0) {
            String countText = totalUnread > 99 ? "99+" : String.valueOf(totalUnread);
            binding.tvTitle.setText("Tin Nhắn (" + countText + ")");
        } else {
            binding.tvTitle.setText("Tin Nhắn");
        }
    }

    private void replaceFullListDeduplicated(List<ConversationResponse> conversations) {
        fullList.clear();
        if (conversations == null) return;
        Map<Long, ConversationResponse> deduped = new LinkedHashMap<>();
        for (ConversationResponse conv : conversations) {
            if (conv != null && conv.getId() > 0) {
                deduped.put(conv.getId(), conv);
            }
        }
        fullList.addAll(deduped.values());
    }

    private void upsertConversationAtTop(ConversationResponse detail) {
        for (int i = 0; i < fullList.size(); i++) {
            ConversationResponse existing = fullList.get(i);
            if (existing != null && existing.getId() == detail.getId()) {
                fullList.remove(i);
                fullList.add(0, detail);
                return;
            }
        }
        fullList.add(0, detail);
    }

    private boolean updateConversationPreviewLocally(long conversationId, MessageResponse lastMessage) {
        for (int i = 0; i < fullList.size(); i++) {
            ConversationResponse conv = fullList.get(i);
            if (conv != null && conv.getId() == conversationId) {
                if (lastMessage != null) {
                    conv.setLastMessage(lastMessage);
                    if (lastMessage.getCreatedAt() != null) {
                        conv.setLastMessageAt(lastMessage.getCreatedAt());
                    }
                }
                fullList.remove(i);
                fullList.add(0, conv);
                return true;
            }
        }
        return false;
    }

    private boolean updateConversationUnreadLocally(long conversationId, int unreadCount) {
        for (ConversationResponse conv : fullList) {
            if (conv != null && conv.getId() == conversationId) {
                if (conv.getUnreadCount() != unreadCount) {
                    conv.setUnreadCount(unreadCount);
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    private void markConversationLastMessageReadLocally(long conversationId) {
        for (ConversationResponse conv : fullList) {
            if (conv != null && conv.getId() == conversationId && conv.getLastMessage() != null) {
                MessageResponse lastMsg = conv.getLastMessage();
                boolean isMine = lastMsg.isMine() || (currentUserId > 0 && lastMsg.getSenderId() == currentUserId);
                if (isMine && !lastMsg.isRead()) {
                    lastMsg.setRead(true);
                }
                return;
            }
        }
    }

    private void applyCurrentFilter() {
        if (binding == null) return;
        String currentQuery = binding.etSearchChat.getText() != null
                ? binding.etSearchChat.getText().toString()
                : "";
        filterConversations(currentQuery);
    }

    /**
     * Lọc danh sách cuộc trò chuyện theo từ khóa tìm kiếm trong bộ nhớ
     */
    private void filterConversations(String query) {
        if (query == null || query.trim().isEmpty()) {
            if (fullList.isEmpty()) {
                showEmptyState();
            } else {
                showContentState();
                adapter.setData(fullList);
            }
            return;
        }

        String lowerQuery = query.trim().toLowerCase();
        List<ConversationResponse> filtered = new ArrayList<>();
        for (ConversationResponse conv : fullList) {
            boolean match = false;
            // Tìm theo tên chủ trọ
            if (conv.getPartner() != null && conv.getPartner().getFullName() != null) {
                if (conv.getPartner().getFullName().toLowerCase().contains(lowerQuery)) {
                    match = true;
                }
            }
            // Tìm theo tên phòng trọ
            if (!match && conv.getRoom() != null && conv.getRoom().getTitle() != null) {
                if (conv.getRoom().getTitle().toLowerCase().contains(lowerQuery)) {
                    match = true;
                }
            }
            // Tìm theo nội dung tin nhắn cuối
            if (!match && conv.getLastMessage() != null && conv.getLastMessage().getContent() != null) {
                if (conv.getLastMessage().getContent().toLowerCase().contains(lowerQuery)) {
                    match = true;
                }
            }

            if (match) {
                filtered.add(conv);
            }
        }

        if (filtered.isEmpty()) {
            binding.rvConversations.setVisibility(View.GONE);
            binding.layoutEmptyState.setVisibility(View.VISIBLE);
            binding.layoutErrorState.setVisibility(View.GONE);
        } else {
            showContentState();
            adapter.setData(filtered);
        }
    }

    private void showContentState() {
        if (binding == null) return;
        binding.rvConversations.setVisibility(View.VISIBLE);
        binding.layoutEmptyState.setVisibility(View.GONE);
        binding.layoutErrorState.setVisibility(View.GONE);
    }

    private void showEmptyState() {
        if (binding == null) return;
        binding.rvConversations.setVisibility(View.GONE);
        binding.layoutEmptyState.setVisibility(View.VISIBLE);
        binding.layoutErrorState.setVisibility(View.GONE);
    }

    private void showError(String message) {
        if (binding == null) return;
        binding.rvConversations.setVisibility(View.GONE);
        binding.layoutEmptyState.setVisibility(View.GONE);
        binding.layoutErrorState.setVisibility(View.VISIBLE);
        binding.tvErrorMessage.setText(message);
    }

    /**
     * Xử lý sự kiện click vào một cuộc hội thoại:
     * Điều hướng sang ChatFragment và truyền conversationId thực tế nhận từ Backend
     */
    @Override
    public void onConversationClick(ConversationResponse conversation) {
        if (conversation == null || !isAdded() || getView() == null) return;

        long conversationId = conversation.getId();
        if (conversationId <= 0) {
            Toast.makeText(requireContext(), "ID cuộc trò chuyện không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo Bundle truyền conversationId sang ChatFragment
        Bundle args = new Bundle();
        args.putLong("conversationId", conversationId);

        try {
            Navigation.findNavController(requireView()).navigate(R.id.action_chatList_to_chat, args);
        } catch (Exception e) {
            Log.e(TAG, "Lỗi điều hướng sang ChatFragment", e);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (socketManager != null) {
            socketManager.removeListener(socketListener);
        }
        binding = null;
    }
}
