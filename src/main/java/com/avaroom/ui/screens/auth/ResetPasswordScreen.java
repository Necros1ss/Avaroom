package com.avaroom.ui.screens.auth;

import com.avaroom.navigation.NavigationController;
import com.avaroom.theme.Theme;
import com.avaroom.ui.components.RoundedButton;
import com.avaroom.ui.components.StyledPasswordField;
import java.awt.*;
import javax.swing.*;

public class ResetPasswordScreen extends JPanel {
    public ResetPasswordScreen() {
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

        JLabel title = new JLabel("Đặt mật khẩu mới");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Theme.PRIMARY_DARK);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(title);

        content.add(Box.createVerticalStrut(10));

        JLabel subtitle = new JLabel("<html>Mật khẩu mới của bạn cần có tối thiểu 8 ký tự, bao gồm chữ hoa, chữ thường và chữ số.</html>");
        subtitle.setFont(Theme.FONT_BODY);
        subtitle.setForeground(Theme.TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitle.setMaximumSize(new Dimension(500, 40));
        content.add(subtitle);

        content.add(Box.createVerticalStrut(28));

        JLabel pass1Label = new JLabel("MẬT KHẨU MỚI");
        pass1Label.setFont(Theme.FONT_CAPTION_BOLD);
        pass1Label.setForeground(Theme.PRIMARY_DARK);
        pass1Label.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(pass1Label);

        content.add(Box.createVerticalStrut(6));
        StyledPasswordField pass1 = new StyledPasswordField("Nhập mật khẩu mới");
        pass1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        pass1.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(pass1);

        content.add(Box.createVerticalStrut(16));

        JLabel pass2Label = new JLabel("XÁC NHẬN MẬT KHẨU");
        pass2Label.setFont(Theme.FONT_CAPTION_BOLD);
        pass2Label.setForeground(Theme.PRIMARY_DARK);
        pass2Label.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(pass2Label);

        content.add(Box.createVerticalStrut(6));
        StyledPasswordField pass2 = new StyledPasswordField("Nhập lại mật khẩu mới");
        pass2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        pass2.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(pass2);

        content.add(Box.createVerticalStrut(28));

        RoundedButton submitBtn = new RoundedButton("LƯU MẬT KHẨU & ĐĂNG NHẬP", Theme.PRIMARY_DARK, Theme.TEXT_WHITE, 16);
        submitBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        submitBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        submitBtn.addActionListener(e -> NavigationController.getInstance().navigateTo("STUDENT_HOME"));
        content.add(submitBtn);

        content.add(Box.createVerticalGlue());

        add(content, BorderLayout.CENTER);
    }
}
