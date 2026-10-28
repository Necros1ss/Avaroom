package com.avaroom.app.ui.student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.avaroom.app.databinding.FragmentChatBinding;

public class ChatFragment extends Fragment {

    private FragmentChatBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Click Back
        binding.btnBackChat.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        // Click Call
        binding.btnCallLandlord.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Đang kết nối cuộc gọi tới Cô Hoàng Thị Ánh Tuyết...", Toast.LENGTH_SHORT).show();
        });

        // Click Send message
        binding.btnSendChat.setOnClickListener(v -> {
            String msg = binding.etChatInput.getText() != null ? binding.etChatInput.getText().toString().trim() : "";
            if (!msg.isEmpty()) {
                binding.etChatInput.setText("");
                Toast.makeText(requireContext(), "Đã gửi tin nhắn!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
