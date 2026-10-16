package com.avaroom.app.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.avaroom.app.databinding.FragmentResetPasswordBinding;
import com.avaroom.app.ui.student.StudentMainActivity;

public class ResetPasswordFragment extends Fragment {

    private FragmentResetPasswordBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentResetPasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Figma Prototype Connection: Đặt mật khẩu mới ==> Trang chủ - Tìm trọ sinh viên
        binding.btnSavePassword.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), StudentMainActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        // Figma Prototype Connection: Button - Go back ==> OtpFragment
        binding.btnBackReset.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
