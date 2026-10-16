package com.avaroom.ui.components;

import com.avaroom.theme.Theme;
import java.awt.*;
import javax.swing.*;

public class StarRatingComponent extends JPanel {
    private double rating;
    private int reviewCount;

    public StarRatingComponent(double rating, int reviewCount) {
        this.rating = rating;
        this.reviewCount = reviewCount;
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.LEFT, 4, 0));

        JLabel starLabel = new JLabel("⭐ " + String.format(java.util.Locale.US, "%.1f", rating));
        starLabel.setFont(Theme.FONT_CAPTION_BOLD);
        starLabel.setForeground(Theme.PRIMARY_DARK);
        add(starLabel);

        if (reviewCount > 0) {
            JLabel countLabel = new JLabel("(" + reviewCount + ")");
            countLabel.setFont(Theme.FONT_CAPTION);
            countLabel.setForeground(Theme.TEXT_SECONDARY);
            add(countLabel);
        }
    }
}
