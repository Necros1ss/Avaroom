package com.avaroom.app.ui.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.avaroom.app.data.model.request.VerifyOtpRequest;
import com.avaroom.app.data.model.response.ApiResponse;
import com.avaroom.app.data.model.response.VerifyOtpResponse;
import com.avaroom.app.data.remote.RetrofitClient;
import com.avaroom.app.databinding.FragmentOtpBinding;
import com.avaroom.app.ui.landlord.LandlordMainActivity;
import com.avaroom.app.ui.student.StudentMainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpFragment extends Fragment {
    private FragmentOtpBinding binding;
    private String email = "";
    private String otpFlow = "FORGOT_PASSWORD"; // "REGISTER" hoặc "FORGOT_PASSWORD"
    private String role = "STUDENT";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOtpBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            email = getArguments().getString("EMAIL_KEY", "");
            otpFlow = getArguments().getString("OTP_FLOW", "FORGOT_PASSWORD");
            role = getArguments().getString("ROLE_KEY", "STUDENT");
        }

        // Tùy chỉnh tiêu đề theo luồng
        if ("REGISTER".equals(otpFlow)) {
            binding.tvTitleOtp.setText("Kích hoạt tài khoản");
            binding.tvSubOtp.setText("Mã xác thực 6 số đã được gửi tới " + email + ". Vui lòng nhập để hoàn tất đăng ký.");
        } else {
            binding.tvTitleOtp.setText("Xác thực OTP");
            binding.tvSubOtp.setText("Mã xác thực khôi phục mật khẩu đã được gửi tới " + email + ".");
        }

        binding.btnConfirmOtp.setOnClickListener(v -> {
            String otp = binding.etOtpCode.getText() != null ? binding.etOtpCode.getText().toString().trim() : "";
            if (otp.length() < 6) {
                Toast.makeText(requireContext(), "Vui lòng nhập đủ mã OTP 6 số", Toast.LENGTH_SHORT).show();
                return;
            }
            binding.btnConfirmOtp.setEnabled(false);
            VerifyOtpRequest request = new VerifyOtpRequest(email, otp);

            RetrofitClient.getApiService().verifyOtp(request).enqueue(new Callback<ApiResponse<VerifyOtpResponse>>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse<VerifyOtpResponse>> call, @NonNull Response<ApiResponse<VerifyOtpResponse>> response) {
                    binding.btnConfirmOtp.setEnabled(true);
                    if (response.isSuccessful() && response.body() != null && response.body().isSuccess() && response.body().getData() != null) {
                        VerifyOtpResponse resData = response.body().getData();

                        if ("REGISTER".equals(otpFlow)) {
                            // LUỒNG 1: ĐĂNG KÝ
                            // Lưu session nếu server trả về token
                            if (resData.getTokens() != null) {
                                String userRole = resData.getUser() != null ? resData.getUser().getRole() : role;
                                saveSession(resData.getTokens().getAccessToken(), resData.getTokens().getRefreshToken(), userRole);
                            }

                            Toast.makeText(requireContext(), "Xác thực tài khoản thành công!", Toast.LENGTH_SHORT).show();

                            // Điều hướng vào màn hình chính phù hợp theo Role
                            Intent intent;
                            if ("LANDLORD".equalsIgnoreCase(role)) {
                                intent = new Intent(requireActivity(), LandlordMainActivity.class);
                            } else {
                                intent = new Intent(requireActivity(), StudentMainActivity.class);
                            }
                            startActivity(intent);
                            requireActivity().finish();

                        } else {
                            // LUỒNG 2: QUÊN MẬT KHẨU
                            // Chuyển sang màn hình Đặt lại mật khẩu với reset_token
                            String resetToken = resData.getResetToken();
                            Bundle bundle = new Bundle();
                            bundle.putString("RESET_TOKEN_KEY", resetToken);
                            Navigation.findNavController(v).navigate(R.id.action_otpFragment_to_resetPasswordFragment, bundle);
                        }
                    } else {
                        String errMsg = "Mã OTP không đúng hoặc đã hết hạn";
                        if (response.body() != null && response.body().getMessage() != null) {
                            errMsg = response.body().getMessage();
                        }
                        Toast.makeText(requireContext(), errMsg, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse<VerifyOtpResponse>> call, @NonNull Throwable t) {
                    binding.btnConfirmOtp.setEnabled(true);
                    Toast.makeText(requireContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        binding.btnBackOtp.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());
    }

    private void saveSession(String accessToken, String refreshToken, String userRole) {
        SharedPreferences pref = requireContext().getSharedPreferences("AvaroomAuth", Context.MODE_PRIVATE);
        pref.edit()
                .putString("ACCESS_TOKEN", accessToken)
                .putString("REFRESH_TOKEN", refreshToken)
                .putString("USER_ROLE", userRole)
                .apply();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
