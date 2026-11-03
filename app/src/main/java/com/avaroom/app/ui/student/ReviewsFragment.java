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
import com.avaroom.app.databinding.FragmentReviewsBinding;

public class ReviewsFragment extends Fragment {

    private FragmentReviewsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentReviewsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Back button ==> Navigate up
        if (binding.btnBackReviews != null) {
            binding.btnBackReviews.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());
        }

        // Write review button ==> Navigate to WriteReviewFragment
        if (binding.btnWriteReview != null) {
            binding.btnWriteReview.setOnClickListener(v -> {
                Navigation.findNavController(v).navigate(R.id.action_reviews_to_writeReview);
            });
        }

        // Book schedule button ==> Navigate to BookingFragment
        if (binding.btnBookFromReviews != null) {
            binding.btnBookFromReviews.setOnClickListener(v -> {
                Navigation.findNavController(v).navigate(R.id.action_reviews_to_booking);
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}