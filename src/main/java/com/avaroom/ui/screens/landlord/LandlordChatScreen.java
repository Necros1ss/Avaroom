package com.avaroom.ui.screens.landlord;

import com.avaroom.models.ChatMessage;
import com.avaroom.navigation.NavigationController;
import com.avaroom.services.DataService;
import com.avaroom.theme.Theme;
import com.avaroom.ui.components.CardPanel;
import com.avaroom.ui.components.RoundedButton;
import com.avaroom.ui.components.StyledTextField;
import java.awt.*;
import java.util.List;
import javax.swing.*;

public class LandlordChatScreen extends JPanel {
    private final JPanel messagesContainer;
    private final StyledTextField inputField;
    private final JScrollPane scrollPane;

    public LandlordChatScreen() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_GRAY_LIGHT);

        // Header
        JPanel chatHeader = new JPanel(new BorderLayout());
        chatHeader.setBackground(Theme.BG_WHITE);
        chatHeader.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER_SUBTLE),
                BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));

        JPanel tenantInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        tenantInfo.setOpaque(false);

        JLabel avatar = new JLabel("🎓");
        avatar.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        tenantInfo.add(avatar);

        JPanel names = new JPanel();
        names.setLayout(new BoxLayout(names, BoxLayout.Y_AXIS));
        names.setOpaque(false);

        JLabel nameLabel = new JLabel("Nguyễn Thanh Tùng • Hỏi phòng #AVR-90412");
        nameLabel.setFont(Theme.FONT_BODY_BOLD);
        nameLabel.setForeground(Theme.PRIMARY_DARK);
        names.add(nameLabel);

        JLabel statusLabel = new JLabel("🟢 Đang hoạt động • SV ĐH Bách Khoa (Đã eKYC)");
        statusLabel.setFont(Theme.FONT_CAPTION);
        statusLabel.setForeground(Theme.ACCENT_GREEN);
        names.add(statusLabel);
        tenantInfo.add(names);

        JPanel rightActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightActions.setOpaque(false);

        RoundedButton callBtn = new RoundedButton("📞 0912 345 678", Theme.BG_GRAY_LIGHT, Theme.TEXT_PRIMARY, 10);
        callBtn.setFont(Theme.FONT_CAPTION);
        callBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Đang kết nối cuộc gọi tới sinh viên Nguyễn Thanh Tùng...", "Cuộc gọi thoại", JOptionPane.INFORMATION_MESSAGE));
        rightActions.add(callBtn);

        RoundedButton aptBtn = new RoundedButton("📅 Lịch hẹn (16:30)", Theme.SECONDARY_CREAM, Theme.PRIMARY_DARK, 10);
        aptBtn.setFont(Theme.FONT_CAPTION);
        aptBtn.addActionListener(e -> NavigationController.getInstance().navigateTo("landlord_appointments"));
        rightActions.add(aptBtn);

        chatHeader.add(tenantInfo, BorderLayout.WEST);
        chatHeader.add(rightActions, BorderLayout.EAST);
        add(chatHeader, BorderLayout.NORTH);

        // Messages area
        messagesContainer = new JPanel();
        messagesContainer.setLayout(new BoxLayout(messagesContainer, BoxLayout.Y_AXIS));
        messagesContainer.setBackground(Theme.BG_GRAY_LIGHT);
        messagesContainer.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        scrollPane = new JScrollPane(messagesContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // Quick responses & Input Bar
        JPanel bottomArea = new JPanel(new BorderLayout());
        bottomArea.setBackground(Theme.BG_WHITE);
        bottomArea.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDER_SUBTLE));

        inputField = new StyledTextField("Nhập tin nhắn trả lời sinh viên...");

        // Quick response chips
        JPanel quickChips = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        quickChips.setOpaque(false);
        String[] templates = {
                "Phòng vẫn còn em nhé!",
                "16:30 em qua xem thoải mái",
                "Điện 3.5k nước 100k/người",
                "Đến cổng gọi cô mở cửa"
        };
        for (String t : templates) {
            RoundedButton chip = new RoundedButton(t, Theme.BG_GRAY_LIGHT, Theme.TEXT_PRIMARY, 12);
            chip.setFont(Theme.FONT_CAPTION);
            chip.addActionListener(e -> {
                inputField.setText(t);
                sendMessage();
            });
            quickChips.add(chip);
        }
        bottomArea.add(quickChips, BorderLayout.NORTH);

        // Input bar
        JPanel inputBar = new JPanel(new BorderLayout(10, 0));
        inputBar.setOpaque(false);
        inputBar.setBorder(BorderFactory.createEmptyBorder(4, 20, 12, 20));

        inputBar.add(inputField, BorderLayout.CENTER);

        RoundedButton sendBtn = new RoundedButton("Gửi", Theme.PRIMARY_DARK, Theme.TEXT_WHITE, 12);
        sendBtn.setPreferredSize(new Dimension(80, 42));
        sendBtn.addActionListener(e -> sendMessage());
        inputField.addActionListener(e -> sendMessage());
        inputBar.add(sendBtn, BorderLayout.EAST);

        bottomArea.add(inputBar, BorderLayout.SOUTH);
        add(bottomArea, BorderLayout.SOUTH);

        refreshMessages();
    }

    private void sendMessage() {
        String txt = inputField.getText().trim();
        if (!txt.isEmpty()) {
            DataService.getInstance().addLandlordChatMessage(txt, true);
            inputField.setText("");
            refreshMessages();

            // Auto reply from student
            Timer timer = new Timer(1200, evt -> {
                DataService.getInstance().addLandlordChatMessage("Dạ vâng cô, cháu cảm ơn cô nhiều ạ!", false);
                refreshMessages();
            });
            timer.setRepeats(false);
            timer.start();
        }
    }

    private void refreshMessages() {
        messagesContainer.removeAll();
        List<ChatMessage> list = DataService.getInstance().getLandlordChatMessages();
        for (ChatMessage msg : list) {
            // For landlord view, if fromMe = true -> Host, else Tenant
            JPanel bubbleWrapper = new JPanel(new FlowLayout(msg.isFromMe() ? FlowLayout.RIGHT : FlowLayout.LEFT));
            bubbleWrapper.setOpaque(false);

            CardPanel bubble = new CardPanel(
                    14,
                    msg.isFromMe() ? Theme.PRIMARY_DARK : Theme.BG_WHITE,
                    !msg.isFromMe()
            );
            bubble.setLayout(new BoxLayout(bubble, BoxLayout.Y_AXIS));
            bubble.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));

            JLabel sender = new JLabel(msg.isFromMe() ? "Bạn (Chủ trọ)" : msg.getSender());
            sender.setFont(new Font("Segoe UI", Font.BOLD, 11));
            sender.setForeground(msg.isFromMe() ? Theme.SECONDARY_CREAM : Theme.TEXT_MUTED);
            bubble.add(sender);
            bubble.add(Box.createVerticalStrut(2));

            JLabel text = new JLabel("<html><body style='width: 260px;'>" + msg.getText() + "</body></html>");
            text.setFont(Theme.FONT_BODY);
            text.setForeground(msg.isFromMe() ? Theme.TEXT_WHITE : Theme.TEXT_PRIMARY);
            bubble.add(text);

            bubble.add(Box.createVerticalStrut(4));
            JLabel time = new JLabel(msg.getTime());
            time.setFont(Theme.FONT_CAPTION);
            time.setForeground(msg.isFromMe() ? new Color(200, 210, 220) : Theme.TEXT_MUTED);
            time.setAlignmentX(Component.RIGHT_ALIGNMENT);
            bubble.add(time);

            bubbleWrapper.add(bubble);
            messagesContainer.add(bubbleWrapper);
            messagesContainer.add(Box.createVerticalStrut(8));
        }
        messagesContainer.revalidate();
        messagesContainer.repaint();

        SwingUtilities.invokeLater(() -> {
            JScrollBar vertical = scrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });
    }
}
