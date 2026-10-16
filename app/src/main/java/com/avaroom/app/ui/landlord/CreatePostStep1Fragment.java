package com.avaroom.app.ui.landlord;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.avaroom.app.R;
import com.avaroom.app.databinding.FragmentCreatePostStep1Binding;

public class CreatePostStep1Fragment extends Fragment {

    private FragmentCreatePostStep1Binding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCreatePostStep1Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Figma Prototype Connection: Button - Quay lại ==> Quản lý tin đăng - Chủ trọ
        binding.btnBackStep1.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        // Save draft
        binding.btnSaveDraft.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Đã lưu nháp tin đăng!", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(v).navigateUp();
        });

        // Figma Prototype Connection: Container ==> Đăng tin - Bước 2: Tiện ích & Nội thất
        binding.btnNextStep2.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_step1_to_step2);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
