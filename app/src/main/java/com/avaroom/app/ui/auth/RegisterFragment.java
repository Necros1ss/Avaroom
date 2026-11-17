package com.avaroom.app.ui.auth;

import android.graphics.Color;
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
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.avaroom.app.R;
import com.avaroom.app.data.model.request.RegisterRequest;
import com.avaroom.app.data.model.response.ApiResponse;
import com.avaroom.app.data.remote.RetrofitClient;
import com.avaroom.app.databinding.FragmentRegisterBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.widget.TextView;
import android.widget.Toast;

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

        // Role switcher giữa 2 Card Sinh viên & Chủ trọ
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

        binding.btnRegisterSubmit.setOnClickListener(v -> {
            String role = isLandlordRole ? "LANDLORD" : "STUDENT";

            String fullName = binding.etFullname.getText().toString().trim();
            String phone = binding.etPhone.getText().toString().trim();
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString();
            String confirmPass = binding.etConfirmPassword.getText().toString();

            String address = null;
            if (isLandlordRole) {
                address = binding.etAddress.getText().toString().trim();
                if (address.isEmpty()) {
                    binding.etAddress.setError("Vui lòng nhập địa chỉ khu trọ");
                    return;
                }
            }

            if (fullName.isEmpty()) {
                binding.etFullname.setError("Vui lòng nhập họ và tên");
                return;
            }
            if (email.isEmpty()) {
                binding.etEmail.setError("Vui lòng nhập email");
                return;
            }
            if (password.length() < 6) {
                binding.etPassword.setError("Mật khẩu tối thiểu 6 ký tự");
                return;
            }
            if (!password.equals(confirmPass)) {
                binding.etConfirmPassword.setError("Mật khẩu xác nhận không khớp");
                return;
            }
            binding.btnRegisterSubmit.setEnabled(false);
            RegisterRequest request = new RegisterRequest(fullName, email, password, phone, role, address);

            RetrofitClient.getApiService().register(request).enqueue(new Callback<ApiResponse<Object>>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse<Object>> call, @NonNull Response<ApiResponse<Object>> response) {
                    binding.btnRegisterSubmit.setEnabled(true);
                    if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                        Toast.makeText(requireContext(), "Mã OTP đã được gửi về email của bạn!", Toast.LENGTH_SHORT).show();
                        // Đóng gói email, role và flow type truyền sang OtpFragment
                        Bundle bundle = new Bundle();
                        bundle.putString("EMAIL_KEY", email);
                        bundle.putString("OTP_FLOW", "REGISTER");
                        bundle.putString("ROLE_KEY", role);
                        Navigation.findNavController(v).navigate(R.id.action_register_to_otpFragment, bundle);
                    } else {
                        String errorMsg = response.body() != null ? response.body().getMessage() : "Đăng ký thất bại";
                        try {
                            if (response.errorBody() != null) {
                                String errJson = response.errorBody().string();
                                org.json.JSONObject obj = new org.json.JSONObject(errJson);
                                if (obj.has("message")) errorMsg = obj.getString("message");
                            }
                        } catch (Exception ignored) {}
                        Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(@NonNull Call<ApiResponse<Object>> call, @NonNull Throwable t) {
                    binding.btnRegisterSubmit.setEnabled(true);
                    Toast.makeText(requireContext(), "Lỗi kết nối máy chủ: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Go back & Đăng nhập ==> LoginFragment
        binding.btnBackRegister.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());
        binding.btnGoLogin.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_register_to_loginFragment);
        });
    }

    private void updateRoleCardState(CardView card, TextView icon, TextView title, TextView subtitle, boolean isSelected) {
        if (isSelected) {
            card.setCardBackgroundColor(Color.parseColor("#1A202C"));
            title.setTextColor(Color.parseColor("#FFFFFF"));
            subtitle.setTextColor(Color.parseColor("#D1D5DB"));
            icon.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else {
            card.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            title.setTextColor(Color.parseColor("#1A202C"));
            subtitle.setTextColor(Color.parseColor("#718096"));
            icon.setBackgroundColor(Color.parseColor("#F3F4F6"));
        }
    }
    private void setRole(boolean landlord) {
        isLandlordRole = landlord;
        updateRoleCardState(
                binding.btnRoleLandlord,
                binding.ivRoleLandlordIcon,
                binding.tvRoleLandlordTitle,
                binding.tvRoleLandlordSubtitle,
                landlord
        );
        updateRoleCardState(
                binding.btnRoleStudent,
                binding.ivRoleStudentIcon,
                binding.tvRoleStudentTitle,
                binding.tvRoleStudentSubtitle,
                !landlord
        );
        if (landlord) {
            binding.layoutLandlordFields.setVisibility(View.VISIBLE);
            binding.btnRoleLandlord.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primary_black));
            binding.btnRoleStudent.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bg_white));
            binding.tvTitleReg.setText("Tạo tài khoản\nchủ trọ.");
            binding.tvSubtitleReg.setText("Quản lý phòng trọ chuyên nghiệp, tiếp cận hàng nghìn sinh viên tìm phòng uy tín.");
            binding.tvLabelFullname.setText("Họ và tên chủ trọ *");
            binding.etFullname.setHint("Nguyễn Văn A");

            binding.tvLabelEmail.setText("Email liên hệ / quản lý *");
            binding.etEmail.setHint("chutro@gmail.com");
        } else {
            binding.layoutLandlordFields.setVisibility(View.GONE);
            binding.btnRoleStudent.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primary_black));
            binding.btnRoleLandlord.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bg_white));
            binding.tvTitleReg.setText("Tạo tài khoản\nsinh viên.");
            binding.tvSubtitleReg.setText("Nhận ngay gói hỗ trợ bảo vệ cọc 100% và ưu đãi giảm cọc khi xác thực thẻ sinh viên.");

            // Cập nhật nhãn và gợi ý form cho Sinh viên
            binding.tvLabelFullname.setText("Họ và tên sinh viên *");
            binding.etFullname.setHint("Nguyễn Văn A");

            binding.tvLabelEmail.setText("Email sinh viên *");
            binding.etEmail.setHint("example@gmail.edu.vn");
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
