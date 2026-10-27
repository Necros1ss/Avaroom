package com.avaroom.app.ui.landlord;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.avaroom.app.R;
import com.avaroom.app.databinding.FragmentUtilityBillingBinding;
import java.text.DecimalFormat;

public class UtilityBillingFragment extends Fragment {

    private FragmentUtilityBillingBinding binding;
    private final DecimalFormat formatter = new DecimalFormat("#,###");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUtilityBillingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnBack.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        binding.etElectricNew.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculateTotals();
            }
        });

        binding.etWaterNew.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculateTotals();
            }
        });

        binding.btnGenerateInvoice.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Chuyển sang Xem trước Hóa đơn PDF & VietQR", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(v).navigate(R.id.action_billing_to_invoice);
        });
    }

    private void calculateTotals() {
        try {
            int oldElec = Integer.parseInt(binding.etElectricOld.getText().toString().trim());
            int newElec = Integer.parseInt(binding.etElectricNew.getText().toString().trim());
            int diffElec = Math.max(0, newElec - oldElec);
            long totalElec = (long) diffElec * 3500;
            binding.tvElectricTotal.setText(formatter.format(totalElec) + " VNĐ");

            int oldWater = Integer.parseInt(binding.etWaterOld.getText().toString().trim());
            int newWater = Integer.parseInt(binding.etWaterNew.getText().toString().trim());
            int diffWater = Math.max(0, newWater - oldWater);
            long totalWater = (long) diffWater * 30000;
            binding.tvWaterTotal.setText(formatter.format(totalWater) + " VNĐ");
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private abstract static class SimpleTextWatcher implements TextWatcher {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override public void afterTextChanged(Editable s) {}
    }
}
