package com.avaroom.app.ui.auth;

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
import com.avaroom.app.data.model.request.ResetPasswordRequest;
import com.avaroom.app.data.model.response.ApiResponse;
import com.avaroom.app.data.remote.RetrofitClient;
import com.avaroom.app.databinding.FragmentResetPasswordBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordFragment extends Fragment {

    private FragmentResetPasswordBinding binding;
    private String resetToken = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentResetPasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Nhận token từ màn hình Otp
        if (getArguments() != null) {
            resetToken = getArguments().getString("RESET_TOKEN_KEY", "");
        }

        binding.btnSavePassword.setOnClickListener(v -> {
            String newPassword = binding.etNewPassword.getText() != null ? binding.etNewPassword.getText().toString() : "";
            String confirmPassword = binding.etConfirmPassword.getText() != null ? binding.etConfirmPassword.getText().toString() : "";

            if (newPassword.length() < 6) {
                binding.etNewPassword.setError("Mật khẩu phải từ 6 ký tự trở lên");
                return;
            }
            if (!newPassword.equals(confirmPassword)) {
                binding.etConfirmPassword.setError("Mật khẩu xác nhận không khớp");
                return;
            }

            binding.btnSavePassword.setEnabled(false);
            ResetPasswordRequest request = new ResetPasswordRequest(resetToken, newPassword);

            RetrofitClient.getApiService().resetPassword(request).enqueue(new Callback<ApiResponse<Object>>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse<Object>> call, @NonNull Response<ApiResponse<Object>> response) {
                    binding.btnSavePassword.setEnabled(true);
                    if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                        Toast.makeText(requireContext(), "Đổi mật khẩu thành công! Vui lòng đăng nhập lại.", Toast.LENGTH_LONG).show();

                        // Quay trở lại màn hình Đăng nhập
                        Navigation.findNavController(v).popBackStack(R.id.loginFragment, false);
                    } else {
                        Toast.makeText(requireContext(), "Phiên đặt lại mật khẩu đã hết hạn, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse<Object>> call, @NonNull Throwable t) {
                    binding.btnSavePassword.setEnabled(true);
                    Toast.makeText(requireContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        binding.btnBackReset.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}