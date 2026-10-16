package com.avaroom.ui.dialogs;

import com.avaroom.navigation.NavigationController;
import com.avaroom.theme.Theme;
import com.avaroom.ui.components.PillTag;
import com.avaroom.ui.components.RoundedButton;
import java.awt.*;
import javax.swing.*;

public class FilterDialog extends JDialog {
    public FilterDialog(Window owner) {
        super(owner, "Bộ lọc tìm kiếm nâng cao", ModalityType.APPLICATION_MODAL);
        setSize(480, 640);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Theme.BG_WHITE);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.BG_WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER_SUBTLE),
                BorderFactory.createEmptyBorder(16, 20, 16, 20)
        ));

        JLabel title = new JLabel("Bộ lọc tìm kiếm");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.PRIMARY_DARK);
        header.add(title, BorderLayout.WEST);

        JButton resetBtn = new JButton("Đặt lại");
        resetBtn.setFont(Theme.FONT_BODY_BOLD);
        resetBtn.setForeground(Theme.ACCENT_RED);
        resetBtn.setFocusPainted(false);
        resetBtn.setBorderPainted(false);
        resetBtn.setContentAreaFilled(false);
        resetBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        header.add(resetBtn, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // Content
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Theme.BG_WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        // University Hub
        JLabel uniTitle = new JLabel("TRỌNG TÂM VỊ TRÍ");
        uniTitle.setFont(Theme.FONT_CAPTION_BOLD);
        uniTitle.setForeground(Theme.TEXT_SECONDARY);
        content.add(uniTitle);
        content.add(Box.createVerticalStrut(6));

        JComboBox<String> uniCombo = new JComboBox<>(new String[]{
                "ĐH Bách Khoa Hà Nội (Hai Bà Trưng)",
                "ĐH Sư Phạm Hà Nội (Xuân Thủy)",
                "ĐH Kinh Tế Quốc Dân (Trần Đại Nghĩa)",
                "ĐH Ngoại Thương (Chùa Láng)",
                "ĐHQG TP. Hồ Chí Minh"
        });
        uniCombo.setFont(Theme.FONT_BODY);
        uniCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        content.add(uniCombo);

        content.add(Box.createVerticalStrut(20));

        // Radius
        JLabel radiusTitle = new JLabel("BÁN KÍNH ĐẾN TRƯỜNG");
        radiusTitle.setFont(Theme.FONT_CAPTION_BOLD);
        radiusTitle.setForeground(Theme.TEXT_SECONDARY);
        content.add(radiusTitle);
        content.add(Box.createVerticalStrut(8));

        JPanel radiusRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        radiusRow.setOpaque(false);
        radiusRow.add(new PillTag("< 500m (Đi bộ)"));
        radiusRow.add(new PillTag("< 1km (Xe đạp)", Theme.PRIMARY_DARK, Theme.TEXT_WHITE, null));
        radiusRow.add(new PillTag("< 2km"));
        radiusRow.add(new PillTag("< 3km"));
        content.add(radiusRow);

        content.add(Box.createVerticalStrut(20));

        // Budget
        JLabel budgetTitle = new JLabel("KHOẢNG NGÂN SÁCH / THÁNG");
        budgetTitle.setFont(Theme.FONT_CAPTION_BOLD);
        budgetTitle.setForeground(Theme.TEXT_SECONDARY);
        content.add(budgetTitle);
        content.add(Box.createVerticalStrut(8));

        JPanel budgetRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        budgetRow.setOpaque(false);
        budgetRow.add(new PillTag("Dưới 2.5 triệu"));
        budgetRow.add(new PillTag("2.5 - 4 triệu 🔥", Theme.PRIMARY_DARK, Theme.TEXT_WHITE, null));
        budgetRow.add(new PillTag("Trên 4 triệu"));
        content.add(budgetRow);

        content.add(Box.createVerticalStrut(20));

        // Amenities
        JLabel amenTitle = new JLabel("TIỆN ÍCH SINH VIÊN QUAN TRỌNG");
        amenTitle.setFont(Theme.FONT_CAPTION_BOLD);
        amenTitle.setForeground(Theme.TEXT_SECONDARY);
        content.add(amenTitle);
        content.add(Box.createVerticalStrut(8));

        JCheckBox c1 = new JCheckBox("🔑 Giờ giấc tự do 24/7 (Vân tay)", true);
        JCheckBox c2 = new JCheckBox("🛡️ Không chung chủ", true);
        JCheckBox c3 = new JCheckBox("🛵 Sạc xe điện an toàn PCCC", true);
        JCheckBox c4 = new JCheckBox("🍳 Nấu ăn thoải mái", true);
        JCheckBox c5 = new JCheckBox("🪟 Ban công / Cửa sổ thoáng", true);

        for (JCheckBox cb : new JCheckBox[]{c1, c2, c3, c4, c5}) {
            cb.setFont(Theme.FONT_BODY);
            cb.setForeground(Theme.TEXT_PRIMARY);
            cb.setOpaque(false);
            content.add(cb);
        }

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom CTA
        JPanel bottomBar = new JPanel(new BorderLayout());
        bottomBar.setBackground(Theme.BG_WHITE);
        bottomBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDER_SUBTLE),
                BorderFactory.createEmptyBorder(12, 20, 16, 20)
        ));

        RoundedButton applyBtn = new RoundedButton("ÁP DỤNG BỘ LỌC (48 PHÒNG)", Theme.PRIMARY_DARK, Theme.TEXT_WHITE, 14);
        applyBtn.setPreferredSize(new Dimension(0, 48));
        applyBtn.addActionListener(e -> {
            dispose();
            NavigationController.getInstance().navigateTo("SEARCH_RESULT");
        });
        bottomBar.add(applyBtn, BorderLayout.CENTER);
        add(bottomBar, BorderLayout.SOUTH);
    }
}
