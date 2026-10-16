package com.avaroom.ui.screens.student;

import com.avaroom.models.RoomModel;
import com.avaroom.navigation.NavigationController;
import com.avaroom.services.DataService;
import com.avaroom.theme.Theme;
import com.avaroom.ui.components.CardPanel;
import com.avaroom.ui.components.PillTag;
import com.avaroom.ui.components.RoundedButton;
import java.awt.*;
import java.util.List;
import javax.swing.*;

public class SavedRoomsScreen extends JPanel {
    public SavedRoomsScreen() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_GRAY_LIGHT);

        JPanel scrollContent = new JPanel();
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.setBackground(Theme.BG_GRAY_LIGHT);
        scrollContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Danh sách phòng đã lưu");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.PRIMARY_DARK);
        scrollContent.add(title);

        scrollContent.add(Box.createVerticalStrut(6));
        JLabel subtitle = new JLabel("Lưu trữ các căn phòng ưng ý để dễ dàng so sánh giá và tiện ích");
        subtitle.setFont(Theme.FONT_BODY);
        subtitle.setForeground(Theme.TEXT_SECONDARY);
        scrollContent.add(subtitle);

        scrollContent.add(Box.createVerticalStrut(20));

        List<RoomModel> saved = DataService.getInstance().getSavedRooms();
        if (saved.isEmpty()) {
            JLabel emptyLabel = new JLabel("Bạn chưa lưu căn phòng nào. Hãy khám phá tại Trang chủ!");
            emptyLabel.setFont(Theme.FONT_BODY);
            emptyLabel.setForeground(Theme.TEXT_MUTED);
            scrollContent.add(emptyLabel);
        } else {
            for (RoomModel r : saved) {
                CardPanel card = new CardPanel(18, Theme.BG_WHITE, true);
                card.setLayout(new BorderLayout(14, 0));
                card.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
                card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
                card.setAlignmentX(Component.LEFT_ALIGNMENT);

                JPanel left = new JPanel();
                left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
                left.setOpaque(false);

                JLabel t = new JLabel(r.getTitle());
                t.setFont(Theme.FONT_BODY_BOLD);
                t.setForeground(Theme.PRIMARY_DARK);
                left.add(t);

                left.add(Box.createVerticalStrut(4));
                JLabel a = new JLabel("📍 " + r.getAddress());
                a.setFont(Theme.FONT_CAPTION);
                a.setForeground(Theme.TEXT_SECONDARY);
                left.add(a);

                left.add(Box.createVerticalStrut(4));
                JLabel p = new JLabel(r.getFormattedPrice() + " / tháng");
                p.setFont(Theme.FONT_BODY_BOLD);
                p.setForeground(Theme.ACCENT_RED);
                left.add(p);
                card.add(left, BorderLayout.CENTER);

                JPanel right = new JPanel(new GridLayout(2, 1, 0, 6));
                right.setOpaque(false);

                RoundedButton viewBtn = new RoundedButton("Xem", Theme.PRIMARY_DARK, Theme.TEXT_WHITE, 10);
                viewBtn.addActionListener(e -> NavigationController.getInstance().navigateTo("ROOM_DETAIL", r.getId()));
                right.add(viewBtn);

                RoundedButton removeBtn = new RoundedButton("Bỏ lưu", Theme.BG_CHIP, Theme.ACCENT_RED, 10);
                removeBtn.addActionListener(e -> {
                    DataService.getInstance().toggleSaveRoom(r.getId());
                    NavigationController.getInstance().navigateTo("SAVED_ROOMS");
                });
                right.add(removeBtn);

                card.add(right, BorderLayout.EAST);
                scrollContent.add(card);
                scrollContent.add(Box.createVerticalStrut(12));
            }
        }

        JScrollPane scrollPane = new JScrollPane(scrollContent);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }
}
