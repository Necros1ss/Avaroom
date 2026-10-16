package com.avaroom.ui.screens.student;

import com.avaroom.models.RoomModel;
import com.avaroom.navigation.NavigationController;
import com.avaroom.services.DataService;
import com.avaroom.theme.Theme;
import com.avaroom.ui.components.CardPanel;
import com.avaroom.ui.components.PillTag;
import com.avaroom.ui.components.RoundedButton;
import com.avaroom.ui.components.StarRatingComponent;
import java.awt.*;
import java.util.List;
import javax.swing.*;

public class SearchResultScreen extends JPanel {
    public SearchResultScreen() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_GRAY_LIGHT);

        JPanel scrollContent = new JPanel();
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.setBackground(Theme.BG_GRAY_LIGHT);
        scrollContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Result header bar
        JPanel headerRow = new JPanel(new BorderLayout());
        headerRow.setOpaque(false);
        headerRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel resultTitle = new JLabel("Tìm kiếm: Gần ĐH Bách Khoa (< 1.5km)");
        resultTitle.setFont(Theme.FONT_SUBTITLE);
        resultTitle.setForeground(Theme.PRIMARY_DARK);
        headerRow.add(resultTitle, BorderLayout.WEST);

        JLabel countBadge = new JLabel("48 phòng phù hợp");
        countBadge.setFont(Theme.FONT_CAPTION_BOLD);
        countBadge.setForeground(Theme.TEXT_SECONDARY);
        headerRow.add(countBadge, BorderLayout.EAST);
        scrollContent.add(headerRow);

        scrollContent.add(Box.createVerticalStrut(10));

        // Sort Bar
        JPanel sortRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        sortRow.setOpaque(false);
        sortRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        sortRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sortLabel = new JLabel("Sắp xếp theo:");
        sortLabel.setFont(Theme.FONT_CAPTION);
        sortLabel.setForeground(Theme.TEXT_SECONDARY);
        sortRow.add(sortLabel);

        JComboBox<String> sortCombo = new JComboBox<>(new String[]{"Khoảng cách gần nhất", "Giá thấp đến cao", "Đánh giá cao nhất", "Mới đăng"});
        sortCombo.setFont(Theme.FONT_CAPTION);
        sortRow.add(sortCombo);

        scrollContent.add(sortRow);
        scrollContent.add(Box.createVerticalStrut(16));

        // List matching rooms
        List<RoomModel> rooms = DataService.getInstance().getAllRooms();
        for (RoomModel r : rooms) {
            CardPanel card = new CardPanel(18, Theme.BG_WHITE, true);
            card.setLayout(new BorderLayout());
            card.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
            card.setAlignmentX(Component.LEFT_ALIGNMENT);

            JPanel top = new JPanel(new BorderLayout());
            top.setOpaque(false);
            JPanel tags = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
            tags.setOpaque(false);
            tags.add(new PillTag(r.getStatus(), "Còn trống".equals(r.getStatus()) ? Theme.ACCENT_GREEN_LIGHT : Theme.BG_CHIP, "Còn trống".equals(r.getStatus()) ? Theme.ACCENT_GREEN : Theme.TEXT_SECONDARY, null));
            tags.add(new PillTag(r.getAreaM2() + "m²"));
            top.add(tags, BorderLayout.WEST);
            top.add(new StarRatingComponent(r.getRating(), r.getReviewCount()), BorderLayout.EAST);
            card.add(top, BorderLayout.NORTH);

            JPanel mid = new JPanel();
            mid.setLayout(new BoxLayout(mid, BoxLayout.Y_AXIS));
            mid.setOpaque(false);
            mid.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

            JLabel t = new JLabel(r.getTitle());
            t.setFont(Theme.FONT_BODY_BOLD);
            t.setForeground(Theme.PRIMARY_DARK);
            mid.add(t);

            JLabel a = new JLabel("📍 " + r.getAddress() + " • " + r.getDistanceMeters() + "m đến trường");
            a.setFont(Theme.FONT_CAPTION);
            a.setForeground(Theme.TEXT_SECONDARY);
            mid.add(a);
            card.add(mid, BorderLayout.CENTER);

            JPanel bot = new JPanel(new BorderLayout());
            bot.setOpaque(false);

            JLabel p = new JLabel(r.getFormattedPrice() + " / tháng");
            p.setFont(Theme.FONT_BODY_BOLD);
            p.setForeground(Theme.ACCENT_RED);
            bot.add(p, BorderLayout.WEST);

            RoundedButton viewBtn = new RoundedButton("Xem chi tiết", Theme.PRIMARY_DARK, Theme.TEXT_WHITE, 12);
            viewBtn.setPreferredSize(new Dimension(110, 36));
            viewBtn.addActionListener(e -> NavigationController.getInstance().navigateTo("ROOM_DETAIL", r.getId()));
            bot.add(viewBtn, BorderLayout.EAST);

            card.add(bot, BorderLayout.SOUTH);

            scrollContent.add(card);
            scrollContent.add(Box.createVerticalStrut(12));
        }

        JScrollPane scrollPane = new JScrollPane(scrollContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }
}
