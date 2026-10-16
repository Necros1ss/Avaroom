package com.avaroom.ui.screens.student;

import com.avaroom.models.ChatMessage;
import com.avaroom.services.DataService;
import com.avaroom.theme.Theme;
import com.avaroom.ui.components.CardPanel;
import com.avaroom.ui.components.RoundedButton;
import com.avaroom.ui.components.StyledTextField;
import java.awt.*;
import java.util.List;
import javax.swing.*;

public class StudentChatScreen extends JPanel {
    private final JPanel messagesContainer;
    private final StyledTextField inputField;
    private final JScrollPane scrollPane;

    public StudentChatScreen() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_GRAY_LIGHT);

        // Header of Chat Partner
        JPanel chatHeader = new JPanel(new BorderLayout());
        chatHeader.setBackground(Theme.BG_WHITE);
        chatHeader.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER_SUBTLE),
                BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));

        JPanel hostInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        hostInfo.setOpaque(false);

        JLabel avatar = new JLabel("👵");
        avatar.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        hostInfo.add(avatar);

        JPanel names = new JPanel();
        names.setLayout(new BoxLayout(names, BoxLayout.Y_AXIS));
        names.setOpaque(false);

        JLabel nameLabel = new JLabel("Cô Hoàng Thị Ánh Tuyết • Phòng #AVR-90412");
        nameLabel.setFont(Theme.FONT_BODY_BOLD);
        nameLabel.setForeground(Theme.PRIMARY_DARK);
        names.add(nameLabel);

        JLabel statusLabel = new JLabel("🟢 Đang hoạt động • Phản hồi trong 5 phút");
        statusLabel.setFont(Theme.FONT_CAPTION);
        statusLabel.setForeground(Theme.ACCENT_GREEN);
        names.add(statusLabel);
        hostInfo.add(names);

        chatHeader.add(hostInfo, BorderLayout.WEST);
        add(chatHeader, BorderLayout.NORTH);

        // Messages History Area
        messagesContainer = new JPanel();
        messagesContainer.setLayout(new BoxLayout(messagesContainer, BoxLayout.Y_AXIS));
        messagesContainer.setBackground(Theme.BG_GRAY_LIGHT);
        messagesContainer.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        scrollPane = new JScrollPane(messagesContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // Message Input Dock
        JPanel inputBar = new JPanel(new BorderLayout(10, 0));
        inputBar.setBackground(Theme.BG_WHITE);
        inputBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDER_SUBTLE),
                BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));

        inputField = new StyledTextField("Nhập tin nhắn cho cô Ánh Tuyết...");
        inputBar.add(inputField, BorderLayout.CENTER);

        RoundedButton sendBtn = new RoundedButton("Gửi", Theme.PRIMARY_DARK, Theme.TEXT_WHITE, 12);
        sendBtn.setPreferredSize(new Dimension(80, 42));
        sendBtn.addActionListener(e -> sendMessage());
        inputField.addActionListener(e -> sendMessage());
        inputBar.add(sendBtn, BorderLayout.EAST);

        add(inputBar, BorderLayout.SOUTH);

        refreshMessages();
    }

    private void sendMessage() {
        String txt = inputField.getText().trim();
        if (!txt.isEmpty()) {
            DataService.getInstance().addStudentChatMessage(txt, true);
            inputField.setText("");
            refreshMessages();

            // Simulate landlord automated reply after short delay
            Timer timer = new Timer(1000, evt -> {
                DataService.getInstance().addStudentChatMessage("Dạ cô nhận được tin nhắn của cháu rồi nhé!", false);
                refreshMessages();
            });
            timer.setRepeats(false);
            timer.start();
        }
    }

    private void refreshMessages() {
        messagesContainer.removeAll();
        List<ChatMessage> list = DataService.getInstance().getStudentChatMessages();
        for (ChatMessage msg : list) {
            JPanel bubbleWrapper = new JPanel(new FlowLayout(msg.isFromMe() ? FlowLayout.RIGHT : FlowLayout.LEFT));
            bubbleWrapper.setOpaque(false);

            CardPanel bubble = new CardPanel(
                    14,
                    msg.isFromMe() ? Theme.PRIMARY_DARK : Theme.BG_WHITE,
                    !msg.isFromMe()
            );
            bubble.setLayout(new BoxLayout(bubble, BoxLayout.Y_AXIS));
            bubble.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));

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
