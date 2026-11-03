package com.avaroom.app.ui.student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.avaroom.app.R;
import com.avaroom.app.databinding.FragmentBookingSuccessBinding;

public class BookingSuccessFragment extends Fragment {

    private FragmentBookingSuccessBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBookingSuccessBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Figma Prototype Connection: Close Button ==> Go back to Home
        binding.btnCloseSuccess.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_bookingSuccess_to_home);
        });

        // Figma Prototype Connection: Manage Appointments ==> Lịch hẹn xem phòng
        binding.btnManageAppointments.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_bookingSuccess_to_appointments);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}