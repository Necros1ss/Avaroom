package com.avaroom.app.ui.student;

import android.app.AlertDialog;
import android.content.Intent;
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
import com.avaroom.app.databinding.FragmentStudentAccountBinding;

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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}