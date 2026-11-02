package com.avaroom.app.ui.landlord;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.avaroom.app.R;
import com.avaroom.app.data.model.response.ApiResponse;
import com.avaroom.app.data.remote.RetrofitClient;
import com.avaroom.app.databinding.FragmentLandlordDashboardBinding;
import com.avaroom.app.utils.TokenManager;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LandlordDashboardFragment extends Fragment {

    private FragmentLandlordDashboardBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLandlordDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Figma Prototype Connection: Button "Đăng tin" ==> Đăng tin phòng trọ mới (Bước 1)
        binding.btnCreatePost.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_dashboard_to_createPost);
        });

        View.OnClickListener goToDetail = v -> {
            Navigation.findNavController(v).navigate(R.id.action_dashboard_to_postDetail);
        };
        binding.btnEditPost1.setOnClickListener(goToDetail);
        binding.cardLandlordPost1.setOnClickListener(goToDetail);

        // Check if a new post was recently added via upload news flow
        if (PostState.isNewPostAdded()) {
            binding.cardLandlordPost3.setVisibility(View.VISIBLE);
        }

        // Delete / Remove buttons
        binding.btnDeletePost1.setOnClickListener(v -> {
            binding.cardLandlordPost1.setVisibility(View.GONE);
            Toast.makeText(requireContext(), "Đã xóa tin đăng #AVR-90412 khỏi danh sách", Toast.LENGTH_SHORT).show();
            checkEmptyState();
        });

        binding.btnDeletePost2.setOnClickListener(v -> {
            binding.cardLandlordPost2.setVisibility(View.GONE);
            Toast.makeText(requireContext(), "Đã xóa tin đăng #AVR-89210 khỏi danh sách", Toast.LENGTH_SHORT).show();
            checkEmptyState();
        });

        binding.btnDeletePost3.setOnClickListener(v -> {
            binding.cardLandlordPost3.setVisibility(View.GONE);
            PostState.setNewPostAdded(false);
            Toast.makeText(requireContext(), "Đã xóa tin đăng mới đăng khỏi danh sách", Toast.LENGTH_SHORT).show();
            checkEmptyState();
        });

        // Setup filter tabs switching
        setupTabs();

        fetchLandlordPosts();
    }

    private void checkEmptyState() {
        boolean post1Gone = binding.cardLandlordPost1.getVisibility() == View.GONE;
        boolean post2Gone = binding.cardLandlordPost2.getVisibility() == View.GONE;
        boolean post3Gone = binding.cardLandlordPost3.getVisibility() == View.GONE;

        if (post1Gone && post2Gone && post3Gone) {
            binding.layoutEmptyDashboard.setVisibility(View.VISIBLE);
        } else {
            binding.layoutEmptyDashboard.setVisibility(View.GONE);
        }
    }

    private void setupTabs() {
        binding.tabTatCa.setOnClickListener(v -> {
            selectTab(binding.tabTatCa);
            checkEmptyState();
        });

        binding.tabConTrong.setOnClickListener(v -> {
            selectTab(binding.tabConTrong);
            binding.cardLandlordPost2.setVisibility(View.GONE);
            checkEmptyState();
        });

        binding.tabDaCoc.setOnClickListener(v -> {
            selectTab(binding.tabDaCoc);
            binding.cardLandlordPost1.setVisibility(View.GONE);
            binding.cardLandlordPost2.setVisibility(View.GONE);
            binding.cardLandlordPost3.setVisibility(View.GONE);
            binding.layoutEmptyDashboard.setVisibility(View.VISIBLE);
        });

        binding.tabChoDuyet.setOnClickListener(v -> {
            selectTab(binding.tabChoDuyet);
            binding.cardLandlordPost1.setVisibility(View.GONE);
            binding.cardLandlordPost2.setVisibility(View.GONE);
            binding.cardLandlordPost3.setVisibility(View.GONE);
            binding.layoutEmptyDashboard.setVisibility(View.VISIBLE);
        });
    }

    private void selectTab(TextView selectedTab) {
        binding.tabTatCa.setBackgroundResource(R.drawable.bg_chip_pill);
        binding.tabTatCa.setTextColor(getResources().getColor(R.color.text_primary, null));

        binding.tabConTrong.setBackgroundResource(R.drawable.bg_chip_pill);
        binding.tabConTrong.setTextColor(getResources().getColor(R.color.text_primary, null));

        binding.tabDaCoc.setBackgroundResource(R.drawable.bg_chip_pill);
        binding.tabDaCoc.setTextColor(getResources().getColor(R.color.text_primary, null));

        binding.tabChoDuyet.setBackgroundResource(R.drawable.bg_chip_pill);
        binding.tabChoDuyet.setTextColor(getResources().getColor(R.color.text_primary, null));

        selectedTab.setBackgroundResource(R.drawable.bg_button_primary);
        selectedTab.setTextColor(getResources().getColor(R.color.bg_white, null));
    }

    private void fetchLandlordPosts() {
        String token = TokenManager.getToken(requireContext());
        RetrofitClient.getApiService().getLandlordPosts(token).enqueue(new Callback<ApiResponse<List<Map<String, Object>>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<Map<String, Object>>>> call, @NonNull Response<ApiResponse<List<Map<String, Object>>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<Map<String, Object>> posts = response.body().getData();
                    if (posts != null) {
                        Log.d("LandlordDashboard", "Loaded posts count: " + posts.size());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<Map<String, Object>>>> call, @NonNull Throwable t) {
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
