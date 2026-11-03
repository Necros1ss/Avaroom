package com.avaroom.app.ui.student;

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
        return inflater.inflate(R.layout.fragment_student_appointment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Back button ==> Navigate up
        View btnBack = view.findViewById(R.id.btn_back_appointment_detail);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());
        }

        // Room detail card / 360 view
        View cardRoom = view.findViewById(R.id.card_room_detail_appointment);
        if (cardRoom != null) {
            cardRoom.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_appointmentDetail_to_roomDetail));
        }

        View btn360 = view.findViewById(R.id.btn_view_360);
        if (btn360 != null) {
            btn360.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_appointmentDetail_to_roomDetail));
        }

        // Directions map
        View btnMap = view.findViewById(R.id.btn_directions_map);
        if (btnMap != null) {
            btnMap.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_appointmentDetail_to_map));
        }

        // Call host
        View btnCall = view.findViewById(R.id.btn_call_host_detail);
        if (btnCall != null) {
            btnCall.setOnClickListener(v -> Toast.makeText(requireContext(), "Đang kết nối gọi cho chủ trọ...", Toast.LENGTH_SHORT).show());
        }

        // Chat host
        View btnChat = view.findViewById(R.id.btn_chat_host_detail);
        if (btnChat != null) {
            btnChat.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_appointmentDetail_to_chat));
        }
    }
}
