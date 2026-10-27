package com.avaroom.app.data.remote;

import com.avaroom.app.data.model.request.RegisterRequest;
import com.avaroom.app.data.model.response.ApiResponse;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @POST("api/auth/register")
    Call<ApiResponse<Object>> register(@Body RegisterRequest request);

    @POST("api/auth/login")
    Call<ApiResponse<Map<String, Object>>> login(@Body Map<String, String> request);

    @GET("api/auth/me")
    Call<ApiResponse<Map<String, Object>>> getMe(@Header("Authorization") String token);

    // Landlord Posts
    @GET("api/landlord/posts")
    Call<ApiResponse<List<Map<String, Object>>>> getLandlordPosts(@Header("Authorization") String token);

    @POST("api/landlord/posts")
    Call<ApiResponse<Map<String, Object>>> createLandlordPost(@Header("Authorization") String token, @Body Map<String, Object> request);

    @PUT("api/landlord/posts/{id}")
    Call<ApiResponse<Map<String, Object>>> updateLandlordPost(@Header("Authorization") String token, @Path("id") long id, @Body Map<String, Object> request);

    @DELETE("api/landlord/posts/{id}")
    Call<ApiResponse<Object>> deleteLandlordPost(@Header("Authorization") String token, @Path("id") long id);

    // Landlord Appointments
    @GET("api/landlord/appointments")
    Call<ApiResponse<List<Map<String, Object>>>> getLandlordAppointments(@Header("Authorization") String token);

    @PATCH("api/landlord/appointments/{id}/status")
    Call<ApiResponse<Map<String, Object>>> updateAppointmentStatus(@Header("Authorization") String token, @Path("id") long id, @Body Map<String, String> body);

    // Invoices & Billing
    @GET("api/landlord/invoices")
    Call<ApiResponse<List<Map<String, Object>>>> getLandlordInvoices(@Header("Authorization") String token);

    @POST("api/landlord/invoices")
    Call<ApiResponse<Map<String, Object>>> generateInvoice(@Header("Authorization") String token, @Body Map<String, Object> body);

    // Escrow Contracts & Handover
    @GET("api/landlord/contracts")
    Call<ApiResponse<List<Map<String, Object>>>> getLandlordContracts(@Header("Authorization") String token);

    @POST("api/landlord/contracts")
    Call<ApiResponse<Map<String, Object>>> createContract(@Header("Authorization") String token, @Body Map<String, Object> body);

    @PATCH("api/landlord/handover")
    Call<ApiResponse<Map<String, Object>>> updateAssetHandover(@Header("Authorization") String token, @Body Map<String, Object> body);
}
