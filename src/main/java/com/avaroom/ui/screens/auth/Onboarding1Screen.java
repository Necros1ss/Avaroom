package com.avaroom.ui.screens.auth;

import com.avaroom.navigation.NavigationController;
import com.avaroom.theme.Theme;
import com.avaroom.ui.components.CardPanel;
import com.avaroom.ui.components.PillTag;
import com.avaroom.ui.components.RoundedButton;
import java.awt.*;
import javax.swing.*;

public class Onboarding1Screen extends JPanel {
    public Onboarding1Screen() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_WARM_CREAM);
        setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        contentPanel.add(Box.createVerticalStrut(20));

        // Preview Card
        CardPanel previewCard = new CardPanel(20, Theme.BG_WHITE, true);
        previewCard.setLayout(new BoxLayout(previewCard, BoxLayout.Y_AXIS));
        previewCard.setBorder(BorderFactory.createEmptyBorder(24, 20, 24, 20));
        previewCard.setMaximumSize(new Dimension(500, 260));

        JLabel locLabel = new JLabel("📍 ĐH Bách Khoa · 850m");
        locLabel.setFont(Theme.FONT_SUBTITLE);
        locLabel.setForeground(Theme.PRIMARY_DARK);
        locLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        previewCard.add(locLabel);

        previewCard.add(Box.createVerticalStrut(10));
        JLabel busLabel = new JLabel("Tuyến xe bus 08A, 26, 31 đi thẳng");
        busLabel.setFont(Theme.FONT_BODY);
        busLabel.setForeground(Theme.TEXT_SECONDARY);
        busLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        previewCard.add(busLabel);

        previewCard.add(Box.createVerticalStrut(16));
        JLabel priceLabel = new JLabel("Từ 2.8tr / tháng");
        priceLabel.setFont(Theme.FONT_TITLE_LARGE);
        priceLabel.setForeground(Theme.ACCENT_RED);
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        previewCard.add(priceLabel);

        previewCard.add(Box.createVerticalStrut(20));
        JPanel tagsRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        tagsRow.setOpaque(false);
        tagsRow.add(new PillTag("⏱️ < 10 phút đi bộ"));
        tagsRow.add(new PillTag("🛡️ Phòng chính chủ"));
        tagsRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        previewCard.add(tagsRow);

        contentPanel.add(previewCard);
        contentPanel.add(Box.createVerticalStrut(32));

        // Title
        JLabel titleLabel = new JLabel("Tìm trọ gần trường chỉ trong tích tắc.");
        titleLabel.setFont(Theme.FONT_TITLE_LARGE);
        titleLabel.setForeground(Theme.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(titleLabel);

        contentPanel.add(Box.createVerticalStrut(12));

        // Subtitle
        JLabel descLabel = new JLabel("<html><center>Định vị bản đồ quanh trường, lọc nhanh theo bán kính và tuyến xe buýt thuận tiện, bảo đảm minh bạch giá cả & an ninh sinh viên.</center></html>");
        descLabel.setFont(Theme.FONT_BODY);
        descLabel.setForeground(Theme.TEXT_SECONDARY);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        descLabel.setMaximumSize(new Dimension(460, 60));
        contentPanel.add(descLabel);

        contentPanel.add(Box.createVerticalGlue());

        add(contentPanel, BorderLayout.CENTER);

        // Bottom CTA
        RoundedButton nextBtn = new RoundedButton("Tiếp tục", Theme.PRIMARY_DARK, Theme.TEXT_WHITE, 16);
        nextBtn.setPreferredSize(new Dimension(0, 52));
        nextBtn.addActionListener(e -> NavigationController.getInstance().navigateTo("ONBOARDING_2"));
        add(nextBtn, BorderLayout.SOUTH);
    }
}
