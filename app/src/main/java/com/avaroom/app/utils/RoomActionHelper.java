package com.avaroom.app.utils;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.avaroom.app.data.model.response.ApiResponse;
import com.avaroom.app.data.remote.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public final class RoomActionHelper {

    private RoomActionHelper() {}

    /**
     * Gọi API lưu hoặc bỏ lưu phòng trọ, tự động hiển thị Toast thông báo và gọi callback an toàn khi Fragment còn active.
     */
    public static void toggleSaveRoom(@NonNull Fragment fragment, long roomId, boolean willSave, @Nullable Runnable onSuccess) {
        Context context = fragment.getContext();
        if (context == null || !fragment.isAdded()) return;

        String token = TokenManager.getToken(context);
        if (token == null || token.isEmpty()) {
            Toast.makeText(context, "Vui lòng đăng nhập để thực hiện thao tác này", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<ApiResponse<Object>> call = willSave
                ? RetrofitClient.getApiService().saveRoom(token, roomId)
                : RetrofitClient.getApiService().removeSavedRoom(token, roomId);

        call.enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Object>> call, @NonNull Response<ApiResponse<Object>> response) {
                if (!fragment.isAdded() || fragment.getContext() == null) return;

                if (response.isSuccessful()) {
                    String msg = willSave ? "Đã lưu phòng thành công!" : "Đã bỏ lưu phòng!";
                    Toast.makeText(fragment.requireContext(), msg, Toast.LENGTH_SHORT).show();
                    if (onSuccess != null) {
                        onSuccess.run();
                    }
                } else {
                    Toast.makeText(fragment.requireContext(), "Thao tác không thành công", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Object>> call, @NonNull Throwable t) {
                if (!fragment.isAdded() || fragment.getContext() == null) return;
                Toast.makeText(fragment.requireContext(), "Lỗi kết nối mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
