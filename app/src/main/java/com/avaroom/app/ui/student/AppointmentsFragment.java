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

import com.avaroom.app.databinding.FragmentAppointmentsBinding;

public class AppointmentsFragment extends Fragment {

    private FragmentAppointmentsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAppointmentsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Click Back
        binding.btnBackAppointments.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        // Click Call Host
        binding.btnCallHost.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Đang kết nối cuộc gọi tới chủ trọ...", Toast.LENGTH_SHORT).show();
        });

        // Click Directions
        binding.btnDirections.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Đang mở bản đồ chỉ đường tới Số 18 Ngõ 40 Tạ Quang Bửu...", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
