package com.avaroom.app.ui.landlord;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.avaroom.app.R;

public class PostDetailFragment extends Fragment {

    private TextView tvDetailTitle, tvDetailPrice;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View btnBack = view.findViewById(R.id.btn_back_post_detail);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());
        }

        tvDetailTitle = view.findViewById(R.id.tv_detail_title);
        tvDetailPrice = view.findViewById(R.id.tv_detail_price);
        View btnEditAction = view.findViewById(R.id.btn_edit_post_action);

        if (btnEditAction != null) {
            btnEditAction.setOnClickListener(v -> showEditDialog());
        }
    }

    private void showEditDialog() {
        if (getContext() == null) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Chỉnh sửa thông tin phòng trọ");

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(48, 32, 48, 32);

        final EditText inputTitle = new EditText(getContext());
        inputTitle.setHint("Tiêu đề phòng trọ");
        if (tvDetailTitle != null) {
            inputTitle.setText(tvDetailTitle.getText().toString());
        }
        layout.addView(inputTitle);

        final EditText inputPrice = new EditText(getContext());
        inputPrice.setHint("Giá thuê (VNĐ/tháng)");
        if (tvDetailPrice != null) {
            inputPrice.setText(tvDetailPrice.getText().toString());
        }
        layout.addView(inputPrice);

        builder.setView(layout);

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String newTitle = inputTitle.getText().toString().trim();
            String newPrice = inputPrice.getText().toString().trim();

            if (!newTitle.isEmpty() && tvDetailTitle != null) {
                tvDetailTitle.setText(newTitle);
            }
            if (!newPrice.isEmpty() && tvDetailPrice != null) {
                tvDetailPrice.setText(newPrice);
            }

            Toast.makeText(getContext(), "Cập nhật thông tin phòng trọ thành công!", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}
