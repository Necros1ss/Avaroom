package com.avaroom.ui.dialogs;

import com.avaroom.models.AppointmentModel;
import com.avaroom.models.RoomModel;
import com.avaroom.navigation.NavigationController;
import com.avaroom.services.DataService;
import com.avaroom.theme.Theme;
import com.avaroom.ui.components.CardPanel;
import com.avaroom.ui.components.PillTag;
import com.avaroom.ui.components.RoundedButton;
import java.awt.*;
import javax.swing.*;

public class BookingViewingDialog extends JDialog {
    private String selectedDate = "14/10/2026 (Hôm nay)";
    private String selectedTime = "16:30";
    private final JTextArea noteArea;

    public BookingViewingDialog(Window owner, RoomModel room) {
        super(owner, "Đặt lịch xem phòng", ModalityType.APPLICATION_MODAL);
        setSize(480, 680);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Theme.BG_WHITE);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.BG_WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER_SUBTLE),
                BorderFactory.createEmptyBorder(16, 20, 16, 20)
        ));

        JLabel title = new JLabel("Đặt lịch xem phòng");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.PRIMARY_DARK);
        header.add(title, BorderLayout.WEST);

        JButton closeBtn = new JButton("✕");
        closeBtn.setFont(Theme.FONT_TITLE);
        closeBtn.setForeground(Theme.TEXT_SECONDARY);
        closeBtn.setFocusPainted(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> dispose());
        header.add(closeBtn, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // Content
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Theme.BG_WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        // Mini summary
        CardPanel summaryCard = new CardPanel(14, Theme.BG_CHIP, false);
        summaryCard.setLayout(new BoxLayout(summaryCard, BoxLayout.Y_AXIS));
        summaryCard.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));
        summaryCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        JLabel roomTitle = new JLabel(room.getId() + " • " + room.getTitle());
        roomTitle.setFont(Theme.FONT_BODY_BOLD);
        roomTitle.setForeground(Theme.PRIMARY_DARK);
        summaryCard.add(roomTitle);

        summaryCard.add(Box.createVerticalStrut(4));
        JLabel roomSub = new JLabel(room.getFormattedPrice() + "/tháng • Chủ trọ: " + room.getHostName());
        roomSub.setFont(Theme.FONT_CAPTION);
        roomSub.setForeground(Theme.TEXT_SECONDARY);
        summaryCard.add(roomSub);

        content.add(summaryCard);
        content.add(Box.createVerticalStrut(20));

        // Step 1: Date selection
        JLabel step1 = new JLabel("1. CHỌN NGÀY XEM PHÒNG");
        step1.setFont(Theme.FONT_CAPTION_BOLD);
        step1.setForeground(Theme.TEXT_SECONDARY);
        content.add(step1);

        content.add(Box.createVerticalStrut(8));
        JPanel dateRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        dateRow.setOpaque(false);
        dateRow.add(new PillTag("Hôm nay (14/10)", Theme.PRIMARY_DARK, Theme.TEXT_WHITE, null));
        dateRow.add(new PillTag("Ngày mai (15/10)"));
        dateRow.add(new PillTag("Thứ Sáu (16/10)"));
        content.add(dateRow);

        content.add(Box.createVerticalStrut(20));

        // Step 2: Time slot selection
        JLabel step2 = new JLabel("2. CHỌN KHUNG GIỜ RẢNH");
        step2.setFont(Theme.FONT_CAPTION_BOLD);
        step2.setForeground(Theme.TEXT_SECONDARY);
        content.add(step2);

        content.add(Box.createVerticalStrut(8));
        JPanel timeRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        timeRow.setOpaque(false);
        timeRow.add(new PillTag("09:00"));
        timeRow.add(new PillTag("14:00"));
        timeRow.add(new PillTag("16:30 🔥", Theme.PRIMARY_DARK, Theme.TEXT_WHITE, null));
        timeRow.add(new PillTag("18:00"));
        content.add(timeRow);

        content.add(Box.createVerticalStrut(20));

        // Step 3: Student Note
        JLabel step3 = new JLabel("3. THÔNG TIN SINH VIÊN & LỜI NHẮN");
        step3.setFont(Theme.FONT_CAPTION_BOLD);
        step3.setForeground(Theme.TEXT_SECONDARY);
        content.add(step3);

        content.add(Box.createVerticalStrut(8));
        noteArea = new JTextArea("Cháu là Tùng K66 Bách Khoa, cháu đi cùng bạn qua xem lúc 16:30 ngày 14/10 ạ.");
        noteArea.setFont(Theme.FONT_BODY);
        noteArea.setForeground(Theme.TEXT_PRIMARY);
        noteArea.setLineWrap(true);
        noteArea.setWrapStyleWord(true);
        noteArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER_SUBTLE, 1, true),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        noteArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        content.add(noteArea);

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        // Submit Button
        JPanel bottomBar = new JPanel(new BorderLayout());
        bottomBar.setBackground(Theme.BG_WHITE);
        bottomBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDER_SUBTLE),
                BorderFactory.createEmptyBorder(12, 20, 16, 20)
        ));

        RoundedButton submitBtn = new RoundedButton("GỬI YÊU CẦU ĐẶT LỊCH NGAY", Theme.PRIMARY_DARK, Theme.TEXT_WHITE, 14);
        submitBtn.setPreferredSize(new Dimension(0, 50));
        submitBtn.addActionListener(e -> {
            DataService.getInstance().addAppointment(new AppointmentModel(
                    "APT-" + System.currentTimeMillis() % 10000,
                    room.getId(),
                    room.getTitle(),
                    DataService.getInstance().getCurrentUserName(),
                    DataService.getInstance().getCurrentUserPhone(),
                    room.getHostName(),
                    selectedDate,
                    selectedTime,
                    "Đã xác nhận",
                    noteArea.getText()
            ));
            dispose();
            JOptionPane.showMessageDialog(owner,
                    "Đặt lịch xem phòng thành công!\nChủ trọ " + room.getHostName() + " đã nhận được yêu cầu của bạn.",
                    "Avaroom Thông Báo",
                    JOptionPane.INFORMATION_MESSAGE);
            NavigationController.getInstance().navigateTo("STUDENT_APPOINTMENTS");
        });
        bottomBar.add(submitBtn, BorderLayout.CENTER);
        add(bottomBar, BorderLayout.SOUTH);
    }
}
