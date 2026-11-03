package com.avaroom.app.ui.student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.avaroom.app.R;
import com.avaroom.app.databinding.FragmentRoomDetailBinding;

public class RoomDetailFragment extends Fragment {

    private FragmentRoomDetailBinding binding;
    private boolean isSaved = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRoomDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Figma Prototype Connection: Back button ==> Trang chủ
        binding.btnBackDetail.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        // Adjust gallery image widths to match gallery container width dynamically to prevent stretching/blurriness
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

        // Save / Unsave room button (heart icon color toggle)
        if (binding.btnSaveDetail != null) {
            binding.btnSaveDetail.setOnClickListener(v -> {
                isSaved = !isSaved;
                ViewGroup group = (ViewGroup) binding.btnSaveDetail;
                if (group.getChildCount() > 0 && group.getChildAt(0) instanceof TextView) {
                    TextView tv = (TextView) group.getChildAt(0);
                    tv.setText(isSaved ? "❤️" : "🤍");
                }
            });
        }

        // Thumbnail click listeners to auto-scroll gallery above
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

        // Figma Prototype Connection: Button - Left: Chat with Host ==> Tin nhắn & Chat với chủ trọ
        binding.btnChatHost.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_roomDetail_to_chat);
        });

        // Figma Prototype Connection: Button - Right: Book Viewing ==> Đặt lịch xem phòng
        binding.btnBookSchedule.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_roomDetail_to_booking);
        });

        // Navigate to Landlord Profile
        binding.cardLandlordInfo.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_roomDetail_to_landlordProfile);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}