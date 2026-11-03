package com.avaroom.app.ui.student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.avaroom.app.R;
import com.avaroom.app.databinding.FragmentFilterBinding;
import android.widget.FrameLayout;

public class FilterFragment extends Fragment {

    private FragmentFilterBinding binding;
    private final int[] radiusProgress = {200, 700, 1700, 2700};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFilterBinding.inflate(inflater, container, false);
        binding.layoutTargetUniversity.setOnClickListener(v -> showUniversityOptions());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Figma Prototype Connection: Close / Cancel ==> Quay lại trang chủ
        binding.btnBackFilter.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        initRoomOptions();
        initAmenityOptions();
        initRadiusSlider();
        initPriceOptions();

        // Reset filters
        binding.btnResetFilter.setOnClickListener(v -> resetAllFilters());
        // Figma Prototype Connection: Áp dụng bộ lọc ==> Kết quả tìm kiếm (Search Result)
        binding.btnApplyFilter.setOnClickListener(v -> {Navigation.findNavController(v).navigate(R.id.action_filter_to_searchResult);
        });
    }
    // Handle filter type selection
    private void initRoomOptions() {

        View.OnClickListener toggleFilterType = v -> {

            boolean isSelected = Boolean.TRUE.equals(v.getTag());
            boolean newSelected = !isSelected;

            v.setTag(newSelected);

            if (newSelected) {
                v.setBackgroundResource(R.drawable.student_filter_bg_card_dark);
            } else {
                v.setBackgroundResource(R.drawable.student_filter_bg_card_white);
            }

            int textColor = ContextCompat.getColor(
                    requireContext(),
                    newSelected ? R.color.student_white : R.color.student_filter_text_dark);
            if (v instanceof ViewGroup) {updateTextColors((ViewGroup) v, textColor);}
        };

        binding.btnFilterRoomPrivate.setOnClickListener(toggleFilterType);
        binding.btnFilterRoomMini.setOnClickListener(toggleFilterType);
        binding.btnFilterRoomDorm.setOnClickListener(toggleFilterType);
        binding.btnFilterRoomApt.setOnClickListener(toggleFilterType);
        binding.btnFilterRoomShared.setOnClickListener(toggleFilterType);
    }
    private void initRadiusSlider() {
        // Giá trị từ 300m đến 5000m
        binding.seekRadius.setMax(4700);
        // Mặc định 1.5km
        binding.seekRadius.setProgress(1200);
        // Lắng nghe khi kéo thanh bán kính
        binding.seekRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int radius = 300 + progress;
                // Cập nhật số bán kính phía trên
                if (radius >= 1000) {
                    binding.textRadiusValue.setText(
                            String.format(
                                    java.util.Locale.getDefault(),
                                    "%.1f km",
                                    radius / 1000f));
                } else {
                    binding.textRadiusValue.setText(radius + " m");
                }
                // Nếu người dùng tự kéo thanh trượt,
                // cập nhật trạng thái 4 lựa chọn nhanh
                if (fromUser) {
                    updateRadiusSelection(progress);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        binding.radius500m.setOnClickListener(v -> selectRadiusPreset(binding.radius500m, 0));
        binding.radius1km.setOnClickListener(v -> selectRadiusPreset(binding.radius1km, 1));
        binding.radius2km.setOnClickListener(v -> selectRadiusPreset(binding.radius2km, 2));
        binding.radius3km.setOnClickListener(v -> selectRadiusPreset(binding.radius3km, 3));
    }
    private void selectRadiusPreset(TextView selectedView, int index) {
        TextView[] radiusViews = {binding.radius500m, binding.radius1km, binding.radius2km, binding.radius3km};
        for (TextView item : radiusViews) {
            boolean selected = item == selectedView;

            item.setTag(selected);
            updateRadiusAppearance(item, selected);
        }
        binding.seekRadius.setProgress(radiusProgress[index]);
    }
    private void updateRadiusSelection(int progress) {

        TextView[] radiusViews = {binding.radius500m, binding.radius1km, binding.radius2km, binding.radius3km};
        for (int i = 0; i < radiusViews.length; i++) {
            boolean selected = progress == radiusProgress[i];
            radiusViews[i].setTag(selected);
            updateRadiusAppearance(radiusViews[i], selected);
        }
    }
    private void updateRadiusAppearance(
            TextView view,
            boolean selected) {
        if (selected) {
            view.setBackgroundResource(R.drawable.student_filter_bg_card_dark);
            view.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.student_white));
        } else {
            view.setBackgroundResource(R.drawable.student_filter_bg_card_white);
            view.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.student_filter_text_dark));
        }
    }
    private void resetAllFilters() {
        // Bỏ chọn các loại phòng
        View[] roomViews = {binding.btnFilterRoomPrivate, binding.btnFilterRoomMini, binding.btnFilterRoomDorm, binding.btnFilterRoomApt, binding.btnFilterRoomShared};
        for (View item : roomViews) {
            item.setTag(false);
            item.setBackgroundResource(R.drawable.student_filter_bg_card_white);

            if (item instanceof ViewGroup) {
                updateTextColors((ViewGroup) item,
                        ContextCompat.getColor(requireContext(), R.color.student_filter_text_dark));
            }
        }

        // Bỏ chọn các mức bán kính nhanh
        TextView[] radiusViews = {binding.radius500m, binding.radius1km, binding.radius2km, binding.radius3km};

        for (TextView item : radiusViews) {
            item.setTag(false);
            updateRadiusAppearance(item, false);
        }

        // Đưa thanh kéo về 1.5km
        binding.seekRadius.setProgress(1200);
        selectPricePreset(1);
        // Bỏ chọn tất cả tiện ích
        View[] amenityViews = {binding.amenity247, binding.amenityNoOwner, binding.amenityEv, binding.amenityCooking, binding.amenityFridge, binding.amenityBalcony, binding.amenityWifi};
        for (View item : amenityViews) {
            item.setTag(false);
            item.setBackgroundResource(R.drawable.student_filter_bg_card_white);

            if (item instanceof ViewGroup) {
                updateTextColors((ViewGroup) item,
                        ContextCompat.getColor(requireContext(), R.color.student_filter_text_dark)
                );
            } else if (item instanceof TextView) {
                ((TextView) item).setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.student_filter_text_dark)
                );
            }
        }
    }
    private void updateTextColors(ViewGroup group, int color) {
        for (int i = 0; i < group.getChildCount(); i++) {
            View child = group.getChildAt(i);

            if (child instanceof TextView) {
                ((TextView) child).setTextColor(color);
            } else if (child instanceof ViewGroup) {
                updateTextColors((ViewGroup) child, color);
            }
        }
    }

    private void initPriceOptions() {
        binding.priceUnder25.setOnClickListener(v -> selectPricePreset(0));
        binding.price254.setOnClickListener(v -> selectPricePreset(1));
        binding.priceOver4.setOnClickListener(v -> selectPricePreset(2));
        // Mặc định chọn mức 2.5 - 4 triệu
        selectPricePreset(1);
    }
    private void selectPricePreset(int index) {
        TextView[] priceViews = {binding.priceUnder25, binding.price254, binding.priceOver4};
        String[] minPrices = {"0đ", "2.500.000đ", "4.000.000đ"};
        String[] maxPrices = {"2.500.000đ", "4.000.000đ", "8.000.000đ+"};
        // Vị trí bắt đầu của từng khoảng giá
        float[] startRatios = {0f, 0.3125f, 0.5f};
        // Chiều rộng của từng khoảng giá
        float[] widthRatios = {0.3125f, 0.1875f, 0.5f};
        // Cập nhật màu các nút
        for (int i = 0; i < priceViews.length; i++) {
            boolean selected = (i == index);

            priceViews[i].setTag(selected);
            updateRadiusAppearance(priceViews[i], selected);
        }
        // Cập nhật giá thấp nhất và cao nhất
        binding.textPriceMin.setText(minPrices[index]);
        binding.textPriceMax.setText(maxPrices[index]);
        // Di chuyển thanh màu
        binding.layoutPriceTrack.post(() -> {
            if (binding == null) {
                return;
            }

            int trackWidth = binding.layoutPriceTrack.getWidth();
            int startMargin = (int) (trackWidth * startRatios[index]);
            int progressWidth = (int) (trackWidth * widthRatios[index]);

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(progressWidth, ViewGroup.LayoutParams.MATCH_PARENT);
            params.gravity = android.view.Gravity.START;
            params.setMarginStart(startMargin);

            binding.viewPriceProgress.setLayoutParams(params);
        });
    }

    private void initAmenityOptions() {
        View.OnClickListener toggleAmenity = v -> {
            // Lấy trạng thái hiện tại
            boolean isSelected = Boolean.TRUE.equals(v.getTag());
            // Đảo trạng thái
            boolean newSelected = !isSelected;
            v.setTag(newSelected);
            // Đổi màu nền
            v.setBackgroundResource(
                    newSelected ? R.drawable.student_filter_bg_card_dark : R.drawable.student_filter_bg_card_white);

            // Đổi màu chữ
            int textColor = ContextCompat.getColor(requireContext(), newSelected ? R.color.student_white : R.color.student_filter_text_dark);
            if (v instanceof ViewGroup) {
                updateTextColors((ViewGroup) v, textColor);
            } else if (v instanceof TextView) {
                ((TextView) v).setTextColor(textColor);
            }
        };

        binding.amenity247.setOnClickListener(toggleAmenity);
        binding.amenityNoOwner.setOnClickListener(toggleAmenity);
        binding.amenityEv.setOnClickListener(toggleAmenity);
        binding.amenityCooking.setOnClickListener(toggleAmenity);
        binding.amenityFridge.setOnClickListener(toggleAmenity);
        binding.amenityBalcony.setOnClickListener(toggleAmenity);
        binding.amenityWifi.setOnClickListener(toggleAmenity);
    }
    private void showUniversityOptions() {
        String[] universities = {
                "Đại học Bách khoa Hà Nội"
        };
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Chọn trường đại học")
                .setItems(universities, (dialog, which) -> {
                    String selectedUniversity = universities[which];
                    binding.tvTargetUniversity.setText(selectedUniversity);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}