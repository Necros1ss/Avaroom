package com.avaroom.app.ui.student;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avaroom.app.data.model.response.MessageResponse;
import com.avaroom.app.databinding.ItemMessageIncomingBinding;
import com.avaroom.app.databinding.ItemMessageOutgoingBinding;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * Adapter hiển thị danh sách tin nhắn giữa Sinh viên và Chủ trọ.
 * Phân tách rõ ràng giữa tin nhắn gửi đi (Sinh viên) và tin nhắn nhận vào (Chủ trọ).
 */
public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_OUTGOING = 1;
    private static final int TYPE_INCOMING = 2;

    private final List<MessageResponse> messages = new ArrayList<>();
    private final long currentUserId;
    private String partnerName = "Chủ trọ";
    private String partnerAvatarUrl = null;

    public MessageAdapter(long currentUserId) {
        this.currentUserId = currentUserId;
    }

    public void setPartnerInfo(String name, String avatarUrl) {
        if (name != null && !name.trim().isEmpty()) {
            this.partnerName = name;
        }
        this.partnerAvatarUrl = avatarUrl;
        notifyDataSetChanged();
    }

    public void setMessages(List<MessageResponse> newMessages) {
        Map<Long, MessageResponse> merged = new LinkedHashMap<>();
        for (MessageResponse existing : messages) {
            if (existing != null && existing.getId() > 0) {
                merged.put(existing.getId(), existing);
            }
        }
        if (newMessages != null) {
            for (MessageResponse msg : newMessages) {
                if (msg != null && msg.getId() > 0) {
                    MessageResponse previous = merged.get(msg.getId());
                    if (previous != null && previous.isRead() && !msg.isRead()) {
                        msg.setRead(true);
                    }
                    merged.put(msg.getId(), msg);
                }
            }
        }

        messages.clear();
        messages.addAll(merged.values());
        Collections.sort(messages, MessageAdapter::compareMessages);
        notifyDataSetChanged();
    }

    /**
     * Thêm tin nhắn mới hoặc cập nhật nếu ID tin nhắn đã tồn tại (Chống duplicate giữa REST, Socket ACK và Broadcast)
     * @return true nếu là tin nhắn mới được thêm, false nếu là tin nhắn đã có được cập nhật
     */
    public boolean addOrUpdateMessage(MessageResponse message) {
        if (message == null || message.getId() <= 0) return false;

        long targetId = message.getId();
        for (int i = 0; i < messages.size(); i++) {
            MessageResponse existing = messages.get(i);
            if (existing != null && Long.valueOf(existing.getId()).equals(Long.valueOf(targetId))) {
                if (existing.isRead() && !message.isRead()) {
                    message.setRead(true);
                }
                messages.set(i, message);
                notifyItemChanged(i);
                return false;
            }
        }

        int insertIndex = messages.size();
        while (insertIndex > 0 && compareMessages(messages.get(insertIndex - 1), message) > 0) {
            insertIndex--;
        }

        messages.add(insertIndex, message);
        notifyItemInserted(insertIndex);
        return true;
    }

    private static int compareMessages(MessageResponse a, MessageResponse b) {
        long timeA = parseTimestampMillis(a != null ? a.getCreatedAt() : null);
        long timeB = parseTimestampMillis(b != null ? b.getCreatedAt() : null);
        if (timeA > 0 && timeB > 0 && timeA != timeB) {
            return Long.compare(timeA, timeB);
        }
        long idA = a != null ? a.getId() : 0L;
        long idB = b != null ? b.getId() : 0L;
        return Long.compare(idA, idB);
    }

    private static long parseTimestampMillis(String isoDate) {
        if (isoDate == null || isoDate.trim().isEmpty()) return -1L;
        try {
            String clean = isoDate.trim();
            if (clean.endsWith("Z")) {
                clean = clean.substring(0, clean.length() - 1);
            }
            int plusIdx = clean.indexOf('+');
            if (plusIdx > 0) {
                clean = clean.substring(0, plusIdx);
            }
            String pattern = clean.contains(".")
                    ? "yyyy-MM-dd'T'HH:mm:ss.SSS"
                    : "yyyy-MM-dd'T'HH:mm:ss";
            if (clean.contains(".")) {
                int dotIdx = clean.indexOf('.');
                String millis = clean.substring(dotIdx + 1);
                if (millis.length() > 3) {
                    millis = millis.substring(0, 3);
                } else {
                    while (millis.length() < 3) {
                        millis = millis + "0";
                    }
                }
                clean = clean.substring(0, dotIdx + 1) + millis;
            }
            SimpleDateFormat iso = new SimpleDateFormat(pattern, Locale.US);
            iso.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date parsed = iso.parse(clean);
            return parsed != null ? parsed.getTime() : -1L;
        } catch (Exception ignored) {
            return -1L;
        }
    }

    public void addMessage(MessageResponse message) {
        addOrUpdateMessage(message);
    }

    private boolean isOutgoingMessage(MessageResponse item) {
        if (item == null) return false;
        return item.isMine() || (currentUserId > 0 && item.getSenderId() == currentUserId);
    }

    /**
     * Kiểm tra còn tin nhắn đến (của đối phương) nào chưa đọc hay không
     */
    public boolean hasUnreadIncomingMessages() {
        for (MessageResponse msg : messages) {
            if (msg != null && msg.getId() > 0 && !isOutgoingMessage(msg) && !msg.isRead()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Cập nhật cục bộ các tin nhắn đến thành đã đọc sau khi Backend xác nhận
     */
    public void markIncomingMessagesAsRead() {
        for (int i = 0; i < messages.size(); i++) {
            MessageResponse msg = messages.get(i);
            if (msg != null && !isOutgoingMessage(msg) && !msg.isRead()) {
                msg.setRead(true);
                notifyItemChanged(i);
            }
        }
    }

    /**
     * Cập nhật trạng thái đã đọc (✓✓) cho các tin nhắn gửi đi của Sinh viên khi đối phương đọc
     */
    public void markOutgoingMessagesAsRead(String readAt) {
        long readAtMs = parseTimestampMillis(readAt);
        for (int i = 0; i < messages.size(); i++) {
            MessageResponse msg = messages.get(i);
            if (msg != null && isOutgoingMessage(msg) && !msg.isRead()) {
                long msgMs = parseTimestampMillis(msg.getCreatedAt());
                if (readAtMs <= 0 || msgMs <= 0 || msgMs <= readAtMs + 2000L) {
                    msg.setRead(true);
                    notifyItemChanged(i);
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        MessageResponse item = messages.get(position);
        return isOutgoingMessage(item) ? TYPE_OUTGOING : TYPE_INCOMING;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_OUTGOING) {
            ItemMessageOutgoingBinding binding = ItemMessageOutgoingBinding.inflate(inflater, parent, false);
            return new OutgoingViewHolder(binding);
        } else {
            ItemMessageIncomingBinding binding = ItemMessageIncomingBinding.inflate(inflater, parent, false);
            return new IncomingViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageResponse item = messages.get(position);
        if (holder instanceof OutgoingViewHolder) {
            ((OutgoingViewHolder) holder).bind(item);
        } else if (holder instanceof IncomingViewHolder) {
            ((IncomingViewHolder) holder).bind(item);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    // --- ViewHolder tin nhắn gửi đi của Sinh viên ---
    class OutgoingViewHolder extends RecyclerView.ViewHolder {
        private final ItemMessageOutgoingBinding binding;

        public OutgoingViewHolder(ItemMessageOutgoingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(MessageResponse item) {
            binding.tvMessageContent.setText(item.getContent() != null ? item.getContent() : "");
            binding.tvMessageTime.setText(formatMessageTime(item.getCreatedAt()));

            // Trạng thái đã đọc (nếu backend đánh dấu is_read = true thì hiển thị ✓✓ Đã đọc, ngược lại ✓ Đã gửi)
            if (item.isRead()) {
                binding.tvReadStatus.setText("✓✓ Đã đọc");
                binding.tvReadStatus.setTextColor(0xFF38BDF8); // Màu xanh dương nhạt cho đã đọc
            } else {
                binding.tvReadStatus.setText("✓ Đã gửi");
                binding.tvReadStatus.setTextColor(0xFF94A3B8); // Muted slate
            }
        }
    }

    // --- ViewHolder tin nhắn nhận vào từ Chủ trọ ---
    class IncomingViewHolder extends RecyclerView.ViewHolder {
        private final ItemMessageIncomingBinding binding;

        public IncomingViewHolder(ItemMessageIncomingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(MessageResponse item) {
            binding.tvSenderName.setText(partnerName);
            binding.tvMessageContent.setText(item.getContent() != null ? item.getContent() : "");
            binding.tvMessageTime.setText(formatMessageTime(item.getCreatedAt()));

            // Avatar chủ trọ
            if (partnerAvatarUrl != null && !partnerAvatarUrl.trim().isEmpty()) {
                binding.ivSenderAvatar.setVisibility(View.VISIBLE);
                binding.tvSenderAvatarPlaceholder.setVisibility(View.GONE);
                Glide.with(itemView.getContext())
                        .load(partnerAvatarUrl)
                        .circleCrop()
                        .into(binding.ivSenderAvatar);
            } else {
                binding.ivSenderAvatar.setVisibility(View.GONE);
                binding.tvSenderAvatarPlaceholder.setVisibility(View.VISIBLE);
            }
        }
    }

    private static String formatMessageTime(String isoDate) {
        long millis = parseTimestampMillis(isoDate);
        if (millis <= 0) return "";
        try {
            Date date = new Date(millis);
            return new SimpleDateFormat("HH:mm • dd/MM", Locale.getDefault()).format(date);
        } catch (Exception ignored) {}
        return "";
    }
}
