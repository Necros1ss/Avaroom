package com.avaroom.app.ui.student;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.avaroom.app.R;
import com.avaroom.app.data.model.response.ApiResponse;
import com.avaroom.app.data.model.response.AppointmentResponse;
import com.avaroom.app.data.remote.RetrofitClient;
import com.avaroom.app.databinding.FragmentStudentAppointmentsBinding;
import com.avaroom.app.utils.TokenManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentsFragment extends Fragment implements AppointmentAdapter.OnAppointmentClickListener {

    private static final String TAG = "AppointmentsFragment";
    private FragmentStudentAppointmentsBinding binding;
    private AppointmentAdapter adapter;

    private final List<AppointmentResponse> allAppointments = new ArrayList<>();
    private int currentTab = 0; // 0: Sắp tới, 1: Hoàn thành, 2: Đã hủy

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStudentAppointmentsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Nút quay lại
        if (binding.btnBackAppointments != null) {
            binding.btnBackAppointments.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());
        }

        // Setup RecyclerView
        adapter = new AppointmentAdapter(this);
        binding.rvAppointments.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvAppointments.setAdapter(adapter);

        // Setup SwipeRefresh
        binding.swipeRefreshAppointments.setOnRefreshListener(this::loadAppointments);

        // Tab switching logic
        if (binding.btnTabUpcoming != null && binding.btnTabCompleted != null && binding.btnTabCancelled != null) {
            binding.btnTabUpcoming.setOnClickListener(v -> selectTab(0));
            binding.btnTabCompleted.setOnClickListener(v -> selectTab(1));
            binding.btnTabCancelled.setOnClickListener(v -> selectTab(2));
        }

        // Tải dữ liệu từ backend
        loadAppointments();
    }

    private void loadAppointments() {
        String token = TokenManager.getToken(requireContext());
        if (token == null || token.isEmpty()) {
            Toast.makeText(requireContext(), "Vui lòng đăng nhập để xem lịch hẹn", Toast.LENGTH_SHORT).show();
            binding.swipeRefreshAppointments.setRefreshing(false);
            return;
        }

        binding.progressLoadingAppointments.setVisibility(View.VISIBLE);

        RetrofitClient.getApiService().getStudentAppointments(token, null, 1, 50)
                .enqueue(new Callback<AppointmentResponse.AppointmentsListData>() {
                    @Override
                    public void onResponse(@NonNull Call<AppointmentResponse.AppointmentsListData> call, @NonNull Response<AppointmentResponse.AppointmentsListData> response) {
                        if (binding == null) return;
                        binding.progressLoadingAppointments.setVisibility(View.GONE);
                        binding.swipeRefreshAppointments.setRefreshing(false);

                        allAppointments.clear();
                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                            allAppointments.addAll(response.body().getData());
                        }

                        updateBadges();
                        filterDataByTab();
                    }

                    @Override
                    public void onFailure(@NonNull Call<AppointmentResponse.AppointmentsListData> call, @NonNull Throwable t) {
                        if (binding == null) return;
                        binding.progressLoadingAppointments.setVisibility(View.GONE);
                        binding.swipeRefreshAppointments.setRefreshing(false);
                        Log.e(TAG, "Lỗi tải lịch hẹn: " + t.getMessage());
                        Toast.makeText(requireContext(), "Lỗi kết nối khi tải lịch hẹn", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateBadges() {
        if (binding == null) return;
        int upcomingCount = 0;
        int completedCount = 0;
        int cancelledCount = 0;

        for (AppointmentResponse item : allAppointments) {
            String status = item.getStatus();
            if ("PENDING".equalsIgnoreCase(status) || "CONFIRMED".equalsIgnoreCase(status)) {
                upcomingCount++;
            } else if ("COMPLETED".equalsIgnoreCase(status)) {
                completedCount++;
            } else if ("CANCELLED".equalsIgnoreCase(status) || "REJECTED".equalsIgnoreCase(status)) {
                cancelledCount++;
            }
        }

        binding.tvBadgeUpcoming.setText(String.format(java.util.Locale.getDefault(), "%02d", upcomingCount));
        binding.tvBadgeCompleted.setText(String.format(java.util.Locale.getDefault(), "%02d", completedCount));
        binding.tvBadgeCancelled.setText(String.format(java.util.Locale.getDefault(), "%02d", cancelledCount));
    }

    private void selectTab(int index) {
        if (binding == null) return;
        currentTab = index;

        // Reset all tabs background to inactive style (#DCE9FF)
        binding.btnTabUpcoming.setBackgroundResource(R.drawable.student_landlord_tab_bg);
        binding.btnTabUpcoming.setBackgroundTintList(null);
        binding.btnTabCompleted.setBackgroundResource(R.drawable.student_landlord_tab_bg);
        binding.btnTabCompleted.setBackgroundTintList(null);
        binding.btnTabCancelled.setBackgroundResource(R.drawable.student_landlord_tab_bg);
        binding.btnTabCancelled.setBackgroundTintList(null);

        // Reset text colors for inactive tabs
        setGroupLayoutTextColor(binding.btnTabUpcoming, Color.parseColor("#45474C"));
        setGroupLayoutTextColor(binding.btnTabCompleted, Color.parseColor("#45474C"));
        setGroupLayoutTextColor(binding.btnTabCancelled, Color.parseColor("#45474C"));

        if (index == 0) {
            binding.btnTabUpcoming.setBackgroundResource(R.drawable.student_landlord_tab_active);
            binding.btnTabUpcoming.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#030813")));
            setGroupLayoutTextColor(binding.btnTabUpcoming, Color.WHITE);
        } else if (index == 1) {
            binding.btnTabCompleted.setBackgroundResource(R.drawable.student_landlord_tab_active);
            binding.btnTabCompleted.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#030813")));
            setGroupLayoutTextColor(binding.btnTabCompleted, Color.WHITE);
        } else if (index == 2) {
            binding.btnTabCancelled.setBackgroundResource(R.drawable.student_landlord_tab_active);
            binding.btnTabCancelled.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#030813")));
            setGroupLayoutTextColor(binding.btnTabCancelled, Color.WHITE);
        }

        filterDataByTab();
    }

    private void filterDataByTab() {
        if (binding == null) return;
        List<AppointmentResponse> filtered = new ArrayList<>();

        for (AppointmentResponse item : allAppointments) {
            String s = item.getStatus();
            boolean match = (currentTab == 0 && ("PENDING".equalsIgnoreCase(s) || "CONFIRMED".equalsIgnoreCase(s)))
                    || (currentTab == 1 && "COMPLETED".equalsIgnoreCase(s))
                    || (currentTab == 2 && ("CANCELLED".equalsIgnoreCase(s) || "REJECTED".equalsIgnoreCase(s)));
            if (match) {
                filtered.add(item);
            }
        }

        adapter.setData(filtered);
        binding.tvEmptyAppointments.setVisibility(filtered.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void setGroupLayoutTextColor(ViewGroup layout, int color) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof TextView) {
                ((TextView) child).setTextColor(color);
            }
        }
    }

    @Override
    public void onDetailClick(AppointmentResponse appointment) {
        Bundle args = new Bundle();
        args.putSerializable("appointment_data", appointment);
        Navigation.findNavController(requireView()).navigate(R.id.action_appointments_to_appointmentDetail, args);
    }

    @Override
    public void onRoomClick(long roomId) {
        Bundle args = new Bundle();
        args.putLong("room_id", roomId);
        Navigation.findNavController(requireView()).navigate(R.id.action_appointments_to_roomDetail, args);
    }

    @Override
    public void onCancelClick(AppointmentResponse appointment) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Hủy lịch hẹn")
                .setMessage("Bạn có chắc chắn muốn hủy lịch hẹn xem phòng này?")
                .setPositiveButton("Hủy lịch", (dialog, which) -> executeCancelAppointment(appointment.getId()))
                .setNegativeButton("Đóng", null)
                .show();
    }

    private void executeCancelAppointment(long appointmentId) {
        String token = TokenManager.getToken(requireContext());
        if (token == null || token.isEmpty()) return;

        Map<String, String> body = new HashMap<>();
        body.put("cancellation_reason", "Sinh viên hủy lịch hẹn");

        RetrofitClient.getApiService().cancelStudentAppointment(token, appointmentId, body)
                .enqueue(new Callback<ApiResponse<Object>>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse<Object>> call, @NonNull Response<ApiResponse<Object>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            Toast.makeText(requireContext(), "Đã hủy lịch hẹn", Toast.LENGTH_SHORT).show();
                            loadAppointments();
                        } else {
                            Toast.makeText(requireContext(), "Hủy lịch hẹn thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse<Object>> call, @NonNull Throwable t) {
                        Toast.makeText(requireContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onCallClick(String phone) {
        if (phone != null && !phone.isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
            startActivity(intent);
        } else {
            Toast.makeText(requireContext(), "Chủ trọ chưa cập nhật số điện thoại", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onChatClick(AppointmentResponse appointment) {
        Navigation.findNavController(requireView()).navigate(R.id.action_appointments_to_chat);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
