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
import com.avaroom.app.databinding.FragmentStudentEkycBinding;

public class StudentEkycFragment extends Fragment {

    private FragmentStudentEkycBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStudentEkycBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Back button ==> Navigate up
        if (binding.btnBackEkyc != null) {
            binding.btnBackEkyc.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());
        }

        // Click personal info card ==> Navigate to Edit Profile
        View cardPersonalInfo = view.findViewById(R.id.card_personal_info);
        if (cardPersonalInfo != null) {
            cardPersonalInfo.setOnClickListener(v -> {
                Navigation.findNavController(v).navigate(R.id.action_studentEkyc_to_editProfile);
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
