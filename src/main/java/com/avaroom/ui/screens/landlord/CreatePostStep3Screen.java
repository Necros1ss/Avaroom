package com.avaroom.ui.screens.landlord;

import com.avaroom.navigation.NavigationController;
import com.avaroom.theme.Theme;
import com.avaroom.ui.components.CardPanel;
import com.avaroom.ui.components.PillTag;
import com.avaroom.ui.components.RoundedButton;
import java.awt.*;
import javax.swing.*;

public class CreatePostStep3Screen extends JPanel {
    public CreatePostStep3Screen() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_GRAY_LIGHT);

        JPanel scrollContent = new JPanel();
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.setBackground(Theme.BG_GRAY_LIGHT);
        scrollContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel stepLabel = new JLabel("BƯỚC 3 / 4 • Bước kế: Hoàn tất & Xuất bản");
        stepLabel.setFont(Theme.FONT_CAPTION_BOLD);
        stepLabel.setForeground(Theme.ACCENT_RED);
        scrollContent.add(stepLabel);

        scrollContent.add(Box.createVerticalStrut(4));
        JLabel title = new JLabel("Ảnh 360° & Xác thực Timestamp");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.PRIMARY_DARK);
        scrollContent.add(title);

        scrollContent.add(Box.createVerticalStrut(8));
        JLabel subtitle = new JLabel("Ảnh chụp thực tế gắn tem thời gian thực để cam kết phòng đúng hiện trạng.");
        subtitle.setFont(Theme.FONT_BODY);
        subtitle.setForeground(Theme.TEXT_SECONDARY);
        scrollContent.add(subtitle);

        scrollContent.add(Box.createVerticalStrut(20));

        // Upload Preview Slots
        JPanel uploadGrid = new JPanel(new GridLayout(2, 2, 12, 12));
        uploadGrid.setOpaque(false);
        uploadGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));

        uploadGrid.add(createPhotoSlot("📷 Phòng ngủ & Ban công", "✓ Đã tải lên (Ảnh 1/4)"));
        uploadGrid.add(createPhotoSlot("📷 Góc bếp & Tủ lạnh", "✓ Đã tải lên (Ảnh 2/4)"));
        uploadGrid.add(createPhotoSlot("📷 Nhà vệ sinh & Máy giặt", "✓ Đã tải lên (Ảnh 3/4)"));
        uploadGrid.add(createPhotoSlot("📷 Thang thoát hiểm PCCC", "✓ Đã tải lên (Ảnh 4/4)"));

        scrollContent.add(uploadGrid);
        scrollContent.add(Box.createVerticalStrut(24));

        // Certification Badge Card
        CardPanel certCard = new CardPanel(16, Theme.BG_WHITE, true);
        certCard.setLayout(new BorderLayout(14, 0));
        certCard.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        certCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel icon = new JLabel("🛡️");
        icon.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        certCard.add(icon, BorderLayout.WEST);

        JPanel certText = new JPanel();
        certText.setLayout(new BoxLayout(certText, BoxLayout.Y_AXIS));
        certText.setOpaque(false);

        JLabel certTitle = new JLabel("Chứng nhận Avaroom Verified 100%");
        certTitle.setFont(Theme.FONT_BODY_BOLD);
        certTitle.setForeground(Theme.ACCENT_GREEN);
        certText.add(certTitle);

        JLabel certDesc = new JLabel("Tin đăng có gắn tem Timestamp sẽ được ưu tiên hiển thị Top 1 trên bản đồ sinh viên.");
        certDesc.setFont(Theme.FONT_CAPTION);
        certDesc.setForeground(Theme.TEXT_SECONDARY);
        certText.add(certDesc);

        certCard.add(certText, BorderLayout.CENTER);
        scrollContent.add(certCard);

        JScrollPane scrollPane = new JScrollPane(scrollContent);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom CTA
        JPanel bottomBar = new JPanel(new BorderLayout());
        bottomBar.setBackground(Theme.BG_WHITE);
        bottomBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDER_SUBTLE),
                BorderFactory.createEmptyBorder(12, 20, 14, 20)
        ));

        RoundedButton nextBtn = new RoundedButton("Tiếp tục (Bước 4) →", Theme.PRIMARY_DARK, Theme.TEXT_WHITE, 12);
        nextBtn.setPreferredSize(new Dimension(170, 44));
        nextBtn.addActionListener(e -> NavigationController.getInstance().navigateTo("CREATE_POST_STEP4"));
        bottomBar.add(nextBtn, BorderLayout.EAST);

        RoundedButton backBtn = new RoundedButton("← Bước 2", Theme.BG_WHITE, Theme.PRIMARY_DARK, 12);
        backBtn.setOutlineOnly(true);
        backBtn.setPreferredSize(new Dimension(110, 44));
        backBtn.addActionListener(e -> NavigationController.getInstance().goBack());
        bottomBar.add(backBtn, BorderLayout.WEST);

        add(bottomBar, BorderLayout.SOUTH);
    }

    private JPanel createPhotoSlot(String title, String status) {
        CardPanel p = new CardPanel(14, Theme.BG_WHITE, true);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(20, 14, 20, 14));

        JLabel t = new JLabel(title);
        t.setFont(Theme.FONT_BODY_BOLD);
        t.setForeground(Theme.PRIMARY_DARK);
        t.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(t);

        p.add(Box.createVerticalStrut(6));
        JLabel s = new JLabel(status);
        s.setFont(Theme.FONT_CAPTION_BOLD);
        s.setForeground(Theme.ACCENT_GREEN);
        s.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(s);

        return p;
    }
}
