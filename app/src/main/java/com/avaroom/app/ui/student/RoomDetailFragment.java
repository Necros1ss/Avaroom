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
import com.avaroom.app.databinding.FragmentRoomDetailBinding;

public class RoomDetailFragment extends Fragment {

    private FragmentRoomDetailBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRoomDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Figma Prototype Connection: Back button ==> Trang chủ
        binding.btnBackDetail.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        // Figma Prototype Connection: Button - Left: Chat with Host ==> Tin nhắn & Chat với chủ trọ
        binding.btnChatHost.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_roomDetail_to_chat);
        });

        // Figma Prototype Connection: Button - Right: Book Viewing ==> Đặt lịch xem phòng
        binding.btnBookSchedule.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_roomDetail_to_booking);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}