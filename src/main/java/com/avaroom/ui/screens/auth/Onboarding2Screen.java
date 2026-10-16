package com.avaroom.ui.screens.auth;

import com.avaroom.navigation.NavigationController;
import com.avaroom.theme.Theme;
import com.avaroom.ui.components.CardPanel;
import com.avaroom.ui.components.PillTag;
import com.avaroom.ui.components.RoundedButton;
import java.awt.*;
import javax.swing.*;

public class Onboarding2Screen extends JPanel {
    public Onboarding2Screen() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_WARM_CREAM);
        setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        contentPanel.add(Box.createVerticalStrut(20));

        // Pricing Card
        CardPanel previewCard = new CardPanel(20, Theme.BG_WHITE, true);
        previewCard.setLayout(new BoxLayout(previewCard, BoxLayout.Y_AXIS));
        previewCard.setBorder(BorderFactory.createEmptyBorder(24, 20, 24, 20));
        previewCard.setMaximumSize(new Dimension(500, 260));

        JLabel tagLabel = new JLabel("GIÁ THUÊ NIÊM YẾT");
        tagLabel.setFont(Theme.FONT_CAPTION_BOLD);
        tagLabel.setForeground(Theme.TEXT_SECONDARY);
        tagLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        previewCard.add(tagLabel);

        previewCard.add(Box.createVerticalStrut(10));
        JLabel priceLabel = new JLabel("3.200.000 đ/tháng");
        priceLabel.setFont(Theme.FONT_TITLE_LARGE);
        priceLabel.setForeground(Theme.PRIMARY_DARK);
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        previewCard.add(priceLabel);

        previewCard.add(Box.createVerticalStrut(12));
        JLabel billsLabel = new JLabel("⚡ Điện: 3.5k/kWh  •  💧 Nước: 50k/người");
        billsLabel.setFont(Theme.FONT_BODY);
        billsLabel.setForeground(Theme.TEXT_SECONDARY);
        billsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        previewCard.add(billsLabel);

        previewCard.add(Box.createVerticalStrut(20));
        JPanel tagsRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        tagsRow.setOpaque(false);
        tagsRow.add(new PillTag("📷 100% Ảnh chụp thực tế"));
        tagsRow.add(new PillTag("✨ Không phí ẩn"));
        tagsRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        previewCard.add(tagsRow);

        contentPanel.add(previewCard);
        contentPanel.add(Box.createVerticalStrut(32));

        // Title
        JLabel titleLabel = new JLabel("Phòng chuẩn giá tốt, không lo bị \"hớ\"");
        titleLabel.setFont(Theme.FONT_TITLE_LARGE);
        titleLabel.setForeground(Theme.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(titleLabel);

        contentPanel.add(Box.createVerticalStrut(12));

        // Subtitle
        JLabel descLabel = new JLabel("<html><center>Hình ảnh thực tế được đội ngũ Avaroom thẩm định tại chỗ kèm minh bạch chi tiết tiền cọc, điện, nước và dịch vụ đi kèm.</center></html>");
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
        nextBtn.addActionListener(e -> NavigationController.getInstance().navigateTo("ONBOARDING_3"));
        add(nextBtn, BorderLayout.SOUTH);
    }
}
