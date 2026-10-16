package com.avaroom.ui.screens.landlord;

import com.avaroom.navigation.NavigationController;
import com.avaroom.theme.Theme;
import com.avaroom.ui.components.CardPanel;
import com.avaroom.ui.components.RoundedButton;
import java.awt.*;
import javax.swing.*;

public class CreatePostStep2Screen extends JPanel {
    public CreatePostStep2Screen() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_GRAY_LIGHT);

        JPanel scrollContent = new JPanel();
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.setBackground(Theme.BG_GRAY_LIGHT);
        scrollContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel stepLabel = new JLabel("BƯỚC 2 / 4 • Bước kế: Ảnh 360° & Xác thực");
        stepLabel.setFont(Theme.FONT_CAPTION_BOLD);
        stepLabel.setForeground(Theme.ACCENT_RED);
        scrollContent.add(stepLabel);

        scrollContent.add(Box.createVerticalStrut(4));
        JLabel title = new JLabel("Tiện ích & Nội thất có sẵn");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.PRIMARY_DARK);
        scrollContent.add(title);

        scrollContent.add(Box.createVerticalStrut(18));

        // Furniture Section
        JLabel fTitle = new JLabel("NỘI THẤT CĂN PHÒNG");
        fTitle.setFont(Theme.FONT_CAPTION_BOLD);
        fTitle.setForeground(Theme.TEXT_SECONDARY);
        scrollContent.add(fTitle);

        scrollContent.add(Box.createVerticalStrut(8));
        CardPanel fCard = new CardPanel(16, Theme.BG_WHITE, true);
        fCard.setLayout(new GridLayout(3, 2, 8, 8));
        fCard.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        fCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        fCard.add(new JCheckBox("🛏️ Giường nệm cao su 1m6", true));
        fCard.add(new JCheckBox("🚪 Tủ quần áo 3 cánh", true));
        fCard.add(new JCheckBox("🪑 Bàn ghế học tập / làm việc", true));
        fCard.add(new JCheckBox("🪟 Rèm cửa chắn sáng", true));
        fCard.add(new JCheckBox("🪜 Gác lửng đúc cao 2m", true));
        fCard.add(new JCheckBox("🌿 Ban công đón gió riêng", true));
        scrollContent.add(fCard);

        scrollContent.add(Box.createVerticalStrut(20));

        // Electronics & Appliances Section
        JLabel eTitle = new JLabel("THIẾT BỊ ĐIỆN TỬ & GIA DỤNG");
        eTitle.setFont(Theme.FONT_CAPTION_BOLD);
        eTitle.setForeground(Theme.TEXT_SECONDARY);
        scrollContent.add(eTitle);

        scrollContent.add(Box.createVerticalStrut(8));
        CardPanel eCard = new CardPanel(16, Theme.BG_WHITE, true);
        eCard.setLayout(new GridLayout(3, 2, 8, 8));
        eCard.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        eCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        eCard.add(new JCheckBox("❄️ Điều hòa Inverter 12000BTU", true));
        eCard.add(new JCheckBox("🧊 Tủ lạnh 2 cánh tiết kiệm điện", true));
        eCard.add(new JCheckBox("🧺 Máy giặt riêng biệt", true));
        eCard.add(new JCheckBox("🚿 Bình nóng lạnh 30L", true));
        eCard.add(new JCheckBox("🍳 Bếp từ đôi & Hút mùi", true));
        eCard.add(new JCheckBox("📶 Wifi Mesh tốc độ cao", true));
        scrollContent.add(eCard);

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

        RoundedButton nextBtn = new RoundedButton("Tiếp tục (Bước 3) →", Theme.PRIMARY_DARK, Theme.TEXT_WHITE, 12);
        nextBtn.setPreferredSize(new Dimension(170, 44));
        nextBtn.addActionListener(e -> NavigationController.getInstance().navigateTo("CREATE_POST_STEP3"));
        bottomBar.add(nextBtn, BorderLayout.EAST);

        RoundedButton backBtn = new RoundedButton("← Bước 1", Theme.BG_WHITE, Theme.PRIMARY_DARK, 12);
        backBtn.setOutlineOnly(true);
        backBtn.setPreferredSize(new Dimension(110, 44));
        backBtn.addActionListener(e -> NavigationController.getInstance().goBack());
        bottomBar.add(backBtn, BorderLayout.WEST);

        add(bottomBar, BorderLayout.SOUTH);
    }
}
