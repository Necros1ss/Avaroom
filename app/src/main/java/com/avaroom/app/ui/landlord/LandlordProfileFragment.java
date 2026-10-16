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

public class LandlordProfileFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_landlord_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        View btnEdit = view.findViewById(R.id.btn_edit_landlord_profile);
        if (btnEdit != null) {
            btnEdit.setOnClickListener(v -> androidx.navigation.Navigation.findNavController(v).navigate(R.id.action_profile_to_editProfile));
        }

        View menuStats = view.findViewById(R.id.menu_stats);
        if (menuStats != null) {
            menuStats.setOnClickListener(v -> androidx.navigation.Navigation.findNavController(v).navigate(R.id.action_profile_to_stats));
        }

        View menuReviews = view.findViewById(R.id.menu_landlord_reviews);
        if (menuReviews != null) {
            menuReviews.setOnClickListener(v -> androidx.navigation.Navigation.findNavController(v).navigate(R.id.action_profile_to_reviews));
        }

        View menuLogout = view.findViewById(R.id.menu_landlord_logout);
        if (menuLogout != null) {
            menuLogout.setOnClickListener(v -> {
                android.content.Intent intent = new android.content.Intent(requireActivity(), com.avaroom.app.MainActivity.class);
                intent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK | android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            });
        }
    }
}
