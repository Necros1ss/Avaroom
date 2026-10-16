package com.avaroom.ui.screens.landlord;

import com.avaroom.models.ReviewModel;
import com.avaroom.navigation.NavigationController;
import com.avaroom.services.DataService;
import com.avaroom.theme.Theme;
import com.avaroom.ui.components.CardPanel;
import com.avaroom.ui.components.RoundedButton;
import com.avaroom.ui.components.StarRatingComponent;
import com.avaroom.ui.components.StyledTextField;
import java.awt.*;
import java.util.List;
import javax.swing.*;

public class LandlordReviewsScreen extends JPanel {
    private final JPanel reviewsListPanel;

    public LandlordReviewsScreen() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_GRAY_LIGHT);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.BG_WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER_SUBTLE),
                BorderFactory.createEmptyBorder(16, 24, 16, 24)
        ));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        left.setOpaque(false);

        RoundedButton backBtn = new RoundedButton("←", Theme.BG_GRAY_LIGHT, Theme.TEXT_PRIMARY, 8);
        backBtn.setFont(Theme.FONT_BODY_BOLD);
        backBtn.addActionListener(e -> NavigationController.getInstance().goBack());
        left.add(backBtn);

        JLabel title = new JLabel("Quản lý & Phản hồi đánh giá");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.PRIMARY_DARK);
        left.add(title);
        header.add(left, BorderLayout.WEST);

        JLabel scoreBadge = new JLabel("⭐ 4.8 / 5.0 (18 Đánh giá)");
        scoreBadge.setFont(Theme.FONT_BODY_BOLD);
        scoreBadge.setForeground(Theme.PRIMARY_DARK);
        header.add(scoreBadge, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // Main content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Theme.BG_GRAY_LIGHT);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 24, 24, 24));

        // Rating summary card
        CardPanel summaryCard = new CardPanel(14, Theme.BG_WHITE, true);
        summaryCard.setLayout(new BorderLayout(24, 0));
        summaryCard.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
        summaryCard.setMaximumSize(new Dimension(1000, 120));

        JPanel scoreCol = new JPanel();
        scoreCol.setLayout(new BoxLayout(scoreCol, BoxLayout.Y_AXIS));
        scoreCol.setOpaque(false);

        JLabel bigScore = new JLabel("4.8");
        bigScore.setFont(new Font("Segoe UI", Font.BOLD, 36));
        bigScore.setForeground(Theme.PRIMARY_DARK);
        scoreCol.add(bigScore);

        StarRatingComponent stars = new StarRatingComponent(5, 18);
        scoreCol.add(stars);

        JLabel sub = new JLabel("100% sinh viên hài lòng");
        sub.setFont(Theme.FONT_CAPTION);
        sub.setForeground(Theme.TEXT_MUTED);
        scoreCol.add(sub);
        summaryCard.add(scoreCol, BorderLayout.WEST);

        // Rating bars
        JPanel barsCol = new JPanel();
        barsCol.setLayout(new BoxLayout(barsCol, BoxLayout.Y_AXIS));
        barsCol.setOpaque(false);
        barsCol.add(createRatingBar("5 sao", 85));
        barsCol.add(Box.createVerticalStrut(4));
        barsCol.add(createRatingBar("4 sao", 15));
        barsCol.add(Box.createVerticalStrut(4));
        barsCol.add(createRatingBar("3 sao", 0));
        barsCol.add(Box.createVerticalStrut(4));
        barsCol.add(createRatingBar("1-2 sao", 0));
        summaryCard.add(barsCol, BorderLayout.CENTER);

        contentPanel.add(summaryCard);
        contentPanel.add(Box.createVerticalStrut(20));

        // Reviews section
        JLabel sectionTitle = new JLabel("Ý kiến phản hồi từ khách thuê");
        sectionTitle.setFont(Theme.FONT_BODY_BOLD);
        sectionTitle.setForeground(Theme.PRIMARY_DARK);
        contentPanel.add(sectionTitle);
        contentPanel.add(Box.createVerticalStrut(12));

        reviewsListPanel = new JPanel();
        reviewsListPanel.setLayout(new BoxLayout(reviewsListPanel, BoxLayout.Y_AXIS));
        reviewsListPanel.setOpaque(false);
        contentPanel.add(reviewsListPanel);

        refreshReviews();

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void refreshReviews() {
        reviewsListPanel.removeAll();
        List<ReviewModel> reviews = DataService.getInstance().getReviews();

        for (int i = 0; i < reviews.size(); i++) {
            ReviewModel r = reviews.get(i);
            final int index = i;

            CardPanel rCard = new CardPanel(12, Theme.BG_WHITE, true);
            rCard.setLayout(new BoxLayout(rCard, BoxLayout.Y_AXIS));
            rCard.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));
            rCard.setMaximumSize(new Dimension(1000, 240));

            JPanel topRow = new JPanel(new BorderLayout());
            topRow.setOpaque(false);

            JPanel authorInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            authorInfo.setOpaque(false);

            JLabel avatar = new JLabel("👤");
            avatar.setFont(new Font("Segoe UI", Font.PLAIN, 20));
            authorInfo.add(avatar);

            JPanel nameBox = new JPanel();
            nameBox.setLayout(new BoxLayout(nameBox, BoxLayout.Y_AXIS));
            nameBox.setOpaque(false);

            JLabel name = new JLabel(r.getStudentName() + " • " + r.getUniversity());
            name.setFont(Theme.FONT_BODY_BOLD);
            name.setForeground(Theme.PRIMARY_DARK);
            nameBox.add(name);

            JLabel date = new JLabel(r.getDate());
            date.setFont(Theme.FONT_CAPTION);
            date.setForeground(Theme.TEXT_MUTED);
            nameBox.add(date);
            authorInfo.add(nameBox);
            topRow.add(authorInfo, BorderLayout.WEST);

            StarRatingComponent rStars = new StarRatingComponent(r.getStars(), 14);
            topRow.add(rStars, BorderLayout.EAST);
            rCard.add(topRow);

            rCard.add(Box.createVerticalStrut(10));
            JLabel comment = new JLabel("<html><body style='width: 700px;'>" + r.getComment() + "</body></html>");
            comment.setFont(Theme.FONT_BODY);
            comment.setForeground(Theme.TEXT_PRIMARY);
            rCard.add(comment);

            // Host reply or Reply Composer
            rCard.add(Box.createVerticalStrut(12));
            if (r.hasHostReply()) {
                JPanel replyBox = new JPanel();
                replyBox.setLayout(new BoxLayout(replyBox, BoxLayout.Y_AXIS));
                replyBox.setBackground(Theme.SECONDARY_CREAM);
                replyBox.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 3, 0, 0, Theme.PRIMARY_DARK),
                        BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));

                JLabel replyHeader = new JLabel("Phản hồi của bạn (Chủ trọ):");
                replyHeader.setFont(Theme.FONT_CAPTION);
                replyHeader.setForeground(Theme.PRIMARY_DARK);
                replyBox.add(replyHeader);
                replyBox.add(Box.createVerticalStrut(4));

                JLabel replyText = new JLabel("<html><body style='width: 660px;'>" + r.getHostReply() + "</body></html>");
                replyText.setFont(Theme.FONT_BODY);
                replyText.setForeground(Theme.TEXT_PRIMARY);
                replyBox.add(replyText);

                rCard.add(replyBox);
            } else {
                JPanel replyComposer = new JPanel(new BorderLayout(8, 0));
                replyComposer.setOpaque(false);

                StyledTextField replyInput = new StyledTextField("Nhập phản hồi cho " + r.getStudentName() + "...");
                replyComposer.add(replyInput, BorderLayout.CENTER);

                RoundedButton replyBtn = new RoundedButton("Phản hồi", Theme.PRIMARY_DARK, Theme.TEXT_WHITE, 8);
                replyBtn.setFont(Theme.FONT_CAPTION);
                replyBtn.setPreferredSize(new Dimension(90, 38));
                replyBtn.addActionListener(e -> {
                    String txt = replyInput.getText().trim();
                    if (!txt.isEmpty()) {
                        DataService.getInstance().addReviewReply(index, txt);
                        refreshReviews();
                    }
                });
                replyComposer.add(replyBtn, BorderLayout.EAST);

                rCard.add(replyComposer);
            }

            reviewsListPanel.add(rCard);
            reviewsListPanel.add(Box.createVerticalStrut(14));
        }

        reviewsListPanel.revalidate();
        reviewsListPanel.repaint();
    }

    private JPanel createRatingBar(String starLabel, int percentage) {
        JPanel barRow = new JPanel(new BorderLayout(8, 0));
        barRow.setOpaque(false);

        JLabel label = new JLabel(starLabel);
        label.setFont(Theme.FONT_CAPTION);
        label.setPreferredSize(new Dimension(45, 16));
        barRow.add(label, BorderLayout.WEST);

        JProgressBar pb = new JProgressBar(0, 100);
        pb.setValue(percentage);
        pb.setForeground(Theme.PRIMARY_DARK);
        pb.setBackground(Theme.BG_GRAY_LIGHT);
        pb.setBorderPainted(false);
        pb.setPreferredSize(new Dimension(200, 8));
        barRow.add(pb, BorderLayout.CENTER);

        JLabel pct = new JLabel(percentage + "%");
        pct.setFont(Theme.FONT_CAPTION);
        pct.setForeground(Theme.TEXT_MUTED);
        pct.setPreferredSize(new Dimension(35, 16));
        barRow.add(pct, BorderLayout.EAST);

        return barRow;
    }
}
