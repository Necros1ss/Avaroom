package com.avaroom.app.ui.landlord;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.avaroom.app.R;

public class AppointmentDetailFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_appointment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View btnBack = view.findViewById(R.id.btn_back_appt_detail);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());
        }

        View btnConfirm = view.findViewById(R.id.btn_confirm_detail);
        if (btnConfirm != null) {
            btnConfirm.setOnClickListener(v -> {
                Toast.makeText(requireContext(), "Đã xác nhận lịch hẹn xem phòng thành công!", Toast.LENGTH_LONG).show();
                Navigation.findNavController(v).navigateUp();
            });
        }

        View btnReject = view.findViewById(R.id.btn_reject_detail);
        if (btnReject != null) {
            btnReject.setOnClickListener(v -> {
                Toast.makeText(requireContext(), "Đã từ chối lịch hẹn xem phòng", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(v).navigateUp();
            });
        }
    }
}
