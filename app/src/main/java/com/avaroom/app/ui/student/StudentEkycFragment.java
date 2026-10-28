package com.avaroom.app.ui.student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.avaroom.app.databinding.FragmentStudentEkycBinding;

public class StudentEkycFragment extends Fragment {

    private FragmentStudentEkycBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStudentEkycBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Click Back
        binding.btnBackEkyc.setOnClickListener(v -> {
            Navigation.findNavController(v).navigateUp();
        });

        // Click Confirm
        binding.btnConfirmEkyc.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Hồ sơ eKYC sinh viên đã được xác nhận!", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(v).navigateUp();
        });

        // Click Update student card
        binding.btnUpdateCard.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Vui lòng chụp lại ảnh thẻ sinh viên để cập nhật kỳ mới", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
