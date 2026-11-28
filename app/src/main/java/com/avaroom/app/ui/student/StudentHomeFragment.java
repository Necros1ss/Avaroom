package com.avaroom.app.ui.student;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.avaroom.app.R;
import com.avaroom.app.data.model.response.ApiResponse;
import com.avaroom.app.data.model.response.RoomResponse;
import com.avaroom.app.data.remote.ApiService;
import com.avaroom.app.data.remote.RetrofitClient;
import com.avaroom.app.databinding.FragmentStudentHomeBinding;
import com.avaroom.app.utils.TokenManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentHomeFragment extends Fragment {

    private static final String TAG = "StudentHome";

    private FragmentStudentHomeBinding binding;
    private RoomCardAdapter roomAdapter;

    // Phân trang
    private int currentPage = 1;
    private static final int PAGE_SIZE = 10;
    private boolean isLoading = false;
    private boolean hasMorePages = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentStudentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerView();
        setupClickListeners();
        loadRooms(true); // load trang đầu tiên
    }

    // ── Setup RecyclerView ─────────────────────────────────────────────────────

    private void setupRecyclerView() {
        roomAdapter = new RoomCardAdapter(new RoomCardAdapter.OnRoomClickListener() {
            @Override
            public void onRoomClick(RoomResponse room) {
                // Truyền roomId qua Bundle sang RoomDetailFragment
                Bundle args = new Bundle();
                args.putLong("room_id", room.getId());
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_home_to_roomDetail, args);
            }

            @Override
            public void onSaveToggle(RoomResponse room, boolean isSaved) {
                // TODO: Gọi API lưu/bỏ lưu phòng khi feature saved rooms được xây dựng
                toggleSaveRoom(room.getId(), isSaved);
            }
        });

        binding.rvRoomList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvRoomList.setAdapter(roomAdapter);

        // Infinite scroll: load thêm khi cuộn gần cuối
        binding.rvRoomList.addOnScrollListener(new androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull androidx.recyclerview.widget.RecyclerView rv, int dx, int dy) {
                LinearLayoutManager lm = (LinearLayoutManager) rv.getLayoutManager();
                if (lm == null) return;
                int lastVisible = lm.findLastCompletelyVisibleItemPosition();
                int total = lm.getItemCount();
                if (!isLoading && hasMorePages && lastVisible >= total - 3) {
                    loadRooms(false);
                }
            }
        });

        // Pull-to-refresh (nếu layout có SwipeRefreshLayout id=swipe_refresh)
        if (binding.swipeRefresh != null) {
            binding.swipeRefresh.setOnRefreshListener(() -> loadRooms(true));
        }
    }

    // ── Lấy danh sách phòng từ API ─────────────────────────────────────────────

    private void loadRooms(boolean reset) {
        if (isLoading) return;
        isLoading = true;

        if (reset) {
            currentPage = 1;
            hasMorePages = true;
        }

        // Hiển thị loading state
        if (currentPage == 1) {
            binding.progressBarHome.setVisibility(View.VISIBLE);
            binding.tvEmptyState.setVisibility(View.GONE);
        }

        Map<String, String> filters = new HashMap<>();
        filters.put("page", String.valueOf(currentPage));
        filters.put("limit", String.valueOf(PAGE_SIZE));

        RetrofitClient.getApiService().getRooms(filters)
                .enqueue(new Callback<ApiResponse<RoomResponse.RoomsData>>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse<RoomResponse.RoomsData>> call,
                                           @NonNull Response<ApiResponse<RoomResponse.RoomsData>> response) {
                        isLoading = false;
                        binding.progressBarHome.setVisibility(View.GONE);
                        if (binding.swipeRefresh != null) binding.swipeRefresh.setRefreshing(false);

                        if (response.isSuccessful() && response.body() != null
                                && response.body().getData() != null) {

                            RoomResponse.RoomsData data = response.body().getData();
                            List<RoomResponse> rooms = data.getRooms();

                            if (reset) {
                                roomAdapter.setRooms(rooms);
                            } else {
                                roomAdapter.appendRooms(rooms);
                            }

                            // Kiểm tra còn trang nữa không
                            RoomResponse.PaginationInfo pagination = data.getPagination();
                            if (pagination != null) {
                                hasMorePages = currentPage < pagination.getTotalPages();
                            } else {
                                hasMorePages = rooms != null && rooms.size() == PAGE_SIZE;
                            }

                            currentPage++;

                            // Hiển thị empty state nếu không có phòng
                            boolean isEmpty = roomAdapter.getItemCount() == 0;
                            binding.tvEmptyState.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
                            binding.rvRoomList.setVisibility(isEmpty ? View.GONE : View.VISIBLE);

                            Log.i(TAG, "Tải danh sách phòng thành công: " + (rooms != null ? rooms.size() : 0) + " phòng");
                        } else {
                            Log.e(TAG, "Lỗi API getRooms: HTTP " + response.code());
                            if (currentPage == 1) {
                                binding.tvEmptyState.setVisibility(View.VISIBLE);
                                binding.tvEmptyState.setText("Không thể tải danh sách phòng. Vui lòng thử lại.");
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse<RoomResponse.RoomsData>> call,
                                          @NonNull Throwable t) {
                        isLoading = false;
                        binding.progressBarHome.setVisibility(View.GONE);
                        if (binding.swipeRefresh != null) binding.swipeRefresh.setRefreshing(false);
                        Log.e(TAG, "Lỗi kết nối getRooms: " + t.getMessage(), t);
                        if (currentPage == 1) {
                            binding.tvEmptyState.setVisibility(View.VISIBLE);
                            binding.tvEmptyState.setText("Lỗi kết nối. Vui lòng kiểm tra mạng và thử lại.");
                        }
                    }
                });
    }

    // ── Setup click listeners (giữ nguyên từ bản cũ) ──────────────────────────

    private void setupClickListeners() {
        // Nút bộ lọc nâng cao
        binding.btnFilterDialog.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_home_to_filter));

        // Search bar
        binding.layoutSearchBox.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_home_to_search));

        // Thông báo
        binding.btnNotification.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_home_to_notifications));

        // Profile icon → eKYC
        if (binding.btnProfileHome != null) {
            binding.btnProfileHome.setOnClickListener(v ->
                    Navigation.findNavController(v).navigate(R.id.action_home_to_ekyc));
        }
    }

    private void toggleSaveRoom(long roomId, boolean willSave)
    {
        com.avaroom.app.utils.RoomActionHelper.toggleSaveRoom(this, roomId, willSave, null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
