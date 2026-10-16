package com.avaroom.ui.screens.landlord;

import com.avaroom.navigation.NavigationController;
import com.avaroom.theme.Theme;
import com.avaroom.ui.components.CardPanel;
import com.avaroom.ui.components.PillTag;
import com.avaroom.ui.components.RoundedButton;
import com.avaroom.ui.components.StyledTextField;
import java.awt.*;
import javax.swing.*;

public class CreatePostStep1Screen extends JPanel {
    private final StyledTextField titleField;
    private final StyledTextField addrField;
    private final StyledTextField priceField;
    private final StyledTextField areaField;

    public CreatePostStep1Screen() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_GRAY_LIGHT);

        JPanel scrollContent = new JPanel();
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.setBackground(Theme.BG_GRAY_LIGHT);
        scrollContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Step indicator
        JLabel stepLabel = new JLabel("BƯỚC 1 / 4 • Bước kế: Tiện ích & Nội thất");
        stepLabel.setFont(Theme.FONT_CAPTION_BOLD);
        stepLabel.setForeground(Theme.ACCENT_RED);
        scrollContent.add(stepLabel);

        scrollContent.add(Box.createVerticalStrut(4));
        JLabel title = new JLabel("Thông tin phòng trọ cơ bản");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.PRIMARY_DARK);
        scrollContent.add(title);

        scrollContent.add(Box.createVerticalStrut(18));

        // Form fields
        JLabel tLabel = new JLabel("TIÊU ĐỀ TIN ĐĂNG");
        tLabel.setFont(Theme.FONT_CAPTION_BOLD);
        tLabel.setForeground(Theme.PRIMARY_DARK);
        scrollContent.add(tLabel);

        scrollContent.add(Box.createVerticalStrut(6));
        titleField = new StyledTextField("Studio ban công thoáng mát gần ĐH Bách Khoa");
        titleField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        scrollContent.add(titleField);

        scrollContent.add(Box.createVerticalStrut(14));

        // Category pills
        JLabel catLabel = new JLabel("LOẠI HÌNH CĂN PHÒNG");
        catLabel.setFont(Theme.FONT_CAPTION_BOLD);
        catLabel.setForeground(Theme.PRIMARY_DARK);
        scrollContent.add(catLabel);

        scrollContent.add(Box.createVerticalStrut(6));
        JPanel catRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        catRow.setOpaque(false);
        catRow.add(new PillTag("Studio tiện nghi", Theme.PRIMARY_DARK, Theme.TEXT_WHITE, null));
        catRow.add(new PillTag("Phòng gác lửng"));
        catRow.add(new PillTag("Khép kín mini"));
        catRow.add(new PillTag("Căn hộ dịch vụ"));
        scrollContent.add(catRow);

        scrollContent.add(Box.createVerticalStrut(14));

        // Address
        JLabel addrLabel = new JLabel("ĐỊA CHỈ PHÒNG CHÍNH XÁC");
        addrLabel.setFont(Theme.FONT_CAPTION_BOLD);
        addrLabel.setForeground(Theme.PRIMARY_DARK);
        scrollContent.add(addrLabel);

        scrollContent.add(Box.createVerticalStrut(6));
        addrField = new StyledTextField("Ngõ 30 Tạ Quang Bửu, P. Bách Khoa, Hai Bà Trưng, Hà Nội");
        addrField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        scrollContent.add(addrField);

        scrollContent.add(Box.createVerticalStrut(14));

        // Price & Area
        JPanel numRow = new JPanel(new GridLayout(1, 2, 12, 0));
        numRow.setOpaque(false);
        numRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        JPanel pCol = new JPanel();
        pCol.setLayout(new BoxLayout(pCol, BoxLayout.Y_AXIS));
        pCol.setOpaque(false);
        JLabel pL = new JLabel("GIÁ THUÊ (VNĐ / THÁNG)");
        pL.setFont(Theme.FONT_CAPTION_BOLD);
        pCol.add(pL);
        pCol.add(Box.createVerticalStrut(6));
        priceField = new StyledTextField("4200000");
        pCol.add(priceField);
        numRow.add(pCol);

        JPanel aCol = new JPanel();
        aCol.setLayout(new BoxLayout(aCol, BoxLayout.Y_AXIS));
        aCol.setOpaque(false);
        JLabel aL = new JLabel("DIỆN TÍCH (M²)");
        aL.setFont(Theme.FONT_CAPTION_BOLD);
        aCol.add(aL);
        aCol.add(Box.createVerticalStrut(6));
        areaField = new StyledTextField("28");
        aCol.add(areaField);
        numRow.add(aCol);

        scrollContent.add(numRow);
        scrollContent.add(Box.createVerticalStrut(18));

        // PCCC Standards Box
        JLabel safeTitle = new JLabel("TIÊU CHUẨN AN TOÀN & PCCC (AVAROOM CORE)");
        safeTitle.setFont(Theme.FONT_CAPTION_BOLD);
        safeTitle.setForeground(Theme.TEXT_SECONDARY);
        scrollContent.add(safeTitle);

        scrollContent.add(Box.createVerticalStrut(8));
        CardPanel safeCard = new CardPanel(16, Theme.BG_WHITE, true);
        safeCard.setLayout(new BoxLayout(safeCard, BoxLayout.Y_AXIS));
        safeCard.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));
        safeCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        safeCard.add(new JCheckBox("🚨 Báo cháy & Thang thoát hiểm độc lập", true));
        safeCard.add(new JCheckBox("⚡ Khu sạc xe điện ngắt tự động (Bình Co2 Foam)", true));
        safeCard.add(new JCheckBox("🔒 Khóa thông minh FaceID 24/7 (Không chung chủ)", true));
        scrollContent.add(safeCard);

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

        RoundedButton nextBtn = new RoundedButton("Tiếp tục (Bước 2) →", Theme.PRIMARY_DARK, Theme.TEXT_WHITE, 12);
        nextBtn.setPreferredSize(new Dimension(170, 44));
        nextBtn.addActionListener(e -> NavigationController.getInstance().navigateTo("CREATE_POST_STEP2"));
        bottomBar.add(nextBtn, BorderLayout.EAST);

        RoundedButton draftBtn = new RoundedButton("Lưu nháp", Theme.BG_WHITE, Theme.PRIMARY_DARK, 12);
        draftBtn.setOutlineOnly(true);
        draftBtn.setPreferredSize(new Dimension(110, 44));
        draftBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Đã lưu nháp tin đăng thành công!", "Avaroom", JOptionPane.INFORMATION_MESSAGE);
            NavigationController.getInstance().navigateTo("LANDLORD_DASHBOARD");
        });
        bottomBar.add(draftBtn, BorderLayout.WEST);

        add(bottomBar, BorderLayout.SOUTH);
    }
}
