package com.avaroom.app.ui.student;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avaroom.app.R;
import com.avaroom.app.data.model.response.AppointmentResponse;
import com.avaroom.app.databinding.ItemAppointmentCardBinding;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    public interface OnAppointmentClickListener {
        void onDetailClick(AppointmentResponse appointment);
        void onRoomClick(long roomId);
        void onCancelClick(AppointmentResponse appointment);
        void onCallClick(String phone);
        void onChatClick(AppointmentResponse appointment);
    }

    private final List<AppointmentResponse> items = new ArrayList<>();
    private final OnAppointmentClickListener listener;

    public AppointmentAdapter(OnAppointmentClickListener listener) {
        this.listener = listener;
    }

    public void setData(List<AppointmentResponse> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAppointmentCardBinding binding = ItemAppointmentCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new AppointmentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class AppointmentViewHolder extends RecyclerView.ViewHolder {
        private final ItemAppointmentCardBinding binding;

        public AppointmentViewHolder(ItemAppointmentCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(AppointmentResponse item) {
            binding.tvItemCode.setText("#AVR-TOUR-" + item.getId());

            // Status format
            String status = item.getStatus() != null ? item.getStatus() : "PENDING";
            if ("CONFIRMED".equalsIgnoreCase(status)) {
                binding.tvItemStatus.setText("✓ Chủ trọ đã xác nhận");
                binding.tvItemStatus.setBackgroundResource(R.drawable.student_landlord_bg_commit_card);
                binding.tvItemStatus.setTextColor(0xFF047857);
                binding.btnItemCancel.setVisibility(View.VISIBLE);
            } else if ("PENDING".equalsIgnoreCase(status)) {
                binding.tvItemStatus.setText("🟠 Đang chờ xác nhận");
                binding.tvItemStatus.setBackgroundResource(R.drawable.student_success_bg_badge_yellow);
                binding.tvItemStatus.setTextColor(0xFF92400E);
                binding.btnItemCancel.setVisibility(View.VISIBLE);
            } else if ("COMPLETED".equalsIgnoreCase(status)) {
                binding.tvItemStatus.setText("✓ Đã hoàn tất");
                binding.tvItemStatus.setBackgroundResource(R.drawable.student_bg_chip_outline);
                binding.tvItemStatus.setTextColor(0xFF1D4ED8);
                binding.btnItemCancel.setVisibility(View.GONE);
            } else if ("CANCELLED".equalsIgnoreCase(status)) {
                binding.tvItemStatus.setText("Đã hủy");
                binding.tvItemStatus.setBackgroundResource(R.drawable.student_bg_chip_outline);
                binding.tvItemStatus.setTextColor(0xFFB51822);
                binding.btnItemCancel.setVisibility(View.GONE);
            } else if ("REJECTED".equalsIgnoreCase(status)) {
                binding.tvItemStatus.setText("Bị từ chối");
                binding.tvItemStatus.setBackgroundResource(R.drawable.student_bg_chip_outline);
                binding.tvItemStatus.setTextColor(0xFFB51822);
                binding.btnItemCancel.setVisibility(View.GONE);
            }

            // Room Info
            if (item.getRoom() != null) {
                binding.tvItemRoomTitle.setText(item.getRoom().getTitle());
                binding.tvItemRoomAddress.setText(item.getRoom().getAddress());
                if (item.getRoom().getThumbnailUrl() != null && !item.getRoom().getThumbnailUrl().isEmpty()) {
                    Glide.with(itemView.getContext())
                            .load(item.getRoom().getThumbnailUrl())
                            .placeholder(R.drawable.student_img_room_1)
                            .centerCrop()
                            .into(binding.ivItemRoomThumb);
                }
            } else {
                binding.tvItemRoomTitle.setText("Phòng trọ");
                binding.tvItemRoomAddress.setText("Thông tin phòng");
            }

            // Date & Time badge
            String timeText = (item.getStartTime() != null ? item.getStartTime() : "") +
                    " • " + formatDisplayDate(item.getAppointmentDate());
            binding.tvItemTimeBadge.setText(timeText);

            // Landlord info
            if (item.getLandlord() != null) {
                binding.tvItemLandlordName.setText(item.getLandlord().getFullName() != null ? item.getLandlord().getFullName() : "Chủ trọ");
                binding.tvItemLandlordPhone.setText(item.getLandlord().getPhoneNumber() != null ? item.getLandlord().getPhoneNumber() : "Đã xác thực");
                if (item.getLandlord().getAvatarUrl() != null && !item.getLandlord().getAvatarUrl().isEmpty()) {
                    Glide.with(itemView.getContext())
                            .load(item.getLandlord().getAvatarUrl())
                            .placeholder(R.drawable.student_bg_chip_dark)
                            .centerCrop()
                            .into(binding.ivItemLandlordAvatar);
                }
            } else {
                binding.tvItemLandlordName.setText("Chủ trọ");
                binding.tvItemLandlordPhone.setText("Đã xác thực");
            }

            // Listeners
            binding.btnItemDetail.setOnClickListener(v -> {
                if (listener != null) listener.onDetailClick(item);
            });

            binding.layoutItemRoom.setOnClickListener(v -> {
                if (listener != null && item.getRoom() != null) {
                    listener.onRoomClick(item.getRoom().getId());
                }
            });

            binding.btnItemCancel.setOnClickListener(v -> {
                if (listener != null) listener.onCancelClick(item);
            });

            binding.btnItemCall.setOnClickListener(v -> {
                if (listener != null && item.getLandlord() != null) {
                    listener.onCallClick(item.getLandlord().getPhoneNumber());
                }
            });

            binding.btnItemChat.setOnClickListener(v -> {
                if (listener != null) listener.onChatClick(item);
            });
        }

        private String formatDisplayDate(String dateYmd) {
            if (dateYmd == null) return "";
            try {
                SimpleDateFormat inFmt = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date d = inFmt.parse(dateYmd);
                if (d != null) {
                    SimpleDateFormat outFmt = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    return outFmt.format(d);
                }
            } catch (Exception ignored) {
            }
            return dateYmd;
        }
    }
}
