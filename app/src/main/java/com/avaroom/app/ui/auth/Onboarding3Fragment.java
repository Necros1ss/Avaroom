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
import com.avaroom.app.databinding.FragmentOnboarding3Binding;

public class Onboarding3Fragment extends Fragment {

    private FragmentOnboarding3Binding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOnboarding3Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Figma Prototype Connection 1: Button - Primary CTA: Register ==> Đăng ký - Avaroom (Warm Cream)
        binding.btnOnboardingRegister.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_onboarding3_to_registerFragment);
        });

        // Figma Prototype Connection 2: Button - Secondary CTA: Sign In ==> Đăng nhập - Avaroom (Warm Cream)
        binding.btnOnboardingLogin.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_onboarding3_to_loginFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
