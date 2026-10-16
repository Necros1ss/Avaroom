package com.avaroom.app.ui.student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.avaroom.app.R;
import com.avaroom.app.databinding.FragmentMapBinding;

public class MapFragment extends Fragment {

    private FragmentMapBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Click Xem phòng trên floating card chuyển sang Chi tiết phòng trọ
        if (binding.btnViewRoomDetail != null) {
            binding.btnViewRoomDetail.setOnClickListener(v -> {
                Navigation.findNavController(v).navigate(R.id.action_map_to_roomDetail);
            });
        }

        // Click Card Room nổi
        if (binding.cardFloatingRoom != null) {
            binding.cardFloatingRoom.setOnClickListener(v -> {
                Navigation.findNavController(v).navigate(R.id.action_map_to_roomDetail);
            });
        }

        // Click Lọc mở Bộ lọc tìm kiếm
        if (binding.btnFilterMap != null) {
            binding.btnFilterMap.setOnClickListener(v -> {
                Navigation.findNavController(v).navigate(R.id.action_map_to_filter);
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}