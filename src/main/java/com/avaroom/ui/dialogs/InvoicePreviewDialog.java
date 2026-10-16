package com.avaroom.ui.dialogs;

import com.avaroom.theme.Theme;
import com.avaroom.ui.components.CardPanel;
import com.avaroom.ui.components.RoundedButton;
import java.awt.*;
import javax.swing.*;

public class InvoicePreviewDialog extends JDialog {

    public InvoicePreviewDialog(
            Frame owner,
            String roomTitle,
            String tenantName,
            long rentAmount,
            int electricUsage,
            long electricAmount,
            int waterUsage,
            long waterAmount,
            long serviceAmount,
            long totalAmount
    ) {
        super(owner, "Chi tiết Hóa đơn & Mã VietQR", true);
        setSize(520, 680);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Theme.BG_GRAY_LIGHT);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.BG_WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER_SUBTLE),
                BorderFactory.createEmptyBorder(16, 20, 16, 20)
        ));

        JLabel title = new JLabel("HÓA ĐƠN TIỀN PHÒNG & DỊCH VỤ");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.PRIMARY_DARK);
        header.add(title, BorderLayout.WEST);

        JLabel month = new JLabel("Tháng 10/2026");
        month.setFont(Theme.FONT_BODY_BOLD);
        month.setForeground(Theme.TEXT_MUTED);
        header.add(month, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Invoice Body Card
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        CardPanel billCard = new CardPanel(14, Theme.BG_WHITE, true);
        billCard.setLayout(new BoxLayout(billCard, BoxLayout.Y_AXIS));
        billCard.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));

        // Meta info
        JLabel roomLbl = new JLabel("🏠 " + roomTitle);
        roomLbl.setFont(Theme.FONT_BODY_BOLD);
        roomLbl.setForeground(Theme.PRIMARY_DARK);
        billCard.add(roomLbl);

        billCard.add(Box.createVerticalStrut(4));
        JLabel tenantLbl = new JLabel("👤 Khách thuê: " + tenantName + " • Hạn thanh toán: 20/10/2026");
        tenantLbl.setFont(Theme.FONT_CAPTION);
        tenantLbl.setForeground(Theme.TEXT_MUTED);
        billCard.add(tenantLbl);

        billCard.add(Box.createVerticalStrut(12));
        billCard.add(new JSeparator());
        billCard.add(Box.createVerticalStrut(12));

        // Itemized table
        billCard.add(createBillRow("1. Tiền thuê phòng cơ sở", String.format("%,d đ", rentAmount)));
        billCard.add(Box.createVerticalStrut(6));
        billCard.add(createBillRow("2. Tiền điện sinh hoạt (" + electricUsage + " kWh x 3.500 đ)", String.format("%,d đ", electricAmount)));
        billCard.add(Box.createVerticalStrut(6));
        billCard.add(createBillRow("3. Tiền nước sinh hoạt (" + waterUsage + " m³ x 25.000 đ)", String.format("%,d đ", waterAmount)));
        billCard.add(Box.createVerticalStrut(6));
        billCard.add(createBillRow("4. Phí dịch vụ (Wifi, rác, thang máy)", String.format("%,d đ", serviceAmount)));

        billCard.add(Box.createVerticalStrut(12));
        billCard.add(new JSeparator());
        billCard.add(Box.createVerticalStrut(12));

        // Total
        JPanel totalRow = new JPanel(new BorderLayout());
        totalRow.setOpaque(false);
        JLabel totalText = new JLabel("TỔNG TIỀN CẦN THANH TOÁN:");
        totalText.setFont(Theme.FONT_BODY_BOLD);
        totalText.setForeground(Theme.PRIMARY_DARK);
        totalRow.add(totalText, BorderLayout.WEST);

        JLabel totalVal = new JLabel(String.format("%,d đ", totalAmount));
        totalVal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        totalVal.setForeground(new Color(220, 50, 50));
        totalRow.add(totalVal, BorderLayout.EAST);
        billCard.add(totalRow);

        centerPanel.add(billCard);
        centerPanel.add(Box.createVerticalStrut(16));

        // VietQR Code Panel
        CardPanel qrCard = new CardPanel(14, Theme.BG_WHITE, true);
        qrCard.setLayout(new BorderLayout(14, 0));
        qrCard.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));

        VietQRCanvas qrCanvas = new VietQRCanvas();
        qrCard.add(qrCanvas, BorderLayout.WEST);

        JPanel qrInfo = new JPanel();
        qrInfo.setLayout(new BoxLayout(qrInfo, BoxLayout.Y_AXIS));
        qrInfo.setOpaque(false);

        JLabel qrTitle = new JLabel("Mã VietQR Chuẩn NAPAS 247");
        qrTitle.setFont(Theme.FONT_BODY_BOLD);
        qrTitle.setForeground(Theme.PRIMARY_DARK);
        qrInfo.add(qrTitle);

        qrInfo.add(Box.createVerticalStrut(4));
        JLabel bankName = new JLabel("Ngân hàng: MB Bank (Quân Đội)");
        bankName.setFont(Theme.FONT_CAPTION);
        bankName.setForeground(Theme.TEXT_PRIMARY);
        qrInfo.add(bankName);

        JLabel accNo = new JLabel("Số tài khoản: 0988765432");
        accNo.setFont(Theme.FONT_CAPTION);
        accNo.setForeground(Theme.TEXT_PRIMARY);
        qrInfo.add(accNo);

        JLabel holder = new JLabel("Chủ TK: HOANG THI ANH TUYET");
        holder.setFont(Theme.FONT_CAPTION);
        holder.setForeground(Theme.TEXT_PRIMARY);
        qrInfo.add(holder);

        JLabel note = new JLabel("Nội dung: AVR90412 T10 2026");
        note.setFont(Theme.FONT_CAPTION);
        note.setForeground(Theme.ACCENT_GREEN);
        qrInfo.add(note);

        qrCard.add(qrInfo, BorderLayout.CENTER);
        centerPanel.add(qrCard);

        add(centerPanel, BorderLayout.CENTER);

        // Bottom Actions
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 14));
        bottomBar.setBackground(Theme.BG_WHITE);
        bottomBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDER_SUBTLE));

        RoundedButton cancelBtn = new RoundedButton("Đóng", Theme.BG_GRAY_LIGHT, Theme.TEXT_PRIMARY, 8);
        cancelBtn.addActionListener(e -> dispose());
        bottomBar.add(cancelBtn);

        RoundedButton sendBtn = new RoundedButton("Gửi hóa đơn cho sinh viên", Theme.PRIMARY_DARK, Theme.TEXT_WHITE, 8);
        sendBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Hóa đơn điện nước tháng 10 kèm mã VietQR đã được gửi đến tài khoản sinh viên " + tenantName + "!",
                    "Gửi thành công", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });
        bottomBar.add(sendBtn);

        add(bottomBar, BorderLayout.SOUTH);
    }

    private JPanel createBillRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setFont(Theme.FONT_BODY);
        lbl.setForeground(Theme.TEXT_PRIMARY);
        row.add(lbl, BorderLayout.WEST);

        JLabel val = new JLabel(value);
        val.setFont(Theme.FONT_BODY_BOLD);
        val.setForeground(Theme.TEXT_PRIMARY);
        row.add(val, BorderLayout.EAST);
        return row;
    }

    // Java2D VietQR code representation
    private static class VietQRCanvas extends JPanel {
        public VietQRCanvas() {
            setPreferredSize(new Dimension(100, 100));
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHints(Theme.RENDERING_HINTS);

            int size = 96;
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(2, 2, size, size, 8, 8);
            g2.setColor(Theme.PRIMARY_DARK);
            g2.drawRoundRect(2, 2, size, size, 8, 8);

            // Draw QR corner blocks
            g2.fillRect(8, 8, 22, 22);
            g2.setColor(Color.WHITE);
            g2.fillRect(12, 12, 14, 14);
            g2.setColor(Theme.PRIMARY_DARK);
            g2.fillRect(15, 15, 8, 8);

            g2.fillRect(70, 8, 22, 22);
            g2.setColor(Color.WHITE);
            g2.fillRect(74, 12, 14, 14);
            g2.setColor(Theme.PRIMARY_DARK);
            g2.fillRect(77, 15, 8, 8);

            g2.fillRect(8, 70, 22, 22);
            g2.setColor(Color.WHITE);
            g2.fillRect(12, 74, 14, 14);
            g2.setColor(Theme.PRIMARY_DARK);
            g2.fillRect(15, 77, 8, 8);

            // Center logo
            g2.setColor(new Color(220, 50, 50));
            g2.fillRoundRect(38, 38, 24, 24, 6, 6);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 9));
            g2.drawString("QR", 44, 54);

            // Random dots
            g2.setColor(Theme.PRIMARY_DARK);
            for (int r = 0; r < 5; r++) {
                for (int c = 0; c < 5; c++) {
                    if ((r + c) % 2 == 0) {
                        g2.fillRect(36 + c * 5, 12 + r * 5, 3, 3);
                        g2.fillRect(36 + c * 5, 70 + r * 5, 3, 3);
                    }
                }
            }

            g2.dispose();
        }
    }
}
