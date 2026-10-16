package com.avaroom.ui.screens.auth;

import com.avaroom.navigation.NavigationController;
import com.avaroom.services.DataService;
import com.avaroom.theme.Theme;
import com.avaroom.ui.components.CardPanel;
import com.avaroom.ui.components.RoundedButton;
import com.avaroom.ui.components.StyledPasswordField;
import com.avaroom.ui.components.StyledTextField;
import java.awt.*;
import javax.swing.*;

public class RegisterScreen extends JPanel {
    private boolean isLandlord = false;
    private final RoundedButton studentRoleBtn;
    private final RoundedButton landlordRoleBtn;
    private final StyledTextField nameField;
    private final StyledTextField phoneField;
    private final StyledPasswordField passField;

    public RegisterScreen() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_WARM_CREAM);

        JPanel scrollContent = new JPanel();
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.setBackground(Theme.BG_WARM_CREAM);
        scrollContent.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        // Back button
        JButton backBtn = new JButton("← Quay lại");
        backBtn.setFont(Theme.FONT_BODY_BOLD);
        backBtn.setForeground(Theme.PRIMARY_DARK);
        backBtn.setFocusPainted(false);
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        backBtn.addActionListener(e -> NavigationController.getInstance().goBack());
        scrollContent.add(backBtn);

        scrollContent.add(Box.createVerticalStrut(16));

        // Title
        JLabel title = new JLabel("Tạo tài khoản mới");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Theme.PRIMARY_DARK);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollContent.add(title);

        scrollContent.add(Box.createVerticalStrut(8));
        JLabel subtitle = new JLabel("Chọn vai trò phù hợp để trải nghiệm các tính năng dành riêng cho bạn.");
        subtitle.setFont(Theme.FONT_BODY);
        subtitle.setForeground(Theme.TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollContent.add(subtitle);

        scrollContent.add(Box.createVerticalStrut(20));

        // Role Switcher Panel
        CardPanel roleBox = new CardPanel(14, Theme.BG_CHIP, false);
        roleBox.setLayout(new GridLayout(1, 2, 6, 0));
        roleBox.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        roleBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        roleBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        studentRoleBtn = new RoundedButton("🎓 Sinh viên", Theme.PRIMARY_DARK, Theme.TEXT_WHITE, 12);
        landlordRoleBtn = new RoundedButton("🏢 Chủ trọ", Theme.BG_CHIP, Theme.TEXT_SECONDARY, 12);

        studentRoleBtn.addActionListener(e -> setLandlordRole(false));
        landlordRoleBtn.addActionListener(e -> setLandlordRole(true));

        roleBox.add(studentRoleBtn);
        roleBox.add(landlordRoleBtn);
        scrollContent.add(roleBox);

        scrollContent.add(Box.createVerticalStrut(24));

        // Full Name
        JLabel nameLabel = new JLabel("HỌ VÀ TÊN");
        nameLabel.setFont(Theme.FONT_CAPTION_BOLD);
        nameLabel.setForeground(Theme.PRIMARY_DARK);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollContent.add(nameLabel);

        scrollContent.add(Box.createVerticalStrut(6));
        nameField = new StyledTextField("Nguyễn Văn A");
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        nameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollContent.add(nameField);

        scrollContent.add(Box.createVerticalStrut(16));

        // Phone Number
        JLabel phoneLabel = new JLabel("SỐ ĐIỆN THOẠI");
        phoneLabel.setFont(Theme.FONT_CAPTION_BOLD);
        phoneLabel.setForeground(Theme.PRIMARY_DARK);
        phoneLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollContent.add(phoneLabel);

        scrollContent.add(Box.createVerticalStrut(6));
        phoneField = new StyledTextField("0912 345 678");
        phoneField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        phoneField.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollContent.add(phoneField);

        scrollContent.add(Box.createVerticalStrut(16));

        // Password
        JLabel passLabel = new JLabel("MẬT KHẨU");
        passLabel.setFont(Theme.FONT_CAPTION_BOLD);
        passLabel.setForeground(Theme.PRIMARY_DARK);
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollContent.add(passLabel);

        scrollContent.add(Box.createVerticalStrut(6));
        passField = new StyledPasswordField("Tạo mật khẩu an toàn (tối thiểu 8 ký tự)");
        passField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        passField.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollContent.add(passField);

        scrollContent.add(Box.createVerticalStrut(24));

        // Submit Button
        RoundedButton submitBtn = new RoundedButton("TIẾP TỤC", Theme.PRIMARY_DARK, Theme.TEXT_WHITE, 16);
        submitBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        submitBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        submitBtn.addActionListener(e -> {
            DataService.getInstance().setCurrentUserRole(isLandlord ? "LANDLORD" : "STUDENT");
            if (isLandlord) {
                NavigationController.getInstance().navigateTo("EKYC_CARD");
            } else {
                NavigationController.getInstance().navigateTo("OTP_VERIFY");
            }
        });
        scrollContent.add(submitBtn);

        scrollContent.add(Box.createVerticalStrut(20));

        // Login link row
        JPanel loginRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        loginRow.setOpaque(false);
        loginRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel hasAccLabel = new JLabel("Đã có tài khoản?");
        hasAccLabel.setFont(Theme.FONT_BODY);
        hasAccLabel.setForeground(Theme.TEXT_SECONDARY);
        loginRow.add(hasAccLabel);

        JButton loginLink = new JButton("Đăng nhập");
        loginLink.setFont(Theme.FONT_BODY_BOLD);
        loginLink.setForeground(Theme.PRIMARY_DARK);
        loginLink.setFocusPainted(false);
        loginLink.setBorderPainted(false);
        loginLink.setContentAreaFilled(false);
        loginLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginLink.addActionListener(e -> NavigationController.getInstance().navigateTo("LOGIN"));
        loginRow.add(loginLink);

        scrollContent.add(loginRow);
        scrollContent.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(scrollContent);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void setLandlordRole(boolean landlord) {
        this.isLandlord = landlord;
        if (landlord) {
            landlordRoleBtn.setCustomColors(Theme.PRIMARY_DARK, Theme.TEXT_WHITE);
            studentRoleBtn.setCustomColors(Theme.BG_CHIP, Theme.TEXT_SECONDARY);
        } else {
            studentRoleBtn.setCustomColors(Theme.PRIMARY_DARK, Theme.TEXT_WHITE);
            landlordRoleBtn.setCustomColors(Theme.BG_CHIP, Theme.TEXT_SECONDARY);
        }
    }
}
