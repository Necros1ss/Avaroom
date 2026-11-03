package com.avaroom.app.ui.student;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.avaroom.app.R;
import com.avaroom.app.databinding.FragmentStudentAppointmentsBinding;

public class AppointmentsFragment extends Fragment {

    private FragmentStudentAppointmentsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStudentAppointmentsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Back button ==> Navigate up
        if (binding.btnBackAppointments != null) {
            binding.btnBackAppointments.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());
        }

        // Tab switching logic
        if (binding.btnTabUpcoming != null && binding.btnTabCompleted != null && binding.btnTabCancelled != null) {
            binding.btnTabUpcoming.setOnClickListener(v -> selectTab(0));
            binding.btnTabCompleted.setOnClickListener(v -> selectTab(1));
            binding.btnTabCancelled.setOnClickListener(v -> selectTab(2));
        }

        // Write review button in completed tab
        if (binding.btnWriteReviewCompleted != null) {
            binding.btnWriteReviewCompleted.setOnClickListener(v -> {
                Navigation.findNavController(v).navigate(R.id.action_appointments_to_reviews);
            });
        }

        // View appointment detail button
        if (binding.btnViewAppointmentDetail != null) {
            binding.btnViewAppointmentDetail.setOnClickListener(v -> {
                Navigation.findNavController(v).navigate(R.id.action_appointments_to_appointmentDetail);
            });
        }

        // Reschedule appointment button
        if (binding.btnRescheduleAppointment != null) {
            binding.btnRescheduleAppointment.setOnClickListener(v -> {
                Navigation.findNavController(v).navigate(R.id.action_appointments_to_changeAppointment);
            });
        }

        // View cancelled room detail button
        if (binding.btnViewCancelledRoom != null) {
            binding.btnViewCancelledRoom.setOnClickListener(v -> {
                Navigation.findNavController(v).navigate(R.id.action_appointments_to_roomDetail);
            });
        }
    }

    private void selectTab(int index) {
        if (binding == null) return;
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

        // Hide containers
        binding.containerUpcoming.setVisibility(View.GONE);
        binding.containerCompleted.setVisibility(View.GONE);
        binding.containerCancelled.setVisibility(View.GONE);

        if (index == 0) {
            binding.btnTabUpcoming.setBackgroundResource(R.drawable.student_landlord_tab_active);
            binding.btnTabUpcoming.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#030813")));
            setGroupLayoutTextColor(binding.btnTabUpcoming, Color.WHITE);
            binding.containerUpcoming.setVisibility(View.VISIBLE);
        } else if (index == 1) {
            binding.btnTabCompleted.setBackgroundResource(R.drawable.student_landlord_tab_active);
            binding.btnTabCompleted.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#030813")));
            setGroupLayoutTextColor(binding.btnTabCompleted, Color.WHITE);
            binding.containerCompleted.setVisibility(View.VISIBLE);
        } else if (index == 2) {
            binding.btnTabCancelled.setBackgroundResource(R.drawable.student_landlord_tab_active);
            binding.btnTabCancelled.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#030813")));
            setGroupLayoutTextColor(binding.btnTabCancelled, Color.WHITE);
            binding.containerCancelled.setVisibility(View.VISIBLE);
        }
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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
