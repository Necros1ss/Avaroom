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
import com.avaroom.app.data.model.request.VerifyOtpRequest;
import com.avaroom.app.data.model.response.VerifyOtpResponse;
import com.avaroom.app.databinding.FragmentOtpBinding;
import android.widget.Toast;
import com.avaroom.app.data.model.response.ApiResponse;
import com.avaroom.app.data.remote.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpFragment extends Fragment {

    private FragmentOtpBinding binding;
    private String email = "";

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
        }

        // Figma Prototype Connection: Confirm OTP ==> Đặt mật khẩu mới hoặc eKYC CCCD
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
                    if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                        String resetToken = response.body().getData().getResetToken();
                        // Truyền reset_token sang màn hình đặt mật khẩu mới
                        Bundle bundle = new Bundle();
                        bundle.putString("RESET_TOKEN_KEY", resetToken);
                        Navigation.findNavController(v).navigate(R.id.action_otpFragment_to_resetPasswordFragment, bundle);
                    } else {
                        Toast.makeText(requireContext(), "Mã OTP không đúng hoặc đã hết hạn", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(@NonNull Call<ApiResponse<VerifyOtpResponse>> call, @NonNull Throwable t) {
                    binding.btnConfirmOtp.setEnabled(true);
                    Toast.makeText(requireContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Figma Prototype Connection: Go back
        binding.btnBackOtp.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
