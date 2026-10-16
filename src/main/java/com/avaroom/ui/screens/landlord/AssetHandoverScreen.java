package com.avaroom.ui.screens.landlord;

import com.avaroom.navigation.NavigationController;
import com.avaroom.theme.Theme;
import com.avaroom.ui.components.CardPanel;
import com.avaroom.ui.components.PillTag;
import com.avaroom.ui.components.RoundedButton;
import java.awt.*;
import javax.swing.*;

public class AssetHandoverScreen extends JPanel {

    public AssetHandoverScreen() {
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

        JLabel title = new JLabel("Biên bản bàn giao tài sản 360°");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.PRIMARY_DARK);
        left.add(title);
        header.add(left, BorderLayout.WEST);

        PillTag roomTag = new PillTag("Phòng #AVR-90412", Theme.SECONDARY_CREAM, Theme.PRIMARY_DARK);
        header.add(roomTag, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Theme.BG_GRAY_LIGHT);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 24, 24, 24));

        // Note Card
        CardPanel noteCard = new CardPanel(14, Theme.PRIMARY_DARK, false);
        noteCard.setLayout(new BorderLayout());
        noteCard.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        noteCard.setMaximumSize(new Dimension(800, 80));

        JLabel noteText = new JLabel("<html><body style='width: 720px; color: white;'><b>🛡️ Cam kết bảo vệ quyền lợi tiền cọc:</b> Mọi hiện trạng và tài sản được ghi nhận có dấu thời gian (Timestamp) để làm căn cứ đối chiếu khi thanh lý hợp đồng và hoàn cọc 100%.</body></html>");
        noteText.setFont(Theme.FONT_BODY);
        noteCard.add(noteText, BorderLayout.CENTER);
        contentPanel.add(noteCard);
        contentPanel.add(Box.createVerticalStrut(18));

        // Asset Checklist Card
        CardPanel checklistCard = new CardPanel(14, Theme.BG_WHITE, true);
        checklistCard.setLayout(new BoxLayout(checklistCard, BoxLayout.Y_AXIS));
        checklistCard.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
        checklistCard.setMaximumSize(new Dimension(800, 420));

        JLabel checkTitle = new JLabel("Danh mục tài sản & trang thiết bị bàn giao");
        checkTitle.setFont(Theme.FONT_BODY_BOLD);
        checkTitle.setForeground(Theme.PRIMARY_DARK);
        checklistCard.add(checkTitle);
        checklistCard.add(Box.createVerticalStrut(12));

        checklistCard.add(createAssetRow("🔑 Khóa cửa & Thẻ từ", "Khóa thông minh vân tay 2 lớp + 2 thẻ từ", "Tốt 100%"));
        checklistCard.add(Box.createVerticalStrut(8));
        checklistCard.add(createAssetRow("❄️ Điều hòa Daikin 12000BTU", "Máy làm lạnh nhanh, remote zin đầy đủ pin", "Mới 99%"));
        checklistCard.add(Box.createVerticalStrut(8));
        checklistCard.add(createAssetRow("🧊 Tủ lạnh Aqua 130L", "Ngăn đá đông tốt, khay kệ nguyên vẹn", "Tốt"));
        checklistCard.add(Box.createVerticalStrut(8));
        checklistCard.add(createAssetRow("🚿 Bình nóng lạnh & Vòi sen", "Ariston 20L chống giật ELCB, hoạt động êm", "Tốt"));
        checklistCard.add(Box.createVerticalStrut(8));
        checklistCard.add(createAssetRow("🪟 Cửa sổ & Rèm ban công", "Khóa chốt an toàn, rèm vải cản sáng sạch sẽ", "Tốt"));
        checklistCard.add(Box.createVerticalStrut(8));
        checklistCard.add(createAssetRow("🧱 Sơn tường & Sàn gạch", "Tường sơn trắng không trầy xước, sàn nguyên vẹn", "Hoàn hảo"));

        contentPanel.add(checklistCard);
        contentPanel.add(Box.createVerticalStrut(18));

        // Signatures Card
        CardPanel signCard = new CardPanel(14, Theme.BG_WHITE, true);
        signCard.setLayout(new GridLayout(1, 2, 20, 0));
        signCard.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
        signCard.setMaximumSize(new Dimension(800, 140));

        // Landlord Sign
        JPanel hostSign = new JPanel();
        hostSign.setLayout(new BoxLayout(hostSign, BoxLayout.Y_AXIS));
        hostSign.setOpaque(false);

        JLabel hTitle = new JLabel("BÊN BÀN GIAO (CHỦ TRỌ)");
        hTitle.setFont(Theme.FONT_CAPTION);
        hTitle.setForeground(Theme.TEXT_MUTED);
        hostSign.add(hTitle);
        hostSign.add(Box.createVerticalStrut(6));

        JLabel hName = new JLabel("Cô Hoàng Thị Ánh Tuyết");
        hName.setFont(Theme.FONT_BODY_BOLD);
        hName.setForeground(Theme.PRIMARY_DARK);
        hostSign.add(hName);
        hostSign.add(Box.createVerticalStrut(4));

        PillTag hStatus = new PillTag("✓ Đã ký số OTP lúc 10:15 15/10/2026", Theme.BG_GRAY_LIGHT, Theme.ACCENT_GREEN);
        hostSign.add(hStatus);
        signCard.add(hostSign);

        // Student Sign
        JPanel stuSign = new JPanel();
        stuSign.setLayout(new BoxLayout(stuSign, BoxLayout.Y_AXIS));
        stuSign.setOpaque(false);

        JLabel sTitle = new JLabel("BÊN NHẬN BÀN GIAO (SINH VIÊN)");
        sTitle.setFont(Theme.FONT_CAPTION);
        sTitle.setForeground(Theme.TEXT_MUTED);
        stuSign.add(sTitle);
        stuSign.add(Box.createVerticalStrut(6));

        JLabel sName = new JLabel("Nguyễn Thanh Tùng");
        sName.setFont(Theme.FONT_BODY_BOLD);
        sName.setForeground(Theme.PRIMARY_DARK);
        stuSign.add(sName);
        stuSign.add(Box.createVerticalStrut(4));

        PillTag sStatus = new PillTag("✓ Đã xác nhận biên bản qua App", Theme.BG_GRAY_LIGHT, Theme.ACCENT_GREEN);
        stuSign.add(sStatus);
        signCard.add(stuSign);

        contentPanel.add(signCard);
        contentPanel.add(Box.createVerticalStrut(20));

        // Action submit button
        RoundedButton saveBtn = new RoundedButton("Xuất Biên bản bàn giao PDF", Theme.PRIMARY_DARK, Theme.TEXT_WHITE, 12);
        saveBtn.setMaximumSize(new Dimension(800, 48));
        saveBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Biên bản bàn giao hiện trạng đã được mã hóa và lưu trữ an toàn!\nMã chứng thực Avaroom: BB-2026-AVR90412.",
                    "Lưu biên bản thành công", JOptionPane.INFORMATION_MESSAGE);
            NavigationController.getInstance().goBack();
        });
        contentPanel.add(saveBtn);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createAssetRow(String name, String desc, String condition) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER_SUBTLE),
                BorderFactory.createEmptyBorder(6, 4, 8, 4)
        ));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        JLabel nLbl = new JLabel(name);
        nLbl.setFont(Theme.FONT_BODY_BOLD);
        nLbl.setForeground(Theme.TEXT_PRIMARY);
        left.add(nLbl);

        JLabel dLbl = new JLabel(desc);
        dLbl.setFont(Theme.FONT_CAPTION);
        dLbl.setForeground(Theme.TEXT_MUTED);
        left.add(dLbl);
        row.add(left, BorderLayout.WEST);

        PillTag status = new PillTag(condition, Theme.SECONDARY_CREAM, Theme.PRIMARY_DARK);
        row.add(status, BorderLayout.EAST);
        return row;
    }
}
