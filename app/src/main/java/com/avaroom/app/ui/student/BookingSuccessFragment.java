package com.avaroom.app.ui.student;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.avaroom.app.R;
import com.avaroom.app.data.model.response.ApiResponse;
import com.avaroom.app.data.model.response.AppointmentResponse;
import com.avaroom.app.data.model.response.RoomResponse;
import com.avaroom.app.data.remote.RetrofitClient;
import com.avaroom.app.databinding.FragmentBookingSuccessBinding;
import com.avaroom.app.utils.TokenManager;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingSuccessFragment extends Fragment {

    private static final String TAG = "BookingSuccessFragment";
    private FragmentBookingSuccessBinding binding;

    private AppointmentResponse appointmentData;
    private RoomResponse roomData;
    private String selectedDate;
    private String selectedTime;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBookingSuccessBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Nhận arguments truyền sang
        if (getArguments() != null) {
            appointmentData = (AppointmentResponse) getArguments().getSerializable("appointment_data");
            roomData = (RoomResponse) getArguments().getSerializable("room_data");
            selectedDate = getArguments().getString("selected_date");
            selectedTime = getArguments().getString("selected_time");
        }

        // 2. Hiển thị dữ liệu lên giao diện
        bindData();

        // 3. Nếu appointmentData thiếu thông tin chi tiết room/landlord, gọi API fetch chi tiết
        if (appointmentData != null && (appointmentData.getRoom() == null || appointmentData.getLandlord() == null)) {
            fetchAppointmentDetail(appointmentData.getId());
        }

        // 4. Setup navigation
        binding.btnCloseSuccess.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_bookingSuccess_to_home);
        });

        binding.btnManageAppointments.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_bookingSuccess_to_appointments);
        });
    }

    private void bindData() {
        if (binding == null) return;

        // Mã yêu cầu
        if (appointmentData != null && appointmentData.getId() > 0) {
            binding.tvSuccessAppointmentCode.setText("#AVR-TOUR-" + appointmentData.getId());
        } else if (roomData != null) {
            binding.tvSuccessAppointmentCode.setText("#AVR-ROOM-" + roomData.getId());
        }

        // Trạng thái
        String status = (appointmentData != null && appointmentData.getStatus() != null)
                ? appointmentData.getStatus() : "PENDING";
        if ("CONFIRMED".equalsIgnoreCase(status)) {
            binding.tvSuccessAppointmentStatus.setText("🟢 Đã xác nhận");
            binding.tvSuccessAppointmentStatus.setBackgroundResource(R.drawable.student_success_bg_badge_green);
            binding.tvSuccessAppointmentStatus.setTextColor(0xFF047857);
        } else if ("REJECTED".equalsIgnoreCase(status) || "CANCELLED".equalsIgnoreCase(status)) {
            binding.tvSuccessAppointmentStatus.setText("🔴 " + status);
            binding.tvSuccessAppointmentStatus.setBackgroundResource(R.drawable.student_bg_chip_red);
            binding.tvSuccessAppointmentStatus.setTextColor(0xFFB51822);
        } else {
            binding.tvSuccessAppointmentStatus.setText("🟠 Chờ xác nhận");
            binding.tvSuccessAppointmentStatus.setBackgroundResource(R.drawable.student_success_bg_badge_yellow);
            binding.tvSuccessAppointmentStatus.setTextColor(0xFF92400E);
        }

        // Thông tin phòng
        String roomTitle = null;
        double roomPrice = 0;
        String roomAddress = null;
        String roomThumb = null;

        if (appointmentData != null && appointmentData.getRoom() != null) {
            roomTitle = appointmentData.getRoom().getTitle();
            roomPrice = appointmentData.getRoom().getPrice();
            roomAddress = appointmentData.getRoom().getAddress();
            roomThumb = appointmentData.getRoom().getThumbnailUrl();
        } else if (roomData != null) {
            roomTitle = roomData.getTitle();
            roomPrice = roomData.getPrice();
            roomAddress = roomData.getAddress();
            roomThumb = roomData.getThumbnailUrl();
        }

        if (roomTitle != null && !roomTitle.isEmpty()) {
            binding.tvSuccessRoomTitle.setText(roomTitle);
        }
        if (roomPrice > 0) {
            double priceInMillion = roomPrice / 1_000_000.0;
            if (priceInMillion == (long) priceInMillion) {
                binding.tvSuccessRoomPrice.setText(String.format(Locale.getDefault(), "%.0f tr/tháng", priceInMillion));
            } else {
                binding.tvSuccessRoomPrice.setText(String.format(Locale.getDefault(), "%.1f tr/tháng", priceInMillion));
            }
        }
        if (roomAddress != null && !roomAddress.isEmpty()) {
            binding.tvSuccessRoomAddress.setText("📍 " + roomAddress);
        }
        if (roomThumb != null && !roomThumb.isEmpty()) {
            Glide.with(this)
                    .load(roomThumb)
                    .placeholder(R.drawable.student_img_room_1)
                    .centerCrop()
                    .into(binding.ivSuccessRoomThumb);
        }

        // Thông tin chủ trọ
        String landlordName = null;
        String landlordPhone = null;
        String landlordAvatar = null;

        if (appointmentData != null && appointmentData.getLandlord() != null) {
            landlordName = appointmentData.getLandlord().getFullName();
            landlordPhone = appointmentData.getLandlord().getPhoneNumber();
            landlordAvatar = appointmentData.getLandlord().getAvatarUrl();
        } else if (roomData != null && roomData.getLandlord() != null) {
            landlordName = roomData.getLandlord().getFullName();
            landlordPhone = roomData.getLandlord().getPhoneNumber();
            landlordAvatar = roomData.getLandlord().getAvatarUrl();
        }

        if (landlordName != null && !landlordName.isEmpty()) {
            binding.tvSuccessLandlordName.setText(landlordName);
            binding.tvSuccessLandlordWaiting.setText("Đang chờ " + landlordName + " xác nhận lịch hẹn của bạn");
        } else {
            binding.tvSuccessLandlordName.setText("Chủ trọ");
            binding.tvSuccessLandlordWaiting.setText("Đang chờ chủ trọ xác nhận lịch hẹn của bạn");
        }

        if (landlordPhone != null && !landlordPhone.isEmpty()) {
            binding.tvSuccessLandlordPhone.setText("Chủ trọ • " + landlordPhone);
        } else {
            binding.tvSuccessLandlordPhone.setText("Chủ trọ • Đã xác thực");
        }

        if (landlordAvatar != null && !landlordAvatar.isEmpty()) {
            Glide.with(this)
                    .load(landlordAvatar)
                    .placeholder(R.drawable.student_bg_chip_dark)
                    .centerCrop()
                    .into(binding.ivSuccessLandlordAvatar);
        }

        // Thời gian hẹn
        String dateStr = (appointmentData != null && appointmentData.getAppointmentDate() != null)
                ? appointmentData.getAppointmentDate() : selectedDate;
        String timeStr = (appointmentData != null && appointmentData.getStartTime() != null)
                ? appointmentData.getStartTime() : selectedTime;

        if (dateStr != null && timeStr != null) {
            String formattedDateTime = formatDateTimeDisplay(dateStr, timeStr);
            binding.tvSuccessTime.setText(formattedDateTime);
            binding.tvStep3Time.setText(timeStr + " " + formatDateShort(dateStr));
        }

        // Tiến trình step 1: Giờ gửi hiện tại
        SimpleDateFormat nowFmt = new SimpleDateFormat("HH:mm 'hôm nay'", Locale.getDefault());
        binding.tvStep1Time.setText(nowFmt.format(new Date()));
    }

    private String formatDateTimeDisplay(String dateYmd, String timeHhMm) {
        try {
            SimpleDateFormat inFmt = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date d = inFmt.parse(dateYmd);
            if (d != null) {
                SimpleDateFormat outFmt = new SimpleDateFormat("EEEE, dd/MM/yyyy", new Locale("vi", "VN"));
                return timeHhMm + " • " + outFmt.format(d);
            }
        } catch (Exception e) {
            Log.e(TAG, "formatDateTimeDisplay error: " + e.getMessage());
        }
        return timeHhMm + " • " + dateYmd;
    }

    private String formatDateShort(String dateYmd) {
        try {
            SimpleDateFormat inFmt = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date d = inFmt.parse(dateYmd);
            if (d != null) {
                Calendar c = Calendar.getInstance();
                c.setTime(d);
                int dow = c.get(Calendar.DAY_OF_WEEK);
                return (dow == Calendar.SUNDAY) ? "Chủ Nhật" : "Thứ " + dow;
            }
        } catch (Exception e) {
            Log.e(TAG, "formatDateShort error: " + e.getMessage());
        }
        return dateYmd;
    }

    private void fetchAppointmentDetail(long id) {
        String token = TokenManager.getToken(requireContext());
        if (token == null || token.isEmpty()) return;

        RetrofitClient.getApiService().getStudentAppointmentDetail(token, id)
                .enqueue(new Callback<ApiResponse<AppointmentResponse>>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse<AppointmentResponse>> call, @NonNull Response<ApiResponse<AppointmentResponse>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                            appointmentData = response.body().getData();
                            bindData();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse<AppointmentResponse>> call, @NonNull Throwable t) {
                        Log.e(TAG, "Lỗi lấy chi tiết lịch hẹn: " + t.getMessage());
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
