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
import com.avaroom.app.data.remote.RetrofitClient;
import com.avaroom.app.databinding.FragmentSavedRoomsBinding;
import com.avaroom.app.utils.TokenManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SavedRoomsFragment extends Fragment {

    private static final String TAG = "SavedRoomsFragment";

    private FragmentSavedRoomsBinding binding;
    private RoomCardAdapter roomAdapter;
    private final List<RoomResponse> savedRoomsList = new ArrayList<>();
    private boolean isLoading = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSavedRoomsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Nút quay lại
        if (binding.btnBackSaved != null) {
            binding.btnBackSaved.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());
        }

        setupRecyclerView();
        setupSwipeRefresh();
        loadSavedRooms();
    }

    private void setupRecyclerView() {
        roomAdapter = new RoomCardAdapter(new RoomCardAdapter.OnRoomClickListener() {
            @Override
            public void onRoomClick(RoomResponse room) {
                // Điều hướng sang chi tiết phòng trọ
                Bundle args = new Bundle();
                args.putLong("room_id", room.getId());
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_saved_to_roomDetail, args);
            }

            @Override
            public void onSaveToggle(RoomResponse room, boolean isSaved) {
                // Nếu người dùng ấn bỏ lưu ở danh sách này
                if (!isSaved) {
                    removeSavedRoom(room);
                }
            }
        });

        binding.rvSavedRooms.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvSavedRooms.setAdapter(roomAdapter);
    }

    private void setupSwipeRefresh() {
        if (binding.swipeRefreshSaved != null) {
            binding.swipeRefreshSaved.setColorSchemeResources(R.color.student_accent_red, R.color.student_accent_blue);
            binding.swipeRefreshSaved.setOnRefreshListener(this::loadSavedRooms);
        }
    }

    private void loadSavedRooms() {
        if (isLoading) return;

        String token = TokenManager.getToken(requireContext());
        if (token == null || token.isEmpty()) {
            if (binding.swipeRefreshSaved != null) binding.swipeRefreshSaved.setRefreshing(false);
            showEmptyState(true, "Vui lòng đăng nhập để xem danh sách phòng đã lưu");
            return;
        }

        isLoading = true;
        binding.progressBarSaved.setVisibility(View.VISIBLE);
        binding.layoutEmptySaved.setVisibility(View.GONE);

        RetrofitClient.getApiService().getSavedRooms(token, 1, 50)
                .enqueue(new Callback<ApiResponse<List<RoomResponse>>>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse<List<RoomResponse>>> call,
                                           @NonNull Response<ApiResponse<List<RoomResponse>>> response) {
                        isLoading = false;
                        if (binding == null) return;

                        binding.progressBarSaved.setVisibility(View.GONE);
                        if (binding.swipeRefreshSaved != null) {
                            binding.swipeRefreshSaved.setRefreshing(false);
                        }

                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                            List<RoomResponse> rooms = response.body().getData();
                            savedRoomsList.clear();
                            savedRoomsList.addAll(rooms);
                            roomAdapter.setRooms(savedRoomsList);

                            // Cập nhật số lượng trên tag
                            if (binding.tvTagAll != null) {
                                binding.tvTagAll.setText("Tất cả (" + savedRoomsList.size() + ")");
                            }

                            if (savedRoomsList.isEmpty()) {
                                showEmptyState(true, "Chưa có phòng nào được lưu");
                            } else {
                                showEmptyState(false, null);
                            }
                        } else {
                            Log.e(TAG, "Không thể tải phòng đã lưu: HTTP " + response.code());
                            Toast.makeText(requireContext(), "Không thể tải danh sách phòng đã lưu", Toast.LENGTH_SHORT).show();
                            if (savedRoomsList.isEmpty()) {
                                showEmptyState(true, "Không thể tải danh sách phòng đã lưu");
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse<List<RoomResponse>>> call, @NonNull Throwable t) {
                        isLoading = false;
                        if (binding == null) return;

                        binding.progressBarSaved.setVisibility(View.GONE);
                        if (binding.swipeRefreshSaved != null) {
                            binding.swipeRefreshSaved.setRefreshing(false);
                        }

                        Log.e(TAG, "Lỗi kết nối getSavedRooms: " + t.getMessage(), t);
                        Toast.makeText(requireContext(), "Lỗi kết nối mạng khi tải phòng đã lưu", Toast.LENGTH_SHORT).show();
                        if (savedRoomsList.isEmpty()) {
                            showEmptyState(true, "Lỗi kết nối mạng");
                        }
                    }
                });
    }

    private void removeSavedRoom(RoomResponse room) {
        String token = TokenManager.getToken(requireContext());
        if (token == null || token.isEmpty()) return;

        RetrofitClient.getApiService().removeSavedRoom(token, room.getId())
                .enqueue(new Callback<ApiResponse<Object>>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse<Object>> call, @NonNull Response<ApiResponse<Object>> response) {
                        if (response.isSuccessful()) {
                            savedRoomsList.remove(room);
                            roomAdapter.setRooms(savedRoomsList);
                            if (binding != null && binding.tvTagAll != null) {
                                binding.tvTagAll.setText("Tất cả (" + savedRoomsList.size() + ")");
                            }
                            if (savedRoomsList.isEmpty()) {
                                showEmptyState(true, "Chưa có phòng nào được lưu");
                            }
                            Toast.makeText(requireContext(), "Đã bỏ lưu phòng", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), "Thao tác bỏ lưu thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse<Object>> call, @NonNull Throwable t) {
                        Toast.makeText(requireContext(), "Lỗi kết nối khi bỏ lưu phòng", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showEmptyState(boolean show, @Nullable String message) {
        if (binding == null) return;
        binding.layoutEmptySaved.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.rvSavedRooms.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
