package com.avaroom.ui;

import com.avaroom.navigation.NavigationController;
import com.avaroom.services.DataService;
import com.avaroom.theme.Theme;
import com.avaroom.ui.components.AppHeader;
import com.avaroom.ui.components.BottomNavDock;
import com.avaroom.ui.screens.auth.*;
import com.avaroom.ui.screens.landlord.*;
import com.avaroom.ui.screens.student.*;
import java.awt.*;
import java.util.Set;
import javax.swing.*;

public class MainWindow extends JFrame implements NavigationController.NavigationListener {
    private final CardLayout cardLayout;
    private final JPanel cardsPanel;
    private final AppHeader appHeader;
    private final BottomNavDock bottomNavDock;

    // Screens that hide header and bottom navigation (e.g. auth and onboarding)
    private static final Set<String> FULLSCREEN_AUTH_PAGES = Set.of(
            "onboarding1",
            "onboarding2",
            "onboarding3",
            "login",
            "register",
            "forgot_password",
            "otp_verification",
            "reset_password",
            "ekyc_id_card",
            "ekyc_verify_info"
    );

    // Screens that hide only the bottom navigation bar (e.g. wizards, detail, maps)
    private static final Set<String> HIDE_BOTTOM_DOCK_PAGES = Set.of(
            "room_detail",
            "post_detail",
            "create_post_step1",
            "create_post_step2",
            "create_post_step3",
            "create_post_step4",
            "stats_growth",
            "escrow_contract",
            "utility_billing",
            "asset_handover",
            "change_password",
            "edit_landlord_profile",
            "near_me_map",
            "reviews"
    );

    public MainWindow() {
        super("Avaroom - Nền tảng Thuê trọ & Ký quỹ Bảo chứng Hợp đồng Sinh viên");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 780);
        setMinimumSize(new Dimension(920, 640));
        setLocationRelativeTo(null);

        // Main Layout
        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(Theme.BG_WARM_CREAM);

        appHeader = new AppHeader();
        rootPanel.add(appHeader, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);
        cardsPanel.setBackground(Theme.BG_WARM_CREAM);
        rootPanel.add(cardsPanel, BorderLayout.CENTER);

        bottomNavDock = new BottomNavDock();
        rootPanel.add(bottomNavDock, BorderLayout.SOUTH);

        setContentPane(rootPanel);

        // Register with NavigationController
        NavigationController.getInstance().setNavigationListener(this);

        registerAllScreens();

        // Start on Onboarding 1
        NavigationController.getInstance().navigateTo("onboarding1");
    }

    private void registerScreen(String id, JPanel screen) {
        cardsPanel.add(screen, id.toLowerCase());
        NavigationController.getInstance().registerScreen(id, screen);
    }

    private void registerAllScreens() {
        // Auth screens
        registerScreen("onboarding1", new Onboarding1Screen());
        registerScreen("onboarding2", new Onboarding2Screen());
        registerScreen("onboarding3", new Onboarding3Screen());
        registerScreen("login", new LoginScreen());
        registerScreen("register", new RegisterScreen());
        registerScreen("forgot_password", new ForgotPasswordScreen());
        registerScreen("otp_verification", new OtpVerificationScreen());
        registerScreen("reset_password", new ResetPasswordScreen());
        registerScreen("ekyc_id_card", new EkycIdCardScreen());
        registerScreen("ekyc_verify_info", new EkycVerifyInfoScreen());

        // Student screens
        registerScreen("student_home", new StudentHomeScreen());
        registerScreen("search_result", new SearchResultScreen());
        registerScreen("room_detail", new RoomDetailScreen());
        registerScreen("student_appointments", new StudentAppointmentsScreen());
        registerScreen("near_me_map", new NearMeMapScreen());
        registerScreen("student_chat", new StudentChatScreen());
        registerScreen("saved_rooms", new SavedRoomsScreen());
        registerScreen("student_profile", new StudentProfileScreen());
        registerScreen("change_password", new ChangePasswordScreen());
        registerScreen("student_ekyc_profile", new StudentEkycProfileScreen());
        registerScreen("reviews", new ReviewsScreen());
        registerScreen("notifications", new NotificationsScreen());

        // Landlord screens
        registerScreen("landlord_dashboard", new LandlordDashboardScreen());
        registerScreen("create_post_step1", new CreatePostStep1Screen());
        registerScreen("create_post_step2", new CreatePostStep2Screen());
        registerScreen("create_post_step3", new CreatePostStep3Screen());
        registerScreen("create_post_step4", new CreatePostStep4Screen());
        registerScreen("post_detail", new PostDetailScreen());
        registerScreen("landlord_appointments", new LandlordAppointmentsScreen());
        registerScreen("landlord_chat", new LandlordChatScreen());
        registerScreen("stats_growth", new StatsGrowthScreen());
        registerScreen("landlord_reviews", new LandlordReviewsScreen());
        registerScreen("escrow_contract", new EscrowContractScreen());
        registerScreen("utility_billing", new UtilityBillingScreen());
        registerScreen("asset_handover", new AssetHandoverScreen());
        registerScreen("landlord_profile", new LandlordProfileScreen());
        registerScreen("edit_landlord_profile", new EditLandlordProfileScreen());
    }

    @Override
    public void onScreenChanged(String screenId, JPanel screenComponent, Object args) {
        String id = screenId.toLowerCase();
        cardLayout.show(cardsPanel, id);

        if (FULLSCREEN_AUTH_PAGES.contains(id)) {
            appHeader.setVisible(false);
            bottomNavDock.setVisible(false);
        } else {
            appHeader.setVisible(true);
            appHeader.setTitle(getScreenTitle(id));
            appHeader.updateState();

            if (HIDE_BOTTOM_DOCK_PAGES.contains(id)) {
                bottomNavDock.setVisible(false);
            } else {
                bottomNavDock.setVisible(true);
                bottomNavDock.updateRole();
            }
        }

        revalidate();
        repaint();
    }

    private String getScreenTitle(String id) {
        return switch (id) {
            case "student_home" -> "Trang chủ Sinh viên";
            case "search_result" -> "Kết quả tìm kiếm";
            case "room_detail" -> "Chi tiết phòng trọ";
            case "student_appointments" -> "Lịch hẹn xem phòng";
            case "near_me_map" -> "Bản đồ tìm trọ quanh trường";
            case "student_chat" -> "Tin nhắn với chủ trọ";
            case "saved_rooms" -> "Phòng đã lưu";
            case "student_profile" -> "Hồ sơ sinh viên";
            case "change_password" -> "Đổi mật khẩu";
            case "student_ekyc_profile" -> "Hồ sơ eKYC Cọc 0đ";
            case "reviews" -> "Đánh giá từ cộng đồng";
            case "notifications" -> "Thông báo";
            case "landlord_dashboard" -> "Quản lý phòng trọ";
            case "create_post_step1" -> "Đăng tin mới (Bước 1)";
            case "create_post_step2" -> "Tiện ích & Nội thất (Bước 2)";
            case "create_post_step3" -> "Ảnh 360° & Timestamp (Bước 3)";
            case "create_post_step4" -> "Hoàn tất đăng tin (Bước 4)";
            case "post_detail" -> "Chi tiết tin đăng";
            case "landlord_appointments" -> "Lịch hẹn khách thuê";
            case "landlord_chat" -> "Tương tác khách thuê";
            case "stats_growth" -> "Thống kê & Tăng trưởng";
            case "landlord_reviews" -> "Quản lý đánh giá";
            case "escrow_contract" -> "Hợp đồng & Ký quỹ Escrow";
            case "utility_billing" -> "Chốt điện nước & Hóa đơn";
            case "asset_handover" -> "Biên bản bàn giao 360°";
            case "landlord_profile" -> "Hồ sơ Chủ trọ";
            case "edit_landlord_profile" -> "Chỉnh sửa thông tin";
            default -> "Avaroom";
        };
    }
}
