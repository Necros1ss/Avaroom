package com.avaroom.app.ui.student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.avaroom.app.R;
import com.avaroom.app.databinding.FragmentSearchResultBinding;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchResultFragment extends Fragment {

    private FragmentSearchResultBinding binding;

    private static class RoomCardItem {
        int id;
        double price; // in millions
        int distance; // in meters
        double rating;
        View cardView;

        RoomCardItem(int id, double price, int distance, double rating, View cardView) {
            this.id = id;
            this.price = price;
            this.distance = distance;
            this.rating = rating;
            this.cardView = cardView;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchResultBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Back button ==> Navigate up
        binding.btnBackSearch.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        // Filter button ==> Navigate to FilterFragment
        if (binding.btnFilterResult != null) {
            binding.btnFilterResult.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.filterFragment));
        }

        // Result card clicks ==> Navigate to RoomDetailFragment
        View.OnClickListener goToDetail = v -> Navigation.findNavController(v).navigate(R.id.action_search_to_roomDetail);

        if (binding.cardResult1 != null) binding.cardResult1.setOnClickListener(goToDetail);
        if (binding.cardResult2 != null) binding.cardResult2.setOnClickListener(goToDetail);
        if (binding.cardResult3 != null) binding.cardResult3.setOnClickListener(goToDetail);
        if (binding.cardResult4 != null) binding.cardResult4.setOnClickListener(goToDetail);
        if (binding.cardResult5 != null) binding.cardResult5.setOnClickListener(goToDetail);
        if (binding.cardResult6 != null) binding.cardResult6.setOnClickListener(goToDetail);

        // Heart (tym) toggle click listeners for result cards (no Toast)
        View.OnClickListener toggleTym = v -> {
            if (v instanceof ViewGroup) {
                ViewGroup container = (ViewGroup) v;
                if (container.getChildCount() > 0 && container.getChildAt(0) instanceof TextView) {
                    TextView tv = (TextView) container.getChildAt(0);
                    String currentText = tv.getText().toString();
                    if (currentText.equals("🤍")) {
                        tv.setText("❤️");
                    } else {
                        tv.setText("🤍");
                    }
                }
            }
        };

        if (binding.btnSaveRes1 != null) binding.btnSaveRes1.setOnClickListener(toggleTym);
        if (binding.btnSaveRes2 != null) binding.btnSaveRes2.setOnClickListener(toggleTym);
        if (binding.btnSaveRes3 != null) binding.btnSaveRes3.setOnClickListener(toggleTym);
        if (binding.btnSaveRes4 != null) binding.btnSaveRes4.setOnClickListener(toggleTym);
        if (binding.btnSaveRes5 != null) binding.btnSaveRes5.setOnClickListener(toggleTym);
        if (binding.btnSaveRes6 != null) binding.btnSaveRes6.setOnClickListener(toggleTym);

        // Setup room items for sorting
        List<RoomCardItem> roomItems = new ArrayList<>();
        if (binding.cardResult1 != null) roomItems.add(new RoomCardItem(1, 3.5, 350, 4.8, binding.cardResult1));
        if (binding.cardResult2 != null) roomItems.add(new RoomCardItem(2, 4.2, 2000, 4.8, binding.cardResult2));
        if (binding.cardResult3 != null) roomItems.add(new RoomCardItem(3, 2.8, 800, 4.7, binding.cardResult3));
        if (binding.cardResult4 != null) roomItems.add(new RoomCardItem(4, 4.2, 250, 5.0, binding.cardResult4));
        if (binding.cardResult5 != null) roomItems.add(new RoomCardItem(5, 2.5, 1100, 4.6, binding.cardResult5));
        if (binding.cardResult6 != null) roomItems.add(new RoomCardItem(6, 3.8, 450, 4.9, binding.cardResult6));

        Runnable applySortAndRender = () -> {
            if (binding.layoutRow1 == null || binding.layoutRow2 == null || binding.layoutRow3 == null) return;
            binding.layoutRow1.removeAllViews();
            binding.layoutRow2.removeAllViews();
            binding.layoutRow3.removeAllViews();

            for (int i = 0; i < roomItems.size(); i++) {
                View card = roomItems.get(i).cardView;
                if (card.getParent() != null) {
                    ((ViewGroup) card.getParent()).removeView(card);
                }
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f
                );
                if (i % 2 == 0) {
                    params.setMarginEnd(8);
                } else {
                    params.setMarginEnd(0);
                }
                card.setLayoutParams(params);

                if (i < 2) {
                    binding.layoutRow1.addView(card);
                } else if (i < 4) {
                    binding.layoutRow2.addView(card);
                } else {
                    binding.layoutRow3.addView(card);
                }
            }
        };

        // Sorting dropdown selection (no Toast)
        if (binding.layoutSortDropdown != null) {
            binding.layoutSortDropdown.setOnClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(requireContext(), v);
                popupMenu.getMenu().add("Phù hợp nhất");
                popupMenu.getMenu().add("Giá thấp đến cao");
                popupMenu.getMenu().add("Giá cao đến thấp");
                popupMenu.getMenu().add("Đánh giá cao nhất");

                popupMenu.setOnMenuItemClickListener(item -> {
                    String title = item.getTitle() != null ? item.getTitle().toString() : "";
                    if (binding.tvSortText != null) {
                        binding.tvSortText.setText(title + " ⌄");
                    }

                    if (title.equals("Phù hợp nhất")) {
                        Collections.sort(roomItems, (a, b) -> Integer.compare(a.id, b.id));
                    } else if (title.equals("Giá thấp đến cao")) {
                        Collections.sort(roomItems, (a, b) -> Double.compare(a.price, b.price));
                    } else if (title.equals("Giá cao đến thấp")) {
                        Collections.sort(roomItems, (a, b) -> Double.compare(b.price, a.price));
                    } else if (title.equals("Đánh giá cao nhất")) {
                        Collections.sort(roomItems, (a, b) -> Double.compare(b.rating, a.rating));
                    }

                    applySortAndRender.run();
                    return true;
                });
                popupMenu.show();
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
