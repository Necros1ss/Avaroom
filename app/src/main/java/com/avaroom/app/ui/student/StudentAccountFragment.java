package com.avaroom.app.ui.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        // Click eKYC Banner / Menu
        View.OnClickListener ekycClick = v -> {
            Navigation.findNavController(v).navigate(R.id.action_account_to_ekyc);
        };
        if (binding.btnEkycStatus != null) binding.btnEkycStatus.setOnClickListener(ekycClick);
        if (binding.menuEkyc != null) binding.menuEkyc.setOnClickListener(ekycClick);

        // Click Đổi mật khẩu
        if (binding.menuChangePassword != null) {
            binding.menuChangePassword.setOnClickListener(v -> {
                Navigation.findNavController(v).navigate(R.id.action_account_to_changePassword);
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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}