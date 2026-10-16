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
import com.avaroom.app.databinding.FragmentStudentHomeBinding;

public class StudentHomeFragment extends Fragment {

    private FragmentStudentHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStudentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Figma Prototype Connection: Button - Bộ lọc nâng cao ==> Bộ lọc tìm kiếm nâng cao - Avaroom
        binding.btnFilterDialog.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_home_to_filter);
        });

        // Search bar click ==> Search Result
        binding.layoutSearchBox.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_home_to_search);
        });

        // Figma Prototype Connection: Card Button -> Chi tiết phòng trọ
        View.OnClickListener goToDetail = v -> {
            Navigation.findNavController(v).navigate(R.id.action_home_to_roomDetail);
        };
        binding.btnViewRoom1.setOnClickListener(goToDetail);
        binding.cardRoom1.setOnClickListener(goToDetail);
        binding.btnViewRoom2.setOnClickListener(goToDetail);
        binding.cardRoom2.setOnClickListener(goToDetail);

        // Notification Bell ==> Notifications Fragment
        binding.btnNotification.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_home_to_notifications);
        });

        // Bottom Navigation Bar Connections from Figma
        binding.navItemMap.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_home_to_map);
        });

        binding.navItemChat.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_home_to_chat);
        });

        binding.navItemAccount.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_home_to_account);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}