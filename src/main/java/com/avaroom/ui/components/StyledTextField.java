package com.avaroom.ui.components;

import com.avaroom.theme.Theme;
import java.awt.*;
import javax.swing.*;

public class StyledTextField extends JTextField {
    private String placeholder = "";

    public StyledTextField() {
        this("");
    }

    public StyledTextField(String placeholder) {
        this.placeholder = placeholder;
        setFont(Theme.FONT_BODY);
        setForeground(Theme.TEXT_PRIMARY);
        setCaretColor(Theme.PRIMARY_DARK);
        setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        setOpaque(false);
    }

    public void setPlaceholder(String ph) {
        this.placeholder = ph;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        Theme.applyRenderingHints(g2);

        int w = getWidth();
        int h = getHeight();

        g2.setColor(Theme.BG_WHITE);
        g2.fillRoundRect(0, 0, w, h, 14, 14);

        g2.setColor(Theme.BORDER_SUBTLE);
        g2.drawRoundRect(0, 0, w - 1, h - 1, 14, 14);

        g2.dispose();
        super.paintComponent(g);

        if (getText().isEmpty() && placeholder != null && !placeholder.isEmpty() && !isFocusOwner()) {
            Graphics2D gText = (Graphics2D) g.create();
            Theme.applyRenderingHints(gText);
            gText.setFont(getFont());
            gText.setColor(Theme.TEXT_MUTED);
            FontMetrics fm = gText.getFontMetrics();
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            gText.drawString(placeholder, 14, y);
            gText.dispose();
        }
    }
}
