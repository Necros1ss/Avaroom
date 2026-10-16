package com.avaroom.app.ui.landlord;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.avaroom.app.R;
import com.avaroom.app.databinding.FragmentLandlordDashboardBinding;

public class LandlordDashboardFragment extends Fragment {

    private FragmentLandlordDashboardBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLandlordDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Figma Prototype Connection: Button "Đăng tin" ==> Đăng tin phòng trọ mới (Bước 1)
        binding.btnCreatePost.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_dashboard_to_createPost);
        });

        // Figma Prototype Connection: Card/Button "Sửa tin" ==> Chi tiết tin đăng [DEMO]
        View.OnClickListener goToDetail = v -> {
            Navigation.findNavController(v).navigate(R.id.action_dashboard_to_postDetail);
        };
        binding.btnEditPost1.setOnClickListener(goToDetail);
        binding.cardLandlordPost1.setOnClickListener(goToDetail);

        // Bottom Navigation Bar Connections for Landlord
        binding.navLandlordAppointments.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_dashboard_to_appointments);
        });

        binding.navLandlordChat.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_dashboard_to_chat);
        });

        binding.navLandlordProfile.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_dashboard_to_profile);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
