package com.avaroom.ui.screens.student;

import com.avaroom.navigation.NavigationController;
import com.avaroom.services.DataService;
import com.avaroom.theme.Theme;
import com.avaroom.ui.components.CardPanel;
import com.avaroom.ui.components.PillTag;
import com.avaroom.ui.components.RoundedButton;
import java.awt.*;
import javax.swing.*;

public class StudentProfileScreen extends JPanel {
    public StudentProfileScreen() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_GRAY_LIGHT);

        JPanel scrollContent = new JPanel();
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.setBackground(Theme.BG_GRAY_LIGHT);
        scrollContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Profile Card
        CardPanel profileCard = new CardPanel(18, Theme.BG_WHITE, true);
        profileCard.setLayout(new BorderLayout(14, 0));
        profileCard.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        profileCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        JLabel avatar = new JLabel("🎓");
        avatar.setFont(new Font("Segoe UI", Font.PLAIN, 36));
        profileCard.add(avatar, BorderLayout.WEST);

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setOpaque(false);

        JLabel name = new JLabel(DataService.getInstance().getCurrentUserName());
        name.setFont(Theme.FONT_TITLE);
        name.setForeground(Theme.PRIMARY_DARK);
        info.add(name);

        info.add(Box.createVerticalStrut(4));
        JLabel uni = new JLabel(DataService.getInstance().getCurrentUniversity() + " • K66");
        uni.setFont(Theme.FONT_BODY);
        uni.setForeground(Theme.TEXT_SECONDARY);
        info.add(uni);

        info.add(Box.createVerticalStrut(4));
        JPanel badges = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        badges.setOpaque(false);
        badges.add(new PillTag("✓ Đã xác thực eKYC", Theme.ACCENT_GREEN_LIGHT, Theme.ACCENT_GREEN, null));
        badges.add(new PillTag("🛡️ Cọc 0đ"));
        info.add(badges);

        profileCard.add(info, BorderLayout.CENTER);
        scrollContent.add(profileCard);

        scrollContent.add(Box.createVerticalStrut(24));

        // Quick Navigation Menu Group
        JLabel menuTitle = new JLabel("QUẢN LÝ TIỆN ÍCH & TÀI KHOẢN");
        menuTitle.setFont(Theme.FONT_CAPTION_BOLD);
        menuTitle.setForeground(Theme.TEXT_SECONDARY);
        scrollContent.add(menuTitle);

        scrollContent.add(Box.createVerticalStrut(8));

        CardPanel menuCard = new CardPanel(18, Theme.BG_WHITE, true);
        menuCard.setLayout(new BoxLayout(menuCard, BoxLayout.Y_AXIS));
        menuCard.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));
        menuCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));

        addMenuItem(menuCard, "❤️ Danh sách phòng đã lưu", () -> NavigationController.getInstance().navigateTo("SAVED_ROOMS"));
        addMenuItem(menuCard, "📅 Lịch hẹn xem phòng", () -> NavigationController.getInstance().navigateTo("STUDENT_APPOINTMENTS"));
        addMenuItem(menuCard, "🛡️ Hồ sơ sinh viên & eKYC (Cọc 0đ)", () -> NavigationController.getInstance().navigateTo("STUDENT_EKYC_PROFILE"));
        addMenuItem(menuCard, "⭐ Đánh giá & Phản hồi cộng đồng", () -> NavigationController.getInstance().navigateTo("REVIEWS"));
        addMenuItem(menuCard, "🔑 Đổi mật khẩu tài khoản", () -> NavigationController.getInstance().navigateTo("CHANGE_PASSWORD"));

        scrollContent.add(menuCard);
        scrollContent.add(Box.createVerticalStrut(24));

        // Logout Button
        RoundedButton logoutBtn = new RoundedButton("Đăng xuất tài khoản", Theme.BG_CHIP, Theme.ACCENT_RED, 14);
        logoutBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Bạn có chắc chắn muốn đăng xuất khỏi Avaroom?",
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                NavigationController.getInstance().navigateTo("LOGIN");
            }
        });
        scrollContent.add(logoutBtn);

        JScrollPane scrollPane = new JScrollPane(scrollContent);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void addMenuItem(JPanel container, String title, Runnable action) {
        JButton itemBtn = new JButton(title);
        itemBtn.setFont(Theme.FONT_BODY);
        itemBtn.setForeground(Theme.TEXT_PRIMARY);
        itemBtn.setHorizontalAlignment(SwingConstants.LEFT);
        itemBtn.setFocusPainted(false);
        itemBtn.setBorderPainted(false);
        itemBtn.setContentAreaFilled(false);
        itemBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        itemBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        itemBtn.addActionListener(e -> action.run());

        container.add(itemBtn);
        container.add(new JSeparator(SwingConstants.HORIZONTAL));
    }
}
