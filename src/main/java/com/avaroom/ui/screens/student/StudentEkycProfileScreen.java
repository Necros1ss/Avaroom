package com.avaroom.ui.screens.student;

import com.avaroom.navigation.NavigationController;
import com.avaroom.services.DataService;
import com.avaroom.theme.Theme;
import com.avaroom.ui.components.CardPanel;
import com.avaroom.ui.components.PillTag;
import com.avaroom.ui.components.RoundedButton;
import java.awt.*;
import javax.swing.*;

public class StudentEkycProfileScreen extends JPanel {
    public StudentEkycProfileScreen() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_GRAY_LIGHT);

        JPanel scrollContent = new JPanel();
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.setBackground(Theme.BG_GRAY_LIGHT);
        scrollContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Hồ sơ sinh viên & eKYC (Cọc 0đ)");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.PRIMARY_DARK);
        scrollContent.add(title);

        scrollContent.add(Box.createVerticalStrut(6));
        JLabel subtitle = new JLabel("Xác thực định danh và thẻ sinh viên để nhận gói bảo lãnh thuê trọ không cần đặt cọc");
        subtitle.setFont(Theme.FONT_BODY);
        subtitle.setForeground(Theme.TEXT_SECONDARY);
        scrollContent.add(subtitle);

        scrollContent.add(Box.createVerticalStrut(20));

        // Status Card
        CardPanel statusCard = new CardPanel(18, Theme.BG_WHITE, true);
        statusCard.setLayout(new BorderLayout());
        statusCard.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        statusCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        JLabel statusTitle = new JLabel("Hồ sơ đã được thẩm định bảo lãnh");
        statusTitle.setFont(Theme.FONT_SUBTITLE);
        statusTitle.setForeground(Theme.ACCENT_GREEN);
        left.add(statusTitle);

        left.add(Box.createVerticalStrut(4));
        JLabel statusDesc = new JLabel("Thẻ sinh viên HUST K66 & CCCD khớp 100% dữ liệu bộ GD-ĐT");
        statusDesc.setFont(Theme.FONT_CAPTION);
        statusDesc.setForeground(Theme.TEXT_SECONDARY);
        left.add(statusDesc);

        left.add(Box.createVerticalStrut(8));
        JPanel badges = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        badges.setOpaque(false);
        badges.add(new PillTag("🛡️ Hạn mức bảo lãnh: 10.000.000đ", Theme.ACCENT_GREEN_LIGHT, Theme.ACCENT_GREEN, null));
        badges.add(new PillTag("Thời hạn: 2026 - 2027"));
        left.add(badges);

        statusCard.add(left, BorderLayout.CENTER);
        scrollContent.add(statusCard);

        scrollContent.add(Box.createVerticalStrut(24));

        // Details
        CardPanel detailsCard = new CardPanel(18, Theme.BG_WHITE, true);
        detailsCard.setLayout(new BoxLayout(detailsCard, BoxLayout.Y_AXIS));
        detailsCard.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        detailsCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        addDetailRow(detailsCard, "Sinh viên:", DataService.getInstance().getCurrentUserName());
        addDetailRow(detailsCard, "Mã số sinh viên (MSSV):", "20225431");
        addDetailRow(detailsCard, "Trường Đại học:", DataService.getInstance().getCurrentUniversity());
        addDetailRow(detailsCard, "Số CCCD gắn chip:", "001203004567");
        addDetailRow(detailsCard, "Tình trạng Escrow cọc:", "Đã kích hoạt bảo đảm Avaroom Safe");

        scrollContent.add(detailsCard);
        scrollContent.add(Box.createVerticalStrut(24));

        RoundedButton reVerifyBtn = new RoundedButton("Cập nhật lại ảnh thẻ sinh viên / CCCD", Theme.BG_WHITE, Theme.PRIMARY_DARK, 14);
        reVerifyBtn.setOutlineOnly(true);
        reVerifyBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        reVerifyBtn.addActionListener(e -> NavigationController.getInstance().navigateTo("EKYC_CARD"));
        scrollContent.add(reVerifyBtn);

        JScrollPane scrollPane = new JScrollPane(scrollContent);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void addDetailRow(JPanel p, String l, String v) {
        JPanel r = new JPanel(new BorderLayout());
        r.setOpaque(false);
        r.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));

        JLabel label = new JLabel(l);
        label.setFont(Theme.FONT_CAPTION_BOLD);
        label.setForeground(Theme.TEXT_SECONDARY);

        JLabel val = new JLabel("<html><b>" + v + "</b></html>");
        val.setFont(Theme.FONT_BODY);
        val.setForeground(Theme.PRIMARY_DARK);

        r.add(label, BorderLayout.WEST);
        r.add(val, BorderLayout.EAST);
        p.add(r);
    }
}
