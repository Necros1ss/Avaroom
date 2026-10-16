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
import com.avaroom.app.R;
import com.avaroom.app.databinding.FragmentLoginBinding;
import com.avaroom.app.ui.student.StudentMainActivity;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Figma Prototype Connection 1: Button - Primary Action CTA ==> Trang chủ - Tìm trọ sinh viên
        binding.btnLoginSubmit.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), StudentMainActivity.class);
            startActivity(intent);
        });

        // Figma Prototype Connection 2: Button ==> Form nhập email lấy lại mật khẩu
        binding.btnForgotPassword.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_forgotPasswordFragment);
        });

        // Figma Prototype Connection 3: Button ==> Đăng ký - Avaroom (Warm Cream)
        binding.btnGoRegister.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registerFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
