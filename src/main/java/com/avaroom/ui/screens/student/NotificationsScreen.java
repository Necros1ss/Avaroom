package com.avaroom.ui.screens.student;

import com.avaroom.models.NotificationItem;
import com.avaroom.navigation.NavigationController;
import com.avaroom.services.DataService;
import com.avaroom.theme.Theme;
import com.avaroom.ui.components.CardPanel;
import com.avaroom.ui.components.PillTag;
import java.awt.*;
import java.util.List;
import javax.swing.*;

public class NotificationsScreen extends JPanel {
    public NotificationsScreen() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_GRAY_LIGHT);

        JPanel scrollContent = new JPanel();
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.setBackground(Theme.BG_GRAY_LIGHT);
        scrollContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        JLabel title = new JLabel("Trung tâm Thông báo");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.PRIMARY_DARK);
        topBar.add(title, BorderLayout.WEST);

        JButton markAllBtn = new JButton("Đánh dấu đã đọc tất cả");
        markAllBtn.setFont(Theme.FONT_CAPTION_BOLD);
        markAllBtn.setForeground(Theme.PRIMARY_DARK);
        markAllBtn.setFocusPainted(false);
        markAllBtn.setBorderPainted(false);
        markAllBtn.setContentAreaFilled(false);
        markAllBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        markAllBtn.addActionListener(e -> {
            DataService.getInstance().markAllNotificationsRead();
            NavigationController.getInstance().navigateTo("NOTIFICATIONS");
        });
        topBar.add(markAllBtn, BorderLayout.EAST);

        scrollContent.add(topBar);
        scrollContent.add(Box.createVerticalStrut(16));

        List<NotificationItem> list = DataService.getInstance().getNotifications();
        for (NotificationItem item : list) {
            CardPanel card = new CardPanel(16, Theme.BG_WHITE, true);
            card.setLayout(new BorderLayout(14, 0));
            card.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
            card.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel icon = new JLabel("appointment".equals(item.getType()) ? "📅" : ("billing".equals(item.getType()) ? "⚡" : "🔔"));
            icon.setFont(new Font("Segoe UI", Font.PLAIN, 24));
            card.add(icon, BorderLayout.WEST);

            JPanel info = new JPanel();
            info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
            info.setOpaque(false);

            JPanel titleRow = new JPanel(new BorderLayout());
            titleRow.setOpaque(false);

            JLabel t = new JLabel(item.getTitle());
            t.setFont(Theme.FONT_BODY_BOLD);
            t.setForeground(Theme.PRIMARY_DARK);
            titleRow.add(t, BorderLayout.WEST);

            JLabel time = new JLabel(item.getTime());
            time.setFont(Theme.FONT_CAPTION);
            time.setForeground(Theme.TEXT_MUTED);
            titleRow.add(time, BorderLayout.EAST);
            info.add(titleRow);

            info.add(Box.createVerticalStrut(4));
            JLabel desc = new JLabel("<html>" + item.getContent() + "</html>");
            desc.setFont(Theme.FONT_BODY);
            desc.setForeground(Theme.TEXT_SECONDARY);
            info.add(desc);

            card.add(info, BorderLayout.CENTER);

            // Click action
            card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            card.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    item.setRead(true);
                    if ("appointment".equals(item.getType())) {
                        String role = DataService.getInstance().getCurrentUserRole();
                        if ("LANDLORD".equals(role)) {
                            NavigationController.getInstance().navigateTo("LANDLORD_APPOINTMENTS");
                        } else {
                            NavigationController.getInstance().navigateTo("STUDENT_APPOINTMENTS");
                        }
                    } else if ("billing".equals(item.getType())) {
                        NavigationController.getInstance().navigateTo("UTILITY_BILLING");
                    }
                }
            });

            scrollContent.add(card);
            scrollContent.add(Box.createVerticalStrut(10));
        }

        JScrollPane scrollPane = new JScrollPane(scrollContent);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }
}
