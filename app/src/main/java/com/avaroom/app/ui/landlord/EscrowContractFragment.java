package com.avaroom.app.ui.landlord;

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
import com.avaroom.app.databinding.FragmentEscrowContractBinding;

public class EscrowContractFragment extends Fragment {

    private FragmentEscrowContractBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEscrowContractBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnBack.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        binding.btnViewPdfContract.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Đang mở Hợp đồng điện tử bảo chứng BIDV...", Toast.LENGTH_SHORT).show();
        });

        binding.btnHandoverAsset.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Chuyển sang Biên bản bàn giao tài sản 360°", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
