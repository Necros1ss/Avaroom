package com.avaroom.app.data.remote;

import com.avaroom.app.data.model.request.RegisterRequest;
import com.avaroom.app.data.model.response.ApiResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("api/auth/register")
    Call<ApiResponse<Object>> register(@Body RegisterRequest request);
}
