package com.avaroom.ui.screens.student;

import com.avaroom.navigation.NavigationController;
import com.avaroom.theme.Theme;
import com.avaroom.ui.components.RoundedButton;
import com.avaroom.ui.components.StyledPasswordField;
import java.awt.*;
import javax.swing.*;

public class ChangePasswordScreen extends JPanel {
    public ChangePasswordScreen() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_GRAY_LIGHT);
        setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);

        JLabel title = new JLabel("Đổi mật khẩu tài khoản");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.PRIMARY_DARK);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(title);

        content.add(Box.createVerticalStrut(8));
        JLabel subtitle = new JLabel("Để bảo mật tài khoản, vui lòng không chia sẻ mật khẩu cho người khác.");
        subtitle.setFont(Theme.FONT_BODY);
        subtitle.setForeground(Theme.TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(subtitle);

        content.add(Box.createVerticalStrut(24));

        JLabel oldPassLabel = new JLabel("MẬT KHẨU HIỆN TẠI");
        oldPassLabel.setFont(Theme.FONT_CAPTION_BOLD);
        oldPassLabel.setForeground(Theme.PRIMARY_DARK);
        oldPassLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(oldPassLabel);

        content.add(Box.createVerticalStrut(6));
        StyledPasswordField oldPass = new StyledPasswordField("Nhập mật khẩu đang dùng");
        oldPass.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        oldPass.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(oldPass);

        content.add(Box.createVerticalStrut(16));

        JLabel newPassLabel = new JLabel("MẬT KHẨU MỚI");
        newPassLabel.setFont(Theme.FONT_CAPTION_BOLD);
        newPassLabel.setForeground(Theme.PRIMARY_DARK);
        newPassLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(newPassLabel);

        content.add(Box.createVerticalStrut(6));
        StyledPasswordField newPass = new StyledPasswordField("Tối thiểu 8 ký tự, có số và ký tự hoa");
        newPass.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        newPass.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(newPass);

        content.add(Box.createVerticalStrut(16));

        JLabel confirmPassLabel = new JLabel("XÁC NHẬN MẬT KHẨU MỚI");
        confirmPassLabel.setFont(Theme.FONT_CAPTION_BOLD);
        confirmPassLabel.setForeground(Theme.PRIMARY_DARK);
        confirmPassLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(confirmPassLabel);

        content.add(Box.createVerticalStrut(6));
        StyledPasswordField confirmPass = new StyledPasswordField("Nhập lại mật khẩu mới");
        confirmPass.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        confirmPass.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(confirmPass);

        content.add(Box.createVerticalStrut(28));

        RoundedButton saveBtn = new RoundedButton("LƯU MẬT KHẨU MỚI", Theme.PRIMARY_DARK, Theme.TEXT_WHITE, 14);
        saveBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        saveBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        saveBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công!", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
            NavigationController.getInstance().goBack();
        });
        content.add(saveBtn);

        content.add(Box.createVerticalGlue());
        add(content, BorderLayout.CENTER);
    }
}
