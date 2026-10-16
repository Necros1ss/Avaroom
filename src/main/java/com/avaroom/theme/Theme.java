package com.avaroom.theme;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public final class Theme {
    private Theme() {}

    // Colors matching Figma Design System
    public static final Color BG_WARM_CREAM = new Color(0xFD, 0xF9, 0xF2);
    public static final Color BG_WHITE = new Color(0xFF, 0xFF, 0xFF);
    public static final Color BG_GRAY_LIGHT = new Color(0xF7, 0xFA, 0xFC);
    public static final Color BG_CHIP = new Color(0xF0, 0xF4, 0xF8);

    public static final Color PRIMARY_DARK = new Color(0x04, 0x05, 0x05);
    public static final Color PRIMARY_BLACK = new Color(0x1A, 0x20, 0x2C);
    public static final Color PRIMARY_BLUE = new Color(0x0D, 0x1C, 0x2E);

    public static final Color ACCENT_RED = new Color(0xE5, 0x3E, 0x3E);
    public static final Color ACCENT_RED_LIGHT = new Color(0xFF, 0xDA, 0xD7);
    public static final Color ACCENT_YELLOW = new Color(0xFB, 0xBF, 0x24);
    public static final Color ACCENT_GOLD = new Color(0xF5, 0x9E, 0x0B);
    public static final Color ACCENT_GREEN = new Color(0x10, 0xB9, 0x81);
    public static final Color ACCENT_GREEN_LIGHT = new Color(0xDC, 0xFC, 0xE7);
    public static final Color ACCENT_BLUE = new Color(0x3B, 0x82, 0xF6);
    public static final Color SECONDARY_CREAM = new Color(0xF6, 0xEE, 0xDC);

    public static final Color TEXT_PRIMARY = new Color(0x1A, 0x20, 0x2C);
    public static final Color TEXT_SECONDARY = new Color(0x71, 0x80, 0x96);
    public static final Color TEXT_MUTED = new Color(0xA0, 0xAE, 0xC0);
    public static final Color TEXT_WHITE = new Color(0xFF, 0xFF, 0xFF);

    public static final Color BORDER_LIGHT = new Color(0xED, 0xF2, 0xF7);
    public static final Color BORDER_SUBTLE = new Color(0xE2, 0xE8, 0xF0);

    // Standard Typography
    public static final Font FONT_TITLE_LARGE = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_BODY_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_CAPTION_BOLD = new Font("Segoe UI", Font.BOLD, 11);
    public static final Font FONT_CAPTION = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_PRICE = new Font("Segoe UI", Font.BOLD, 19);

    // Standard Paddings & Borders
    public static final Border PADDING_SCREEN = new EmptyBorder(16, 20, 20, 20);
    public static final Border PADDING_CARD = new EmptyBorder(14, 16, 14, 16);

    public static final RenderingHints RENDERING_HINTS = new RenderingHints(
            java.util.Map.of(
                    RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON,
                    RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON,
                    RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY
            )
    );

    /**
     * Applies high quality rendering hints to graphics context
     */
    public static void applyRenderingHints(Graphics2D g2) {
        g2.setRenderingHints(RENDERING_HINTS);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    }
}
