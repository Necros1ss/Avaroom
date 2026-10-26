package com.avaroom.app.data.remote;

import com.avaroom.app.data.model.request.ForgotPasswordRequest;
import com.avaroom.app.data.model.request.LoginRequest;
import com.avaroom.app.data.model.request.RegisterRequest;
import com.avaroom.app.data.model.request.ResetPasswordRequest;
import com.avaroom.app.data.model.request.VerifyOtpRequest;
import com.avaroom.app.data.model.response.ApiResponse;
import com.avaroom.app.data.model.response.AuthResponse;
import com.avaroom.app.data.model.response.VerifyOtpResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("api/auth/register")
    Call<ApiResponse<Object>> register(@Body RegisterRequest request);
    // 1. Gửi yêu cầu quên mật khẩu (nhận mã OTP qua email)
    @POST("api/auth/forgot-password")
    Call<ApiResponse<Object>> forgotPassword(@Body ForgotPasswordRequest request);
    // 2. Xác thực OTP (nhận về reset_token)
    @POST("api/auth/verify-otp")
    Call<ApiResponse<VerifyOtpResponse>> verifyOtp(@Body VerifyOtpRequest request);
    // 3. Đặt lại mật khẩu mới
    @POST("api/auth/reset-password")
    Call<ApiResponse<Object>> resetPassword(@Body ResetPasswordRequest request);

    @POST("api/auth/login")
    Call<ApiResponse<AuthResponse>> login(@Body LoginRequest request);
}
