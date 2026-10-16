package com.avaroom.ui.screens.landlord;

import com.avaroom.navigation.NavigationController;
import com.avaroom.services.DataService;
import com.avaroom.theme.Theme;
import com.avaroom.ui.components.CardPanel;
import com.avaroom.ui.components.RoundedButton;
import com.avaroom.ui.components.StyledTextField;
import java.awt.*;
import javax.swing.*;

public class EditLandlordProfileScreen extends JPanel {
    private final StyledTextField nameField;
    private final StyledTextField phoneField;
    private final StyledTextField emailField;
    private final StyledTextField cccdField;
    private final JComboBox<String> bankCombo;
    private final StyledTextField accountField;

    public EditLandlordProfileScreen() {
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

        JLabel title = new JLabel("Chỉnh sửa thông tin Chủ trọ");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.PRIMARY_DARK);
        left.add(title);
        header.add(left, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // Content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Theme.BG_GRAY_LIGHT);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 24, 24, 24));

        CardPanel formCard = new CardPanel(14, Theme.BG_WHITE, true);
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        formCard.setMaximumSize(new Dimension(700, 520));

        // Name
        formCard.add(createFieldLabel("Họ và tên chủ trọ:"));
        nameField = new StyledTextField(DataService.getInstance().getLandlordName());
        formCard.add(nameField);
        formCard.add(Box.createVerticalStrut(14));

        // Phone
        formCard.add(createFieldLabel("Số điện thoại liên hệ:"));
        phoneField = new StyledTextField(DataService.getInstance().getLandlordPhone());
        formCard.add(phoneField);
        formCard.add(Box.createVerticalStrut(14));

        // Email
        formCard.add(createFieldLabel("Email nhận thông báo doanh thu:"));
        emailField = new StyledTextField("anhtuyet.avahost@gmail.com");
        formCard.add(emailField);
        formCard.add(Box.createVerticalStrut(14));

        // CCCD
        formCard.add(createFieldLabel("Số Căn cước công dân (Đã eKYC):"));
        cccdField = new StyledTextField("001185012345");
        cccdField.setEditable(false);
        formCard.add(cccdField);
        formCard.add(Box.createVerticalStrut(14));

        // Bank
        formCard.add(createFieldLabel("Ngân hàng thụ hưởng tiền trọ:"));
        String[] banks = {"MB Bank (Quân Đội)", "Vietcombank", "Techcombank", "BIDV", "Agribank", "ACB"};
        bankCombo = new JComboBox<>(banks);
        bankCombo.setFont(Theme.FONT_BODY);
        bankCombo.setBackground(Theme.BG_WHITE);
        bankCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        formCard.add(bankCombo);
        formCard.add(Box.createVerticalStrut(14));

        // Account number
        formCard.add(createFieldLabel("Số tài khoản ngân hàng:"));
        accountField = new StyledTextField("0988765432");
        formCard.add(accountField);
        formCard.add(Box.createVerticalStrut(24));

        // Buttons
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        btnRow.setOpaque(false);

        RoundedButton cancelBtn = new RoundedButton("Hủy bỏ", Theme.BG_GRAY_LIGHT, Theme.TEXT_PRIMARY, 10);
        cancelBtn.setPreferredSize(new Dimension(100, 42));
        cancelBtn.addActionListener(e -> NavigationController.getInstance().goBack());
        btnRow.add(cancelBtn);

        RoundedButton saveBtn = new RoundedButton("Lưu thay đổi", Theme.PRIMARY_DARK, Theme.TEXT_WHITE, 10);
        saveBtn.setPreferredSize(new Dimension(140, 42));
        saveBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            if (name.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            DataService.getInstance().setLandlordName(name);
            DataService.getInstance().setLandlordPhone(phone);
            DataService.getInstance().setLandlordBank((String) bankCombo.getSelectedItem() + " - " + accountField.getText().trim());

            JOptionPane.showMessageDialog(this, "Đã cập nhật thông tin chủ trọ thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            NavigationController.getInstance().goBack();
        });
        btnRow.add(saveBtn);
        formCard.add(btnRow);

        contentPanel.add(formCard);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JLabel createFieldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(Theme.FONT_BODY_BOLD);
        lbl.setForeground(Theme.TEXT_PRIMARY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
        return lbl;
    }
}
