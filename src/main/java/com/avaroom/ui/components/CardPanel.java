package com.avaroom.ui.components;

import com.avaroom.theme.Theme;
import java.awt.*;
import javax.swing.*;

public class CardPanel extends JPanel {
    private int cornerRadius = 18;
    private Color bgColor = Theme.BG_WHITE;
    private Color borderColor = Theme.BORDER_SUBTLE;
    private boolean drawBorder = true;

    public CardPanel() {
        this(18, Theme.BG_WHITE, true);
    }

    public CardPanel(int radius, Color bg, boolean border) {
        this.cornerRadius = radius;
        this.bgColor = bg;
        this.drawBorder = border;
        setOpaque(false);
    }

    public void setBackgroundColor(Color c) {
        this.bgColor = c;
        repaint();
    }

    public void setDrawBorder(boolean draw) {
        this.drawBorder = draw;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        Theme.applyRenderingHints(g2);

        int w = getWidth();
        int h = getHeight();

        // Background
        g2.setColor(bgColor);
        g2.fillRoundRect(0, 0, w, h, cornerRadius, cornerRadius);

        // Border
        if (drawBorder) {
            g2.setColor(borderColor);
            g2.drawRoundRect(0, 0, w - 1, h - 1, cornerRadius, cornerRadius);
        }

        g2.dispose();
        super.paintComponent(g);
    }
}
