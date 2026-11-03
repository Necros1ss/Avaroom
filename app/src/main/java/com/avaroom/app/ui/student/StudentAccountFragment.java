package com.avaroom.app.ui.student;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.avaroom.app.MainActivity;
import com.avaroom.app.R;
import com.avaroom.app.data.model.response.ApiResponse;
import com.avaroom.app.data.remote.RetrofitClient;
import com.avaroom.app.databinding.FragmentStudentAccountBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import java.util.Map;

public class StudentAccountFragment extends Fragment {

    private FragmentStudentAccountBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStudentAccountBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Click Phòng đã lưu
        if (binding.cardStatSaved != null) {
            binding.cardStatSaved.setOnClickListener(v -> {
                Navigation.findNavController(v).navigate(R.id.action_account_to_savedRooms);
            });
        }

        // Click Lịch xem phòng
        if (binding.cardStatAppointments != null) {
            binding.cardStatAppointments.setOnClickListener(v -> {
                Navigation.findNavController(v).navigate(R.id.action_account_to_appointments);
            });
        }

        // Click eKYC Menu
        if (binding.menuEkyc != null) {
            binding.menuEkyc.setOnClickListener(v -> {
                Navigation.findNavController(v).navigate(R.id.action_account_to_ekyc);
            });
        }

        // Click Đổi mật khẩu
        if (binding.menuChangePassword != null) {
            binding.menuChangePassword.setOnClickListener(v -> {
                Navigation.findNavController(v).navigate(R.id.action_account_to_changePassword);
            });
        }

        // Click Ngôn ngữ ứng dụng
        if (binding.menuLanguage != null) {
            binding.menuLanguage.setOnClickListener(v -> {
                String[] languages = {"🇻🇳 Tiếng Việt (Vietnamese)", "🇬🇧 English (English)"};
                new AlertDialog.Builder(requireContext())
                        .setTitle("Chọn ngôn ngữ / Select Language")
                        .setItems(languages, (dialog, which) -> {
                            if (which == 0) {
                                if (binding.tvLanguageSubtitle != null) binding.tvLanguageSubtitle.setText("Tiếng Việt (Mặc định)");
                                if (binding.tvLanguageCode != null) binding.tvLanguageCode.setText("VIE");
                                Toast.makeText(requireContext(), "Đã chuyển sang Tiếng Việt", Toast.LENGTH_SHORT).show();
                            } else {
                                if (binding.tvLanguageSubtitle != null) binding.tvLanguageSubtitle.setText("English (Default)");
                                if (binding.tvLanguageCode != null) binding.tvLanguageCode.setText("ENG");
                                Toast.makeText(requireContext(), "Switched to English", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            });
        }

        // Click Đăng xuất
        if (binding.menuLogout != null) {
            binding.menuLogout.setOnClickListener(v -> {
                Intent intent = new Intent(requireActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            });
        }

        // Notification bell ==> Notifications Fragment
        if (binding.btnNotificationAccount != null) {
            binding.btnNotificationAccount.setOnClickListener(v -> {
                Navigation.findNavController(v).navigate(R.id.action_account_to_notifications);
            });
        }

        // Profile icon ==> Student EKYC / Profile Fragment
        if (binding.btnProfileAccount != null) {
            binding.btnProfileAccount.setOnClickListener(v -> {
                Navigation.findNavController(v).navigate(R.id.action_account_to_ekyc);
            });
        }

        loadUserProfile();
    }

    private void loadUserProfile() {
        SharedPreferences pref = requireContext().getSharedPreferences("AvaroomAuth", Context.MODE_PRIVATE);
        String token = "Bearer " + pref.getString("ACCESS_TOKEN", "");
        RetrofitClient.getApiService().getMe(token).enqueue(new Callback<ApiResponse<Map<String, Object>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Map<String, Object>>> call,
                                   @NonNull Response<ApiResponse<Map<String, Object>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    Map<String, Object> data = response.body().getData();

                    // Lấy thông tin cơ bản
                    String fullName = (String) data.get("full_name");
                    String email = (String) data.get("email");

                    // Cập nhật lên View
                    if (binding.tvUserName != null && fullName != null) {
                        binding.tvUserName.setText(fullName);
                    }
                    if (binding.tvUserEmail != null && email != null) {
                        binding.tvUserEmail.setText(email);
                    }
                    // Lấy thông tin sinh viên từ data.get("studentProfile")
                    Map<String, Object> studentProfile = (Map<String, Object>) data.get("studentProfile");
                    if (studentProfile != null && binding.tvUniversityInfo != null) {
                        String academicYear = (String) studentProfile.get("academic_year");
                        String studentCode = (String) studentProfile.get("student_code");
                        if (academicYear != null || studentCode != null) {
                            binding.tvUniversityInfo.setText((studentCode != null ? studentCode : "")
                                    + (academicYear != null ? " • " + academicYear : ""));
                        }
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<ApiResponse<Map<String, Object>>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Không thể tải thông tin: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}