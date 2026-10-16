package com.avaroom.ui.screens.landlord;

import com.avaroom.navigation.NavigationController;
import com.avaroom.services.DataService;
import com.avaroom.theme.Theme;
import com.avaroom.ui.components.CardPanel;
import com.avaroom.ui.components.PillTag;
import com.avaroom.ui.components.RoundedButton;
import java.awt.*;
import javax.swing.*;

public class LandlordProfileScreen extends JPanel {

    public LandlordProfileScreen() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_GRAY_LIGHT);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.BG_WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER_SUBTLE),
                BorderFactory.createEmptyBorder(16, 24, 16, 24)
        ));

        JLabel title = new JLabel("Hồ sơ & Quản trị Chủ trọ");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.PRIMARY_DARK);
        header.add(title, BorderLayout.WEST);

        RoundedButton editBtn = new RoundedButton("Chỉnh sửa", Theme.BG_GRAY_LIGHT, Theme.TEXT_PRIMARY, 8);
        editBtn.addActionListener(e -> NavigationController.getInstance().navigateTo("edit_landlord_profile"));
        header.add(editBtn, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Scrollable content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Theme.BG_GRAY_LIGHT);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 24, 24, 24));

        // Profile Identity Card
        CardPanel profileCard = new CardPanel(14, Theme.BG_WHITE, true);
        profileCard.setLayout(new BorderLayout(16, 0));
        profileCard.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
        profileCard.setMaximumSize(new Dimension(800, 110));

        JLabel avatar = new JLabel("👵");
        avatar.setFont(new Font("Segoe UI", Font.PLAIN, 40));
        profileCard.add(avatar, BorderLayout.WEST);

        JPanel infoCol = new JPanel();
        infoCol.setLayout(new BoxLayout(infoCol, BoxLayout.Y_AXIS));
        infoCol.setOpaque(false);

        JPanel nameRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        nameRow.setOpaque(false);
        JLabel nameLbl = new JLabel(DataService.getInstance().getLandlordName());
        nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        nameLbl.setForeground(Theme.PRIMARY_DARK);
        nameRow.add(nameLbl);

        PillTag verified = new PillTag("✓ Chủ trọ Xác thực eKYC", Theme.BG_GRAY_LIGHT, Theme.ACCENT_GREEN);
        nameRow.add(verified);
        infoCol.add(nameRow);

        infoCol.add(Box.createVerticalStrut(4));
        JLabel phoneLbl = new JLabel("📞 " + DataService.getInstance().getLandlordPhone() + "  •  ⭐ 4.9 (18 Đánh giá uy tín)");
        phoneLbl.setFont(Theme.FONT_CAPTION);
        phoneLbl.setForeground(Theme.TEXT_MUTED);
        infoCol.add(phoneLbl);

        infoCol.add(Box.createVerticalStrut(4));
        JLabel addrLbl = new JLabel("📍 Cụm nhà trọ Bách - Kinh - Xây (12 phòng đang khai thác)");
        addrLbl.setFont(Theme.FONT_CAPTION);
        addrLbl.setForeground(Theme.TEXT_PRIMARY);
        infoCol.add(addrLbl);

        profileCard.add(infoCol, BorderLayout.CENTER);
        contentPanel.add(profileCard);
        contentPanel.add(Box.createVerticalStrut(18));

        // Management Tools Card
        CardPanel toolsCard = new CardPanel(14, Theme.BG_WHITE, true);
        toolsCard.setLayout(new BoxLayout(toolsCard, BoxLayout.Y_AXIS));
        toolsCard.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        toolsCard.setMaximumSize(new Dimension(800, 310));

        JLabel toolsTitle = new JLabel("Công cụ quản lý & Tiện ích");
        toolsTitle.setFont(Theme.FONT_BODY_BOLD);
        toolsTitle.setForeground(Theme.PRIMARY_DARK);
        toolsCard.add(toolsTitle);
        toolsCard.add(Box.createVerticalStrut(12));

        toolsCard.add(createToolRow("📊 Thống kê & Báo cáo Tăng trưởng", "Theo dõi doanh thu, tỉ lệ lấp đầy", () -> NavigationController.getInstance().navigateTo("stats_growth")));
        toolsCard.add(Box.createVerticalStrut(8));
        toolsCard.add(createToolRow("⭐ Quản lý & Phản hồi Đánh giá", "Tương tác với nhận xét của sinh viên", () -> NavigationController.getInstance().navigateTo("landlord_reviews")));
        toolsCard.add(Box.createVerticalStrut(8));
        toolsCard.add(createToolRow("🛡️ Quản lý Hợp đồng & Ký quỹ Escrow", "Bảo chứng tiền cọc 3 bên minh bạch", () -> NavigationController.getInstance().navigateTo("escrow_contract")));
        toolsCard.add(Box.createVerticalStrut(8));
        toolsCard.add(createToolRow("⚡ Chốt điện nước & Lập hóa đơn", "Tự động tính tiền và phát sinh mã VietQR", () -> NavigationController.getInstance().navigateTo("utility_billing")));
        toolsCard.add(Box.createVerticalStrut(8));
        toolsCard.add(createToolRow("📋 Biên bản bàn giao tài sản 360°", "Kiểm kê hiện trạng tài sản kèm ảnh timestamp", () -> NavigationController.getInstance().navigateTo("asset_handover")));

        contentPanel.add(toolsCard);
        contentPanel.add(Box.createVerticalStrut(18));

        // Payout Account Card
        CardPanel payoutCard = new CardPanel(14, Theme.BG_WHITE, true);
        payoutCard.setLayout(new BorderLayout());
        payoutCard.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        payoutCard.setMaximumSize(new Dimension(800, 80));

        JPanel pLeft = new JPanel();
        pLeft.setLayout(new BoxLayout(pLeft, BoxLayout.Y_AXIS));
        pLeft.setOpaque(false);

        JLabel pTitle = new JLabel("Tài khoản thụ hưởng nhận tiền trọ (VietQR)");
        pTitle.setFont(Theme.FONT_BODY_BOLD);
        pTitle.setForeground(Theme.PRIMARY_DARK);
        pLeft.add(pTitle);

        pLeft.add(Box.createVerticalStrut(4));
        JLabel pDetails = new JLabel(DataService.getInstance().getLandlordBank());
        pDetails.setFont(Theme.FONT_CAPTION);
        pDetails.setForeground(Theme.TEXT_MUTED);
        pLeft.add(pDetails);

        payoutCard.add(pLeft, BorderLayout.WEST);

        PillTag pActive = new PillTag("Đang liên kết", Theme.SECONDARY_CREAM, Theme.PRIMARY_DARK);
        payoutCard.add(pActive, BorderLayout.EAST);
        contentPanel.add(payoutCard);
        contentPanel.add(Box.createVerticalStrut(20));

        // Role Switch & Logout Buttons
        JPanel actionsRow = new JPanel(new GridLayout(1, 2, 16, 0));
        actionsRow.setOpaque(false);
        actionsRow.setMaximumSize(new Dimension(800, 46));

        RoundedButton switchBtn = new RoundedButton("🔄 Đổi sang giao diện Sinh viên", Theme.PRIMARY_DARK, Theme.TEXT_WHITE, 10);
        switchBtn.addActionListener(e -> {
            DataService.getInstance().setCurrentUserRole("student");
            NavigationController.getInstance().navigateTo("student_home");
        });
        actionsRow.add(switchBtn);

        RoundedButton logoutBtn = new RoundedButton("🚪 Đăng xuất", Theme.BG_WHITE, new Color(220, 50, 50), 10);
        logoutBtn.addActionListener(e -> {
            int c = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đăng xuất?", "Đăng xuất", JOptionPane.YES_NO_OPTION);
            if (c == JOptionPane.YES_OPTION) {
                NavigationController.getInstance().navigateTo("login");
            }
        });
        actionsRow.add(logoutBtn);

        contentPanel.add(actionsRow);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createToolRow(String title, String desc, Runnable action) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setCursor(new Cursor(Cursor.HAND_CURSOR));
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER_SUBTLE),
                BorderFactory.createEmptyBorder(6, 4, 8, 4)
        ));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        JLabel tLbl = new JLabel(title);
        tLbl.setFont(Theme.FONT_BODY_BOLD);
        tLbl.setForeground(Theme.TEXT_PRIMARY);
        left.add(tLbl);

        JLabel dLbl = new JLabel(desc);
        dLbl.setFont(Theme.FONT_CAPTION);
        dLbl.setForeground(Theme.TEXT_MUTED);
        left.add(dLbl);
        row.add(left, BorderLayout.WEST);

        JLabel arrow = new JLabel("→");
        arrow.setFont(Theme.FONT_BODY_BOLD);
        arrow.setForeground(Theme.TEXT_MUTED);
        row.add(arrow, BorderLayout.EAST);

        row.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                action.run();
            }
        });

        return row;
    }
}
