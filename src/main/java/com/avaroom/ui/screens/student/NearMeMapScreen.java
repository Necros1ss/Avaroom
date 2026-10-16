package com.avaroom.ui.screens.student;

import com.avaroom.models.RoomModel;
import com.avaroom.navigation.NavigationController;
import com.avaroom.services.DataService;
import com.avaroom.theme.Theme;
import com.avaroom.ui.components.CardPanel;
import com.avaroom.ui.components.PillTag;
import com.avaroom.ui.components.RoundedButton;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class NearMeMapScreen extends JPanel {
    private RoomModel selectedRoom;
    private final MapCanvas mapCanvas;
    private final CardPanel bottomCard;
    private final JLabel cardTitle;
    private final JLabel cardPrice;
    private final JLabel cardAddr;

    public NearMeMapScreen() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_GRAY_LIGHT);

        selectedRoom = DataService.getInstance().getAllRooms().get(0);

        // Map Canvas in Center
        mapCanvas = new MapCanvas();
        add(mapCanvas, BorderLayout.CENTER);

        // Bottom Room Preview Card
        bottomCard = new CardPanel(18, Theme.BG_WHITE, true);
        bottomCard.setLayout(new BorderLayout(14, 0));
        bottomCard.setBorder(BorderFactory.createEmptyBorder(14, 18, 14, 18));
        bottomCard.setPreferredSize(new Dimension(0, 110));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        cardTitle = new JLabel(selectedRoom.getTitle());
        cardTitle.setFont(Theme.FONT_SUBTITLE);
        cardTitle.setForeground(Theme.PRIMARY_DARK);
        infoPanel.add(cardTitle);

        infoPanel.add(Box.createVerticalStrut(4));
        cardAddr = new JLabel("📍 " + selectedRoom.getAddress() + " • Cách trường " + selectedRoom.getDistanceMeters() + "m");
        cardAddr.setFont(Theme.FONT_CAPTION);
        cardAddr.setForeground(Theme.TEXT_SECONDARY);
        infoPanel.add(cardAddr);

        infoPanel.add(Box.createVerticalStrut(4));
        cardPrice = new JLabel(selectedRoom.getFormattedPrice() + " / tháng");
        cardPrice.setFont(Theme.FONT_BODY_BOLD);
        cardPrice.setForeground(Theme.ACCENT_RED);
        infoPanel.add(cardPrice);

        bottomCard.add(infoPanel, BorderLayout.CENTER);

        RoundedButton viewBtn = new RoundedButton("Xem phòng", Theme.PRIMARY_DARK, Theme.TEXT_WHITE, 12);
        viewBtn.setPreferredSize(new Dimension(110, 42));
        viewBtn.addActionListener(e -> {
            NavigationController.getInstance().navigateTo("ROOM_DETAIL", selectedRoom.getId());
        });
        bottomCard.add(viewBtn, BorderLayout.EAST);

        add(bottomCard, BorderLayout.SOUTH);
    }

    private class MapCanvas extends JPanel {
        // Pin positions
        private final Point pin1 = new Point(180, 150); // AVR-90412
        private final Point pin2 = new Point(320, 260); // AVR-89210
        private final Point pin3 = new Point(140, 310); // AVR-91045

        public MapCanvas() {
            setOpaque(true);
            setBackground(new Color(238, 242, 246));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getPoint().distance(pin1) < 40) {
                        selectRoom("AVR-90412");
                    } else if (e.getPoint().distance(pin2) < 40) {
                        selectRoom("AVR-89210");
                    } else if (e.getPoint().distance(pin3) < 40) {
                        selectRoom("AVR-91045");
                    }
                }
            });
        }

        private void selectRoom(String roomId) {
            RoomModel r = DataService.getInstance().getRoomById(roomId);
            if (r != null) {
                selectedRoom = r;
                cardTitle.setText(r.getTitle());
                cardAddr.setText("📍 " + r.getAddress() + " • Cách trường " + r.getDistanceMeters() + "m");
                cardPrice.setText(r.getFormattedPrice() + " / tháng");
                repaint();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            Theme.applyRenderingHints(g2);

            int w = getWidth();
            int h = getHeight();

            // Draw grid roads
            g2.setColor(new Color(255, 255, 255));
            g2.setStroke(new BasicStroke(22, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawLine(0, 150, w, 150); // Tạ Quang Bửu
            g2.drawLine(0, 280, w, 280); // Đại Cồ Việt
            g2.drawLine(200, 0, 200, h); // Giải Phóng
            g2.drawLine(350, 0, 350, h); // Trần Đại Nghĩa

            g2.setColor(new Color(226, 232, 240));
            g2.setStroke(new BasicStroke(1));
            g2.drawLine(0, 150, w, 150);
            g2.drawLine(0, 280, w, 280);
            g2.drawLine(200, 0, 200, h);
            g2.drawLine(350, 0, 350, h);

            // Street labels
            g2.setColor(new Color(160, 174, 192));
            g2.setFont(Theme.FONT_CAPTION_BOLD);
            g2.drawString("ĐƯỜNG TẠ QUANG BỬU", 30, 142);
            g2.drawString("ĐƯỜNG GIẢI PHÓNG", 208, 40);
            g2.drawString("ĐƯỜNG TRẦN ĐẠI NGHĨA", 358, 70);

            // University Landmark Hub (HUST)
            g2.setColor(new Color(219, 234, 254));
            g2.fillRoundRect(220, 165, 110, 60, 14, 14);
            g2.setColor(new Color(30, 64, 175));
            g2.drawString("🏫 ĐH BÁCH KHOA", 228, 192);
            g2.setFont(Theme.FONT_CAPTION);
            g2.drawString("Cổng Parabol", 228, 208);

            // Radius Circle around HUST
            g2.setColor(new Color(59, 130, 246, 35));
            g2.fillOval(120, 65, 310, 260);
            g2.setColor(new Color(59, 130, 246, 90));
            g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{6}, 0));
            g2.drawOval(120, 65, 310, 260);

            // Draw Property Pins
            drawPin(g2, pin1.x, pin1.y, "4.2tr", "AVR-90412".equals(selectedRoom.getId()));
            drawPin(g2, pin2.x, pin2.y, "2.4tr", "AVR-89210".equals(selectedRoom.getId()));
            drawPin(g2, pin3.x, pin3.y, "4.0tr", "AVR-91045".equals(selectedRoom.getId()));

            g2.dispose();
        }

        private void drawPin(Graphics2D g2, int x, int y, String price, boolean isSelected) {
            Color pinBg = isSelected ? Theme.ACCENT_RED : Theme.PRIMARY_DARK;
            g2.setColor(pinBg);
            g2.fillRoundRect(x - 30, y - 18, 60, 28, 14, 14);

            // Pin pointer triangle
            int[] xPoints = {x - 5, x + 5, x};
            int[] yPoints = {y + 10, y + 10, y + 16};
            g2.fillPolygon(xPoints, yPoints, 3);

            g2.setColor(Theme.TEXT_WHITE);
            g2.setFont(Theme.FONT_BODY_BOLD);
            FontMetrics fm = g2.getFontMetrics();
            int tx = x - fm.stringWidth(price) / 2;
            int ty = y + 2;
            g2.drawString(price, tx, ty);
        }
    }
}
