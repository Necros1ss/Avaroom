package com.avaroom.ui.screens.auth;

import com.avaroom.navigation.NavigationController;
import com.avaroom.theme.Theme;
import com.avaroom.ui.components.RoundedButton;
import com.avaroom.ui.components.StyledTextField;
import java.awt.*;
import javax.swing.*;

public class OtpVerificationScreen extends JPanel {
    public OtpVerificationScreen() {
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

        JLabel title = new JLabel("Xác thực mã OTP");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Theme.PRIMARY_DARK);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(title);

        content.add(Box.createVerticalStrut(10));

        JLabel subtitle = new JLabel("<html>Mã gồm 6 chữ số đã được gửi qua email/tin nhắn của bạn. Vui lòng kiểm tra và nhập vào ô bên dưới.</html>");
        subtitle.setFont(Theme.FONT_BODY);
        subtitle.setForeground(Theme.TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitle.setMaximumSize(new Dimension(500, 40));
        content.add(subtitle);

        content.add(Box.createVerticalStrut(32));

        StyledTextField otpField = new StyledTextField("• • • • • •");
        otpField.setFont(new Font("Segoe UI", Font.BOLD, 24));
        otpField.setHorizontalAlignment(SwingConstants.CENTER);
        otpField.setText("8 6 4 2 0 9");
        otpField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
        otpField.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(otpField);

        content.add(Box.createVerticalStrut(16));

        JLabel timerLabel = new JLabel("Gửi lại mã sau: 00:45s");
        timerLabel.setFont(Theme.FONT_CAPTION);
        timerLabel.setForeground(Theme.TEXT_SECONDARY);
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(timerLabel);

        content.add(Box.createVerticalStrut(28));

        RoundedButton submitBtn = new RoundedButton("XÁC NHẬN", Theme.PRIMARY_DARK, Theme.TEXT_WHITE, 16);
        submitBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        submitBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        submitBtn.addActionListener(e -> NavigationController.getInstance().navigateTo("RESET_PASSWORD"));
        content.add(submitBtn);

        content.add(Box.createVerticalGlue());

        add(content, BorderLayout.CENTER);
    }
}
