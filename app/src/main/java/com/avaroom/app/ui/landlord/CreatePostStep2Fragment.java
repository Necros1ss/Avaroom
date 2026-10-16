package com.avaroom.app.ui.landlord;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.avaroom.app.R;

public class CreatePostStep2Fragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_post_step2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        View btnNext = view.findViewById(R.id.btn_next_step2);
        if (btnNext != null) {
            btnNext.setOnClickListener(v -> androidx.navigation.Navigation.findNavController(v).navigate(R.id.action_step2_to_step3));
        }

        View btnBack = view.findViewById(R.id.btn_back_step2);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> androidx.navigation.Navigation.findNavController(v).navigateUp());
        }
    }
}
