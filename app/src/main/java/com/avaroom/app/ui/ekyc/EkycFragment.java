package com.avaroom.app.ui.ekyc;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.avaroom.app.databinding.FragmentEkycBinding;
import com.avaroom.app.ui.landlord.LandlordMainActivity;

public class EkycFragment extends Fragment {

    private FragmentEkycBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEkycBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Figma Prototype Connection: Main Continue ==> Landlord Dashboard / Student Home
        binding.btnContinueEkyc.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), LandlordMainActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        // Figma Prototype Connection: Go back
        binding.btnBackEkyc.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
