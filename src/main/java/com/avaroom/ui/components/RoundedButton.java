package com.avaroom.ui.components;

import com.avaroom.theme.Theme;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class RoundedButton extends JButton {
    private Color normalBg;
    private Color hoverBg;
    private Color pressedBg;
    private Color textColor;
    private int cornerRadius = 14;
    private boolean isHovered = false;
    private boolean isPressed = false;
    private boolean outlineOnly = false;

    public RoundedButton(String text) {
        this(text, Theme.PRIMARY_DARK, Theme.TEXT_WHITE, 14);
    }

    public RoundedButton(String text, Color bg, Color textCol, int radius) {
        super(text);
        this.normalBg = bg;
        this.textColor = textCol;
        this.cornerRadius = radius;
        this.hoverBg = computeHover(bg);
        this.pressedBg = computePressed(bg);

        setFont(Theme.FONT_BODY_BOLD);
        setForeground(textColor);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                isPressed = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint();
            }
        });
    }

    public void setOutlineOnly(boolean outline) {
        this.outlineOnly = outline;
        repaint();
    }

    public void setCornerRadius(int r) {
        this.cornerRadius = r;
        repaint();
    }

    public void setCustomColors(Color bg, Color textCol) {
        this.normalBg = bg;
        this.textColor = textCol;
        this.hoverBg = computeHover(bg);
        this.pressedBg = computePressed(bg);
        setForeground(textCol);
        repaint();
    }

    private Color computeHover(Color c) {
        int r = Math.min(255, c.getRed() + 25);
        int g = Math.min(255, c.getGreen() + 25);
        int b = Math.min(255, c.getBlue() + 25);
        return new Color(r, g, b);
    }

    private Color computePressed(Color c) {
        int r = Math.max(0, c.getRed() - 30);
        int g = Math.max(0, c.getGreen() - 30);
        int b = Math.max(0, c.getBlue() - 30);
        return new Color(r, g, b);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        Theme.applyRenderingHints(g2);

        int width = getWidth();
        int height = getHeight();

        Color bg = isPressed ? pressedBg : (isHovered ? hoverBg : normalBg);

        if (outlineOnly) {
            g2.setColor(isHovered ? Theme.BG_CHIP : normalBg);
            g2.fillRoundRect(1, 1, width - 2, height - 2, cornerRadius, cornerRadius);
            g2.setColor(Theme.BORDER_SUBTLE);
            g2.drawRoundRect(1, 1, width - 3, height - 3, cornerRadius, cornerRadius);
        } else {
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, width, height, cornerRadius, cornerRadius);
        }

        // Text
        FontMetrics fm = g2.getFontMetrics(getFont());
        int textX = (width - fm.stringWidth(getText())) / 2;
        int textY = (height - fm.getHeight()) / 2 + fm.getAscent();

        g2.setColor(textColor);
        g2.setFont(getFont());
        g2.drawString(getText(), textX, textY);

        g2.dispose();
    }
}
