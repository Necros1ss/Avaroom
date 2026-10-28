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
import com.avaroom.app.databinding.FragmentSearchResultBinding;

public class SearchResultFragment extends Fragment {

    private FragmentSearchResultBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchResultBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Click Back
        binding.btnBackSearch.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        // Click Filter
        binding.btnFilterResult.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.filterFragment);
        });

        // Click Result item 1
        if (binding.cardResult1 != null) {
            binding.cardResult1.setOnClickListener(v -> {
                Navigation.findNavController(v).navigate(R.id.roomDetailFragment);
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
