package com.avaroom.app.data.remote;

import com.avaroom.app.data.model.request.ForgotPasswordRequest;
import com.avaroom.app.data.model.request.LoginRequest;
import com.avaroom.app.data.model.request.RegisterRequest;
import com.avaroom.app.data.model.request.ResetPasswordRequest;
import com.avaroom.app.data.model.request.VerifyOtpRequest;
import com.avaroom.app.data.model.response.ApiResponse;
import com.avaroom.app.data.model.response.AuthResponse;
import com.avaroom.app.data.model.response.EkycResponse;
import com.avaroom.app.data.model.response.VerifyOtpResponse;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

import com.avaroom.app.data.model.response.RoomResponse;

public interface ApiService {

    // ==================== AUTH ENDPOINTS (from main) ====================
    @POST("api/auth/register")
    Call<ApiResponse<Object>> register(@Body RegisterRequest request);

    @POST("api/auth/login")
    Call<ApiResponse<AuthResponse>> login(@Body LoginRequest request);

    // Overload login hỗ trợ Map request nếu có màn hình nào cần
    @POST("api/auth/login")
    Call<ApiResponse<Map<String, Object>>> loginWithMap(@Body Map<String, String> request);

    @POST("api/auth/forgot-password")
    Call<ApiResponse<Object>> forgotPassword(@Body ForgotPasswordRequest request);

    @POST("api/auth/verify-otp")
    Call<ApiResponse<VerifyOtpResponse>> verifyOtp(@Body VerifyOtpRequest request);

    @POST("api/auth/reset-password")
    Call<ApiResponse<Object>> resetPassword(@Body ResetPasswordRequest request);

    @GET("api/auth/me")
    Call<ApiResponse<Map<String, Object>>> getMe(@Header("Authorization") String token);

    @GET("api/student/saved-rooms")
    Call<ApiResponse<List<RoomResponse>>> getSavedRooms(
            @Header("Authorization") String token,
            @Query("page") int page,
            @Query("limit") int limit
    );

    @GET("api/student/saved-rooms/check/{roomId}")
    Call<Map<String, Object>> checkRoomSaved(
            @Header("Authorization") String token,
            @Path("roomId") long roomId
    );
    /** Lưu phòng trọ */
    @POST("api/student/saved-rooms/{roomId}")
    Call<ApiResponse<Object>> saveRoom(
            @Header("Authorization") String token,
            @Path("roomId") long roomId
    );
    /** Bỏ lưu phòng trọ */
    @DELETE("api/student/saved-rooms/{roomId}")
    Call<ApiResponse<Object>> removeSavedRoom(
            @Header("Authorization") String token,
            @Path("roomId") long roomId
    );

    // ==================== STUDENT APPOINTMENTS ====================
    /** Kiểm tra xem sinh viên đã đặt lịch xem phòng này chưa */
    @GET("api/student/appointments/check/{roomId}")
    Call<Map<String, Object>> checkStudentRoomAppointment(
            @Header("Authorization") String token,
            @Path("roomId") long roomId
    );

    /** Lấy danh sách khung giờ đã có người đặt trong ngày */
    @GET("api/student/appointments/occupied-slots")
    Call<com.avaroom.app.data.model.response.AppointmentResponse.OccupiedSlotsData> getOccupiedSlots(
            @Header("Authorization") String token,
            @Query("roomId") long roomId,
            @Query("date") String date
    );

    /** Sinh viên tạo lịch hẹn xem phòng */
    @POST("api/student/appointments")
    Call<ApiResponse<com.avaroom.app.data.model.response.AppointmentResponse>> createAppointment(
            @Header("Authorization") String token,
            @Body com.avaroom.app.data.model.request.CreateAppointmentRequest request
    );

    /** Sinh viên lấy danh sách lịch hẹn của mình */
    @GET("api/student/appointments")
    Call<com.avaroom.app.data.model.response.AppointmentResponse.AppointmentsListData> getStudentAppointments(
            @Header("Authorization") String token,
            @Query("status") String status,
            @Query("page") int page,
            @Query("limit") int limit
    );

    /** Chi tiết lịch hẹn */
    @GET("api/student/appointments/{id}")
    Call<ApiResponse<com.avaroom.app.data.model.response.AppointmentResponse>> getStudentAppointmentDetail(
            @Header("Authorization") String token,
            @Path("id") long id
    );

    /** Hủy lịch hẹn */
    @PATCH("api/student/appointments/{id}/cancel")
    Call<ApiResponse<Object>> cancelStudentAppointment(
            @Header("Authorization") String token,
            @Path("id") long id,
            @Body Map<String, String> body
    );

    // ==================== LANDLORD ENDPOINTS (from PuuUpdate) ====================
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

    @Multipart
    @POST("api/landlord/ekyc")
    Call<ApiResponse<EkycResponse>> uploadLandlordEkyc(
            @Header("Authorization") String token,
            @Part MultipartBody.Part frontImage,
            @Part MultipartBody.Part backImage
    );

    // ==================== ROOM ENDPOINTS ====================

    /**
     * Lấy danh sách phòng trọ AVAILABLE (public, không cần token)
     * Query: page, limit, district, min_price, max_price, room_type, keyword
     * has_wifi, has_ac, has_parking, has_private_bathroom, has_kitchen, has_security
     */
    @GET("api/rooms")
    Call<ApiResponse<RoomResponse.RoomsData>> getRooms(
            @retrofit2.http.QueryMap Map<String, String> filters
    );

    /** Chi tiết một phòng trọ theo ID */
    @GET("api/rooms/{id}")
    Call<ApiResponse<RoomResponse>> getRoomById(@Path("id") long id);

    /** Chủ trọ tạo bài đăng phòng mới */
    @POST("api/rooms")
    Call<ApiResponse<RoomResponse>> createRoom(
            @Header("Authorization") String token,
            @Body Map<String, Object> body
    );

    /** Chủ trọ cập nhật bài đăng phòng */
    @PUT("api/rooms/{id}")
    Call<ApiResponse<RoomResponse>> updateRoom(
            @Header("Authorization") String token,
            @Path("id") long id,
            @Body Map<String, Object> body
    );

    /** Chủ trọ xóa bài đăng phòng */
    @DELETE("api/rooms/{id}")
    Call<ApiResponse<Object>> deleteRoom(
            @Header("Authorization") String token,
            @Path("id") long id
    );

    /** Chủ trọ lấy danh sách phòng của mình */
    @GET("api/rooms/landlord/my-rooms")
    Call<ApiResponse<List<RoomResponse>>> getMyRooms(@Header("Authorization") String token);
}
