package com.avaroom.ui.screens.student;

import com.avaroom.models.ReviewModel;
import com.avaroom.services.DataService;
import com.avaroom.theme.Theme;
import com.avaroom.ui.components.CardPanel;
import com.avaroom.ui.components.PillTag;
import java.awt.*;
import java.util.List;
import javax.swing.*;

public class ReviewsScreen extends JPanel {
    public ReviewsScreen() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_GRAY_LIGHT);

        JPanel scrollContent = new JPanel();
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.setBackground(Theme.BG_GRAY_LIGHT);
        scrollContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Đánh giá & Phản hồi cộng đồng");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.PRIMARY_DARK);
        scrollContent.add(title);

        scrollContent.add(Box.createVerticalStrut(6));
        JLabel subtitle = new JLabel("100% đánh giá thật từ các sinh viên đã từng ở hoặc khảo sát phòng thực tế");
        subtitle.setFont(Theme.FONT_BODY);
        subtitle.setForeground(Theme.TEXT_SECONDARY);
        scrollContent.add(subtitle);

        scrollContent.add(Box.createVerticalStrut(20));

        // Rating Overview Card
        CardPanel ratingOverview = new CardPanel(18, Theme.BG_WHITE, true);
        ratingOverview.setLayout(new BorderLayout(20, 0));
        ratingOverview.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
        ratingOverview.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        JPanel leftScore = new JPanel();
        leftScore.setLayout(new BoxLayout(leftScore, BoxLayout.Y_AXIS));
        leftScore.setOpaque(false);

        JLabel score = new JLabel("4.8");
        score.setFont(new Font("Segoe UI", Font.BOLD, 36));
        score.setForeground(Theme.PRIMARY_DARK);
        leftScore.add(score);

        JLabel stars = new JLabel("⭐⭐⭐⭐⭐");
        stars.setFont(Theme.FONT_BODY);
        leftScore.add(stars);

        JLabel totalReviews = new JLabel("42 đánh giá xác thực");
        totalReviews.setFont(Theme.FONT_CAPTION);
        totalReviews.setForeground(Theme.TEXT_SECONDARY);
        leftScore.add(totalReviews);

        ratingOverview.add(leftScore, BorderLayout.WEST);

        // Rating breakdown bars on right
        JPanel rightBars = new JPanel(new GridLayout(3, 1, 0, 4));
        rightBars.setOpaque(false);
        rightBars.add(createBarRow("5 ⭐ Tuyệt vời", 88));
        rightBars.add(createBarRow("4 ⭐ Tốt", 10));
        rightBars.add(createBarRow("3 ⭐ Bình thường", 2));
        ratingOverview.add(rightBars, BorderLayout.CENTER);

        scrollContent.add(ratingOverview);
        scrollContent.add(Box.createVerticalStrut(24));

        // Reviews List
        List<ReviewModel> list = DataService.getInstance().getReviews();
        for (ReviewModel rev : list) {
            CardPanel revCard = new CardPanel(16, Theme.BG_WHITE, true);
            revCard.setLayout(new BoxLayout(revCard, BoxLayout.Y_AXIS));
            revCard.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
            revCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));

            JPanel authorRow = new JPanel(new BorderLayout());
            authorRow.setOpaque(false);

            JLabel author = new JLabel(rev.getAuthorName() + " • " + rev.getUniversity());
            author.setFont(Theme.FONT_BODY_BOLD);
            author.setForeground(Theme.PRIMARY_DARK);
            authorRow.add(author, BorderLayout.WEST);

            JLabel starsLabel = new JLabel("⭐".repeat(rev.getStars()) + "  " + rev.getDate());
            starsLabel.setFont(Theme.FONT_CAPTION);
            starsLabel.setForeground(Theme.TEXT_SECONDARY);
            authorRow.add(starsLabel, BorderLayout.EAST);
            revCard.add(authorRow);

            revCard.add(Box.createVerticalStrut(8));
            JLabel comment = new JLabel("<html><body style='width: 440px;'>" + rev.getComment() + "</body></html>");
            comment.setFont(Theme.FONT_BODY);
            comment.setForeground(Theme.TEXT_PRIMARY);
            revCard.add(comment);

            if (rev.getHostReply() != null) {
                revCard.add(Box.createVerticalStrut(10));
                JPanel replyBox = new JPanel(new BorderLayout());
                replyBox.setBackground(Theme.BG_CHIP);
                replyBox.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

                JLabel replyText = new JLabel("<html><b>Phản hồi từ Chủ trọ:</b> " + rev.getHostReply() + "</html>");
                replyText.setFont(Theme.FONT_CAPTION);
                replyText.setForeground(Theme.PRIMARY_DARK);
                replyBox.add(replyText, BorderLayout.CENTER);
                revCard.add(replyBox);
            }

            scrollContent.add(revCard);
            scrollContent.add(Box.createVerticalStrut(12));
        }

        JScrollPane scrollPane = new JScrollPane(scrollContent);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createBarRow(String label, int pct) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);

        JLabel l = new JLabel(label);
        l.setFont(Theme.FONT_CAPTION);
        l.setPreferredSize(new Dimension(100, 16));
        row.add(l, BorderLayout.WEST);

        JProgressBar pb = new JProgressBar(0, 100);
        pb.setValue(pct);
        pb.setStringPainted(false);
        pb.setForeground(Theme.ACCENT_YELLOW);
        row.add(pb, BorderLayout.CENTER);

        JLabel pLabel = new JLabel(pct + "%");
        pLabel.setFont(Theme.FONT_CAPTION);
        row.add(pLabel, BorderLayout.EAST);

        return row;
    }
}
