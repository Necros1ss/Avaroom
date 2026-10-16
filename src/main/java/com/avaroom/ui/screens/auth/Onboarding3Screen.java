package com.avaroom.ui.screens.auth;

import com.avaroom.navigation.NavigationController;
import com.avaroom.theme.Theme;
import com.avaroom.ui.components.CardPanel;
import com.avaroom.ui.components.RoundedButton;
import java.awt.*;
import javax.swing.*;

public class Onboarding3Screen extends JPanel {
    public Onboarding3Screen() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_WARM_CREAM);
        setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        contentPanel.add(Box.createVerticalStrut(20));

        // Community trust Card
        CardPanel previewCard = new CardPanel(20, Theme.BG_WHITE, true);
        previewCard.setLayout(new BoxLayout(previewCard, BoxLayout.Y_AXIS));
        previewCard.setBorder(BorderFactory.createEmptyBorder(28, 20, 28, 20));
        previewCard.setMaximumSize(new Dimension(500, 240));

        JLabel starLabel = new JLabel("⭐ 4.9/5 · 24k+ sinh viên tin dùng");
        starLabel.setFont(Theme.FONT_SUBTITLE);
        starLabel.setForeground(Theme.ACCENT_YELLOW);
        starLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        previewCard.add(starLabel);

        previewCard.add(Box.createVerticalStrut(12));
        JLabel titleTrust = new JLabel("100% Phòng được xác thực");
        titleTrust.setFont(Theme.FONT_TITLE);
        titleTrust.setForeground(Theme.PRIMARY_DARK);
        titleTrust.setAlignmentX(Component.CENTER_ALIGNMENT);
        previewCard.add(titleTrust);

        previewCard.add(Box.createVerticalStrut(8));
        JLabel noteTrust = new JLabel("Hợp đồng minh bạch · Không phí môi giới ẩn");
        noteTrust.setFont(Theme.FONT_BODY);
        noteTrust.setForeground(Theme.TEXT_SECONDARY);
        noteTrust.setAlignmentX(Component.CENTER_ALIGNMENT);
        previewCard.add(noteTrust);

        contentPanel.add(previewCard);
        contentPanel.add(Box.createVerticalStrut(32));

        // Title
        JLabel titleLabel = new JLabel("Sẵn sàng tìm căn phòng mơ ước?");
        titleLabel.setFont(Theme.FONT_TITLE_LARGE);
        titleLabel.setForeground(Theme.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(titleLabel);

        contentPanel.add(Box.createVerticalStrut(12));

        // Subtitle
        JLabel descLabel = new JLabel("<html><center>Tham gia cùng cộng đồng sinh viên văn minh. Tìm kiếm phòng trọ, căn hộ mini và bạn ở ghép an toàn, tiện nghi ngay hôm nay.</center></html>");
        descLabel.setFont(Theme.FONT_BODY);
        descLabel.setForeground(Theme.TEXT_SECONDARY);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        descLabel.setMaximumSize(new Dimension(460, 60));
        contentPanel.add(descLabel);

        contentPanel.add(Box.createVerticalGlue());

        add(contentPanel, BorderLayout.CENTER);

        // Bottom CTA Buttons (Primary: Register, Secondary: Login)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);

        RoundedButton regBtn = new RoundedButton("Đăng ký tài khoản mới", Theme.PRIMARY_DARK, Theme.TEXT_WHITE, 16);
        regBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        regBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        regBtn.addActionListener(e -> NavigationController.getInstance().navigateTo("REGISTER"));
        buttonPanel.add(regBtn);

        buttonPanel.add(Box.createVerticalStrut(12));

        RoundedButton loginBtn = new RoundedButton("Tôi đã có tài khoản • Đăng nhập", Theme.BG_WHITE, Theme.TEXT_PRIMARY, 16);
        loginBtn.setOutlineOnly(true);
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.addActionListener(e -> NavigationController.getInstance().navigateTo("LOGIN"));
        buttonPanel.add(loginBtn);

        add(buttonPanel, BorderLayout.SOUTH);
    }
}
