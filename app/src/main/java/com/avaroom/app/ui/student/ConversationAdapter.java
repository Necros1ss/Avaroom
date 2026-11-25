package com.avaroom.app.ui.student;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.avaroom.app.R;
import com.avaroom.app.data.model.response.ConversationResponse;
import com.avaroom.app.data.model.response.MessageResponse;
import com.avaroom.app.databinding.ItemConversationBinding;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Adapter hiển thị danh sách cuộc trò chuyện thực tế từ Backend REST API cho Sinh viên.
 */
public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder> {

    public interface OnConversationClickListener {
        void onConversationClick(ConversationResponse conversation);
    }

    private final List<ConversationResponse> items = new ArrayList<>();
    private final OnConversationClickListener listener;

    public ConversationAdapter(OnConversationClickListener listener) {
        this.listener = listener;
    }

    public void setData(List<ConversationResponse> newItems) {
        items.clear();
        if (newItems != null) {
            java.util.Set<Long> seenIds = new java.util.HashSet<>();
            for (ConversationResponse conv : newItems) {
                if (conv != null && conv.getId() > 0 && seenIds.add(conv.getId())) {
                    items.add(conv);
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemConversationBinding binding = ItemConversationBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ConversationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ConversationViewHolder extends RecyclerView.ViewHolder {
        private final ItemConversationBinding binding;

        public ConversationViewHolder(ItemConversationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ConversationResponse item) {
            if (item == null) return;

            // 1. Thông tin đối phương (Partner)
            ConversationResponse.PartnerInfo partner = item.getPartner();
            if (partner != null) {
                String name = partner.getFullName();
                String fallbackName = "STUDENT".equalsIgnoreCase(partner.getRole()) ? "Sinh viên" : "Chủ trọ";
                String avatarEmoji = "STUDENT".equalsIgnoreCase(partner.getRole()) ? "🎓" : "👩‍💼";
                binding.tvPartnerName.setText(name != null && !name.trim().isEmpty() ? name : fallbackName);

                // Avatar URL
                String avatarUrl = partner.getAvatarUrl();
                if (avatarUrl != null && !avatarUrl.trim().isEmpty()) {
                    binding.ivPartnerAvatar.setVisibility(View.VISIBLE);
                    binding.tvAvatarPlaceholder.setVisibility(View.GONE);
                    Glide.with(itemView.getContext())
                            .load(avatarUrl)
                            .circleCrop()
                            .into(binding.ivPartnerAvatar);
                } else {
                    binding.ivPartnerAvatar.setVisibility(View.GONE);
                    binding.tvAvatarPlaceholder.setVisibility(View.VISIBLE);
                    binding.tvAvatarPlaceholder.setText(avatarEmoji);
                }
            } else {
                binding.tvPartnerName.setText("Người dùng");
                binding.ivPartnerAvatar.setVisibility(View.GONE);
                binding.tvAvatarPlaceholder.setVisibility(View.VISIBLE);
                binding.tvAvatarPlaceholder.setText("👤");
            }

            // 2. Thông tin phòng trọ đính kèm (Room Info Chip)
            ConversationResponse.RoomInfo room = item.getRoom();
            if (room != null && room.getTitle() != null && !room.getTitle().trim().isEmpty()) {
                binding.layoutRoomChip.setVisibility(View.VISIBLE);
                String roomText = (room.getId() > 0 ? "#AVR-" + room.getId() + " • " : "") + room.getTitle();
                binding.tvRoomInfo.setText(roomText);
            } else {
                binding.layoutRoomChip.setVisibility(View.GONE);
            }

            // 3. Tin nhắn gần nhất (Last Message) & số tin chưa đọc (Unread Count Badge)
            int unreadCount = item.getUnreadCount();
            MessageResponse lastMsg = item.getLastMessage();
            if (lastMsg != null && lastMsg.getContent() != null && !lastMsg.getContent().trim().isEmpty()) {
                binding.tvLastMessage.setText(lastMsg.getContent());
                if (unreadCount > 0) {
                    binding.tvLastMessage.setTypeface(null, Typeface.BOLD);
                    binding.tvLastMessage.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.student_text_primary));
                } else {
                    binding.tvLastMessage.setTypeface(null, Typeface.NORMAL);
                    binding.tvLastMessage.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.student_text_secondary));
                }
            } else {
                binding.tvLastMessage.setText("Chưa có tin nhắn");
                binding.tvLastMessage.setTypeface(null, Typeface.ITALIC);
                binding.tvLastMessage.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.student_text_muted));
            }

            // 4. Số tin chưa đọc (Unread Count Badge)
            if (unreadCount > 0) {
                binding.tvUnreadBadge.setVisibility(View.VISIBLE);
                binding.tvUnreadBadge.setText(unreadCount > 99 ? "99+" : String.valueOf(unreadCount));
            } else {
                binding.tvUnreadBadge.setVisibility(View.GONE);
            }

            // 5. Thời gian tin nhắn cuối cùng (Timestamp)
            String rawTimestamp = (lastMsg != null && lastMsg.getCreatedAt() != null)
                    ? lastMsg.getCreatedAt()
                    : item.getLastMessageAt();
            if (rawTimestamp == null || rawTimestamp.isEmpty()) {
                rawTimestamp = item.getCreatedAt();
            }
            binding.tvTimestamp.setText(formatTimestamp(rawTimestamp));

            // 6. Xử lý click chuyển sang ChatFragment
            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onConversationClick(item);
                }
            });
        }

        private String formatTimestamp(String isoDate) {
            if (isoDate == null || isoDate.trim().isEmpty()) return "";
            try {
                // Backend trả ISO-8601 UTC (vd: 2026-11-25T19:07:18.646Z) -> parse theo UTC để đổi về giờ máy
                String cleanDate = isoDate.trim();
                if (cleanDate.endsWith("Z")) {
                    cleanDate = cleanDate.substring(0, cleanDate.length() - 1);
                }
                int plusIdx = cleanDate.indexOf('+');
                if (plusIdx > 0) {
                    cleanDate = cleanDate.substring(0, plusIdx);
                }
                if (cleanDate.contains(".")) {
                    cleanDate = cleanDate.substring(0, cleanDate.indexOf('.'));
                }

                SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
                isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date date = isoFormat.parse(cleanDate);
                if (date != null) {
                    return new SimpleDateFormat("HH:mm • dd/MM", Locale.getDefault()).format(date);
                }
            } catch (Exception ignored) {
                // Xử lý an toàn không để crash khi chuỗi thời gian bất thường
            }
            return "";
        }
    }
}
