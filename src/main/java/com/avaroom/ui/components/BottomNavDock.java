package com.avaroom.ui.components;

import com.avaroom.navigation.NavigationController;
import com.avaroom.services.DataService;
import com.avaroom.theme.Theme;
import java.awt.*;
import javax.swing.*;

public class BottomNavDock extends JPanel {
    private final JButton tab1;
    private final JButton tab2;
    private final JButton tab3;
    private final JButton tab4;

    public BottomNavDock() {
        setLayout(new GridLayout(1, 4, 8, 0));
        setBackground(Theme.BG_WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDER_SUBTLE),
                BorderFactory.createEmptyBorder(6, 16, 6, 16)
        ));
        setPreferredSize(new Dimension(0, 58));

        tab1 = createNavButton("🏠", "Trang chủ");
        tab2 = createNavButton("🗺️", "Lân cận");
        tab3 = createNavButton("💬", "Tin nhắn");
        tab4 = createNavButton("👤", "Tài khoản");

        add(tab1);
        add(tab2);
        add(tab3);
        add(tab4);

        updateRole();
    }

    private JButton createNavButton(String icon, String label) {
        JButton btn = new JButton("<html><center>" + icon + "<br><font size='2'>" + label + "</font></center></html>");
        btn.setFont(Theme.FONT_CAPTION_BOLD);
        btn.setForeground(Theme.TEXT_SECONDARY);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public void updateRole() {
        String role = DataService.getInstance().getCurrentUserRole();
        // Remove existing listeners
        for (var l : tab1.getActionListeners()) tab1.removeActionListener(l);
        for (var l : tab2.getActionListeners()) tab2.removeActionListener(l);
        for (var l : tab3.getActionListeners()) tab3.removeActionListener(l);
        for (var l : tab4.getActionListeners()) tab4.removeActionListener(l);

        if ("LANDLORD".equals(role)) {
            tab1.setText("<html><center>📋<br><font size='2'>Tin đăng</font></center></html>");
            tab2.setText("<html><center>📅<br><font size='2'>Lịch hẹn</font></center></html>");
            tab3.setText("<html><center>💬<br><font size='2'>Tin nhắn</font></center></html>");
            tab4.setText("<html><center>👤<br><font size='2'>Hồ sơ</font></center></html>");

            tab1.addActionListener(e -> NavigationController.getInstance().navigateTo("LANDLORD_DASHBOARD"));
            tab2.addActionListener(e -> NavigationController.getInstance().navigateTo("LANDLORD_APPOINTMENTS"));
            tab3.addActionListener(e -> NavigationController.getInstance().navigateTo("LANDLORD_CHAT"));
            tab4.addActionListener(e -> NavigationController.getInstance().navigateTo("LANDLORD_PROFILE"));
        } else {
            tab1.setText("<html><center>🏠<br><font size='2'>Trang chủ</font></center></html>");
            tab2.setText("<html><center>🗺️<br><font size='2'>Lân cận</font></center></html>");
            tab3.setText("<html><center>💬<br><font size='2'>Tin nhắn</font></center></html>");
            tab4.setText("<html><center>👤<br><font size='2'>Tài khoản</font></center></html>");

            tab1.addActionListener(e -> NavigationController.getInstance().navigateTo("STUDENT_HOME"));
            tab2.addActionListener(e -> NavigationController.getInstance().navigateTo("NEAR_ME_MAP"));
            tab3.addActionListener(e -> NavigationController.getInstance().navigateTo("STUDENT_CHAT"));
            tab4.addActionListener(e -> NavigationController.getInstance().navigateTo("STUDENT_PROFILE"));
        }
        revalidate();
        repaint();
    }
}
