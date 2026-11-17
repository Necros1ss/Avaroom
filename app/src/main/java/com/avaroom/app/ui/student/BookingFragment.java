package com.avaroom.app.ui.student;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.avaroom.app.R;
import com.avaroom.app.data.model.response.ApiResponse;
import com.avaroom.app.data.model.response.RoomResponse;
import com.avaroom.app.data.remote.RetrofitClient;
import com.avaroom.app.databinding.FragmentBookingBinding;
import com.avaroom.app.utils.TokenManager;
import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingFragment extends Fragment {

    private static final String TAG = "BookingFragment";

    private FragmentBookingBinding binding;
    private long roomId = -1;
    private RoomResponse roomData = null;

    // Ngày và giờ được chọn
    private final Calendar selectedCalendar = Calendar.getInstance();
    private String selectedTime = "16:30";

    // Danh sách 5 ngày hiển thị ngang
    private final List<Calendar> upcomingDates = new ArrayList<>();
    private int selectedDateIndex = 1; // Mặc định: ngày mai (index 1)

    // Các button giờ
    private TextView[] timeButtons;
    private final String[] timeValues = {"09:00", "10:30", "14:00", "15:30", "16:30", "18:00", "19:30"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBookingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Nhận arguments từ RoomDetailFragment
        if (getArguments() != null) {
            roomId = getArguments().getLong("room_id", -1);
        }

        // 2. Setup dữ liệu ngày giờ mặc định (bắt đầu từ ngày mai)
        initDateSelection();
        initTimeButtons();

        // 3. Load thông tin sinh viên từ session
        loadStudentProfile();

        // 4. Load thông tin phòng từ API
        if (roomId > 0) {
            loadRoomInfo(roomId);
        }

        // 5. Setup các listener (Click ngày, click icon lịch, click giờ, nút gửi)
        setupListeners();
    }

    // ── Khởi tạo 5 ngày kế tiếp theo ngày thực tế hiện tại ─────────────────────────
    private void initDateSelection() {
        upcomingDates.clear();
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < 5; i++) {
            Calendar c = (Calendar) cal.clone();
            c.add(Calendar.DAY_OF_YEAR, i);
            upcomingDates.add(c);
        }

        // Mặc định chọn ngày mai (index 1)
        selectedCalendar.setTime(upcomingDates.get(1).getTime());
        selectedDateIndex = 1;

        updateDateTabsUI();
        updateSelectedDateTimePreview();
    }

    private void updateDateTabsUI() {
        LinearLayout[] dateTabs = {
                binding.btnDate1, binding.btnDate2, binding.btnDate3, binding.btnDate4, binding.btnDate5
        };
        TextView[] dayNames = {
                binding.tvDateDayName1, binding.tvDateDayName2, binding.tvDateDayName3, binding.tvDateDayName4, binding.tvDateDayName5
        };
        TextView[] dayNumbers = {
                binding.tvDateNumber1, binding.tvDateNumber2, binding.tvDateNumber3, binding.tvDateNumber4, binding.tvDateNumber5
        };
        TextView[] daySubs = {
                binding.tvDateSub1, binding.tvDateSub2, binding.tvDateSub3, binding.tvDateSub4, binding.tvDateSub5
        };

        SimpleDateFormat dayOfWeekFmt = new SimpleDateFormat("EEE", new Locale("vi", "VN"));

        for (int i = 0; i < 5; i++) {
            Calendar c = upcomingDates.get(i);
            int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

            // Tiêu đề ngày: Hôm nay, Ngày mai, hoặc Thứ X
            String title;
            if (i == 0) title = "Hôm nay";
            else if (i == 1) title = "Ngày mai";
            else {
                int dow = c.get(Calendar.DAY_OF_WEEK);
                title = (dow == Calendar.SUNDAY) ? "Chủ Nhật" : "Thứ " + dow;
            }

            dayNames[i].setText(title);
            dayNumbers[i].setText(String.valueOf(dayOfMonth));
            daySubs[i].setText(dayOfWeekFmt.format(c.getTime()));

            boolean isSelected = (i == selectedDateIndex);
            if (isSelected) {
                dateTabs[i].setBackgroundResource(R.drawable.student_booking_bg_date_selected);
                dateTabs[i].setElevation(4f);
                dayNames[i].setTextColor(0xCCFFFFFF);
                dayNumbers[i].setTextColor(0xFFFFFFFF);
                daySubs[i].setTextColor(0xCCFFFFFF);
            } else {
                dateTabs[i].setBackgroundResource(R.drawable.student_booking_bg_date_unselected);
                dateTabs[i].setElevation(1f);
                dayNames[i].setTextColor(ContextCompat.getColor(requireContext(), R.color.student_booking_text_muted));
                dayNumbers[i].setTextColor(ContextCompat.getColor(requireContext(), R.color.student_booking_text_dark));
                daySubs[i].setTextColor(ContextCompat.getColor(requireContext(), R.color.student_booking_text_muted));
            }
        }
    }

    // ── Khởi tạo danh sách nút chọn giờ ──────────────────────────────────────────
    private void initTimeButtons() {
        timeButtons = new TextView[]{
                binding.tvTime0900, binding.tvTime1030, binding.tvTime1400,
                binding.tvTime1530, binding.tvTime1630, binding.tvTime1800, binding.tvTime1930
        };

        for (int i = 0; i < timeButtons.length; i++) {
            final int index = i;
            timeButtons[i].setOnClickListener(v -> selectTime(timeValues[index]));
        }

        // Mặc định chọn 16:30
        selectTime("16:30");
    }

    private void selectTime(String time) {
        selectedTime = time;

        for (int i = 0; i < timeButtons.length; i++) {
            boolean isMatch = timeValues[i].equals(time);
            if (isMatch) {
                timeButtons[i].setBackgroundResource(R.drawable.student_booking_bg_date_selected);
                timeButtons[i].setTextColor(0xFFFFFFFF);
            } else {
                timeButtons[i].setBackgroundResource(R.drawable.student_booking_bg_card_highlight);
                timeButtons[i].setTextColor(ContextCompat.getColor(requireContext(), R.color.student_booking_text_dark));
            }
        }

        // Ẩn/hiện indicator chấm đỏ dưới 16:30 nếu có
        if (binding.indicator1630 != null) {
            binding.indicator1630.setVisibility("16:30".equals(time) ? View.VISIBLE : View.GONE);
        }

        updateSelectedDateTimePreview();
    }

    private void updateSelectedDateTimePreview() {
        SimpleDateFormat previewFmt = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dateStr = previewFmt.format(selectedCalendar.getTime());
        binding.tvSelectedDatetimePreview.setText(selectedTime + " • " + dateStr);

        // Tự động cập nhật gợi ý lời nhắn
        updateDefaultMessage();
    }

    // ── Hộp thoại DatePickerDialog khi bấm icon lịch 📅 ──────────────────────────
    private void openDatePickerDialog() {
        int year = selectedCalendar.get(Calendar.YEAR);
        int month = selectedCalendar.get(Calendar.MONTH);
        int day = selectedCalendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(requireContext(), (view, y, m, d) -> {
            selectedCalendar.set(Calendar.YEAR, y);
            selectedCalendar.set(Calendar.MONTH, m);
            selectedCalendar.set(Calendar.DAY_OF_MONTH, d);

            // Kiểm tra xem ngày chọn có nằm trong 5 tab không
            selectedDateIndex = -1;
            for (int i = 0; i < upcomingDates.size(); i++) {
                Calendar c = upcomingDates.get(i);
                if (c.get(Calendar.YEAR) == y && c.get(Calendar.DAY_OF_YEAR) == selectedCalendar.get(Calendar.DAY_OF_YEAR)) {
                    selectedDateIndex = i;
                    break;
                }
            }

            updateDateTabsUI();
            updateSelectedDateTimePreview();

            SimpleDateFormat fmt = new SimpleDateFormat("EEEE, dd/MM/yyyy", new Locale("vi", "VN"));
            Toast.makeText(requireContext(), "Đã chọn: " + fmt.format(selectedCalendar.getTime()), Toast.LENGTH_SHORT).show();
        }, year, month, day);

        // Giới hạn chỉ chọn từ hôm nay trở đi
        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.show();
    }

    // ── Load thông tin phòng từ API ─────────────────────────────────────────────
    private void loadRoomInfo(long id) {
        RetrofitClient.getApiService().getRoomById(id)
                .enqueue(new Callback<ApiResponse<RoomResponse>>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse<RoomResponse>> call, @NonNull Response<ApiResponse<RoomResponse>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                            roomData = response.body().getData();
                            bindRoomInfo(roomData);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse<RoomResponse>> call, @NonNull Throwable t) {
                        Log.e(TAG, "Lỗi load room: " + t.getMessage());
                    }
                });
    }

    private void bindRoomInfo(RoomResponse room) {
        if (binding == null || room == null) return;

        // Tiêu đề phòng
        binding.tvBookingRoomTitle.setText(room.getTitle());

        // Mã phòng
        binding.tvBookingRoomCode.setText("#AVR-" + room.getId());

        // Giá phòng: ví dụ 4.2 tr hoặc 4.200.000 đ
        if (room.getPrice() > 0) {
            double priceInMillion = room.getPrice() / 1_000_000.0;
            if (priceInMillion == (long) priceInMillion) {
                binding.tvBookingRoomPrice.setText(String.format(Locale.getDefault(), "%.0f tr", priceInMillion));
            } else {
                binding.tvBookingRoomPrice.setText(String.format(Locale.getDefault(), "%.1f tr", priceInMillion));
            }
        }

        // Tên chủ trọ
        if (room.getLandlord() != null && room.getLandlord().getFullName() != null) {
            String landlordName = room.getLandlord().getFullName();
            binding.tvBookingLandlordName.setText(landlordName);
            binding.tvLandlordMessageLabel.setText("Lời nhắn gửi " + landlordName + " (Tùy chọn):");
        }

        // Ảnh thumbnail
        String thumb = room.getThumbnailUrl();
        if (thumb != null && !thumb.isEmpty()) {
            Glide.with(this)
                    .load(thumb)
                    .placeholder(R.drawable.student_img_room_1)
                    .centerCrop()
                    .into(binding.ivBookingRoomThumb);
        }

        updateDefaultMessage();
    }

    // ── Load thông tin sinh viên từ session ─────────────────────────────────────
    private void loadStudentProfile() {
        String studentName = TokenManager.getUserName(requireContext());
        if (studentName == null || studentName.trim().isEmpty()) {
            studentName = "Sinh viên Avaroom";
        }

        binding.tvStudentName.setText(studentName);

        // Chữ cái đại diện avatar (Ví dụ "Nguyễn Văn A" -> "NA")
        String initials = getInitials(studentName);
        binding.tvStudentAvatarLetter.setText(initials);

        String email = TokenManager.getUserEmail(requireContext());
        if (email != null && !email.isEmpty()) {
            binding.tvStudentSubInfo.setText(email + " • Hồ sơ đã xác thực");
        } else {
            binding.tvStudentSubInfo.setText("Tài khoản sinh viên • Đã xác thực");
        }

        updateDefaultMessage();
    }

    private String getInitials(String name) {
        if (name == null || name.trim().isEmpty()) return "SV";
        String[] parts = name.trim().split("\\s+");
        if (parts.length == 1) {
            return parts[0].substring(0, Math.min(2, parts[0].length())).toUpperCase();
        }
        return ("" + parts[0].charAt(0) + parts[parts.length - 1].charAt(0)).toUpperCase();
    }

    private void updateDefaultMessage() {
        if (binding == null) return;
        String studentName = TokenManager.getUserName(requireContext());
        if (studentName == null || studentName.isEmpty()) studentName = "em";

        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dateStr = fmt.format(selectedCalendar.getTime());

        String defaultMsg = "Em là " + studentName + ", em muốn đặt lịch qua xem phòng lúc "
                + selectedTime + " ngày " + dateStr + " ạ. Rất mong nhận được phản hồi từ chủ trọ.";
        binding.etBookingMessage.setText(defaultMsg);
    }

    // ── Gắn sự kiện Click ────────────────────────────────────────────────────────
    private void setupListeners() {
        // Nút quay lại
        binding.btnBackBooking.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        // 5 tab ngày
        LinearLayout[] dateTabs = {
                binding.btnDate1, binding.btnDate2, binding.btnDate3, binding.btnDate4, binding.btnDate5
        };
        for (int i = 0; i < dateTabs.length; i++) {
            final int index = i;
            dateTabs[i].setOnClickListener(v -> {
                selectedDateIndex = index;
                selectedCalendar.setTime(upcomingDates.get(index).getTime());
                updateDateTabsUI();
                updateSelectedDateTimePreview();
            });
        }

        // Icon lịch 📅: Mở DatePickerDialog để chọn bất kỳ ngày nào
        binding.btnOpenCalendar.setOnClickListener(v -> openDatePickerDialog());

        // Nút "Tự điền"
        binding.btnAutoFill.setOnClickListener(v -> {
            loadStudentProfile();
            updateDefaultMessage();
            Toast.makeText(requireContext(), "Đã tự động cập nhật thông tin cá nhân và thời gian xem", Toast.LENGTH_SHORT).show();
        });

        // Nút gửi yêu cầu đặt lịch
        binding.btnSendBooking.setOnClickListener(v -> {
            SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String dateStr = fmt.format(selectedCalendar.getTime());
            Toast.makeText(requireContext(), "Đã gửi yêu cầu đặt lịch xem phòng lúc " + selectedTime + " ngày " + dateStr + "!", Toast.LENGTH_LONG).show();

            Navigation.findNavController(v).navigate(R.id.action_booking_to_bookingSuccess);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
