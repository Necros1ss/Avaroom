package com.avaroom.ui.screens.auth;

import com.avaroom.navigation.NavigationController;
import com.avaroom.theme.Theme;
import com.avaroom.ui.components.RoundedButton;
import com.avaroom.ui.components.StyledTextField;
import java.awt.*;
import javax.swing.*;

public class ForgotPasswordScreen extends JPanel {
    public ForgotPasswordScreen() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_WARM_CREAM);
        setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);

        // Back
        JButton backBtn = new JButton("← Quay lại");
        backBtn.setFont(Theme.FONT_BODY_BOLD);
        backBtn.setForeground(Theme.PRIMARY_DARK);
        backBtn.setFocusPainted(false);
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        backBtn.addActionListener(e -> NavigationController.getInstance().goBack());
        content.add(backBtn);

        content.add(Box.createVerticalStrut(20));

        JLabel title = new JLabel("Khôi phục mật khẩu");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Theme.PRIMARY_DARK);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(title);

        content.add(Box.createVerticalStrut(10));

        JLabel subtitle = new JLabel("<html>Vui lòng nhập địa chỉ email đã đăng ký tài khoản Avaroom để nhận mã xác thực OTP.</html>");
        subtitle.setFont(Theme.FONT_BODY);
        subtitle.setForeground(Theme.TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitle.setMaximumSize(new Dimension(500, 40));
        content.add(subtitle);

        content.add(Box.createVerticalStrut(30));

        JLabel emailLabel = new JLabel("EMAIL ĐĂNG KÝ");
        emailLabel.setFont(Theme.FONT_CAPTION_BOLD);
        emailLabel.setForeground(Theme.PRIMARY_DARK);
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(emailLabel);

        content.add(Box.createVerticalStrut(8));
        StyledTextField emailField = new StyledTextField("sinhvien@hust.edu.vn");
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);
        emailField.setText("tung.nt225431@sis.hust.edu.vn");
        content.add(emailField);

        content.add(Box.createVerticalStrut(28));

        RoundedButton submitBtn = new RoundedButton("GỬI MÃ XÁC THỰC", Theme.PRIMARY_DARK, Theme.TEXT_WHITE, 16);
        submitBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        submitBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        submitBtn.addActionListener(e -> NavigationController.getInstance().navigateTo("OTP_VERIFY"));
        content.add(submitBtn);

        content.add(Box.createVerticalGlue());

        add(content, BorderLayout.CENTER);
    }
}
