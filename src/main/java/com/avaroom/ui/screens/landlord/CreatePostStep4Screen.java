package com.avaroom.ui.screens.landlord;

import com.avaroom.models.RoomModel;
import com.avaroom.navigation.NavigationController;
import com.avaroom.services.DataService;
import com.avaroom.theme.Theme;
import com.avaroom.ui.components.CardPanel;
import com.avaroom.ui.components.PillTag;
import com.avaroom.ui.components.RoundedButton;
import java.awt.*;
import javax.swing.*;

public class CreatePostStep4Screen extends JPanel {
    public CreatePostStep4Screen() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_GRAY_LIGHT);

        JPanel scrollContent = new JPanel();
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.setBackground(Theme.BG_GRAY_LIGHT);
        scrollContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel stepLabel = new JLabel("BƯỚC 4 / 4 • HOÀN TẤT & XUẤT BẢN");
        stepLabel.setFont(Theme.FONT_CAPTION_BOLD);
        stepLabel.setForeground(Theme.ACCENT_GREEN);
        scrollContent.add(stepLabel);

        scrollContent.add(Box.createVerticalStrut(4));
        JLabel title = new JLabel("Xem trước tin đăng trước khi xuất bản");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.PRIMARY_DARK);
        scrollContent.add(title);

        scrollContent.add(Box.createVerticalStrut(18));

        // Preview Card
        CardPanel previewCard = new CardPanel(18, Theme.BG_WHITE, true);
        previewCard.setLayout(new BoxLayout(previewCard, BoxLayout.Y_AXIS));
        previewCard.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        previewCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));

        JPanel tagsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        tagsRow.setOpaque(false);
        tagsRow.add(new PillTag("✓ Đã xác thực Timestamp", Theme.ACCENT_GREEN_LIGHT, Theme.ACCENT_GREEN, null));
        tagsRow.add(new PillTag("28m²"));
        tagsRow.add(new PillTag("Chính chủ"));
        previewCard.add(tagsRow);

        previewCard.add(Box.createVerticalStrut(10));
        JLabel previewTitle = new JLabel("Studio ban công thoáng mát gần ĐH Bách Khoa");
        previewTitle.setFont(Theme.FONT_SUBTITLE);
        previewTitle.setForeground(Theme.PRIMARY_DARK);
        previewCard.add(previewTitle);

        previewCard.add(Box.createVerticalStrut(6));
        JLabel previewAddr = new JLabel("📍 Ngõ 30 Tạ Quang Bửu, P. Bách Khoa, Hai Bà Trưng, Hà Nội");
        previewAddr.setFont(Theme.FONT_BODY);
        previewAddr.setForeground(Theme.TEXT_SECONDARY);
        previewCard.add(previewAddr);

        previewCard.add(Box.createVerticalStrut(12));
        JLabel previewPrice = new JLabel("4.200.000 đ / tháng");
        previewPrice.setFont(Theme.FONT_TITLE_LARGE);
        previewPrice.setForeground(Theme.ACCENT_RED);
        previewCard.add(previewPrice);

        scrollContent.add(previewCard);
        scrollContent.add(Box.createVerticalStrut(20));

        // Terms and verification commitment
        JCheckBox agreeCheck = new JCheckBox("Tôi cam kết hình ảnh, tiện ích và giá điện nước minh bạch đúng thực tế 100%.", true);
        agreeCheck.setFont(Theme.FONT_BODY);
        agreeCheck.setForeground(Theme.TEXT_PRIMARY);
        agreeCheck.setOpaque(false);
        scrollContent.add(agreeCheck);

        scrollContent.add(Box.createVerticalStrut(24));

        RoundedButton publishBtn = new RoundedButton("XUẤT BẢN TIN ĐĂNG NGAY", Theme.PRIMARY_DARK, Theme.TEXT_WHITE, 16);
        publishBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        publishBtn.addActionListener(e -> {
            RoomModel newRoom = new RoomModel(
                    "AVR-" + (System.currentTimeMillis() % 100000),
                    "Studio ban công thoáng mát gần ĐH Bách Khoa",
                    "Ngõ 30 Tạ Quang Bửu, Hai Bà Trưng, Hà Nội",
                    "ĐH Bách Khoa",
                    250,
                    4200000L,
                    28,
                    5.0,
                    1,
                    DataService.getInstance().getLandlordName(),
                    DataService.getInstance().getLandlordPhone(),
                    true,
                    "Còn trống"
            );
            DataService.getInstance().addRoom(newRoom);
            JOptionPane.showMessageDialog(this,
                    "Chúc mừng! Tin đăng của bạn đã được xuất bản thành công lên hệ thống Avaroom.",
                    "Avaroom Chủ Trọ",
                    JOptionPane.INFORMATION_MESSAGE);
            NavigationController.getInstance().navigateTo("LANDLORD_DASHBOARD");
        });
        scrollContent.add(publishBtn);

        JScrollPane scrollPane = new JScrollPane(scrollContent);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }
}
