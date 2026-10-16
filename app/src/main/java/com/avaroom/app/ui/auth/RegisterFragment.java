package com.avaroom.app.ui.auth;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.avaroom.app.R;
import com.avaroom.app.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private boolean isLandlordRole = false;
    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Figma Prototype Connection: Role switcher giữa 2 Card Sinh viên & Chủ trọ
        binding.btnRoleStudent.setOnClickListener(v -> setRole(false));
        binding.btnRoleLandlord.setOnClickListener(v -> setRole(true));

        // Toggle ẩn/hiện mật khẩu
        binding.btnTogglePass.setOnClickListener(v -> {
            isPasswordVisible = !isPasswordVisible;
            if (isPasswordVisible) {
                binding.etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                binding.etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            binding.etPassword.setSelection(binding.etPassword.getText().length());
        });

        binding.btnToggleConfirmPass.setOnClickListener(v -> {
            isConfirmPasswordVisible = !isConfirmPasswordVisible;
            if (isConfirmPasswordVisible) {
                binding.etConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                binding.etConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            binding.etConfirmPassword.setSelection(binding.etConfirmPassword.getText().length());
        });

        // Thanh đo độ an toàn mật khẩu (Password Strength Bar)
        binding.etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updatePasswordStrength(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Figma Prototype Connection: Primary CTA Button ==> Xác thực OTP hoặc eKYC CCCD
        binding.btnRegisterSubmit.setOnClickListener(v -> {
            if (isLandlordRole) {
                Navigation.findNavController(v).navigate(R.id.action_register_to_ekycFragment);
            } else {
                Navigation.findNavController(v).navigate(R.id.action_register_to_otpFragment);
            }
        });

        // Figma Prototype Connection: Button - Go back & Đăng nhập ==> LoginFragment
        binding.btnBackRegister.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());
        binding.btnGoLogin.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_register_to_loginFragment);
        });
    }

    private void setRole(boolean landlord) {
        isLandlordRole = landlord;
        if (landlord) {
            binding.btnRoleLandlord.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primary_black));
            binding.btnRoleStudent.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bg_white));
            binding.tvTitleReg.setText("Tạo tài khoản\nchủ trọ.");
        } else {
            binding.btnRoleStudent.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primary_black));
            binding.btnRoleLandlord.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bg_white));
            binding.tvTitleReg.setText("Tạo tài khoản\nsinh viên.");
        }
    }

    private void updatePasswordStrength(String pass) {
        int colorActive = ContextCompat.getColor(requireContext(), R.color.accent_green);
        int colorDefault = ContextCompat.getColor(requireContext(), R.color.border_subtle);

        int len = pass.length();
        binding.strengthBar1.setBackgroundColor(len >= 2 ? colorActive : colorDefault);
        binding.strengthBar2.setBackgroundColor(len >= 6 ? colorActive : colorDefault);
        binding.strengthBar3.setBackgroundColor(len >= 8 ? colorActive : colorDefault);
        binding.strengthBar4.setBackgroundColor(len >= 10 && pass.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*") ? colorActive : colorDefault);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
