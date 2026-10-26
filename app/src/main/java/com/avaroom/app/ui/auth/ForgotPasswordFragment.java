package com.avaroom.app.ui.auth;

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
import com.avaroom.app.data.model.request.ForgotPasswordRequest;
import com.avaroom.app.data.model.response.ApiResponse;
import com.avaroom.app.data.remote.RetrofitClient;
import com.avaroom.app.databinding.FragmentForgotPasswordBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordFragment extends Fragment {

    private FragmentForgotPasswordBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Figma Prototype Connection: Button:shadow ==> Xác thực OTP - Avaroom (Warm Cream)
        binding.btnSubmitForgot.setOnClickListener(v -> {
            String email = binding.etForgotEmail.getText() != null ? binding.etForgotEmail.getText().toString().trim() : "";
            if (email.isEmpty()) {
                binding.etForgotEmail.setError("Vui lòng nhập địa chỉ email");
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.etForgotEmail.setError("Email không đúng định dạng");
                return;
            }

            binding.btnSubmitForgot.setEnabled(false);
            ForgotPasswordRequest request = new ForgotPasswordRequest(email);

            RetrofitClient.getApiService().forgotPassword(request).enqueue(new Callback<ApiResponse<Object>>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse<Object>> call, @NonNull Response<ApiResponse<Object>> response) {
                    binding.btnSubmitForgot.setEnabled(true);
                    if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                        Toast.makeText(requireContext(), "Mã OTP đã được gửi đến email của bạn!", Toast.LENGTH_SHORT).show();
                        // Đóng gói email truyền sang OtpFragment
                        Bundle bundle = new Bundle();
                        bundle.putString("EMAIL_KEY", email);
                        Navigation.findNavController(v).navigate(R.id.action_forgotPassword_to_otpFragment, bundle);
                    } else {
                        String errorMsg = response.body() != null ? response.body().getMessage() : "Email không tồn tại trong hệ thống";
                        Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(@NonNull Call<ApiResponse<Object>> call, @NonNull Throwable t) {
                    binding.btnSubmitForgot.setEnabled(true);
                    Toast.makeText(requireContext(), "Lỗi kết nối máy chủ: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
        binding.btnBackForgot.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
