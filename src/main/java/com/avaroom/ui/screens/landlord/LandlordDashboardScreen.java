package com.avaroom.ui.screens.landlord;

import com.avaroom.models.RoomModel;
import com.avaroom.navigation.NavigationController;
import com.avaroom.services.DataService;
import com.avaroom.theme.Theme;
import com.avaroom.ui.components.CardPanel;
import com.avaroom.ui.components.PillTag;
import com.avaroom.ui.components.RoundedButton;
import com.avaroom.ui.components.StyledTextField;
import java.awt.*;
import java.util.List;
import javax.swing.*;

public class LandlordDashboardScreen extends JPanel {
    public LandlordDashboardScreen() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_GRAY_LIGHT);

        JPanel scrollContent = new JPanel();
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.setBackground(Theme.BG_GRAY_LIGHT);
        scrollContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top Title + "+ Đăng tin" CTA Row
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        topRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        JLabel title = new JLabel("Quản lý tin đăng & Phòng trọ");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.PRIMARY_DARK);
        topRow.add(title, BorderLayout.WEST);

        RoundedButton createBtn = new RoundedButton("+ Đăng tin mới", Theme.PRIMARY_DARK, Theme.TEXT_WHITE, 12);
        createBtn.setPreferredSize(new Dimension(130, 40));
        createBtn.addActionListener(e -> NavigationController.getInstance().navigateTo("CREATE_POST_STEP1"));
        topRow.add(createBtn, BorderLayout.EAST);
        scrollContent.add(topRow);

        scrollContent.add(Box.createVerticalStrut(16));

        // 3 Key Stats Cards
        JPanel statsRow = new JPanel(new GridLayout(1, 3, 10, 0));
        statsRow.setOpaque(false);
        statsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        statsRow.add(createStatCard("TỔNG PHÒNG", "12", "2 cơ sở quản lý"));
        statsRow.add(createStatCard("TỈ LỆ THUÊ", "10 / 12", "Trống 2 phòng", Theme.ACCENT_RED));
        statsRow.add(createStatCard("LƯỢT XEM", "1.480", "+18% tuần này", Theme.ACCENT_GREEN));
        scrollContent.add(statsRow);

        scrollContent.add(Box.createVerticalStrut(18));

        // Search bar
        StyledTextField searchField = new StyledTextField("🔍 Tìm theo mã AVR hoặc tên đường...");
        searchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        searchField.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollContent.add(searchField);

        scrollContent.add(Box.createVerticalStrut(12));

        // Status Tabs Row
        JPanel tabsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        tabsRow.setOpaque(false);
        tabsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        tabsRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        tabsRow.add(new PillTag("Tất cả (12)", Theme.PRIMARY_DARK, Theme.TEXT_WHITE, null));
        tabsRow.add(new PillTag("Còn trống (2)"));
        tabsRow.add(new PillTag("Đã cọc (1)"));
        tabsRow.add(new PillTag("Chờ duyệt (1)"));
        scrollContent.add(tabsRow);

        scrollContent.add(Box.createVerticalStrut(16));

        // Rooms List for Landlord
        List<RoomModel> rooms = DataService.getInstance().getAllRooms();
        for (RoomModel r : rooms) {
            CardPanel card = new CardPanel(16, Theme.BG_WHITE, true);
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 170));
            card.setAlignmentX(Component.LEFT_ALIGNMENT);

            JPanel row1 = new JPanel(new BorderLayout());
            row1.setOpaque(false);

            JPanel leftPills = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
            leftPills.setOpaque(false);
            leftPills.add(new JLabel("<html><b>#" + r.getId() + "</b></html>"));
            leftPills.add(new PillTag(r.getStatus(), "Còn trống".equals(r.getStatus()) ? Theme.ACCENT_GREEN_LIGHT : Theme.BG_CHIP, "Còn trống".equals(r.getStatus()) ? Theme.ACCENT_GREEN : Theme.TEXT_SECONDARY, null));
            row1.add(leftPills, BorderLayout.WEST);

            row1.add(new PillTag(r.getAreaM2() + "m²"), BorderLayout.EAST);
            card.add(row1);

            card.add(Box.createVerticalStrut(8));
            JLabel roomTitle = new JLabel(r.getTitle());
            roomTitle.setFont(Theme.FONT_SUBTITLE);
            roomTitle.setForeground(Theme.PRIMARY_DARK);
            card.add(roomTitle);

            card.add(Box.createVerticalStrut(4));
            JLabel addr = new JLabel("📍 " + r.getAddress());
            addr.setFont(Theme.FONT_CAPTION);
            addr.setForeground(Theme.TEXT_SECONDARY);
            card.add(addr);

            card.add(Box.createVerticalStrut(10));

            JPanel bottomRow = new JPanel(new BorderLayout());
            bottomRow.setOpaque(false);

            JLabel price = new JLabel(r.getFormattedPrice() + " / tháng");
            price.setFont(Theme.FONT_BODY_BOLD);
            price.setForeground(Theme.ACCENT_RED);
            bottomRow.add(price, BorderLayout.WEST);

            JPanel actionBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            actionBtns.setOpaque(false);

            RoundedButton boostBtn = new RoundedButton("Đẩy tin (Top)", Theme.BG_CHIP, Theme.PRIMARY_DARK, 10);
            boostBtn.setPreferredSize(new Dimension(95, 34));
            actionBtns.add(boostBtn);

            RoundedButton editBtn = new RoundedButton("Sửa tin", Theme.PRIMARY_DARK, Theme.TEXT_WHITE, 10);
            editBtn.setPreferredSize(new Dimension(85, 34));
            editBtn.addActionListener(e -> NavigationController.getInstance().navigateTo("POST_DETAIL", r.getId()));
            actionBtns.add(editBtn);

            bottomRow.add(actionBtns, BorderLayout.EAST);
            card.add(bottomRow);

            scrollContent.add(card);
            scrollContent.add(Box.createVerticalStrut(12));
        }

        JScrollPane scrollPane = new JScrollPane(scrollContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createStatCard(String label, String value, String sub) {
        return createStatCard(label, value, sub, Theme.TEXT_SECONDARY);
    }

    private JPanel createStatCard(String label, String value, String sub, Color subCol) {
        CardPanel p = new CardPanel(14, Theme.BG_WHITE, true);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel l = new JLabel(label);
        l.setFont(Theme.FONT_CAPTION_BOLD);
        l.setForeground(Theme.TEXT_SECONDARY);
        p.add(l);

        p.add(Box.createVerticalStrut(4));
        JLabel v = new JLabel(value);
        v.setFont(Theme.FONT_TITLE_LARGE);
        v.setForeground(Theme.PRIMARY_DARK);
        p.add(v);

        p.add(Box.createVerticalStrut(2));
        JLabel s = new JLabel(sub);
        s.setFont(Theme.FONT_CAPTION);
        s.setForeground(subCol);
        p.add(s);

        return p;
    }
}
