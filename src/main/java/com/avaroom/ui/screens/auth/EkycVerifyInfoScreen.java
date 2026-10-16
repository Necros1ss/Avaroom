package com.avaroom.ui.screens.auth;

import com.avaroom.navigation.NavigationController;
import com.avaroom.services.DataService;
import com.avaroom.theme.Theme;
import com.avaroom.ui.components.CardPanel;
import com.avaroom.ui.components.RoundedButton;
import java.awt.*;
import javax.swing.*;

public class EkycVerifyInfoScreen extends JPanel {
    public EkycVerifyInfoScreen() {
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

        JLabel title = new JLabel("Kiểm tra thông tin CCCD");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Theme.PRIMARY_DARK);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollContent.add(title);

        scrollContent.add(Box.createVerticalStrut(8));
        JLabel subtitle = new JLabel("Vui lòng đối chiếu dữ liệu trích xuất tự động từ thẻ căn cước trước khi hoàn tất.");
        subtitle.setFont(Theme.FONT_BODY);
        subtitle.setForeground(Theme.TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollContent.add(subtitle);

        scrollContent.add(Box.createVerticalStrut(20));

        // Info Card
        CardPanel infoCard = new CardPanel(18, Theme.BG_WHITE, true);
        infoCard.setLayout(new BoxLayout(infoCard, BoxLayout.Y_AXIS));
        infoCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        infoCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));
        infoCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        addInfoRow(infoCard, "HỌ VÀ TÊN:", "HOÀNG THỊ ÁNH TUYẾT");
        addInfoRow(infoCard, "SỐ CĂN CƯỚC:", "001185012345");
        addInfoRow(infoCard, "NGÀY SINH:", "15/08/1975");
        addInfoRow(infoCard, "QUÊ QUÁN:", "Hai Bà Trưng, Hà Nội");
        addInfoRow(infoCard, "NƠI THƯỜNG TRÚ:", "Số 42 Ngõ 187 Tạ Quang Bửu, P. Bách Khoa, Hai Bà Trưng, Hà Nội");

        scrollContent.add(infoCard);
        scrollContent.add(Box.createVerticalStrut(28));

        // Confirm Button
        RoundedButton confirmBtn = new RoundedButton("HOÀN TẤT XÁC THỰC & VÀO ỨNG DỤNG", Theme.PRIMARY_DARK, Theme.TEXT_WHITE, 16);
        confirmBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        confirmBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        confirmBtn.addActionListener(e -> {
            DataService.getInstance().setEkycVerified(true);
            String role = DataService.getInstance().getCurrentUserRole();
            if ("LANDLORD".equals(role)) {
                NavigationController.getInstance().navigateTo("LANDLORD_DASHBOARD");
            } else {
                NavigationController.getInstance().navigateTo("STUDENT_HOME");
            }
        });
        scrollContent.add(confirmBtn);

        scrollContent.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(scrollContent);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void addInfoRow(JPanel container, String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setBorder(BorderFactory.createEmptyBorder(4, 0, 8, 0));

        JLabel l = new JLabel(label);
        l.setFont(Theme.FONT_CAPTION_BOLD);
        l.setForeground(Theme.TEXT_SECONDARY);

        JLabel v = new JLabel("<html><b>" + value + "</b></html>");
        v.setFont(Theme.FONT_BODY);
        v.setForeground(Theme.PRIMARY_DARK);

        row.add(l, BorderLayout.WEST);
        row.add(v, BorderLayout.EAST);
        container.add(row);
    }
}
