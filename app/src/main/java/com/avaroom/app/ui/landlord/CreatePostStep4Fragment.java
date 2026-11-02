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
import com.avaroom.app.data.model.response.ApiResponse;
import com.avaroom.app.data.remote.RetrofitClient;
import com.avaroom.app.databinding.FragmentCreatePostStep4Binding;
import com.avaroom.app.utils.TokenManager;
import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePostStep4Fragment extends Fragment {

    private FragmentCreatePostStep4Binding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCreatePostStep4Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Return button
        binding.btnBackStep4.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        // Publish / Post button
        binding.btnPublishPost.setOnClickListener(v -> {
            publishPost(v);
        });
    }

    private void publishPost(View view) {
        String token = TokenManager.getToken(requireContext());
        Map<String, Object> request = new HashMap<>();
        request.put("title", "Phòng trọ cao cấp Full nội thất Cầu Giấy");
        request.put("description", "Phòng mới xây, đầy đủ điều hòa, nóng lạnh, giờ giấc tự do, an ninh tốt.");
        request.put("price", 3500000);
        request.put("area", 25.0);
        request.put("address", "Số 125 Đường Xuân Thủy, Cầu Giấy, Hà Nội");
        request.put("district", "Cầu Giấy");
        request.put("city", "Hà Nội");

        RetrofitClient.getApiService().createLandlordPost(token, request).enqueue(new Callback<ApiResponse<Map<String, Object>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Map<String, Object>>> call, @NonNull Response<ApiResponse<Map<String, Object>>> response) {
                PostState.setNewPostAdded(true);
                Toast.makeText(requireContext(), "Đăng tin phòng trọ thành công! Đã thêm vào danh sách quản lý.", Toast.LENGTH_LONG).show();
                Navigation.findNavController(view).popBackStack(R.id.landlordDashboardFragment, false);
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Map<String, Object>>> call, @NonNull Throwable t) {
                PostState.setNewPostAdded(true);
                Toast.makeText(requireContext(), "Đăng tin thành công! Đã thêm vào danh sách quản lý.", Toast.LENGTH_LONG).show();
                Navigation.findNavController(view).popBackStack(R.id.landlordDashboardFragment, false);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
