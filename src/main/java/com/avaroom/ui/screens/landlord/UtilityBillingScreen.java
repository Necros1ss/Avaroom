package com.avaroom.ui.screens.landlord;

import com.avaroom.navigation.NavigationController;
import com.avaroom.theme.Theme;
import com.avaroom.ui.components.CardPanel;
import com.avaroom.ui.components.RoundedButton;
import com.avaroom.ui.components.StyledTextField;
import com.avaroom.ui.dialogs.InvoicePreviewDialog;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class UtilityBillingScreen extends JPanel {
    private final StyledTextField electricOldField;
    private final StyledTextField electricNewField;
    private final StyledTextField waterOldField;
    private final StyledTextField waterNewField;

    private final JLabel electricCalcLabel;
    private final JLabel waterCalcLabel;
    private final JLabel totalCalcLabel;

    private int electricUsage = 125;
    private long electricCost = 437500;
    private int waterUsage = 8;
    private long waterCost = 200000;
    private final long rentCost = 3500000;
    private final long serviceCost = 150000;

    public UtilityBillingScreen() {
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

        JLabel title = new JLabel("Chốt điện nước & Lập hóa đơn");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.PRIMARY_DARK);
        left.add(title);
        header.add(left, BorderLayout.WEST);

        JLabel monthBadge = new JLabel("Kỳ thanh toán: Tháng 10/2026");
        monthBadge.setFont(Theme.FONT_BODY_BOLD);
        monthBadge.setForeground(Theme.TEXT_MUTED);
        header.add(monthBadge, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Form content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Theme.BG_GRAY_LIGHT);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 24, 24, 24));

        // Room selection info
        CardPanel roomCard = new CardPanel(14, Theme.BG_WHITE, true);
        roomCard.setLayout(new BorderLayout(14, 0));
        roomCard.setBorder(BorderFactory.createEmptyBorder(14, 18, 14, 18));
        roomCard.setMaximumSize(new Dimension(800, 70));

        JLabel rInfo = new JLabel("🏠 Phòng 301 - Studio ban công (#AVR-90412)");
        rInfo.setFont(Theme.FONT_BODY_BOLD);
        rInfo.setForeground(Theme.PRIMARY_DARK);
        roomCard.add(rInfo, BorderLayout.WEST);

        JLabel tenantInfo = new JLabel("Khách thuê: Nguyễn Thanh Tùng (0912 345 678)");
        tenantInfo.setFont(Theme.FONT_CAPTION);
        tenantInfo.setForeground(Theme.TEXT_MUTED);
        roomCard.add(tenantInfo, BorderLayout.EAST);
        contentPanel.add(roomCard);
        contentPanel.add(Box.createVerticalStrut(18));

        // Electric meter card
        CardPanel electricCard = new CardPanel(14, Theme.BG_WHITE, true);
        electricCard.setLayout(new BoxLayout(electricCard, BoxLayout.Y_AXIS));
        electricCard.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
        electricCard.setMaximumSize(new Dimension(800, 150));

        JLabel eTitle = new JLabel("⚡ Chỉ số công tơ Điện (Đơn giá: 3.500 đ/kWh)");
        eTitle.setFont(Theme.FONT_BODY_BOLD);
        eTitle.setForeground(Theme.PRIMARY_DARK);
        electricCard.add(eTitle);
        electricCard.add(Box.createVerticalStrut(12));

        JPanel eGrid = new JPanel(new GridLayout(1, 2, 16, 0));
        eGrid.setOpaque(false);

        JPanel eOldBox = new JPanel(new BorderLayout(0, 4));
        eOldBox.setOpaque(false);
        eOldBox.add(new JLabel("Chỉ số cũ (kWh):"), BorderLayout.NORTH);
        electricOldField = new StyledTextField("1420");
        electricOldField.setEditable(false);
        eOldBox.add(electricOldField, BorderLayout.CENTER);
        eGrid.add(eOldBox);

        JPanel eNewBox = new JPanel(new BorderLayout(0, 4));
        eNewBox.setOpaque(false);
        eNewBox.add(new JLabel("Chỉ số mới (kWh):"), BorderLayout.NORTH);
        electricNewField = new StyledTextField("1545");
        eNewBox.add(electricNewField, BorderLayout.CENTER);
        eGrid.add(eNewBox);
        electricCard.add(eGrid);

        electricCard.add(Box.createVerticalStrut(10));
        electricCalcLabel = new JLabel("Tiêu thụ: 125 kWh  •  Thành tiền: 437.500 đ");
        electricCalcLabel.setFont(Theme.FONT_CAPTION);
        electricCalcLabel.setForeground(Theme.ACCENT_GREEN);
        electricCard.add(electricCalcLabel);

        contentPanel.add(electricCard);
        contentPanel.add(Box.createVerticalStrut(16));

        // Water meter card
        CardPanel waterCard = new CardPanel(14, Theme.BG_WHITE, true);
        waterCard.setLayout(new BoxLayout(waterCard, BoxLayout.Y_AXIS));
        waterCard.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
        waterCard.setMaximumSize(new Dimension(800, 150));

        JLabel wTitle = new JLabel("💧 Chỉ số đồng hồ Nước (Đơn giá: 25.000 đ/m³)");
        wTitle.setFont(Theme.FONT_BODY_BOLD);
        wTitle.setForeground(Theme.PRIMARY_DARK);
        waterCard.add(wTitle);
        waterCard.add(Box.createVerticalStrut(12));

        JPanel wGrid = new JPanel(new GridLayout(1, 2, 16, 0));
        wGrid.setOpaque(false);

        JPanel wOldBox = new JPanel(new BorderLayout(0, 4));
        wOldBox.setOpaque(false);
        wOldBox.add(new JLabel("Chỉ số cũ (m³):"), BorderLayout.NORTH);
        waterOldField = new StyledTextField("312");
        waterOldField.setEditable(false);
        wOldBox.add(waterOldField, BorderLayout.CENTER);
        wGrid.add(wOldBox);

        JPanel wNewBox = new JPanel(new BorderLayout(0, 4));
        wNewBox.setOpaque(false);
        wNewBox.add(new JLabel("Chỉ số mới (m³):"), BorderLayout.NORTH);
        waterNewField = new StyledTextField("320");
        wNewBox.add(waterNewField, BorderLayout.CENTER);
        wGrid.add(wNewBox);
        waterCard.add(wGrid);

        waterCard.add(Box.createVerticalStrut(10));
        waterCalcLabel = new JLabel("Tiêu thụ: 8 m³  •  Thành tiền: 200.000 đ");
        waterCalcLabel.setFont(Theme.FONT_CAPTION);
        waterCalcLabel.setForeground(Theme.ACCENT_BLUE);
        waterCard.add(waterCalcLabel);

        contentPanel.add(waterCard);
        contentPanel.add(Box.createVerticalStrut(16));

        // Total Summary Card
        CardPanel summaryCard = new CardPanel(14, Theme.BG_WHITE, true);
        summaryCard.setLayout(new BorderLayout());
        summaryCard.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
        summaryCard.setMaximumSize(new Dimension(800, 80));

        JPanel sLeft = new JPanel();
        sLeft.setLayout(new BoxLayout(sLeft, BoxLayout.Y_AXIS));
        sLeft.setOpaque(false);

        JLabel totalDesc = new JLabel("Tổng cộng tiền phòng & dịch vụ:");
        totalDesc.setFont(Theme.FONT_CAPTION);
        totalDesc.setForeground(Theme.TEXT_MUTED);
        sLeft.add(totalDesc);

        sLeft.add(Box.createVerticalStrut(4));
        totalCalcLabel = new JLabel("4.287.500 đ");
        totalCalcLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        totalCalcLabel.setForeground(new Color(220, 50, 50));
        sLeft.add(totalCalcLabel);
        summaryCard.add(sLeft, BorderLayout.WEST);

        RoundedButton previewBtn = new RoundedButton("Tạo hóa đơn & Mã VietQR", Theme.PRIMARY_DARK, Theme.TEXT_WHITE, 10);
        previewBtn.setPreferredSize(new Dimension(220, 44));
        previewBtn.addActionListener(e -> {
            Frame topFrame = (Frame) SwingUtilities.getWindowAncestor(this);
            long total = rentCost + electricCost + waterCost + serviceCost;
            InvoicePreviewDialog dlg = new InvoicePreviewDialog(
                    topFrame,
                    "Phòng 301 - Studio ban công (#AVR-90412)",
                    "Nguyễn Thanh Tùng",
                    rentCost,
                    electricUsage,
                    electricCost,
                    waterUsage,
                    waterCost,
                    serviceCost,
                    total
            );
            dlg.setVisible(true);
        });
        summaryCard.add(previewBtn, BorderLayout.EAST);

        contentPanel.add(summaryCard);

        // Document listeners for real-time recalculation
        DocumentListener recalculateListener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { updateMath(); }
            public void removeUpdate(DocumentEvent e) { updateMath(); }
            public void changedUpdate(DocumentEvent e) { updateMath(); }
        };
        electricNewField.getDocument().addDocumentListener(recalculateListener);
        waterNewField.getDocument().addDocumentListener(recalculateListener);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void updateMath() {
        try {
            int eOld = Integer.parseInt(electricOldField.getText().trim());
            int eNew = Integer.parseInt(electricNewField.getText().trim());
            electricUsage = Math.max(0, eNew - eOld);
            electricCost = electricUsage * 3500L;
            electricCalcLabel.setText("Tiêu thụ: " + electricUsage + " kWh  •  Thành tiền: " + String.format("%,d đ", electricCost));

            int wOld = Integer.parseInt(waterOldField.getText().trim());
            int wNew = Integer.parseInt(waterNewField.getText().trim());
            waterUsage = Math.max(0, wNew - wOld);
            waterCost = waterUsage * 25000L;
            waterCalcLabel.setText("Tiêu thụ: " + waterUsage + " m³  •  Thành tiền: " + String.format("%,d đ", waterCost));

            long total = rentCost + electricCost + waterCost + serviceCost;
            totalCalcLabel.setText(String.format("%,d đ", total));
        } catch (Exception ex) {
            // Ignore parse errors while typing
        }
    }
}
