package com.avaroom.app.ui.landlord;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.avaroom.app.R;

public class LandlordAppointmentsFragment extends Fragment {

    private LinearLayout dayT2, dayT3, dayT4, dayT5, dayT6, dayT7, dayCn;
    private View cardAppt1, layoutEmptyAppts;
    private TextView tvApptStatus;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_landlord_appointments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View btnBack = view.findViewById(R.id.btn_back_landlord_appts);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());
        }

        dayT2 = view.findViewById(R.id.day_t2);
        dayT3 = view.findViewById(R.id.day_t3);
        dayT4 = view.findViewById(R.id.day_t4);
        dayT5 = view.findViewById(R.id.day_t5);
        dayT6 = view.findViewById(R.id.day_t6);
        dayT7 = view.findViewById(R.id.day_t7);
        dayCn = view.findViewById(R.id.day_cn);

        cardAppt1 = view.findViewById(R.id.card_landlord_appt_1);
        layoutEmptyAppts = view.findViewById(R.id.layout_empty_appointments);
        tvApptStatus = view.findViewById(R.id.tv_appt_status);
        
        View btnConfirm = view.findViewById(R.id.btn_confirm_appt);
        View btnReject = view.findViewById(R.id.btn_reject_appt);

        if (btnConfirm != null) {
            btnConfirm.setOnClickListener(v -> {
                if (tvApptStatus != null) {
                    tvApptStatus.setText("Đã xác nhận lịch hẹn ✔");
                    tvApptStatus.setTextColor(getResources().getColor(R.color.accent_green, null));
                }
                btnConfirm.setVisibility(View.GONE);
                if (btnReject != null) {
                    btnReject.setVisibility(View.GONE);
                }
                Toast.makeText(requireContext(), "Đã xác nhận lịch hẹn thành công! Khách thuê đã được thông báo.", Toast.LENGTH_LONG).show();
            });
        }

        if (btnReject != null) {
            btnReject.setOnClickListener(v -> {
                cardAppt1.setVisibility(View.GONE);
                if (layoutEmptyAppts != null) {
                    layoutEmptyAppts.setVisibility(View.VISIBLE);
                }
                Toast.makeText(requireContext(), "Đã từ chối lịch hẹn", Toast.LENGTH_SHORT).show();
            });
        }

        // Default selection: T5 (Hôm nay)
        selectDay(dayT5, true);

        dayT2.setOnClickListener(v -> selectDay(dayT2, false));
        dayT3.setOnClickListener(v -> selectDay(dayT3, false));
        dayT4.setOnClickListener(v -> selectDay(dayT4, true));
        dayT5.setOnClickListener(v -> selectDay(dayT5, true));
        dayT6.setOnClickListener(v -> selectDay(dayT6, true));
        dayT7.setOnClickListener(v -> selectDay(dayT7, false));
        dayCn.setOnClickListener(v -> selectDay(dayCn, false));
    }

    private void selectDay(LinearLayout selectedDay, boolean hasAppointments) {
        updateDayUI(dayT2, false);
        updateDayUI(dayT3, false);
        updateDayUI(dayT4, false);
        updateDayUI(dayT5, false);
        updateDayUI(dayT6, false);
        updateDayUI(dayT7, false);
        updateDayUI(dayCn, false);

        updateDayUI(selectedDay, true);

        if (cardAppt1 != null && layoutEmptyAppts != null) {
            if (hasAppointments && cardAppt1.getVisibility() != View.GONE) {
                cardAppt1.setVisibility(View.VISIBLE);
                layoutEmptyAppts.setVisibility(View.GONE);
            } else if (!hasAppointments) {
                cardAppt1.setVisibility(View.GONE);
                layoutEmptyAppts.setVisibility(View.VISIBLE);
            }
        }
    }

    private void updateDayUI(LinearLayout dayLayout, boolean isSelected) {
        if (dayLayout == null) return;
        if (isSelected) {
            dayLayout.setBackgroundResource(R.drawable.bg_button_primary);
            TextView tv1 = (TextView) dayLayout.getChildAt(0);
            TextView tv2 = (TextView) dayLayout.getChildAt(1);
            TextView tv3 = (TextView) dayLayout.getChildAt(2);
            if (tv1 != null) tv1.setTextColor(Color.parseColor("#CBD5E1"));
            if (tv2 != null) tv2.setTextColor(Color.WHITE);
            if (tv3 != null) tv3.setTextColor(Color.parseColor("#FDE047"));
        } else {
            dayLayout.setBackgroundResource(R.drawable.bg_chip_pill);
            TextView tv1 = (TextView) dayLayout.getChildAt(0);
            TextView tv2 = (TextView) dayLayout.getChildAt(1);
            TextView tv3 = (TextView) dayLayout.getChildAt(2);
            if (tv1 != null) tv1.setTextColor(getResources().getColor(R.color.text_muted, null));
            if (tv2 != null) tv2.setTextColor(getResources().getColor(R.color.primary_black, null));
            if (tv3 != null) tv3.setTextColor(getResources().getColor(R.color.text_muted, null));
        }
    }
}
