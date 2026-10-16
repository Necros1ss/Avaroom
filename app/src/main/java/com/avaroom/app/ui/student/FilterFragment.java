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
import com.avaroom.app.databinding.FragmentFilterBinding;

public class FilterFragment extends Fragment {

    private FragmentFilterBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFilterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Figma Prototype Connection: Close / Cancel ==> Quay lại trang chủ
        binding.btnBackFilter.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        // Reset filter
        binding.btnResetFilter.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        // Figma Prototype Connection: Áp dụng bộ lọc ==> Kết quả tìm kiếm (Search Result)
        binding.btnApplyFilter.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_filter_to_searchResult);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}