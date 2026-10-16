package com.avaroom.ui.screens.student;

import com.avaroom.models.RoomModel;
import com.avaroom.navigation.NavigationController;
import com.avaroom.services.DataService;
import com.avaroom.theme.Theme;
import com.avaroom.ui.components.CardPanel;
import com.avaroom.ui.components.PillTag;
import com.avaroom.ui.components.RoundedButton;
import com.avaroom.ui.components.StarRatingComponent;
import com.avaroom.ui.dialogs.FilterDialog;
import java.awt.*;
import java.util.List;
import javax.swing.*;

public class StudentHomeScreen extends JPanel {
    public StudentHomeScreen() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_GRAY_LIGHT);

        JPanel scrollContent = new JPanel();
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.setBackground(Theme.BG_GRAY_LIGHT);
        scrollContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Greeting Header
        JLabel greeting = new JLabel("Chào bạn sinh viên 👋");
        greeting.setFont(Theme.FONT_TITLE);
        greeting.setForeground(Theme.TEXT_PRIMARY);
        greeting.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollContent.add(greeting);

        scrollContent.add(Box.createVerticalStrut(4));
        JLabel location = new JLabel("📍 Gần ĐH Bách Khoa Hà Nội (Hai Bà Trưng)");
        location.setFont(Theme.FONT_BODY);
        location.setForeground(Theme.TEXT_SECONDARY);
        location.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollContent.add(location);

        scrollContent.add(Box.createVerticalStrut(16));

        // Search Bar + Filter Button
        JPanel searchRow = new JPanel(new BorderLayout(10, 0));
        searchRow.setOpaque(false);
        searchRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        searchRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton searchBox = new JButton("🔍  Tìm phòng trọ, căn hộ gần trường...");
        searchBox.setFont(Theme.FONT_BODY);
        searchBox.setForeground(Theme.TEXT_MUTED);
        searchBox.setBackground(Theme.BG_WHITE);
        searchBox.setFocusPainted(false);
        searchBox.setHorizontalAlignment(SwingConstants.LEFT);
        searchBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER_SUBTLE, 1, true),
                BorderFactory.createEmptyBorder(10, 16, 10, 16)
        ));
        searchBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchBox.addActionListener(e -> NavigationController.getInstance().navigateTo("SEARCH_RESULT"));
        searchRow.add(searchBox, BorderLayout.CENTER);

        RoundedButton filterBtn = new RoundedButton("⚙️ Bộ lọc", Theme.PRIMARY_DARK, Theme.TEXT_WHITE, 14);
        filterBtn.setPreferredSize(new Dimension(100, 48));
        filterBtn.addActionListener(e -> {
            Window parentWindow = SwingUtilities.getWindowAncestor(this);
            FilterDialog dialog = new FilterDialog(parentWindow);
            dialog.setVisible(true);
        });
        searchRow.add(filterBtn, BorderLayout.EAST);
        scrollContent.add(searchRow);

        scrollContent.add(Box.createVerticalStrut(20));

        // Quick criteria chips
        JLabel chipsTitle = new JLabel("TIÊU CHÍ SINH VIÊN");
        chipsTitle.setFont(Theme.FONT_CAPTION_BOLD);
        chipsTitle.setForeground(Theme.TEXT_SECONDARY);
        chipsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollContent.add(chipsTitle);

        scrollContent.add(Box.createVerticalStrut(8));
        JPanel chipsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        chipsRow.setOpaque(false);
        chipsRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        chipsRow.add(new PillTag("🏫 Gần trường (< 1km)"));
        chipsRow.add(new PillTag("💰 Giá < 2.5 triệu"));
        chipsRow.add(new PillTag("🔑 Không chung chủ"));
        chipsRow.add(new PillTag("🛏️ Có gác lửng"));
        chipsRow.add(new PillTag("⏰ Giờ tự do"));
        scrollContent.add(chipsRow);

        scrollContent.add(Box.createVerticalStrut(24));

        // Feed Header
        JLabel feedTitle = new JLabel("Bài đăng mới nhất hôm nay");
        feedTitle.setFont(Theme.FONT_SUBTITLE);
        feedTitle.setForeground(Theme.TEXT_PRIMARY);
        feedTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollContent.add(feedTitle);

        scrollContent.add(Box.createVerticalStrut(12));

        // Room cards list
        List<RoomModel> rooms = DataService.getInstance().getAllRooms();
        for (RoomModel r : rooms) {
            scrollContent.add(createRoomCard(r));
            scrollContent.add(Box.createVerticalStrut(14));
        }

        JScrollPane scrollPane = new JScrollPane(scrollContent);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createRoomCard(RoomModel room) {
        CardPanel card = new CardPanel(18, Theme.BG_WHITE, true);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Top tag row
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);

        JPanel leftTags = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        leftTags.setOpaque(false);
        leftTags.add(new PillTag("🛡️ Chính chủ - Sổ hồng"));
        leftTags.add(new PillTag(room.getAreaM2() + "m²"));
        topRow.add(leftTags, BorderLayout.WEST);

        topRow.add(new StarRatingComponent(room.getRating(), room.getReviewCount()), BorderLayout.EAST);
        card.add(topRow, BorderLayout.NORTH);

        // Center Content
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel title = new JLabel(room.getTitle());
        title.setFont(Theme.FONT_SUBTITLE);
        title.setForeground(Theme.PRIMARY_DARK);
        centerPanel.add(title);

        centerPanel.add(Box.createVerticalStrut(4));
        JLabel addr = new JLabel("📍 " + room.getAddress() + " • Cách trường " + room.getDistanceMeters() + "m");
        addr.setFont(Theme.FONT_CAPTION);
        addr.setForeground(Theme.TEXT_SECONDARY);
        centerPanel.add(addr);

        card.add(centerPanel, BorderLayout.CENTER);

        // Bottom row: Price + "Xem phòng" CTA
        JPanel bottomRow = new JPanel(new BorderLayout());
        bottomRow.setOpaque(false);

        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        pricePanel.setOpaque(false);
        JLabel price = new JLabel(room.getFormattedPrice());
        price.setFont(Theme.FONT_PRICE);
        price.setForeground(Theme.ACCENT_RED);
        pricePanel.add(price);

        JLabel month = new JLabel("/ tháng");
        month.setFont(Theme.FONT_CAPTION);
        month.setForeground(Theme.TEXT_SECONDARY);
        pricePanel.add(month);

        bottomRow.add(pricePanel, BorderLayout.WEST);

        RoundedButton viewBtn = new RoundedButton("Xem phòng", Theme.PRIMARY_DARK, Theme.TEXT_WHITE, 12);
        viewBtn.setPreferredSize(new Dimension(110, 38));
        viewBtn.addActionListener(e -> {
            NavigationController.getInstance().navigateTo("ROOM_DETAIL", room.getId());
        });
        bottomRow.add(viewBtn, BorderLayout.EAST);

        card.add(bottomRow, BorderLayout.SOUTH);
        return card;
    }
}
