package com.avaroom.ui.screens.landlord;

import com.avaroom.models.AppointmentModel;
import com.avaroom.navigation.NavigationController;
import com.avaroom.services.DataService;
import com.avaroom.theme.Theme;
import com.avaroom.ui.components.CardPanel;
import com.avaroom.ui.components.PillTag;
import com.avaroom.ui.components.RoundedButton;
import com.avaroom.ui.dialogs.AppointmentDetailDialog;
import java.awt.*;
import java.util.List;
import javax.swing.*;

public class LandlordAppointmentsScreen extends JPanel {
    public LandlordAppointmentsScreen() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_GRAY_LIGHT);

        JPanel scrollContent = new JPanel();
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.setBackground(Theme.BG_GRAY_LIGHT);
        scrollContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Lịch hẹn xem phòng & Khảo sát");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.PRIMARY_DARK);
        scrollContent.add(title);

        scrollContent.add(Box.createVerticalStrut(6));
        JLabel subtitle = new JLabel("Tiếp nhận và quản lý các lịch hẹn xem phòng từ sinh viên");
        subtitle.setFont(Theme.FONT_BODY);
        subtitle.setForeground(Theme.TEXT_SECONDARY);
        scrollContent.add(subtitle);

        scrollContent.add(Box.createVerticalStrut(16));

        // Status Tabs
        JPanel tabsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        tabsRow.setOpaque(false);
        tabsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        tabsRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        tabsRow.add(new PillTag("Tất cả yêu cầu", Theme.PRIMARY_DARK, Theme.TEXT_WHITE, null));
        tabsRow.add(new PillTag("Chờ duyệt (1)"));
        tabsRow.add(new PillTag("Đã xác nhận (1)"));
        scrollContent.add(tabsRow);

        scrollContent.add(Box.createVerticalStrut(16));

        List<AppointmentModel> list = DataService.getInstance().getAppointments();
        for (AppointmentModel apt : list) {
            CardPanel card = new CardPanel(18, Theme.BG_WHITE, true);
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 190));
            card.setAlignmentX(Component.LEFT_ALIGNMENT);

            JPanel topRow = new JPanel(new BorderLayout());
            topRow.setOpaque(false);
            topRow.add(new JLabel("<html><b>Khách hẹn: " + apt.getStudentName() + "</b> (HUST K66)</html>"), BorderLayout.WEST);
            topRow.add(new PillTag(apt.getStatus(), Theme.ACCENT_GREEN_LIGHT, Theme.ACCENT_GREEN, null), BorderLayout.EAST);
            card.add(topRow);

            card.add(Box.createVerticalStrut(6));
            JLabel roomInfo = new JLabel("Phòng hẹn xem: " + apt.getRoomTitle());
            roomInfo.setFont(Theme.FONT_BODY);
            roomInfo.setForeground(Theme.PRIMARY_DARK);
            card.add(roomInfo);

            card.add(Box.createVerticalStrut(4));
            JLabel timeInfo = new JLabel("⏰ Thời gian hẹn: " + apt.getTimeSlot() + " ngày " + apt.getDate());
            timeInfo.setFont(Theme.FONT_BODY_BOLD);
            timeInfo.setForeground(Theme.ACCENT_RED);
            card.add(timeInfo);

            card.add(Box.createVerticalStrut(4));
            JLabel noteInfo = new JLabel("💬 Lời nhắn: \"" + apt.getNote() + "\"");
            noteInfo.setFont(Theme.FONT_CAPTION);
            noteInfo.setForeground(Theme.TEXT_SECONDARY);
            card.add(noteInfo);

            card.add(Box.createVerticalStrut(12));

            JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            actionRow.setOpaque(false);

            RoundedButton chatBtn = new RoundedButton("💬 Nhắn tin", Theme.BG_CHIP, Theme.PRIMARY_DARK, 10);
            chatBtn.setPreferredSize(new Dimension(100, 34));
            chatBtn.addActionListener(e -> NavigationController.getInstance().navigateTo("LANDLORD_CHAT"));
            actionRow.add(chatBtn);

            RoundedButton detailBtn = new RoundedButton("Xem chi tiết", Theme.PRIMARY_DARK, Theme.TEXT_WHITE, 10);
            detailBtn.setPreferredSize(new Dimension(105, 34));
            detailBtn.addActionListener(e -> {
                Window parent = SwingUtilities.getWindowAncestor(this);
                AppointmentDetailDialog dialog = new AppointmentDetailDialog(parent, apt);
                dialog.setVisible(true);
            });
            actionRow.add(detailBtn);

            card.add(actionRow);

            scrollContent.add(card);
            scrollContent.add(Box.createVerticalStrut(12));
        }

        JScrollPane scrollPane = new JScrollPane(scrollContent);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }
}
