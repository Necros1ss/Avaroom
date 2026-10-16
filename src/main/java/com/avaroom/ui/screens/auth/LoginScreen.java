package com.avaroom.ui.screens.auth;

import com.avaroom.navigation.NavigationController;
import com.avaroom.services.DataService;
import com.avaroom.theme.Theme;
import com.avaroom.ui.components.RoundedButton;
import com.avaroom.ui.components.StyledPasswordField;
import com.avaroom.ui.components.StyledTextField;
import java.awt.*;
import javax.swing.*;

public class LoginScreen extends JPanel {
    private final StyledTextField accountField;
    private final StyledPasswordField passwordField;
    private final JCheckBox rememberCheckbox;

    public LoginScreen() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_WARM_CREAM);

        JPanel scrollContent = new JPanel();
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.setBackground(Theme.BG_WARM_CREAM);
        scrollContent.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        // Badge
        JLabel badge = new JLabel("• ĐĂNG NHẬP HỆ THỐNG");
        badge.setFont(Theme.FONT_CAPTION_BOLD);
        badge.setForeground(Theme.TEXT_SECONDARY);
        badge.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollContent.add(badge);

        scrollContent.add(Box.createVerticalStrut(12));

        // Title
        JLabel title = new JLabel("<html>Chào mừng bạn<br>quay trở lại.</html>");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Theme.PRIMARY_DARK);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollContent.add(title);

        scrollContent.add(Box.createVerticalStrut(10));

        // Subtitle
        JLabel subtitle = new JLabel("<html>Đăng nhập để xem danh sách phòng đã lưu, kết nối trực tiếp với chủ trọ và tìm bạn cùng phòng an toàn.</html>");
        subtitle.setFont(Theme.FONT_BODY);
        subtitle.setForeground(Theme.TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitle.setMaximumSize(new Dimension(500, 50));
        scrollContent.add(subtitle);

        scrollContent.add(Box.createVerticalStrut(24));

        // Account Label & Input
        JLabel accLabel = new JLabel("TÀI KHOẢN");
        accLabel.setFont(Theme.FONT_CAPTION_BOLD);
        accLabel.setForeground(Theme.PRIMARY_DARK);
        accLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollContent.add(accLabel);

        scrollContent.add(Box.createVerticalStrut(6));
        accountField = new StyledTextField("0912... hoặc ten@hust.edu.vn");
        accountField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        accountField.setAlignmentX(Component.LEFT_ALIGNMENT);
        accountField.setText("tung.nt225431@sis.hust.edu.vn");
        scrollContent.add(accountField);

        scrollContent.add(Box.createVerticalStrut(16));

        // Password Label & Input
        JLabel passLabel = new JLabel("MẬT KHẨU");
        passLabel.setFont(Theme.FONT_CAPTION_BOLD);
        passLabel.setForeground(Theme.PRIMARY_DARK);
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollContent.add(passLabel);

        scrollContent.add(Box.createVerticalStrut(6));
        passwordField = new StyledPasswordField("Nhập mật khẩu an toàn");
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordField.setText("AvaroomPass2026@");
        scrollContent.add(passwordField);

        scrollContent.add(Box.createVerticalStrut(12));

        // Remember & Forgot Password Row
        JPanel optionsRow = new JPanel(new BorderLayout());
        optionsRow.setOpaque(false);
        optionsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        optionsRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        rememberCheckbox = new JCheckBox("Ghi nhớ đăng nhập", true);
        rememberCheckbox.setFont(Theme.FONT_BODY);
        rememberCheckbox.setForeground(Theme.TEXT_SECONDARY);
        rememberCheckbox.setOpaque(false);
        optionsRow.add(rememberCheckbox, BorderLayout.WEST);

        JButton forgotBtn = new JButton("Quên mật khẩu?");
        forgotBtn.setFont(Theme.FONT_BODY_BOLD);
        forgotBtn.setForeground(Theme.PRIMARY_DARK);
        forgotBtn.setFocusPainted(false);
        forgotBtn.setBorderPainted(false);
        forgotBtn.setContentAreaFilled(false);
        forgotBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        forgotBtn.addActionListener(e -> NavigationController.getInstance().navigateTo("FORGOT_PASSWORD"));
        optionsRow.add(forgotBtn, BorderLayout.EAST);

        scrollContent.add(optionsRow);
        scrollContent.add(Box.createVerticalStrut(24));

        // Login CTA Button
        RoundedButton loginBtn = new RoundedButton("ĐĂNG NHẬP", Theme.PRIMARY_DARK, Theme.TEXT_WHITE, 16);
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        loginBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginBtn.addActionListener(e -> {
            String role = DataService.getInstance().getCurrentUserRole();
            if ("LANDLORD".equals(role)) {
                NavigationController.getInstance().navigateTo("LANDLORD_DASHBOARD");
            } else {
                NavigationController.getInstance().navigateTo("STUDENT_HOME");
            }
        });
        scrollContent.add(loginBtn);

        scrollContent.add(Box.createVerticalStrut(20));

        // Register link row
        JPanel registerRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        registerRow.setOpaque(false);
        registerRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        registerRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel noAccLabel = new JLabel("Chưa có tài khoản?");
        noAccLabel.setFont(Theme.FONT_BODY);
        noAccLabel.setForeground(Theme.TEXT_SECONDARY);
        registerRow.add(noAccLabel);

        JButton regLink = new JButton("Đăng ký ngay");
        regLink.setFont(Theme.FONT_BODY_BOLD);
        regLink.setForeground(Theme.PRIMARY_DARK);
        regLink.setFocusPainted(false);
        regLink.setBorderPainted(false);
        regLink.setContentAreaFilled(false);
        regLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        regLink.addActionListener(e -> NavigationController.getInstance().navigateTo("REGISTER"));
        registerRow.add(regLink);

        scrollContent.add(registerRow);
        scrollContent.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(scrollContent);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        add(scrollPane, BorderLayout.CENTER);
    }
}
