package com.avaroom.ui.screens.landlord;

import com.avaroom.navigation.NavigationController;
import com.avaroom.theme.Theme;
import com.avaroom.ui.components.CardPanel;
import com.avaroom.ui.components.RoundedButton;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

public class StatsGrowthScreen extends JPanel {

    public StatsGrowthScreen() {
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

        JLabel title = new JLabel("Thống kê & Tăng trưởng phòng trọ");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.PRIMARY_DARK);
        left.add(title);
        header.add(left, BorderLayout.WEST);

        // Filter tabs
        JPanel periodTabs = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        periodTabs.setOpaque(false);
        String[] periods = {"Tháng này", "Quý 3/2026", "Năm 2026"};
        for (int i = 0; i < periods.length; i++) {
            boolean active = i == 0;
            RoundedButton pBtn = new RoundedButton(
                    periods[i],
                    active ? Theme.PRIMARY_DARK : Theme.BG_GRAY_LIGHT,
                    active ? Theme.TEXT_WHITE : Theme.TEXT_PRIMARY,
                    8
            );
            pBtn.setFont(Theme.FONT_CAPTION);
            periodTabs.add(pBtn);
        }
        header.add(periodTabs, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Theme.BG_GRAY_LIGHT);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 24, 24, 24));

        // 4 KPI Cards
        JPanel kpiGrid = new JPanel(new GridLayout(1, 4, 16, 0));
        kpiGrid.setOpaque(false);
        kpiGrid.setMaximumSize(new Dimension(1000, 100));

        kpiGrid.add(createKpiCard("💰 Doanh thu dự kiến", "42.500.000 đ", "▲ +12% so với T9", Theme.ACCENT_GREEN));
        kpiGrid.add(createKpiCard("🏠 Tỉ lệ lấp đầy", "91.6% (11/12)", "▲ +8.3% hiệu suất", Theme.ACCENT_BLUE));
        kpiGrid.add(createKpiCard("👁️ Lượt xem tin đăng", "3.840 lượt", "▲ +24% quan tâm", Theme.ACCENT_GOLD));
        kpiGrid.add(createKpiCard("📅 Lịch hẹn xem phòng", "28 lượt", "Tỉ lệ chốt 78%", Theme.PRIMARY_DARK));

        contentPanel.add(kpiGrid);
        contentPanel.add(Box.createVerticalStrut(20));

        // Chart Card
        CardPanel chartCard = new CardPanel(14, Theme.BG_WHITE, true);
        chartCard.setLayout(new BorderLayout());
        chartCard.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
        chartCard.setMaximumSize(new Dimension(1000, 320));
        chartCard.setPreferredSize(new Dimension(800, 300));

        JPanel chartHeader = new JPanel(new BorderLayout());
        chartHeader.setOpaque(false);
        JLabel chartTitle = new JLabel("Biểu đồ doanh thu 6 tháng gần nhất (Triệu VNĐ)");
        chartTitle.setFont(Theme.FONT_BODY_BOLD);
        chartTitle.setForeground(Theme.PRIMARY_DARK);
        chartHeader.add(chartTitle, BorderLayout.WEST);

        JLabel legend = new JLabel("■ Doanh thu thực tế   ■ Mục tiêu tháng");
        legend.setFont(Theme.FONT_CAPTION);
        legend.setForeground(Theme.TEXT_MUTED);
        chartHeader.add(legend, BorderLayout.EAST);
        chartCard.add(chartHeader, BorderLayout.NORTH);

        RevenueBarChartCanvas chartCanvas = new RevenueBarChartCanvas();
        chartCard.add(chartCanvas, BorderLayout.CENTER);

        contentPanel.add(chartCard);
        contentPanel.add(Box.createVerticalStrut(20));

        // Property efficiency list
        CardPanel efficiencyCard = new CardPanel(14, Theme.BG_WHITE, true);
        efficiencyCard.setLayout(new BoxLayout(efficiencyCard, BoxLayout.Y_AXIS));
        efficiencyCard.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
        efficiencyCard.setMaximumSize(new Dimension(1000, 240));

        JLabel effTitle = new JLabel("Hiệu quả khai thác theo từng phòng");
        effTitle.setFont(Theme.FONT_BODY_BOLD);
        effTitle.setForeground(Theme.PRIMARY_DARK);
        efficiencyCard.add(effTitle);
        efficiencyCard.add(Box.createVerticalStrut(12));

        efficiencyCard.add(createListingRow("Phòng 301 - Studio ban công (#AVR-90412)", "3.500.000 đ/th", "100% Lấp đầy", Theme.ACCENT_GREEN));
        efficiencyCard.add(Box.createVerticalStrut(8));
        efficiencyCard.add(createListingRow("Phòng 202 - Gác lửng (#AVR-89210)", "3.200.000 đ/th", "100% Lấp đầy", Theme.ACCENT_GREEN));
        efficiencyCard.add(Box.createVerticalStrut(8));
        efficiencyCard.add(createListingRow("Phòng 401 - Căn hộ mini (#AVR-91045)", "4.000.000 đ/th", "Đang mở thuê (420 view)", Theme.ACCENT_GOLD));

        contentPanel.add(efficiencyCard);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createKpiCard(String label, String value, String delta, Color deltaColor) {
        CardPanel card = new CardPanel(12, Theme.BG_WHITE, true);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));

        JLabel lbl = new JLabel(label);
        lbl.setFont(Theme.FONT_CAPTION);
        lbl.setForeground(Theme.TEXT_MUTED);
        card.add(lbl);

        card.add(Box.createVerticalStrut(6));
        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.BOLD, 17));
        val.setForeground(Theme.PRIMARY_DARK);
        card.add(val);

        card.add(Box.createVerticalStrut(4));
        JLabel d = new JLabel(delta);
        d.setFont(new Font("Segoe UI", Font.BOLD, 11));
        d.setForeground(deltaColor);
        card.add(d);

        return card;
    }

    private JPanel createListingRow(String title, String price, String status, Color statusColor) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER_SUBTLE),
                BorderFactory.createEmptyBorder(6, 4, 8, 4)
        ));

        JLabel nameLbl = new JLabel(title);
        nameLbl.setFont(Theme.FONT_BODY);
        nameLbl.setForeground(Theme.TEXT_PRIMARY);
        row.add(nameLbl, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        right.setOpaque(false);

        JLabel priceLbl = new JLabel(price);
        priceLbl.setFont(Theme.FONT_BODY_BOLD);
        priceLbl.setForeground(Theme.PRIMARY_DARK);
        right.add(priceLbl);

        JLabel statusLbl = new JLabel(status);
        statusLbl.setFont(Theme.FONT_CAPTION);
        statusLbl.setForeground(statusColor);
        right.add(statusLbl);

        row.add(right, BorderLayout.EAST);
        return row;
    }

    // Java2D custom bar chart
    private static class RevenueBarChartCanvas extends JPanel {
        private final String[] months = {"T5", "T6", "T7", "T8", "T9", "T10"};
        private final int[] revenues = {32, 35, 36, 39, 41, 43}; // in millions
        private final int[] targets = {30, 32, 35, 38, 40, 42};

        public RevenueBarChartCanvas() {
            setOpaque(false);
            setPreferredSize(new Dimension(700, 200));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHints(Theme.RENDERING_HINTS);

            int width = getWidth();
            int height = getHeight();
            int padLeft = 45;
            int padBottom = 30;
            int padTop = 20;
            int padRight = 20;

            int chartW = width - padLeft - padRight;
            int chartH = height - padTop - padBottom;

            // Draw gridlines
            g2.setColor(Theme.BORDER_SUBTLE);
            g2.setFont(Theme.FONT_CAPTION);
            for (int i = 0; i <= 4; i++) {
                int y = padTop + chartH - (i * chartH / 4);
                g2.drawLine(padLeft, y, width - padRight, y);
                g2.setColor(Theme.TEXT_MUTED);
                g2.drawString((i * 12) + "M", 10, y + 4);
                g2.setColor(Theme.BORDER_SUBTLE);
            }

            int n = months.length;
            int barGroupW = chartW / n;
            int barWidth = Math.min(28, barGroupW / 3);

            for (int i = 0; i < n; i++) {
                int groupCenterX = padLeft + (i * barGroupW) + (barGroupW / 2);

                // Revenue bar
                int barH = (int) ((revenues[i] / 48.0) * chartH);
                int barX = groupCenterX - barWidth - 2;
                int barY = padTop + chartH - barH;

                GradientPaint gp = new GradientPaint(
                        barX, barY, Theme.PRIMARY_DARK,
                        barX, barY + barH, new Color(45, 60, 80)
                );
                g2.setPaint(gp);
                g2.fill(new RoundRectangle2D.Double(barX, barY, barWidth, barH, 6, 6));

                // Target bar
                int targetH = (int) ((targets[i] / 48.0) * chartH);
                int targetX = groupCenterX + 2;
                int targetY = padTop + chartH - targetH;

                g2.setColor(new Color(220, 230, 240));
                g2.fill(new RoundRectangle2D.Double(targetX, targetY, barWidth, targetH, 6, 6));

                // Label
                g2.setColor(Theme.TEXT_PRIMARY);
                g2.setFont(Theme.FONT_CAPTION);
                FontMetrics fm = g2.getFontMetrics();
                int txtW = fm.stringWidth(months[i]);
                g2.drawString(months[i], groupCenterX - (txtW / 2), height - 10);
            }

            g2.dispose();
        }
    }
}
