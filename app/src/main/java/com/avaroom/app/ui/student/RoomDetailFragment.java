package com.avaroom.app.ui.student;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.avaroom.app.R;
import com.avaroom.app.data.model.response.ApiResponse;
import com.avaroom.app.data.model.response.RoomResponse;
import com.avaroom.app.data.remote.RetrofitClient;
import com.avaroom.app.databinding.FragmentRoomDetailBinding;
import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomDetailFragment extends Fragment {

    private static final String TAG = "RoomDetail";

    private FragmentRoomDetailBinding binding;
    private boolean isSaved = false;
    private long currentRoomId = -1;
    private RoomResponse currentRoom = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRoomDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Lấy room_id truyền từ danh sách phòng
        if (getArguments() != null) {
            currentRoomId = getArguments().getLong("room_id", -1);
        }

        setupStaticListeners();

        // 2. Tải thông tin phòng từ API nếu có roomId hợp lệ
        if (currentRoomId > 0) {
            loadRoomDetail(currentRoomId);
        } else {
            Log.w(TAG, "Không nhận được room_id hợp lệ, hiển thị mặc định");
        }
    }

    private void loadRoomDetail(long roomId) {
        RetrofitClient.getApiService().getRoomById(roomId)
                .enqueue(new Callback<ApiResponse<RoomResponse>>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse<RoomResponse>> call, @NonNull Response<ApiResponse<RoomResponse>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                            currentRoom = response.body().getData();
                            bindRoomData(currentRoom);
                        } else {
                            Log.e(TAG, "Lỗi nạp chi tiết phòng: HTTP " + response.code());
                            Toast.makeText(requireContext(), "Không thể tải chi tiết phòng trọ", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse<RoomResponse>> call, @NonNull Throwable t) {
                        Log.e(TAG, "Lỗi kết nối getRoomById: " + t.getMessage(), t);
                        Toast.makeText(requireContext(), "Lỗi kết nối khi tải chi tiết phòng", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void bindRoomData(RoomResponse room) {
        if (binding == null || room == null) return;

        // Tiêu đề & Địa chỉ
        if (binding.tvDetailTitle != null) {
            binding.tvDetailTitle.setText(room.getTitle());
        }
        if (binding.tvDetailAddress != null) {
            binding.tvDetailAddress.setText(room.getAddress());
        }

        // Giá tiền format "3.800.000"
        if (binding.tvDetailPrice != null) {
            NumberFormat numFmt = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
            binding.tvDetailPrice.setText(numFmt.format(room.getPrice()));
        }

        // Diện tích
        if (binding.tvDetailArea != null) {
            binding.tvDetailArea.setText(room.getFormattedArea());
        }

        // Loại phòng
        if (binding.tvDetailType != null && room.getRoomType() != null) {
            String typeName = room.getRoomType();
            switch (room.getRoomType()) {
                case "PRIVATE_ROOM": typeName = "Phòng riêng"; break;
                case "MINI_APARTMENT": typeName = "Chung cư mini"; break;
                case "WHOLE_HOUSE": typeName = "Nhà nguyên căn"; break;
                case "DORMITORY": typeName = "Ký túc xá"; break;
                case "SHARED_ROOM": typeName = "Phòng ở ghép"; break;
            }
            binding.tvDetailType.setText(typeName);
        }

        // Thông tin chủ trọ
        if (room.getLandlord() != null && binding.tvLandlordName != null) {
            binding.tvLandlordName.setText(room.getLandlord().getFullName());
        }

        // Danh sách ảnh gallery
        List<String> images = room.getImageUrls();
        if (images != null && !images.isEmpty()) {
            ImageView[] galleryViews = {binding.ivGallery0, binding.ivGallery1, binding.ivGallery2, binding.ivGallery3, binding.ivGallery4};
            for (int i = 0; i < galleryViews.length; i++) {
                if (galleryViews[i] != null) {
                    if (i < images.size()) {
                        Glide.with(this)
                                .load(images.get(i))
                                .placeholder(R.drawable.student_img_room_1)
                                .centerCrop()
                                .into(galleryViews[i]);
                    } else if (!images.isEmpty()) {
                        // Nếu ít hơn 5 ảnh, lặp lại ảnh đầu
                        Glide.with(this)
                                .load(images.get(0))
                                .placeholder(R.drawable.student_img_room_1)
                                .centerCrop()
                                .into(galleryViews[i]);
                    }
                }
            }
        } else if (room.getThumbnailUrl() != null && binding.ivGallery0 != null) {
            Glide.with(this)
                    .load(room.getThumbnailUrl())
                    .placeholder(R.drawable.student_img_room_1)
                    .centerCrop()
                    .into(binding.ivGallery0);
        }
    }

    private void setupStaticListeners() {
        // Nút quay lại
        binding.btnBackDetail.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        // Chỉnh chiều rộng ảnh gallery khớp với màn hình
        binding.scrollGallery.post(() -> {
            int width = binding.scrollGallery.getWidth();
            if (width > 0) {
                View[] galleryImages = {binding.ivGallery0, binding.ivGallery1, binding.ivGallery2, binding.ivGallery3, binding.ivGallery4};
                for (View img : galleryImages) {
                    if (img != null) {
                        ViewGroup.LayoutParams params = img.getLayoutParams();
                        params.width = width;
                        img.setLayoutParams(params);
                    }
                }
            }
        });

        // Nút lưu phòng
        if (binding.btnSaveDetail != null) {
            binding.btnSaveDetail.setOnClickListener(v -> {
                isSaved = !isSaved;
                ViewGroup group = (ViewGroup) binding.btnSaveDetail;
                if (group.getChildCount() > 0 && group.getChildAt(0) instanceof TextView) {
                    TextView tv = (TextView) group.getChildAt(0);
                    tv.setText(isSaved ? "❤️" : "🤍");
                }
                String msg = isSaved ? "Đã lưu phòng vào danh sách yêu thích" : "Đã bỏ lưu phòng";
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
            });
        }

        // Click thumbnail để cuộn gallery
        if (binding.cardThumb0 != null && binding.ivGallery0 != null) {
            binding.cardThumb0.setOnClickListener(v -> binding.scrollGallery.smoothScrollTo(binding.ivGallery0.getLeft(), 0));
        }
        if (binding.cardThumb1 != null && binding.ivGallery1 != null) {
            binding.cardThumb1.setOnClickListener(v -> binding.scrollGallery.smoothScrollTo(binding.ivGallery1.getLeft(), 0));
        }
        if (binding.cardThumb2 != null && binding.ivGallery2 != null) {
            binding.cardThumb2.setOnClickListener(v -> binding.scrollGallery.smoothScrollTo(binding.ivGallery2.getLeft(), 0));
        }
        if (binding.cardThumb3 != null && binding.ivGallery3 != null) {
            binding.cardThumb3.setOnClickListener(v -> binding.scrollGallery.smoothScrollTo(binding.ivGallery3.getLeft(), 0));
        }
        if (binding.cardThumb4 != null && binding.ivGallery4 != null) {
            binding.cardThumb4.setOnClickListener(v -> binding.scrollGallery.smoothScrollTo(binding.ivGallery4.getLeft(), 0));
        }

        // Nhắn chủ trọ
        binding.btnChatHost.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_roomDetail_to_chat);
        });

        // Đặt lịch xem phòng
        binding.btnBookSchedule.setOnClickListener(v -> {
            Bundle args = new Bundle();
            if (currentRoomId > 0) args.putLong("room_id", currentRoomId);
            Navigation.findNavController(v).navigate(R.id.action_roomDetail_to_booking, args);
        });

        // Trang thông tin chủ trọ
        binding.cardLandlordInfo.setOnClickListener(v -> {
            Bundle args = new Bundle();
            if (currentRoom != null && currentRoom.getLandlord() != null) {
                args.putLong("landlord_id", currentRoom.getLandlord().getId());
            }
            Navigation.findNavController(v).navigate(R.id.action_roomDetail_to_landlordProfile, args);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
