# UI AUDIT REPORT — AVAROOM JAVA SWING APPLICATION

**Figma Design Source:** [Avaroom Design System (Figma)](https://www.figma.com/design/DiHZEesg49LjjcnbgmGttP/Avaroom?node-id=0-1&t=K9tqguRreYL5uV03-1)  
**Implementation Technology:** Pure Java Desktop (Java Swing & AWT, Java 17, Gradle)  
**Audit Date:** 2026-10-16  
**Auditor:** Senior Java Desktop Application Engineer & UI Specialist

---

## 1. Executive Summary

This report provides a comprehensive screen-by-screen audit comparing the Figma design specification (`DiHZEesg49LjjcnbgmGttP`) against the Java Native desktop implementation for **Avaroom**, an accommodation finding & rental platform for students and landlords.

The application spans three core functional domains:
1. **Onboarding, Authentication & eKYC Flow**
2. **Student Experience (Sinh viên)**
3. **Landlord Portal & Property Management (Chủ trọ)**

All screens are implemented as responsive, high-fidelity Java Swing desktop panels and dialogs conforming strictly to Java Swing/AWT without external prohibited UI frameworks.

---

## 2. Screen-by-Screen Traceability Matrix

| # | Figma page/frame | Java implementation | Status | Visual issues & Design specs | Functional issues & Prototype flow | Required changes | Priority | Verification |
|---|---|---|---|---|---|---|---|---|
| 1 | Onboarding 1 - Tìm trọ gần trường (Warm Cream) | `Onboarding1Screen.java` | Complete | Warm cream background (`#FDF9F2`), student illustration card, university proximity badge (850m ĐHBK), bus route pills, price badge (từ 2.8tr) | Button "Tiếp tục" navigates to Onboarding 2 | Full custom card layout & navigation | High | Passed |
| 2 | Onboarding 2 - Minh bạch giá & Ảnh thật (Warm Cream) | `Onboarding2Screen.java` | Complete | Pricing transparency card (`3.200.000 đ/th`), utility cost breakdown (Điện 3.5k, Nước 50k), verified photo badges | Button "Tiếp tục" navigates to Onboarding 3 | Custom pricing preview card & transitions | High | Passed |
| 3 | Onboarding 3 - Đăng nhập & Đăng ký (Warm Cream) | `Onboarding3Screen.java` | Complete | Community trust statistics (4.9⭐, 24k+ SV), dual CTA buttons: Primary Register, Secondary Sign In | Register CTA -> `RegisterScreen`, Sign In CTA -> `LoginScreen` | Dual button layout and navigation hooks | High | Passed |
| 4 | Đăng nhập - Avaroom (Warm Cream) | `LoginScreen.java` | Complete | Warm Cream header, Account/Email input, Password input with eye toggle, Remember Me checkbox, Forgot password link, Dark primary CTA | Primary button navigates to `StudentHomeScreen`; Forgot link -> `ForgotPasswordScreen`; Register link -> `RegisterScreen` | Custom text fields, remember-me state, validation | Critical | Passed |
| 5 | Đăng ký - Avaroom (Warm Cream) | `RegisterScreen.java` | Complete | Role switcher tab (Sinh viên vs. Chủ trọ), Full Name field, Phone number, Password, Terms agreement checkbox | Student submit -> OTP; Landlord submit -> eKYC ID verification; Back -> Login | Role switch toggles landlord form mode | Critical | Passed |
| 6 | Đăng ký - Chủ trọ (Warm Cream) | `RegisterScreen.java` | Complete | Landlord registration variant with property count and address context | Active Landlord role switch -> navigates to Landlord eKYC | Integrated role switch with live visual updates | High | Passed |
| 7 | Form nhập email lấy lại mật khẩu | `ForgotPasswordScreen.java` | Complete | Email recovery input field, instruction subtitle, "GỬI MÃ XÁC THỰC" primary CTA | Submit -> `OtpVerificationScreen`; Back -> `LoginScreen` | Input validation & transition | Medium | Passed |
| 8 | Xác thực OTP - Avaroom (Warm Cream) | `OtpVerificationScreen.java` | Complete | 6-digit centered OTP boxes, timer countdown (60s), Resend OTP link | Submit -> `ResetPasswordScreen`; Back -> Forgot Password | 6-box input handling & verification action | Medium | Passed |
| 9 | Đặt mật khẩu mới - Avaroom (Warm Cream) | `ResetPasswordScreen.java` | Complete | New password & Confirm password inputs with 8-character requirements indicator | Submit -> Auto-login to `StudentHomeScreen`; Back -> OTP | Password match verification | Medium | Passed |
| 10 | Xác thực CCCD 2 mặt - Avaroom | `EkycIdCardScreen.java` | Complete | Front ID card capture frame + Back ID card capture frame, instruction notes, guidelines | Front/Back capture triggers simulated capture & info review -> `EkycVerifyInfoScreen` | Custom image/placeholder capture cards | High | Passed |
| 11 | Chụp CCCD Mặt trước & Hướng dẫn | `EkycIdCardScreen.java` | Complete | Detailed guide on 4 corners, no glare, valid chip card | Integrated modal guidance | Captured state preview | Medium | Passed |
| 12 | Chụp CCCD Mặt sau & Hướng dẫn | `EkycIdCardScreen.java` | Complete | Detailed guide on MRZ code, fingerprint & identification features | Integrated modal guidance | Captured state preview | Medium | Passed |
| 13 | Kiểm tra thông tin CCCD - Avaroom | `EkycVerifyInfoScreen.java` | Complete | Extracted OCR data: Full name, DOB, ID number, Address; Edit and Confirm CTA | Confirm -> `LandlordDashboardScreen` or `StudentHomeScreen` | Data confirmation form & completion routing | High | Passed |
| 14 | Trang chủ - Tìm trọ sinh viên | `StudentHomeScreen.java` | Complete | App brand header, notification bell, greeting with university hub, search bar, filter button, quick filter chips (gần trường, <2.5tr, gác lửng, giờ tự do), property feed cards | Property card click -> `RoomDetailScreen`; Filter button -> `FilterDialog`; Nav items -> Map, Chat, Account | High-fidelity scrollable feed with property cards & badges | Critical | Passed |
| 15 | Bộ lọc tìm kiếm nâng cao - Avaroom | `FilterDialog.java` | Complete | Target university selection, radius slider (<500m, <1km, <2km, <3km), budget range chips (<2.5tr, 2.5-4tr, >4tr), room type checkboxes, amenities checklist | "Áp dụng bộ lọc" -> `SearchResultScreen`; "Đặt lại" -> reset defaults | Modal dialog with interactive criteria | High | Passed |
| 16 | Search Result | `SearchResultScreen.java` | Complete | Search query breadcrumb, active filter count (48 rooms), sorting dropdown (Giá thấp đến cao, Gần nhất), property cards list | Card click -> `RoomDetailScreen`; Back -> Home | Filtered results list with sorting | High | Passed |
| 17 | Chi tiết phòng trọ - Avaroom Sinh viên | `RoomDetailScreen.java` | Complete | Video 360°/photo preview with 100% eKYC badge, room specs (28m², gác lửng, tự do 24/7), transparent utility bill table (Điện, Nước, Wifi, Xe), host profile card (Cô Ánh Tuyết, 4.9⭐), sticky bottom action dock | "Nhắn chủ trọ" -> `StudentChatScreen`; "Đặt lịch xem phòng" -> `BookingViewingDialog` | Rich detail scroll view with transparent billing breakdown | Critical | Passed |
| 18 | Đặt lịch xem phòng - Avaroom Sinh viên | `BookingViewingDialog.java` | Complete | Room mini summary, step 1 date picker chips (Hôm nay 14, 15, 16...), step 2 time slot chips (09:00, 14:00, 16:30, 18:00), step 3 student note text area, submit CTA | Submit -> Saves appointment & opens `StudentAppointmentsScreen` | Interactive appointment booking flow | High | Passed |
| 19 | Lịch hẹn xem phòng - Avaroom Sinh viên | `StudentAppointmentsScreen.java` | Complete | Status tabs (Sắp tới, Hoàn tất, Đã hủy), appointment cards with host name, time, address, and actions (Gọi chủ trọ, Chỉ đường, Hủy hẹn) | Card action -> direct chat or room detail | Appointment lifecycle management | High | Passed |
| 20 | Tìm trọ lân cận - Bản đồ | `NearMeMapScreen.java` | Complete | Custom-painted vector map canvas with university markers (HUST, NEU, NUCE), interactive price pins, bottom room carousel preview card | Pin click updates selected room preview; Card click -> Room Detail | Custom Swing Java2D map rendering with interactive markers | High | Passed |
| 21 | Tin nhắn & Chat với chủ trọ | `StudentChatScreen.java` | Complete | Two-pane split: left conversations list (Cô Ánh Tuyết, Chú Dũng...), right chat thread with timestamps, message bubbles, and send input | Typing message and hitting Send adds live message bubble | Chat thread manager with instant replies | High | Passed |
| 22 | Phòng đã lưu - Avaroom Sinh viên | `SavedRoomsScreen.java` | Complete | Grid/List of bookmarked rooms with remove favorite button and view detail button | Click room -> `RoomDetailScreen`; Unsave removes from list | Favorite rooms state persistence | Medium | Passed |
| 23 | Tài khoản & Cài đặt sinh viên | `StudentProfileScreen.java` | Complete | Student profile header (Nguyễn Thanh Tùng, K66 Bách Khoa, Verified badge), quick links (Phòng đã lưu, Lịch hẹn, Đổi mật khẩu, eKYC), Logout CTA | Link clicks navigate to corresponding screens; Logout -> Login | Profile management & navigation hub | High | Passed |
| 24 | Đổi mật khẩu tài khoản - Avaroom | `ChangePasswordScreen.java` | Complete | Current password, new password, confirm password, save button | Submit updates password with validation feedback | Form validation & feedback | Medium | Passed |
| 25 | Hồ sơ sinh viên & eKYC (Cọc 0đ) | `StudentEkycProfileScreen.java` | Complete | Student eKYC verification status, university student card preview, "Cọc 0đ" eligibility badge | "Cập nhật hồ sơ" / "Xác thực lại" actions | eKYC status card & details | Medium | Passed |
| 26 | Đánh giá | `ReviewsScreen.java` | Complete | Overall rating 4.8/5, rating breakdown bars (5⭐ to 1⭐), student review cards with photos & comments, filter by rating | Star filter updates visible reviews list | Rating calculation & review cards | Medium | Passed |
| 27 | Quản lý tin đăng - Chủ trọ | `LandlordDashboardScreen.java` | Complete | Stats cards: Tổng phòng (12), Tỉ lệ thuê (10/12), Lượt xem (1.480, +18%); Search input; status tabs (Tất cả, Còn trống, Đã cọc, Chờ duyệt); room list cards; "+ Đăng tin" CTA | "+ Đăng tin" -> `CreatePostStep1Screen`; Card "Sửa tin" -> `PostDetailScreen`; Nav -> Appointments, Chat, Profile | Landlord operational hub with real-time stats | Critical | Passed |
| 28 | Đăng tin phòng trọ mới - Bước 1 | `CreatePostStep1Screen.java` | Complete | Title input, room category pills, university cluster, exact address, price & area fields, PCCC/safety checklist | "Tiếp tục" -> Step 2; "Lưu nháp" -> Dashboard | Step 1 validation & wizard flow | Critical | Passed |
| 29 | Đăng tin - Bước 2: Tiện ích & Nội thất | `CreatePostStep2Screen.java` | Complete | Furniture & appliances checklist (Giường nệm, Tủ lạnh, Máy giặt, Điều hòa, Bếp, Sạc xe điện), bathroom specs | "Tiếp tục" -> Step 3; "Quay lại" -> Step 1 | Step 2 amenities state | High | Passed |
| 30 | Đăng tin - Bước 3: Ảnh 360° & Timestamp | `CreatePostStep3Screen.java` | Complete | 360° photo upload slots, timestamp verification certification badge, guidelines | "Tiếp tục" -> Step 4; "Quay lại" -> Step 2 | Step 3 media verification | High | Passed |
| 31 | Đăng tin - Bước 4 - Hoàn tất | `CreatePostStep4Screen.java` | Complete | Summary preview card of the listing, terms checkbox, "XUẤT BẢN TIN ĐĂNG" CTA | Submit adds room to Landlord dashboard & navigates to Dashboard | Final review & publishing pipeline | High | Passed |
| 32 | Chi tiết tin đăng [DEMO] | `PostDetailScreen.java` | Complete | Landlord view of listing, visibility toggle switch (Hiển thị / Tạm ẩn), edit details, performance stats | "Quản lý đánh giá" -> Landlord Reviews; "Sửa tin" -> Edit mode | Listing detail & management controls | High | Passed |
| 33 | Lịch hẹn xem phòng & Khảo sát (Chủ trọ) | `LandlordAppointmentsScreen.java` | Complete | Calendar header, pending viewing requests list, tenant info, date & time, accept/decline buttons | Click appointment -> `AppointmentDetailDialog`; Direct message button -> Landlord Chat | Landlord appointment response system | High | Passed |
| 34 | Chi tiết lịch hẹn xem phòng - Góc nhìn Chủ trọ | `AppointmentDetailDialog.java` | Complete | Tenant details (Student name, university, phone, rating), requested viewing slot, notes, Accept/Reschedule buttons | Accept confirms appointment; Chat opens tenant message thread | Dialog for appointment confirmation | High | Passed |
| 35 | Tin nhắn & Tương tác khách thuê | `LandlordChatScreen.java` | Complete | Landlord messaging view with tenant inquiries, room reference badge (#AVR-90412), response rate timer | Send message, quick response templates | Landlord tenant communication hub | High | Passed |
| 36 | Thống kê & Báo cáo Tăng trưởng - Chủ trọ | `StatsGrowthScreen.java` | Complete | Monthly revenue chart, occupancy rate comparison, inquiry volume, top performing listings | Period selector (Tháng này, Quý này, Năm nay) | Custom-drawn Java2D bar chart & metrics | High | Passed |
| 37 | Quản lý & Phản hồi Đánh giá - Chủ trọ | `LandlordReviewsScreen.java` | Complete | Tenant feedback list, rating breakdown, inline reply composer for landlord | Reply submission appends host response | Review response management | Medium | Passed |
| 38 | Quản lý Hợp đồng & Tiền cọc Escrow | `EscrowContractScreen.java` | Complete | Escrow deposit status, active contracts list, deposit protection guarantee, return deposit CTA | Contract view & escrow release actions | Escrow trust & contract dashboard | High | Passed |
| 39 | Chốt điện nước & Hóa đơn tháng - Chủ trọ | `UtilityBillingScreen.java` | Complete | Meter input fields (Chỉ số điện cũ/mới, Nước cũ/mới), automated cost calculation, Bill breakdown summary | "Tạo hóa đơn" generates VietQR invoice view | Utility calculation & billing engine | High | Passed |
| 40 | Xem trước Hóa đơn PDF & VietQR | `InvoicePreviewDialog.java` | Complete | Formatted invoice preview with itemized costs, student name, and simulated VietQR code for instant banking payment | "Gửi hóa đơn cho sinh viên" action | VietQR payment dialog | Medium | Passed |
| 41 | Biên bản bàn giao tài sản 360° | `AssetHandoverScreen.java` | Complete | Room asset checklist (Khóa cửa, Điều hòa, Tủ lạnh, Tường/Sàn), photo timestamp record, check-in signature | Sign & Save handover record | Asset inspection checklist | Medium | Passed |
| 42 | Hồ sơ chủ trọ & Cài đặt | `LandlordProfileScreen.java` | Complete | Landlord profile card (Cô Hoàng Thị Ánh Tuyết, verified landlord badge, phone, email), bank account for payout, notification switches | "Chỉnh sửa hồ sơ" -> Edit profile screen; Switch role to Student | Landlord settings & role switching | High | Passed |
| 43 | Chỉnh sửa Hồ sơ Chủ trọ & Cài đặt | `EditLandlordProfileScreen.java` | Complete | Edit form for landlord name, phone, bank account, identity card | Save updates landlord profile with notification | Profile edit validation | Medium | Passed |
| 44 | Trung tâm Thông báo - Chủ trọ / Sinh viên | `NotificationsScreen.java` | Complete | Categorized notifications: Lịch hẹn xem phòng, Hóa đơn điện nước, Tin nhắn mới, Cảnh báo hệ thống | Click notification routes to corresponding screen (Appointment, Bill, Chat) | Notification dispatch and badge clearing | High | Passed |

---

## 3. Technology Stack & Architecture Compliance

- **Language:** Java 17 (Microsoft OpenJDK / Standard JVM)
- **GUI Toolkit:** Java Swing (`javax.swing.*`) and AWT (`java.awt.*`)
- **Build Tool:** Gradle 8.5 with `application` and `java` plugins (`gradlew.bat build`, `gradlew.bat run`)
- **Prohibited Frameworks Avoided:** No JavaFX, No Android UI, No Web/Electron frontends, No 3rd-party component libraries
- **Rendering Enhancements:** Java2D Anti-aliasing (`KEY_ANTIALIASING`, `KEY_TEXT_ANTIALIASING`, `KEY_RENDERING`) enabled globally
- **Threading:** Swing Event Dispatch Thread (`SwingUtilities.invokeLater`) respected throughout all UI operations and navigation transitions.
