package com.avaroom.app.ui.student;

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

        // Figma Prototype Connection: Xem phòng button -> Chi tiết phòng trọ
        View.OnClickListener goToDetail = v -> {
            Navigation.findNavController(v).navigate(R.id.action_home_to_roomDetail);
        };

        if (binding.btnViewRoom1 != null) {
            binding.btnViewRoom1.setOnClickListener(goToDetail);
        }
        if (binding.btnViewRoom2 != null) {
            binding.btnViewRoom2.setOnClickListener(goToDetail);
        }

        // Notification Bell ==> Notifications Fragment
        binding.btnNotification.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_home_to_notifications);
        });

        // Profile icon ==> Student EKYC / Profile Fragment
        if (binding.btnProfileHome != null) {
            binding.btnProfileHome.setOnClickListener(v -> {
                Navigation.findNavController(v).navigate(R.id.action_home_to_ekyc);
            });
        }

        // Handle save room logic
        View.OnClickListener toggleSaveRoom = v -> {
            TextView iconView = (TextView) ((ViewGroup) v).getChildAt(0);
            if (iconView.getText().toString().equals("🤍")) {
                iconView.setText("❤️"); // Saved state
            } else {
                iconView.setText("🤍"); // Unsaved state
            }
        };
        if (binding.btnSaveRoom1 != null) {
            binding.btnSaveRoom1.setOnClickListener(v -> {
                toggleSaveRoom.onClick(v);
                v.setPressed(false);
            });
        }
        if (binding.btnSaveRoom2 != null) {
            binding.btnSaveRoom2.setOnClickListener(v -> {
                toggleSaveRoom.onClick(v);
                v.setPressed(false);
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}