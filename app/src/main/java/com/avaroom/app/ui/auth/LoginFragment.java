package com.avaroom.app.ui.auth;

import android.companion.WifiDeviceFilter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.avaroom.app.R;
import com.avaroom.app.data.model.request.LoginRequest;
import com.avaroom.app.data.model.response.ApiResponse;
import com.avaroom.app.data.model.response.AuthResponse;
import com.avaroom.app.data.remote.RetrofitClient;
import com.avaroom.app.databinding.FragmentLoginBinding;
import com.avaroom.app.ui.landlord.LandlordMainActivity;
import com.avaroom.app.ui.student.StudentMainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        binding.btnLoginSubmit.setOnClickListener(v -> handleLogin());

        // Figma Prototype Connection 2: Button ==> Form nhập email lấy lại mật khẩu
        binding.btnForgotPassword.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_forgotPasswordFragment);
        });

        // Figma Prototype Connection 3: Button ==> Đăng ký - Avaroom (Warm Cream)
        binding.btnGoRegister.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registerFragment);
        });
    }

    private void handleLogin()
    {
        String email = binding.etAccount.getText() != null ? binding.etAccount.getText().toString().trim() : "";
        String password = binding.etPassword.getText() != null ? binding.etPassword.getText().toString().trim() : "";

        if (email.isEmpty()){
            binding.etAccount.setError("Vui lòng nhập email");
            binding.etAccount.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etAccount.setError("Email không đúng định dạng");
            binding.etAccount.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            binding.etPassword.setError("Vui lòng nhập mật khẩu");
            binding.etPassword.requestFocus();
            return;
        }

        binding.btnLoginSubmit.setEnabled(false);

        LoginRequest request = new LoginRequest(email, password);

        RetrofitClient.getApiService().login(request).enqueue(new Callback<ApiResponse<AuthResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<AuthResponse>> call,
                                   @NonNull Response<ApiResponse<AuthResponse>> response) {
                binding.btnLoginSubmit.setEnabled(true);

                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    AuthResponse authData = response.body().getData();

                    if (authData != null && authData.getTokens() != null) {
                        // 4. Lưu Token và Role vào SharedPreferences
                        saveSession(
                                authData.getTokens().getAccessToken(),
                                authData.getTokens().getRefreshToken(),
                                authData.getUser().getRole()
                        );
                        Toast.makeText(requireContext(), "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        // 5. Phân luồng giao diện theo Vai trò (STUDENT vs LANDLORD)
                        navigateByRole(authData.getUser().getRole());
                    }
                } else {
                    String errorMsg = "Email hoặc mật khẩu không chính xác";
                    if (response.code() == 429) {
                        errorMsg = "Bạn đã thử đăng nhập quá nhiều lần. Vui lòng thử lại sau 15 phút!";
                    } else if (response.body() != null && response.body().getMessage() != null) {
                        errorMsg = response.body().getMessage();
                    }
                    Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ApiResponse<AuthResponse>> call, Throwable t) {
                binding.btnLoginSubmit.setEnabled(true);
                Toast.makeText(requireContext(), "Lỗi kết nối máy chủ: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void saveSession(String accessToken, String refreshToken, String role) {
        SharedPreferences pref = requireContext().getSharedPreferences("AvaroomAuth", Context.MODE_PRIVATE);
        pref.edit()
                .putString("ACCESS_TOKEN", accessToken)
                .putString("REFRESH_TOKEN", refreshToken)
                .putString("USER_ROLE", role)
                .apply();
    }
    private void navigateByRole(String role) {
        Intent intent;
        if ("LANDLORD".equalsIgnoreCase(role)) {
            intent = new Intent(requireActivity(), LandlordMainActivity.class);
        } else {
            intent = new Intent(requireActivity(), StudentMainActivity.class);
        }
        startActivity(intent);
        requireActivity().finish(); // Đóng Login flow để không back lại được màn hình login
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
