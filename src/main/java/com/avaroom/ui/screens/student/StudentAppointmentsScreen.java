package com.avaroom.ui.screens.student;

import com.avaroom.models.AppointmentModel;
import com.avaroom.navigation.NavigationController;
import com.avaroom.services.DataService;
import com.avaroom.theme.Theme;
import com.avaroom.ui.components.CardPanel;
import com.avaroom.ui.components.PillTag;
import com.avaroom.ui.components.RoundedButton;
import java.awt.*;
import java.util.List;
import javax.swing.*;

public class StudentAppointmentsScreen extends JPanel {
    public StudentAppointmentsScreen() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_GRAY_LIGHT);

        JPanel scrollContent = new JPanel();
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.setBackground(Theme.BG_GRAY_LIGHT);
        scrollContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header Title
        JLabel title = new JLabel("Lịch hẹn xem phòng của tôi");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.PRIMARY_DARK);
        scrollContent.add(title);

        scrollContent.add(Box.createVerticalStrut(6));
        JLabel subtitle = new JLabel("Theo dõi lịch hẹn trực tiếp với chủ trọ & được Avaroom bảo lãnh an toàn");
        subtitle.setFont(Theme.FONT_BODY);
        subtitle.setForeground(Theme.TEXT_SECONDARY);
        scrollContent.add(subtitle);

        scrollContent.add(Box.createVerticalStrut(16));

        // Status Tabs Row
        JPanel tabsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        tabsRow.setOpaque(false);
        tabsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        tabsRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        tabsRow.add(new PillTag("Tất cả", Theme.PRIMARY_DARK, Theme.TEXT_WHITE, null));
        tabsRow.add(new PillTag("Đã xác nhận (1)"));
        tabsRow.add(new PillTag("Sắp tới (1)"));
        tabsRow.add(new PillTag("Đã hủy (0)"));
        scrollContent.add(tabsRow);

        scrollContent.add(Box.createVerticalStrut(16));

        // Appointment Cards
        List<AppointmentModel> list = DataService.getInstance().getAppointments();
        for (AppointmentModel apt : list) {
            CardPanel card = new CardPanel(18, Theme.BG_WHITE, true);
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 190));
            card.setAlignmentX(Component.LEFT_ALIGNMENT);

            // Card Top Row
            JPanel topRow = new JPanel(new BorderLayout());
            topRow.setOpaque(false);

            JLabel code = new JLabel(apt.getId() + " • " + apt.getRoomId());
            code.setFont(Theme.FONT_CAPTION_BOLD);
            code.setForeground(Theme.TEXT_SECONDARY);
            topRow.add(code, BorderLayout.WEST);

            topRow.add(new PillTag(apt.getStatus(), Theme.ACCENT_GREEN_LIGHT, Theme.ACCENT_GREEN, null), BorderLayout.EAST);
            card.add(topRow);

            card.add(Box.createVerticalStrut(8));

            JLabel roomTitle = new JLabel(apt.getRoomTitle());
            roomTitle.setFont(Theme.FONT_SUBTITLE);
            roomTitle.setForeground(Theme.PRIMARY_DARK);
            card.add(roomTitle);

            card.add(Box.createVerticalStrut(4));
            JLabel timeInfo = new JLabel("⏰ Thời gian: " + apt.getTimeSlot() + " ngày " + apt.getDate());
            timeInfo.setFont(Theme.FONT_BODY_BOLD);
            timeInfo.setForeground(Theme.ACCENT_RED);
            card.add(timeInfo);

            card.add(Box.createVerticalStrut(4));
            JLabel hostInfo = new JLabel("👤 Chủ trọ: " + apt.getHostName());
            hostInfo.setFont(Theme.FONT_BODY);
            hostInfo.setForeground(Theme.TEXT_SECONDARY);
            card.add(hostInfo);

            card.add(Box.createVerticalStrut(12));

            // Card Action Buttons
            JPanel actionsRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            actionsRow.setOpaque(false);

            RoundedButton chatBtn = new RoundedButton("💬 Nhắn tin", Theme.BG_WHITE, Theme.PRIMARY_DARK, 10);
            chatBtn.setOutlineOnly(true);
            chatBtn.setPreferredSize(new Dimension(100, 32));
            chatBtn.addActionListener(e -> NavigationController.getInstance().navigateTo("STUDENT_CHAT"));
            actionsRow.add(chatBtn);

            RoundedButton mapBtn = new RoundedButton("🗺️ Chỉ đường", Theme.BG_CHIP, Theme.TEXT_PRIMARY, 10);
            mapBtn.setPreferredSize(new Dimension(105, 32));
            mapBtn.addActionListener(e -> NavigationController.getInstance().navigateTo("NEAR_ME_MAP"));
            actionsRow.add(mapBtn);

            card.add(actionsRow);

            scrollContent.add(card);
            scrollContent.add(Box.createVerticalStrut(14));
        }

        JScrollPane scrollPane = new JScrollPane(scrollContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }
}
