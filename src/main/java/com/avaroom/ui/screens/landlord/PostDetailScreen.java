package com.avaroom.ui.screens.landlord;

import com.avaroom.models.RoomModel;
import com.avaroom.navigation.NavigationController;
import com.avaroom.services.DataService;
import com.avaroom.theme.Theme;
import com.avaroom.ui.components.CardPanel;
import com.avaroom.ui.components.PillTag;
import com.avaroom.ui.components.RoundedButton;
import java.awt.*;
import javax.swing.*;

public class PostDetailScreen extends JPanel {
    private RoomModel room;

    public PostDetailScreen() {
        this("AVR-90412");
    }

    public PostDetailScreen(String roomId) {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_GRAY_LIGHT);

        this.room = DataService.getInstance().getRoomById(roomId);
        if (room == null) room = DataService.getInstance().getAllRooms().get(0);

        JPanel scrollContent = new JPanel();
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.setBackground(Theme.BG_GRAY_LIGHT);
        scrollContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top Status & Code Row
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        topRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel code = new JLabel("Chi tiết tin #" + room.getId());
        code.setFont(Theme.FONT_TITLE);
        code.setForeground(Theme.PRIMARY_DARK);
        topRow.add(code, BorderLayout.WEST);

        topRow.add(new PillTag(room.getStatus(), Theme.ACCENT_GREEN_LIGHT, Theme.ACCENT_GREEN, null), BorderLayout.EAST);
        scrollContent.add(topRow);

        scrollContent.add(Box.createVerticalStrut(16));

        // Details Card
        CardPanel detailCard = new CardPanel(18, Theme.BG_WHITE, true);
        detailCard.setLayout(new BoxLayout(detailCard, BoxLayout.Y_AXIS));
        detailCard.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        detailCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));

        JLabel title = new JLabel(room.getTitle());
        title.setFont(Theme.FONT_SUBTITLE);
        title.setForeground(Theme.PRIMARY_DARK);
        detailCard.add(title);

        detailCard.add(Box.createVerticalStrut(6));
        JLabel addr = new JLabel("📍 " + room.getAddress());
        addr.setFont(Theme.FONT_BODY);
        addr.setForeground(Theme.TEXT_SECONDARY);
        detailCard.add(addr);

        detailCard.add(Box.createVerticalStrut(10));
        JLabel price = new JLabel("Giá thuê: " + room.getFormattedPrice() + " / tháng  •  Diện tích: " + room.getAreaM2() + "m²");
        price.setFont(Theme.FONT_BODY_BOLD);
        price.setForeground(Theme.ACCENT_RED);
        detailCard.add(price);

        detailCard.add(Box.createVerticalStrut(14));
        JLabel stats = new JLabel("📊 Thống kê: 1.480 lượt xem • 42 lưu phòng • 3 lịch hẹn mới");
        stats.setFont(Theme.FONT_CAPTION_BOLD);
        stats.setForeground(Theme.TEXT_PRIMARY);
        detailCard.add(stats);

        scrollContent.add(detailCard);
        scrollContent.add(Box.createVerticalStrut(20));

        // Management Actions Card
        CardPanel actionsCard = new CardPanel(18, Theme.BG_WHITE, true);
        actionsCard.setLayout(new GridLayout(3, 1, 0, 10));
        actionsCard.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        actionsCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 170));

        RoundedButton toggleStatusBtn = new RoundedButton("Tạm ẩn tin đăng (Khi đã đủ khách)", Theme.BG_CHIP, Theme.PRIMARY_DARK, 12);
        toggleStatusBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Đã cập nhật trạng thái tin đăng!", "Avaroom", JOptionPane.INFORMATION_MESSAGE);
        });
        actionsCard.add(toggleStatusBtn);

        RoundedButton reviewsBtn = new RoundedButton("⭐ Quản lý & Phản hồi đánh giá phòng này", Theme.BG_CHIP, Theme.PRIMARY_DARK, 12);
        reviewsBtn.addActionListener(e -> NavigationController.getInstance().navigateTo("LANDLORD_REVIEWS"));
        actionsCard.add(reviewsBtn);

        RoundedButton billingBtn = new RoundedButton("⚡ Chốt điện nước tháng này cho phòng", Theme.PRIMARY_DARK, Theme.TEXT_WHITE, 12);
        billingBtn.addActionListener(e -> NavigationController.getInstance().navigateTo("UTILITY_BILLING"));
        actionsCard.add(billingBtn);

        scrollContent.add(actionsCard);

        JScrollPane scrollPane = new JScrollPane(scrollContent);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }
}
