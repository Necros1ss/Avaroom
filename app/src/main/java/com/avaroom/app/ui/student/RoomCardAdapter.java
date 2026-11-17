package com.avaroom.app.ui.student;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avaroom.app.R;
import com.avaroom.app.data.model.response.RoomResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter hiển thị danh sách phòng trọ tại trang chủ Sinh viên.
 * Dùng RecyclerView với item_room_card.xml
 */
public class RoomCardAdapter extends RecyclerView.Adapter<RoomCardAdapter.RoomViewHolder> {

    public interface OnRoomClickListener {
        void onRoomClick(RoomResponse room);
        void onSaveToggle(RoomResponse room, boolean isSaved);
    }

    private final List<RoomResponse> rooms = new ArrayList<>();
    private final OnRoomClickListener listener;

    public RoomCardAdapter(OnRoomClickListener listener) {
        this.listener = listener;
    }

    public void setRooms(List<RoomResponse> newRooms) {
        rooms.clear();
        if (newRooms != null) rooms.addAll(newRooms);
        notifyDataSetChanged();
    }

    public void appendRooms(List<RoomResponse> moreRooms) {
        if (moreRooms == null || moreRooms.isEmpty()) return;
        int start = rooms.size();
        rooms.addAll(moreRooms);
        notifyItemRangeInserted(start, moreRooms.size());
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_room_card, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        RoomResponse room = rooms.get(position);
        holder.bind(room);
    }

    @Override
    public int getItemCount() { return rooms.size(); }

    // ── ViewHolder ─────────────────────────────────────────────────────────────

    class RoomViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivThumbnail;
        private final TextView tvTitle;
        private final TextView tvAddress;
        private final TextView tvPrice;
        private final TextView tvArea;
        private final TextView tvRating;
        private final TextView tvAmenities;
        private final TextView btnSave;

        RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            ivThumbnail    = itemView.findViewById(R.id.iv_room_thumbnail);
            tvTitle        = itemView.findViewById(R.id.tv_room_title);
            tvAddress      = itemView.findViewById(R.id.tv_room_address);
            tvPrice        = itemView.findViewById(R.id.tv_room_price);
            tvArea         = itemView.findViewById(R.id.tv_room_area);
            tvRating       = itemView.findViewById(R.id.tv_room_rating);
            tvAmenities    = itemView.findViewById(R.id.tv_room_amenities);
            btnSave        = itemView.findViewById(R.id.btn_save_room);
        }

        void bind(RoomResponse room) {
            tvTitle.setText(room.getTitle());
            tvAddress.setText(room.getAddress());
            tvPrice.setText(room.getFormattedPrice());
            tvArea.setText(room.getFormattedArea());
            tvRating.setText(room.getFormattedRating());

            // Tiện nghi
            StringBuilder amenities = new StringBuilder();
            if (room.isHasWifi()) amenities.append("WiFi  ");
            if (room.isHasAc()) amenities.append("Điều hòa  ");
            if (room.isHasParking()) amenities.append("Giữ xe  ");
            if (room.isHasPrivateBathroom()) amenities.append("WC riêng  ");
            if (room.isHasKitchen()) amenities.append("Bếp  ");
            tvAmenities.setText(amenities.length() > 0 ? amenities.toString().trim() : "Không có thông tin tiện nghi");

            // Thumbnail qua Glide
            if (room.getThumbnailUrl() != null && !room.getThumbnailUrl().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(room.getThumbnailUrl())
                        .placeholder(R.drawable.ic_nav_home)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .centerCrop()
                        .into(ivThumbnail);
            } else {
                ivThumbnail.setImageResource(R.drawable.ic_nav_home);
            }

            // Click vào card → chi tiết phòng
            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onRoomClick(room);
            });

            // Click nút lưu ❤️
            if (btnSave != null) {
                btnSave.setOnClickListener(v -> {
                    boolean nowSaved = "❤️".equals(btnSave.getText().toString());
                    btnSave.setText(nowSaved ? "🤍" : "❤️");
                    if (listener != null) listener.onSaveToggle(room, !nowSaved);
                });
            }
        }
    }
}

