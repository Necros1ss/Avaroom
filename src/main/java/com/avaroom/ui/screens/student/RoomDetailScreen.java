package com.avaroom.ui.screens.student;

import com.avaroom.models.RoomModel;
import com.avaroom.navigation.NavigationController;
import com.avaroom.services.DataService;
import com.avaroom.theme.Theme;
import com.avaroom.ui.components.CardPanel;
import com.avaroom.ui.components.PillTag;
import com.avaroom.ui.components.RoundedButton;
import com.avaroom.ui.dialogs.BookingViewingDialog;
import java.awt.*;
import javax.swing.*;

public class RoomDetailScreen extends JPanel {
    private RoomModel currentRoom;

    public RoomDetailScreen() {
        this("AVR-90412");
    }

    public RoomDetailScreen(String roomId) {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_GRAY_LIGHT);

        this.currentRoom = DataService.getInstance().getRoomById(roomId);
        if (currentRoom == null) {
            currentRoom = DataService.getInstance().getAllRooms().get(0);
        }

        JPanel scrollContent = new JPanel();
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.setBackground(Theme.BG_GRAY_LIGHT);
        scrollContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Hero Preview Box
        CardPanel heroCard = new CardPanel(20, Theme.PRIMARY_DARK, false);
        heroCard.setLayout(new BorderLayout());
        heroCard.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        heroCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        heroCard.setPreferredSize(new Dimension(0, 200));

        JPanel heroTop = new JPanel(new BorderLayout());
        heroTop.setOpaque(false);
        heroTop.add(new PillTag("✓ Đã xác thực eKYC 100%", Theme.ACCENT_GREEN, Theme.TEXT_WHITE, null), BorderLayout.WEST);
        heroTop.add(new PillTag("1 / 8 Ảnh", Theme.PRIMARY_BLACK, Theme.TEXT_WHITE, null), BorderLayout.EAST);
        heroCard.add(heroTop, BorderLayout.NORTH);

        JLabel heroCenter = new JLabel("<html><center>📷 Video 360° & Ảnh chụp thực tế<br><font size='3' color='#A0AEC0'>Đã kiểm định phòng chính chủ tại chỗ</font></center></html>");
        heroCenter.setFont(Theme.FONT_SUBTITLE);
        heroCenter.setForeground(Theme.TEXT_WHITE);
        heroCenter.setHorizontalAlignment(SwingConstants.CENTER);
        heroCard.add(heroCenter, BorderLayout.CENTER);

        scrollContent.add(heroCard);
        scrollContent.add(Box.createVerticalStrut(18));

        // Title & Location
        JLabel title = new JLabel(currentRoom.getTitle());
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.PRIMARY_DARK);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollContent.add(title);

        scrollContent.add(Box.createVerticalStrut(6));
        JLabel addr = new JLabel("📍 " + currentRoom.getAddress());
        addr.setFont(Theme.FONT_BODY);
        addr.setForeground(Theme.TEXT_SECONDARY);
        addr.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollContent.add(addr);

        scrollContent.add(Box.createVerticalStrut(14));

        // Price Row
        JPanel priceRow = new JPanel(new BorderLayout());
        priceRow.setOpaque(false);
        priceRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        priceRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel priceLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        priceLeft.setOpaque(false);
        JLabel p = new JLabel(currentRoom.getFormattedPrice());
        p.setFont(Theme.FONT_TITLE_LARGE);
        p.setForeground(Theme.ACCENT_RED);
        priceLeft.add(p);

        JLabel m = new JLabel("/ tháng");
        m.setFont(Theme.FONT_BODY);
        m.setForeground(Theme.TEXT_SECONDARY);
        priceLeft.add(m);
        priceRow.add(priceLeft, BorderLayout.WEST);

        priceRow.add(new PillTag("Còn 1 phòng duy nhất", Theme.ACCENT_RED_LIGHT, Theme.ACCENT_RED, null), BorderLayout.EAST);
        scrollContent.add(priceRow);

        scrollContent.add(Box.createVerticalStrut(18));

        // 3 Key Specs Cards Row
        JPanel specsRow = new JPanel(new GridLayout(1, 3, 10, 0));
        specsRow.setOpaque(false);
        specsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        specsRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        specsRow.add(createMiniSpecCard(currentRoom.getAreaM2() + " m²", "Diện tích"));
        specsRow.add(createMiniSpecCard("Gác lửng", "Cao 2.0m"));
        specsRow.add(createMiniSpecCard("Tự do 24/7", "Khóa vân tay"));
        scrollContent.add(specsRow);

        scrollContent.add(Box.createVerticalStrut(24));

        // Transparent Bill Table Card
        JLabel billTitle = new JLabel("Biểu phí sinh hoạt minh bạch (Cam kết đúng hợp đồng)");
        billTitle.setFont(Theme.FONT_SUBTITLE);
        billTitle.setForeground(Theme.PRIMARY_DARK);
        billTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollContent.add(billTitle);

        scrollContent.add(Box.createVerticalStrut(10));

        CardPanel billCard = new CardPanel(16, Theme.BG_WHITE, true);
        billCard.setLayout(new BoxLayout(billCard, BoxLayout.Y_AXIS));
        billCard.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        billCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));
        billCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        addBillRow(billCard, "⚡ Tiền điện", "3.500 đ / kWh");
        addBillRow(billCard, "💧 Tiền nước sinh hoạt", "100.000 đ / người");
        addBillRow(billCard, "📶 Internet Wifi Mesh", "100.000 đ / phòng");
        addBillRow(billCard, "🛵 Gửi xe & sạc xe điện an toàn", "120.000 đ / xe");
        addBillRow(billCard, "🧹 Vệ sinh chung & rác", "50.000 đ / người");

        scrollContent.add(billCard);
        scrollContent.add(Box.createVerticalStrut(24));

        // University Hub Proximity Card
        JLabel uniProxTitle = new JLabel("Vị trí & Trường Đại học lân cận");
        uniProxTitle.setFont(Theme.FONT_SUBTITLE);
        uniProxTitle.setForeground(Theme.PRIMARY_DARK);
        uniProxTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollContent.add(uniProxTitle);

        scrollContent.add(Box.createVerticalStrut(10));

        CardPanel uniCard = new CardPanel(16, Theme.BG_WHITE, true);
        uniCard.setLayout(new BoxLayout(uniCard, BoxLayout.Y_AXIS));
        uniCard.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        uniCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        uniCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        addBillRow(uniCard, "🏫 ĐH Bách Khoa (Cổng Parabol)", "Cách 250m (3 phút đi bộ)");
        addBillRow(uniCard, "🏫 ĐH Xây Dựng (NUCE)", "Cách 400m (5 phút đi bộ)");
        addBillRow(uniCard, "🏫 ĐH Kinh Tế Quốc Dân (NEU)", "Cách 600m (2 phút xe máy)");
        addBillRow(uniCard, "🚌 Điểm đón Bus 08A, 18, 31", "Cách 100m đầu ngõ");

        scrollContent.add(uniCard);
        scrollContent.add(Box.createVerticalStrut(24));

        // Host Info Card
        JLabel hostTitle = new JLabel("THÔNG TIN CHỦ NHÀ CHÍNH THỨC");
        hostTitle.setFont(Theme.FONT_CAPTION_BOLD);
        hostTitle.setForeground(Theme.TEXT_SECONDARY);
        hostTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollContent.add(hostTitle);

        scrollContent.add(Box.createVerticalStrut(8));

        CardPanel hostCard = new CardPanel(16, Theme.BG_WHITE, true);
        hostCard.setLayout(new BorderLayout(14, 0));
        hostCard.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        hostCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        hostCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel avatar = new JLabel("👵");
        avatar.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        hostCard.add(avatar, BorderLayout.WEST);

        JPanel hostDetails = new JPanel();
        hostDetails.setLayout(new BoxLayout(hostDetails, BoxLayout.Y_AXIS));
        hostDetails.setOpaque(false);

        JLabel hostName = new JLabel(currentRoom.getHostName());
        hostName.setFont(Theme.FONT_BODY_BOLD);
        hostName.setForeground(Theme.PRIMARY_DARK);
        hostDetails.add(hostName);

        JLabel hostMeta = new JLabel("⭐ 4.9 • 38 đánh giá sinh viên • Tỷ lệ phản hồi 98% (trong 5 phút)");
        hostMeta.setFont(Theme.FONT_CAPTION);
        hostMeta.setForeground(Theme.TEXT_SECONDARY);
        hostDetails.add(hostMeta);
        hostCard.add(hostDetails, BorderLayout.CENTER);

        scrollContent.add(hostCard);
        scrollContent.add(Box.createVerticalStrut(20));

        JScrollPane scrollPane = new JScrollPane(scrollContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // Sticky Bottom Action Dock
        JPanel bottomDock = new JPanel(new GridLayout(1, 2, 12, 0));
        bottomDock.setBackground(Theme.BG_WHITE);
        bottomDock.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDER_SUBTLE),
                BorderFactory.createEmptyBorder(12, 20, 14, 20)
        ));
        bottomDock.setPreferredSize(new Dimension(0, 72));

        RoundedButton chatBtn = new RoundedButton("💬 Nhắn chủ trọ", Theme.BG_WHITE, Theme.PRIMARY_DARK, 14);
        chatBtn.setOutlineOnly(true);
        chatBtn.addActionListener(e -> NavigationController.getInstance().navigateTo("STUDENT_CHAT"));
        bottomDock.add(chatBtn);

        RoundedButton bookBtn = new RoundedButton("📅 Đặt lịch xem phòng", Theme.PRIMARY_DARK, Theme.TEXT_WHITE, 14);
        bookBtn.addActionListener(e -> {
            Window parentWindow = SwingUtilities.getWindowAncestor(this);
            BookingViewingDialog dialog = new BookingViewingDialog(parentWindow, currentRoom);
            dialog.setVisible(true);
        });
        bottomDock.add(bookBtn);

        add(bottomDock, BorderLayout.SOUTH);
    }

    private JPanel createMiniSpecCard(String topText, String botText) {
        CardPanel p = new CardPanel(14, Theme.BG_WHITE, true);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(10, 8, 10, 8));

        JLabel t = new JLabel(topText);
        t.setFont(Theme.FONT_BODY_BOLD);
        t.setForeground(Theme.PRIMARY_DARK);
        t.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(t);

        p.add(Box.createVerticalStrut(4));
        JLabel b = new JLabel(botText);
        b.setFont(Theme.FONT_CAPTION);
        b.setForeground(Theme.TEXT_SECONDARY);
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(b);

        return p;
    }

    private void addBillRow(JPanel container, String item, String fee) {
        JPanel r = new JPanel(new BorderLayout());
        r.setOpaque(false);
        r.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        JLabel i = new JLabel(item);
        i.setFont(Theme.FONT_BODY);
        i.setForeground(Theme.TEXT_PRIMARY);

        JLabel f = new JLabel("<html><b>" + fee + "</b></html>");
        f.setFont(Theme.FONT_BODY);
        f.setForeground(Theme.PRIMARY_DARK);

        r.add(i, BorderLayout.WEST);
        r.add(f, BorderLayout.EAST);
        container.add(r);
    }
}
