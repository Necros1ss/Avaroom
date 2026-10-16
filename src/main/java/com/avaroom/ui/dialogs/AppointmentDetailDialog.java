package com.avaroom.ui.dialogs;

import com.avaroom.models.AppointmentModel;
import com.avaroom.navigation.NavigationController;
import com.avaroom.theme.Theme;
import com.avaroom.ui.components.CardPanel;
import com.avaroom.ui.components.PillTag;
import com.avaroom.ui.components.RoundedButton;
import java.awt.*;
import javax.swing.*;

public class AppointmentDetailDialog extends JDialog {
    public AppointmentDetailDialog(Window owner, AppointmentModel apt) {
        super(owner, "Chi tiết lịch hẹn xem phòng", ModalityType.APPLICATION_MODAL);
        setSize(460, 520);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Theme.BG_WHITE);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.BG_WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER_SUBTLE),
                BorderFactory.createEmptyBorder(16, 20, 16, 20)
        ));

        JLabel title = new JLabel("Chi tiết lịch hẹn #" + apt.getId());
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.PRIMARY_DARK);
        header.add(title, BorderLayout.WEST);

        JButton closeBtn = new JButton("✕");
        closeBtn.setFont(Theme.FONT_TITLE);
        closeBtn.setForeground(Theme.TEXT_SECONDARY);
        closeBtn.setFocusPainted(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> dispose());
        header.add(closeBtn, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // Content
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Theme.BG_WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        CardPanel studentCard = new CardPanel(14, Theme.BG_CHIP, false);
        studentCard.setLayout(new BorderLayout(12, 0));
        studentCard.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        studentCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel avatar = new JLabel("🎓");
        avatar.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        studentCard.add(avatar, BorderLayout.WEST);

        JPanel sInfo = new JPanel();
        sInfo.setLayout(new BoxLayout(sInfo, BoxLayout.Y_AXIS));
        sInfo.setOpaque(false);

        JLabel sName = new JLabel(apt.getStudentName());
        sName.setFont(Theme.FONT_BODY_BOLD);
        sName.setForeground(Theme.PRIMARY_DARK);
        sInfo.add(sName);

        JLabel sMeta = new JLabel("SĐT: " + apt.getStudentPhone() + " • ĐH Bách Khoa K66");
        sMeta.setFont(Theme.FONT_CAPTION);
        sMeta.setForeground(Theme.TEXT_SECONDARY);
        sInfo.add(sMeta);

        studentCard.add(sInfo, BorderLayout.CENTER);
        content.add(studentCard);

        content.add(Box.createVerticalStrut(18));

        addDetailRow(content, "Phòng đăng ký:", apt.getRoomTitle());
        addDetailRow(content, "Thời gian hẹn:", apt.getTimeSlot() + " ngày " + apt.getDate());
        addDetailRow(content, "Trạng thái:", apt.getStatus());
        addDetailRow(content, "Lời nhắn sinh viên:", "\"" + apt.getNote() + "\"");

        add(content, BorderLayout.CENTER);

        // Actions Bottom Bar
        JPanel bottomBar = new JPanel(new GridLayout(1, 2, 10, 0));
        bottomBar.setBackground(Theme.BG_WHITE);
        bottomBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDER_SUBTLE),
                BorderFactory.createEmptyBorder(12, 20, 16, 20)
        ));

        RoundedButton chatBtn = new RoundedButton("💬 Nhắn tin", Theme.BG_WHITE, Theme.PRIMARY_DARK, 12);
        chatBtn.setOutlineOnly(true);
        chatBtn.addActionListener(e -> {
            dispose();
            NavigationController.getInstance().navigateTo("LANDLORD_CHAT");
        });
        bottomBar.add(chatBtn);

        RoundedButton acceptBtn = new RoundedButton("✓ Xác nhận hẹn", Theme.PRIMARY_DARK, Theme.TEXT_WHITE, 12);
        acceptBtn.addActionListener(e -> {
            apt.setStatus("Đã xác nhận");
            dispose();
            JOptionPane.showMessageDialog(owner, "Đã gửi thông báo xác nhận lịch hẹn cho sinh viên!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            NavigationController.getInstance().navigateTo("LANDLORD_APPOINTMENTS");
        });
        bottomBar.add(acceptBtn);

        add(bottomBar, BorderLayout.SOUTH);
    }

    private void addDetailRow(JPanel p, String label, String value) {
        JPanel r = new JPanel(new BorderLayout());
        r.setOpaque(false);
        r.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));

        JLabel l = new JLabel(label);
        l.setFont(Theme.FONT_CAPTION_BOLD);
        l.setForeground(Theme.TEXT_SECONDARY);

        JLabel v = new JLabel("<html><b>" + value + "</b></html>");
        v.setFont(Theme.FONT_BODY);
        v.setForeground(Theme.PRIMARY_DARK);

        r.add(l, BorderLayout.WEST);
        r.add(v, BorderLayout.EAST);
        p.add(r);
    }
}
