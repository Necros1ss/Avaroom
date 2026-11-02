package com.avaroom.app.ui.landlord;

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
import com.avaroom.app.databinding.FragmentStatsBinding;

public class StatsFragment extends Fragment {

    private FragmentStatsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStatsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnBackStats.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        setupTabs();
    }

    private void setupTabs() {
        binding.tabMonth.setOnClickListener(v -> {
            selectTab(binding.tabMonth);
            binding.cardStat1.setVisibility(View.VISIBLE);
            binding.cardStat2.setVisibility(View.VISIBLE);
            binding.layoutEmptyStats.setVisibility(View.GONE);
        });

        binding.tabWeek.setOnClickListener(v -> {
            selectTab(binding.tabWeek);
            binding.cardStat1.setVisibility(View.VISIBLE);
            binding.cardStat2.setVisibility(View.VISIBLE);
            binding.layoutEmptyStats.setVisibility(View.GONE);
        });

        binding.tabQuarter.setOnClickListener(v -> {
            selectTab(binding.tabQuarter);
            binding.cardStat1.setVisibility(View.GONE);
            binding.cardStat2.setVisibility(View.GONE);
            binding.layoutEmptyStats.setVisibility(View.VISIBLE);
        });

        binding.tabYear.setOnClickListener(v -> {
            selectTab(binding.tabYear);
            binding.cardStat1.setVisibility(View.GONE);
            binding.cardStat2.setVisibility(View.GONE);
            binding.layoutEmptyStats.setVisibility(View.VISIBLE);
        });
    }

    private void selectTab(TextView selectedTab) {
        binding.tabMonth.setBackgroundResource(R.drawable.bg_chip_pill);
        binding.tabMonth.setTextColor(getResources().getColor(R.color.text_secondary, null));

        binding.tabWeek.setBackgroundResource(R.drawable.bg_chip_pill);
        binding.tabWeek.setTextColor(getResources().getColor(R.color.text_secondary, null));

        binding.tabQuarter.setBackgroundResource(R.drawable.bg_chip_pill);
        binding.tabQuarter.setTextColor(getResources().getColor(R.color.text_secondary, null));

        binding.tabYear.setBackgroundResource(R.drawable.bg_chip_pill);
        binding.tabYear.setTextColor(getResources().getColor(R.color.text_secondary, null));

        selectedTab.setBackgroundResource(R.drawable.bg_button_primary);
        selectedTab.setTextColor(getResources().getColor(R.color.bg_white, null));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
