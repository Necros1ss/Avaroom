package com.avaroom.app.ui.student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.avaroom.app.databinding.FragmentWriteReviewBinding;

public class WriteReviewFragment extends Fragment {

    private FragmentWriteReviewBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWriteReviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Back button
        if (binding.btnBackWriteReview != null) {
            binding.btnBackWriteReview.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());
        }

        // Submit review button
        if (binding.btnSubmitReview != null) {
            binding.btnSubmitReview.setOnClickListener(v -> {
                Toast.makeText(requireContext(), "Gửi đánh giá thành công!", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(v).navigateUp();
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
