package com.avaroom.ui.screens.landlord;

import com.avaroom.navigation.NavigationController;
import com.avaroom.theme.Theme;
import com.avaroom.ui.components.CardPanel;
import com.avaroom.ui.components.PillTag;
import com.avaroom.ui.components.RoundedButton;
import java.awt.*;
import javax.swing.*;

public class EscrowContractScreen extends JPanel {

    public EscrowContractScreen() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_GRAY_LIGHT);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.BG_WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER_SUBTLE),
                BorderFactory.createEmptyBorder(16, 24, 16, 24)
        ));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        left.setOpaque(false);

        RoundedButton backBtn = new RoundedButton("←", Theme.BG_GRAY_LIGHT, Theme.TEXT_PRIMARY, 8);
        backBtn.setFont(Theme.FONT_BODY_BOLD);
        backBtn.addActionListener(e -> NavigationController.getInstance().goBack());
        left.add(backBtn);

        JLabel title = new JLabel("Quản lý Hợp đồng & Tiền cọc Escrow");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.PRIMARY_DARK);
        left.add(title);
        header.add(left, BorderLayout.WEST);

        PillTag badge = new PillTag("🛡️ Bảo chứng Avaroom Trust", Theme.SECONDARY_CREAM, Theme.PRIMARY_DARK);
        header.add(badge, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Theme.BG_GRAY_LIGHT);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 24, 24, 24));

        // Trust Overview Card
        CardPanel trustCard = new CardPanel(14, Theme.PRIMARY_DARK, false);
        trustCard.setLayout(new BorderLayout(20, 0));
        trustCard.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        trustCard.setMaximumSize(new Dimension(1000, 130));

        JPanel trustLeft = new JPanel();
        trustLeft.setLayout(new BoxLayout(trustLeft, BoxLayout.Y_AXIS));
        trustLeft.setOpaque(false);

        JLabel trustTitle = new JLabel("Tổng quỹ tiền cọc đang lưu ký an toàn (Escrow)");
        trustTitle.setFont(Theme.FONT_CAPTION);
        trustTitle.setForeground(Theme.SECONDARY_CREAM);
        trustLeft.add(trustTitle);

        trustLeft.add(Box.createVerticalStrut(6));
        JLabel trustTotal = new JLabel("14.000.000 đ");
        trustTotal.setFont(new Font("Segoe UI", Font.BOLD, 30));
        trustTotal.setForeground(Theme.TEXT_WHITE);
        trustLeft.add(trustTotal);

        trustLeft.add(Box.createVerticalStrut(4));
        JLabel trustNote = new JLabel("4 hợp đồng đang vận hành • Tiền cọc được ngân hàng liên kết giữ bảo chứng 100%");
        trustNote.setFont(Theme.FONT_CAPTION);
        trustNote.setForeground(new Color(200, 215, 230));
        trustLeft.add(trustNote);
        trustCard.add(trustLeft, BorderLayout.CENTER);

        contentPanel.add(trustCard);
        contentPanel.add(Box.createVerticalStrut(20));

        // Contract list title
        JLabel listTitle = new JLabel("Danh sách hợp đồng điện tử");
        listTitle.setFont(Theme.FONT_BODY_BOLD);
        listTitle.setForeground(Theme.PRIMARY_DARK);
        contentPanel.add(listTitle);
        contentPanel.add(Box.createVerticalStrut(12));

        // Contract 1: Active
        contentPanel.add(createContractCard(
                "HĐ #HD-2026-90412 • Phòng 301 Studio ban công Tạ Quang Bửu",
                "Nguyễn Thanh Tùng", "0912 345 678", "ĐH Bách Khoa",
                "15/10/2026 – 15/10/2027 (12 tháng)",
                "3.500.000 đ",
                "Đang hiệu lực",
                Theme.ACCENT_GREEN,
                true,
                false
        ));
        contentPanel.add(Box.createVerticalStrut(14));

        // Contract 2: Active
        contentPanel.add(createContractCard(
                "HĐ #HD-2026-89210 • Phòng 202 Gác lửng Xuân Thủy",
                "Trần Minh Quang", "0987 654 321", "ĐH Sư Phạm",
                "01/09/2026 – 01/09/2027 (12 tháng)",
                "3.200.000 đ",
                "Đang hiệu lực",
                Theme.ACCENT_GREEN,
                true,
                false
        ));
        contentPanel.add(Box.createVerticalStrut(14));

        // Contract 3: Pending deposit refund
        contentPanel.add(createContractCard(
                "HĐ #HD-2025-77102 • Phòng 102 Khép kín Chùa Láng",
                "Vũ Thùy Trang", "0934 112 233", "ĐH Quốc Gia",
                "10/10/2025 – 10/10/2026 (Đã kết thúc)",
                "3.000.000 đ",
                "Chờ hoàn cọc",
                Theme.ACCENT_GOLD,
                true,
                true
        ));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private CardPanel createContractCard(
            String title, String tenantName, String tenantPhone, String university,
            String duration, String deposit, String status, Color statusColor,
            boolean hasHandoverBtn, boolean hasRefundBtn
    ) {
        CardPanel card = new CardPanel(14, Theme.BG_WHITE, true);
        card.setLayout(new BorderLayout(16, 0));
        card.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
        card.setMaximumSize(new Dimension(1000, 160));

        JPanel details = new JPanel();
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));
        details.setOpaque(false);

        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        topRow.setOpaque(false);

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(Theme.FONT_BODY_BOLD);
        titleLbl.setForeground(Theme.PRIMARY_DARK);
        topRow.add(titleLbl);

        PillTag statusTag = new PillTag(status, Theme.BG_GRAY_LIGHT, statusColor);
        topRow.add(statusTag);
        details.add(topRow);

        details.add(Box.createVerticalStrut(8));
        JLabel tenantLbl = new JLabel("👤 Khách thuê: " + tenantName + " (" + tenantPhone + ") • " + university);
        tenantLbl.setFont(Theme.FONT_BODY);
        tenantLbl.setForeground(Theme.TEXT_PRIMARY);
        details.add(tenantLbl);

        details.add(Box.createVerticalStrut(4));
        JLabel timeLbl = new JLabel("📅 Thời hạn hợp đồng: " + duration);
        timeLbl.setFont(Theme.FONT_CAPTION);
        timeLbl.setForeground(Theme.TEXT_MUTED);
        details.add(timeLbl);

        details.add(Box.createVerticalStrut(4));
        JLabel depLbl = new JLabel("🔒 Tiền cọc Escrow: " + deposit + " (Được phong tỏa an toàn)");
        depLbl.setFont(Theme.FONT_BODY_BOLD);
        depLbl.setForeground(Theme.PRIMARY_DARK);
        details.add(depLbl);

        card.add(details, BorderLayout.CENTER);

        // Actions
        JPanel actions = new JPanel();
        actions.setLayout(new BoxLayout(actions, BoxLayout.Y_AXIS));
        actions.setOpaque(false);

        RoundedButton viewPdfBtn = new RoundedButton("Xem hợp đồng", Theme.BG_GRAY_LIGHT, Theme.TEXT_PRIMARY, 8);
        viewPdfBtn.setFont(Theme.FONT_CAPTION);
        viewPdfBtn.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Hợp đồng thuê phòng điện tử có giá trị pháp lý theo Luật Nhà ở.\nMã xác thực ký số e-Contract: AVAR-2026-VN-88412.\nCác bên cam kết tuân thủ quy chế giải quyết tranh chấp qua Avaroom.",
                "Hợp đồng thuê phòng điện tử", JOptionPane.INFORMATION_MESSAGE));
        actions.add(viewPdfBtn);

        if (hasHandoverBtn) {
            actions.add(Box.createVerticalStrut(8));
            RoundedButton handoverBtn = new RoundedButton("Biên bản bàn giao", Theme.SECONDARY_CREAM, Theme.PRIMARY_DARK, 8);
            handoverBtn.setFont(Theme.FONT_CAPTION);
            handoverBtn.addActionListener(e -> NavigationController.getInstance().navigateTo("asset_handover"));
            actions.add(handoverBtn);
        }

        if (hasRefundBtn) {
            actions.add(Box.createVerticalStrut(8));
            RoundedButton refundBtn = new RoundedButton("Xác nhận hoàn cọc", Theme.ACCENT_GREEN, Theme.TEXT_WHITE, 8);
            refundBtn.setFont(Theme.FONT_CAPTION);
            refundBtn.addActionListener(e -> {
                int res = JOptionPane.showConfirmDialog(this,
                        "Xác nhận phòng đã bàn giao đầy đủ hiện trạng nguyên vẹn?\nAvaroom sẽ hoàn trả " + deposit + " về tài khoản ngân hàng của " + tenantName + ".",
                        "Xác nhận hoàn trả ký quỹ Escrow", JOptionPane.YES_NO_OPTION);
                if (res == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(this, "Hoàn cọc thành công! Tiền cọc đã được chuyển trả cho sinh viên.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                }
            });
            actions.add(refundBtn);
        }

        card.add(actions, BorderLayout.EAST);
        return card;
    }
}
