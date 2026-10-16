package com.avaroom.app.ui.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.avaroom.app.R;
import com.avaroom.app.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private boolean isLandlordRole = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Figma Prototype Connection: Role switcher
        binding.btnRoleLandlord.setOnClickListener(v -> {
            isLandlordRole = true;
            binding.btnRoleLandlord.setBackgroundResource(R.drawable.bg_button_primary);
            binding.btnRoleLandlord.setTextColor(getResources().getColor(R.color.bg_white, null));
            binding.btnRoleStudent.setBackground(null);
            binding.btnRoleStudent.setTextColor(getResources().getColor(R.color.text_secondary, null));
        });

        binding.btnRoleStudent.setOnClickListener(v -> {
            isLandlordRole = false;
            binding.btnRoleStudent.setBackgroundResource(R.drawable.bg_button_primary);
            binding.btnRoleStudent.setTextColor(getResources().getColor(R.color.bg_white, null));
            binding.btnRoleLandlord.setBackground(null);
            binding.btnRoleLandlord.setTextColor(getResources().getColor(R.color.text_secondary, null));
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
