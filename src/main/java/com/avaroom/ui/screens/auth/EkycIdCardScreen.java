package com.avaroom.ui.screens.auth;

import com.avaroom.navigation.NavigationController;
import com.avaroom.theme.Theme;
import com.avaroom.ui.components.CardPanel;
import com.avaroom.ui.components.RoundedButton;
import java.awt.*;
import javax.swing.*;

public class EkycIdCardScreen extends JPanel {
    private boolean frontCaptured = true;
    private boolean backCaptured = true;

    public EkycIdCardScreen() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_WARM_CREAM);

        JPanel scrollContent = new JPanel();
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.setBackground(Theme.BG_WARM_CREAM);
        scrollContent.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        // Back
        JButton backBtn = new JButton("← Quay lại");
        backBtn.setFont(Theme.FONT_BODY_BOLD);
        backBtn.setForeground(Theme.PRIMARY_DARK);
        backBtn.setFocusPainted(false);
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        backBtn.addActionListener(e -> NavigationController.getInstance().goBack());
        scrollContent.add(backBtn);

        scrollContent.add(Box.createVerticalStrut(16));

        JLabel title = new JLabel("Xác thực CCCD 2 mặt");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Theme.PRIMARY_DARK);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollContent.add(title);

        scrollContent.add(Box.createVerticalStrut(8));
        JLabel subtitle = new JLabel("<html>Để đảm bảo an toàn cho cộng đồng Avaroom, vui lòng chụp rõ 2 mặt thẻ Căn cước công dân gắn chip còn hiệu lực.</html>");
        subtitle.setFont(Theme.FONT_BODY);
        subtitle.setForeground(Theme.TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitle.setMaximumSize(new Dimension(500, 40));
        scrollContent.add(subtitle);

        scrollContent.add(Box.createVerticalStrut(20));

        // Front Card
        CardPanel frontCard = new CardPanel(16, Theme.BG_WHITE, true);
        frontCard.setLayout(new BoxLayout(frontCard, BoxLayout.Y_AXIS));
        frontCard.setBorder(BorderFactory.createEmptyBorder(18, 16, 18, 16));
        frontCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));
        frontCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel frontTitle = new JLabel("📷 MẶT TRƯỚC CCCD: ĐÃ CHỤP");
        frontTitle.setFont(Theme.FONT_SUBTITLE);
        frontTitle.setForeground(Theme.ACCENT_GREEN);
        frontCard.add(frontTitle);

        frontCard.add(Box.createVerticalStrut(6));
        JLabel frontNote = new JLabel("✓ Ảnh sắc nét, đủ 4 góc, quốc huy & khuôn mặt rõ ràng");
        frontNote.setFont(Theme.FONT_CAPTION);
        frontNote.setForeground(Theme.TEXT_SECONDARY);
        frontCard.add(frontNote);

        scrollContent.add(frontCard);
        scrollContent.add(Box.createVerticalStrut(14));

        // Back Card
        CardPanel backCard = new CardPanel(16, Theme.BG_WHITE, true);
        backCard.setLayout(new BoxLayout(backCard, BoxLayout.Y_AXIS));
        backCard.setBorder(BorderFactory.createEmptyBorder(18, 16, 18, 16));
        backCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));
        backCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel backTitle = new JLabel("📷 MẶT SAU CCCD: ĐÃ CHỤP");
        backTitle.setFont(Theme.FONT_SUBTITLE);
        backTitle.setForeground(Theme.ACCENT_GREEN);
        backCard.add(backTitle);

        backCard.add(Box.createVerticalStrut(6));
        JLabel backNote = new JLabel("✓ Đã quét chip điện tử & vùng MRZ nhận dạng bảo mật");
        backNote.setFont(Theme.FONT_CAPTION);
        backNote.setForeground(Theme.TEXT_SECONDARY);
        backCard.add(backNote);

        scrollContent.add(backCard);
        scrollContent.add(Box.createVerticalStrut(24));

        // Continue Button
        RoundedButton continueBtn = new RoundedButton("TIẾP TỤC KIỂM TRA THÔNG TIN", Theme.PRIMARY_DARK, Theme.TEXT_WHITE, 16);
        continueBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        continueBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        continueBtn.addActionListener(e -> NavigationController.getInstance().navigateTo("EKYC_VERIFY_INFO"));
        scrollContent.add(continueBtn);

        scrollContent.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(scrollContent);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        add(scrollPane, BorderLayout.CENTER);
    }
}
