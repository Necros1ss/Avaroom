package com.avaroom.ui.components;

import com.avaroom.theme.Theme;
import java.awt.*;
import javax.swing.*;

public class PillTag extends JLabel {
    private Color bgColor;
    private Color textColor;
    private Color borderColor;

    public PillTag(String text) {
        this(text, Theme.BG_CHIP, Theme.TEXT_PRIMARY, null);
    }

    public PillTag(String text, Color bg, Color textCol, Color borderCol) {
        super(text);
        this.bgColor = bg;
        this.textColor = textCol;
        this.borderColor = borderCol;

        setFont(Theme.FONT_CAPTION_BOLD);
        setForeground(textColor);
        setHorizontalAlignment(SwingConstants.CENTER);
        setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        Theme.applyRenderingHints(g2);

        int w = getWidth();
        int h = getHeight();

        g2.setColor(bgColor);
        g2.fillRoundRect(0, 0, w, h, h, h);

        if (borderColor != null) {
            g2.setColor(borderColor);
            g2.drawRoundRect(0, 0, w - 1, h - 1, h, h);
        }

        g2.dispose();
        super.paintComponent(g);
    }
}
