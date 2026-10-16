package com.avaroom.ui.components;

import com.avaroom.navigation.NavigationController;
import com.avaroom.services.DataService;
import com.avaroom.theme.Theme;
import java.awt.*;
import javax.swing.*;

public class AppHeader extends JPanel {
    private final JLabel titleLabel;
    private final RoundedButton backBtn;
    private final RoundedButton roleSwitchBtn;
    private final RoundedButton notifBtn;

    public AppHeader() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER_SUBTLE),
                BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));

        // Left Container: Back Button + Brand Logo + Title
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setOpaque(false);

        backBtn = new RoundedButton("←", Theme.BG_CHIP, Theme.TEXT_PRIMARY, 12);
        backBtn.setPreferredSize(new Dimension(36, 36));
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        backBtn.addActionListener(e -> NavigationController.getInstance().goBack());
        leftPanel.add(backBtn);

        JLabel brandLogo = new JLabel("AVAROOM");
        brandLogo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        brandLogo.setForeground(Theme.PRIMARY_DARK);
        leftPanel.add(brandLogo);

        titleLabel = new JLabel("• Trang Chủ");
        titleLabel.setFont(Theme.FONT_BODY);
        titleLabel.setForeground(Theme.TEXT_SECONDARY);
        leftPanel.add(titleLabel);

        add(leftPanel, BorderLayout.WEST);

        // Right Container: Role Switcher + Notification Bell
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);

        roleSwitchBtn = new RoundedButton("👤 Sinh viên", Theme.PRIMARY_DARK, Theme.TEXT_WHITE, 12);
        roleSwitchBtn.setFont(Theme.FONT_CAPTION_BOLD);
        roleSwitchBtn.setPreferredSize(new Dimension(110, 36));
        roleSwitchBtn.addActionListener(e -> toggleRole());
        rightPanel.add(roleSwitchBtn);

        notifBtn = new RoundedButton("🔔", Theme.BG_CHIP, Theme.TEXT_PRIMARY, 12);
        notifBtn.setPreferredSize(new Dimension(38, 36));
        notifBtn.addActionListener(e -> {
            NavigationController.getInstance().navigateTo("NOTIFICATIONS");
        });
        rightPanel.add(notifBtn);

        add(rightPanel, BorderLayout.EAST);
        updateState();
    }

    public void updateState() {
        backBtn.setVisible(NavigationController.getInstance().canGoBack());
        String role = DataService.getInstance().getCurrentUserRole();
        if ("LANDLORD".equals(role)) {
            roleSwitchBtn.setText("🏢 Chủ trọ");
            roleSwitchBtn.setCustomColors(Theme.PRIMARY_BLUE, Theme.TEXT_WHITE);
        } else {
            roleSwitchBtn.setText("🎓 Sinh viên");
            roleSwitchBtn.setCustomColors(Theme.PRIMARY_DARK, Theme.TEXT_WHITE);
        }
        int unread = DataService.getInstance().getUnreadNotificationCount();
        notifBtn.setText(unread > 0 ? "🔔 " + unread : "🔔");
        revalidate();
        repaint();
    }

    public void setTitle(String title) {
        titleLabel.setText("• " + title);
        updateState();
    }

    private void toggleRole() {
        String current = DataService.getInstance().getCurrentUserRole();
        if ("STUDENT".equals(current)) {
            DataService.getInstance().setCurrentUserRole("LANDLORD");
            NavigationController.getInstance().navigateTo("LANDLORD_DASHBOARD");
        } else {
            DataService.getInstance().setCurrentUserRole("STUDENT");
            NavigationController.getInstance().navigateTo("STUDENT_HOME");
        }
        updateState();
    }
}
