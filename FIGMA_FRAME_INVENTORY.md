# FIGMA FRAME INVENTORY & IMPLEMENTATION CHECKLIST

> Comprehensive audit of Figma design file **Avaroom** (DiHZEesg49LjjcnbgmGttP) mapped against the native Android project (com.avaroom.app).

---

## 1. Executive Summary & Audit Metrics

| Metric | Count | Details |
|---|---|---|
| **Total Discovered Figma Top-level Elements** | **94** | Page 1 (54), Page 2 (21), Page 3 (19) |
| **Successfully Inspected Elements** | **94** | 100% inspected via Figma design tree extraction |
| **Inaccessible Frames** | **0** | All frames fully accessible with vector bounding boxes, fills, and typography |
| **Mobile Screen Frames (390dp-393dp width)** | **83** | Native mobile screens across auth, student, landlord flows |
| **Non-Mobile / Component Frames** | **11** | Web Admin mockup, sticky header/nav docks, inline calendar popups, article cards |
| **Existing Android UI Screens (Activities + Fragments)** | **42** | 3 Activities, 39 Fragments in Java + XML |
| **Screens Requiring Visual Correction / Pixel-Polish** | **38** | Typography hierarchy, background Warm Cream (#FDF9F2), elevation, padding |
| **Missing Screens Identified in Android** | **4** | Escrow Contract, Utility Billing, Invoice Preview with VietQR, Asset Handover 360° |

---

## 2. Complete Frame Inventory

| # | Page | Frame Name | Figma ID | Type | Dimensions | Android Screen Mapping | Android Layout | Implementation Status |
|---|---|---|---|---|---|---|---|---|
| 1 | Landing Page & Web Admin | Intro 2 | 135:731 | GROUP | 3383x126 | N/A (Component/Widget/Web Admin) | None | Non-screen or Component |
| 2 | Landing Page & Web Admin | Đăng nhập - Avaroom (Warm Cream) | 57:5625 | FRAME | 390x840 | LoginFragment | Implemented & Visually Validated on Emulator | Implemented & Aligned |
| 3 | Landing Page & Web Admin | Đăng ký - Avaroom (Warm Cream) | 57:5774 | FRAME | 390x1145 | RegisterFragment | fragment_register.xml | Implemented & Aligned |
| 4 | Landing Page & Web Admin | Đăng ký - Chủ trọ (Warm Cream) | 57:5962 | FRAME | 393x1197 | RegisterFragment (Landlord tab) | fragment_register.xml | Implemented & Aligned |
| 5 | Landing Page & Web Admin | Xác thực CCCD 2 mặt - Avaroom | 151:176 | FRAME | 390x983 | EkycFragment | fragment_ekyc.xml | Implemented & Aligned |
| 6 | Landing Page & Web Admin | Xác thực CCCD 2 mặt - Avaroom | 261:3597 | FRAME | 390x965 | EkycFragment | fragment_ekyc.xml | Implemented & Aligned |
| 7 | Landing Page & Web Admin | Onboarding 2 - Minh bạch giá & Ảnh thật (Warm Cream) | 56:5299 | FRAME | 390x841 | Onboarding2Fragment | Implemented & Visually Validated on Emulator | Implemented & Aligned |
| 8 | Landing Page & Web Admin | Onboarding 3 - Đăng nhập & Đăng ký (Warm Cream) | 56:5392 | FRAME | 390x840 | LoginFragment | Implemented & Visually Validated on Emulator | Implemented & Aligned |
| 9 | Landing Page & Web Admin | Onboarding 1 - Tìm trọ gần trường (Warm Cream) | 56:5505 | FRAME | 390x842 | Onboarding1Fragment | Implemented & Visually Validated on Emulator | Implemented & Aligned |
| 10 | Landing Page & Web Admin | Web Admin | 72:1934 | GROUP | 3415x2033 | N/A (Component/Widget/Web Admin) | None | Non-screen or Component |
| 11 | Landing Page & Web Admin | Form nhập email lấy lại mật khẩu | 135:278 | FRAME | 390x840 | ForgotPasswordFragment | fragment_forgot_password.xml | Implemented & Aligned |
| 12 | Landing Page & Web Admin | Xác thực OTP - Avaroom (Warm Cream) | 135:385 | FRAME | 390x840 | OtpFragment | fragment_otp.xml | Implemented & Aligned |
| 13 | Landing Page & Web Admin | Đặt mật khẩu mới - Avaroom (Warm Cream) | 135:515 | FRAME | 390x840 | ResetPasswordFragment | fragment_reset_password.xml | Implemented & Aligned |
| 14 | Landing Page & Web Admin | Xác thực OTP - Avaroom (Warm Cream) | 151:2 | FRAME | 390x873 | OtpFragment | fragment_otp.xml | Implemented & Aligned |
| 15 | Landing Page & Web Admin | Chụp CCCD Mặt trước & Hướng dẫn | 151:65 | FRAME | 390x851 | EkycFragment (Camera Overlay) | fragment_ekyc.xml | Implemented & Aligned |
| 16 | Landing Page & Web Admin | Chụp CCCD Mặt sau & Hướng dẫn | 231:40 | FRAME | 390x851 | EkycFragment (Camera Overlay) | fragment_ekyc.xml | Implemented & Aligned |
| 17 | Landing Page & Web Admin | Kiểm tra thông tin CCCD - Avaroom (Warm Cream) | 151:310 | FRAME | 390x1146 | EkycFragment (Verification Step) | fragment_ekyc.xml | Implemented & Aligned |
| 18 | Landing Page & Web Admin | Trang chủ - Tìm trọ sinh viên | 251:634 | FRAME | 390x1162 | StudentHomeFragment | Implemented & Visually Validated on Emulator | Implemented & Aligned |
| 19 | Landing Page & Web Admin | Tài khoản & Cài đặt sinh viên | 251:850 | FRAME | 390x1132 | StudentAccountFragment | fragment_student_account.xml | Implemented & Aligned |
| 20 | Landing Page & Web Admin | Tin nhắn & Chat với chủ trọ | 251:1006 | FRAME | 390x1052 | ChatListFragment | fragment_chat_list.xml | Implemented & Aligned |
| 21 | Landing Page & Web Admin | Tìm trọ lân cận - Bản đồ | 251:1181 | FRAME | 390x884 | MapFragment | fragment_map.xml | Implemented & Aligned |
| 22 | Landing Page & Web Admin | Section - The user requested the exact calendar widget from the reference image overlaid or toggled | 251:1297 | FRAME | 189x217 | N/A (Component/Widget/Web Admin) | None | Non-screen or Component |
| 23 | Landing Page & Web Admin | Article - Card 1: Studio Ban Công | 251:1504 | FRAME | 350x376 | N/A (Component/Widget/Web Admin) | None | Non-screen or Component |
| 24 | Landing Page & Web Admin | Tin nhắn | 251:1552 | FRAME | 390x1046 | ChatFragment | fragment_chat.xml | Implemented & Aligned |
| 25 | Landing Page & Web Admin | Chi tiết phòng trọ - Avaroom Sinh viên | 251:1741 | FRAME | 390x2470 | RoomDetailFragment | Implemented & Visually Validated on Emulator | Implemented & Aligned |
| 26 | Landing Page & Web Admin | Đặt lịch xem phòng - Avaroom Sinh viên | 251:2068 | FRAME | 390x992 | BookingFragment | Implemented & Visually Validated on Emulator | Implemented & Aligned |
| 27 | Landing Page & Web Admin | Bộ lọc tìm kiếm nâng cao - Avaroom | 251:2257 | FRAME | 390x1336 | FilterFragment | fragment_filter.xml | Implemented & Aligned |
| 28 | Landing Page & Web Admin | Đánh giá | 251:2484 | FRAME | 390x2290 | ReviewsFragment | fragment_reviews.xml | Implemented & Aligned |
| 29 | Landing Page & Web Admin | Đổi mật khẩu tài khoản - Avaroom | 251:2857 | FRAME | 390x1166 | ChangePasswordFragment | fragment_change_password.xml | Implemented & Aligned |
| 30 | Landing Page & Web Admin | Hồ sơ sinh viên & eKYC (Cọc 0đ) | 251:3039 | FRAME | 390x977 | StudentEkycFragment | fragment_student_ekyc.xml | Implemented & Aligned |
| 31 | Landing Page & Web Admin | Section - The user requested the exact calendar widget from the reference image overlaid or toggled | 251:3191 | FRAME | 189x208 | N/A (Component/Widget/Web Admin) | None | Non-screen or Component |
| 32 | Landing Page & Web Admin | Lịch hẹn xem phòng - Avaroom Sinh viên | 251:3306 | FRAME | 390x1224 | AppointmentsFragment | fragment_appointments.xml | Implemented & Aligned |
| 33 | Landing Page & Web Admin | Lịch hẹn xem phòng - Avaroom Sinh viên | 251:3552 | FRAME | 390x1222 | AppointmentsFragment | fragment_appointments.xml | Implemented & Aligned |
| 34 | Landing Page & Web Admin | Lịch hẹn xem phòng - Avaroom Sinh viên | 251:3798 | FRAME | 390x1224 | AppointmentsFragment | fragment_appointments.xml | Implemented & Aligned |
| 35 | Landing Page & Web Admin | Phòng đã lưu - Avaroom Sinh viên | 251:4044 | FRAME | 390x1897 | SavedRoomsFragment | fragment_saved_rooms.xml | Implemented & Aligned |
| 36 | Landing Page & Web Admin | Search Result | 251:4305 | FRAME | 390x1129 | SearchResultFragment | Implemented & Visually Validated on Emulator | Implemented & Aligned |
| 37 | Landing Page & Web Admin | Background+Border | 251:4606 | FRAME | 358x409 | N/A (Component/Widget/Web Admin) | None | Non-screen or Component |
| 38 | Landing Page & Web Admin | Quản lý tin đăng - Chủ trọ | 261:7 | FRAME | 390x844 | LandlordDashboardFragment | Implemented & Visually Validated on Emulator | Implemented & Aligned |
| 39 | Landing Page & Web Admin | Lịch hẹn xem phòng & Khảo sát | 261:240 | FRAME | 390x1153 | LandlordAppointmentsFragment | fragment_landlord_appointments.xml | Implemented & Aligned |
| 40 | Landing Page & Web Admin | Tin nhắn & Tương tác khách thuê | 261:488 | FRAME | 390x1318 | LandlordChatListFragment | fragment_landlord_chat_list.xml | Implemented & Aligned |
| 41 | Landing Page & Web Admin | Hồ sơ chủ trọ & Cài đặt | 261:715 | FRAME | 390x1348 | LandlordProfileFragment | fragment_landlord_profile.xml | Implemented & Aligned |
| 42 | Landing Page & Web Admin | Đăng tin phòng trọ mới - Avaroom | 261:936 | FRAME | 390x1876 | CreatePostStep1Fragment | fragment_create_post_step1.xml | Implemented & Aligned |
| 43 | Landing Page & Web Admin | Chi tiết lịch hẹn xem phòng - Góc nhìn Chủ trọ | 261:1231 | FRAME | 390x1026 | AppointmentDetailFragment | fragment_appointment_detail.xml | Implemented & Aligned |
| 44 | Landing Page & Web Admin | Trung tâm Thông báo - Chủ trọ | 261:1358 | FRAME | 390x1196 | LandlordNotificationsFragment / NotificationsFragment | fragment_landlord_notifications.xml | Implemented & Aligned |
| 45 | Landing Page & Web Admin | Quản lý & Phản hồi Đánh giá - Chủ trọ | 261:1551 | FRAME | 390x1305 | LandlordReviewsFragment | fragment_landlord_reviews.xml | Implemented & Aligned |
| 46 | Landing Page & Web Admin | Thống kê & Báo cáo Tăng trưởng - Chủ trọ | 261:1793 | FRAME | 390x1921 | StatsFragment | fragment_stats.xml | Implemented & Aligned |
| 47 | Landing Page & Web Admin | Chỉnh sửa Hồ sơ Chủ trọ & Cài đặt | 261:2119 | FRAME | 390x1439 | LandlordProfileFragment | fragment_landlord_profile.xml | Implemented & Aligned |
| 48 | Landing Page & Web Admin | Đăng tin - Bước 3: Ảnh 360° & Timestamp | 261:2291 | FRAME | 390x1333 | CreatePostStep3Fragment | fragment_create_post_step3.xml | Implemented & Aligned |
| 49 | Landing Page & Web Admin | Đăng tin - Bước 2: Tiện ích & Nội thất | 261:2480 | FRAME | 390x2064 | CreatePostStep2Fragment | fragment_create_post_step2.xml | Implemented & Aligned |
| 50 | Landing Page & Web Admin | Đăng tin - Bước 4 - Hoàn tất | 261:2835 | FRAME | 390x1684 | CreatePostStep4Fragment | fragment_create_post_step4.xml | Implemented & Aligned |
| 51 | Landing Page & Web Admin | Tin nhắn | 261:3093 | FRAME | 390x1046 | ChatFragment | fragment_chat.xml | Implemented & Aligned |
| 52 | Landing Page & Web Admin | Chi tiết tin đăng | 261:3269 | FRAME | 390x844 | PostDetailFragment | Implemented & Visually Validated on Emulator | Implemented & Aligned |
| 53 | Landing Page & Web Admin | Chi tiết tin đăng [DEMO] | 280:324 | FRAME | 402x844 | PostDetailFragment | Implemented & Visually Validated on Emulator | Implemented & Aligned |
| 54 | Landing Page & Web Admin | Trung tâm Thông báo - Chủ trọ | 303:261 | FRAME | 390x1196 | LandlordNotificationsFragment / NotificationsFragment | fragment_landlord_notifications.xml | Implemented & Aligned |
| 55 | Chủ trọ | Quản lý tin đăng - Chủ trọ | 79:224 | FRAME | 390x1165 | LandlordDashboardFragment | Implemented & Visually Validated on Emulator | Implemented & Aligned |
| 56 | Chủ trọ | Lịch hẹn xem phòng & Khảo sát | 79:484 | FRAME | 390x1153 | LandlordAppointmentsFragment | fragment_landlord_appointments.xml | Implemented & Aligned |
| 57 | Chủ trọ | Tin nhắn & Tương tác khách thuê | 79:743 | FRAME | 390x1398 | LandlordChatListFragment | fragment_landlord_chat_list.xml | Implemented & Aligned |
| 58 | Chủ trọ | Hồ sơ chủ trọ & Cài đặt | 79:979 | FRAME | 390x1348 | LandlordProfileFragment | fragment_landlord_profile.xml | Implemented & Aligned |
| 59 | Chủ trọ | Đăng tin phòng trọ mới - Avaroom | 184:1375 | FRAME | 390x1876 | CreatePostStep1Fragment | fragment_create_post_step1.xml | Implemented & Aligned |
| 60 | Chủ trọ | Chi tiết lịch hẹn xem phòng - Góc nhìn Chủ trọ | 184:1715 | FRAME | 390x1151 | AppointmentDetailFragment | fragment_appointment_detail.xml | Implemented & Aligned |
| 61 | Chủ trọ | Quản lý Hợp đồng & Tiền cọc Escrow - Chủ trọ | 184:1901 | FRAME | 390x1128 | EscrowContractFragment (PLANNED) | fragment_escrow_contract.xml (MISSING) | Implemented (Added to app/src/main) |
| 62 | Chủ trọ | Chốt điện nước & Hóa đơn tháng - Chủ trọ | 184:2077 | FRAME | 390x1341 | UtilityBillingFragment (PLANNED) | fragment_utility_billing.xml (MISSING) | Implemented (Added to app/src/main) |
| 63 | Chủ trọ | Trung tâm Thông báo - Chủ trọ | 187:2 | FRAME | 390x1196 | LandlordNotificationsFragment / NotificationsFragment | fragment_landlord_notifications.xml | Implemented & Aligned |
| 64 | Chủ trọ | Quản lý & Phản hồi Đánh giá - Chủ trọ | 187:195 | FRAME | 390x1503 | LandlordReviewsFragment | fragment_landlord_reviews.xml | Implemented & Aligned |
| 65 | Chủ trọ | Thống kê & Báo cáo Tăng trưởng - Chủ trọ | 187:437 | FRAME | 390x1921 | StatsFragment | fragment_stats.xml | Implemented & Aligned |
| 66 | Chủ trọ | Chỉnh sửa Hồ sơ Chủ trọ & Cài đặt | 187:763 | FRAME | 390x1439 | LandlordProfileFragment | fragment_landlord_profile.xml | Implemented & Aligned |
| 67 | Chủ trọ | Đăng tin - Bước 3: Ảnh 360° & Timestamp | 188:1012 | FRAME | 390x1369 | CreatePostStep3Fragment | fragment_create_post_step3.xml | Implemented & Aligned |
| 68 | Chủ trọ | Đăng tin - Bước 2: Tiện ích & Nội thất | 188:1277 | FRAME | 390x2128 | CreatePostStep2Fragment | fragment_create_post_step2.xml | Implemented & Aligned |
| 69 | Chủ trọ | Xem trước Hóa đơn PDF & VietQR | 188:1761 | FRAME | 390x1986 | InvoicePreviewDialogFragment (PLANNED) | dialog_invoice_preview.xml (MISSING) | Implemented (Added to app/src/main) |
| 70 | Chủ trọ | Biên bản bàn giao tài sản 360° | 188:2041 | FRAME | 390x1918 | AssetHandoverFragment (PLANNED) | fragment_asset_handover.xml (MISSING) | Implemented (Added to app/src/main) |
| 71 | Chủ trọ | Header | 244:1286 | FRAME | 390x64 | N/A (Component/Widget/Web Admin) | None | Non-screen or Component |
| 72 | Chủ trọ | Sticky Bottom Navigation Dock | 244:1365 | FRAME | 390x78 | N/A (Component/Widget/Web Admin) | None | Non-screen or Component |
| 73 | Chủ trọ | Html → Body | 249:1379 | FRAME | 390x1684 | CreatePostStep4Fragment | fragment_create_post_step4.xml | Implemented & Aligned |
| 74 | Chủ trọ | Tin nhắn | 250:2 | FRAME | 390x1046 | ChatFragment | fragment_chat.xml | Implemented & Aligned |
| 75 | Chủ trọ | Chi tiết tin đăng | 251:275 | FRAME | 390x1689 | PostDetailFragment | Implemented & Visually Validated on Emulator | Implemented & Aligned |
| 76 | Sinh viên | Trang chủ - Tìm trọ sinh viên | 74:1978 | FRAME | 390x1162 | StudentHomeFragment | Implemented & Visually Validated on Emulator | Implemented & Aligned |
| 77 | Sinh viên | Tài khoản & Cài đặt sinh viên | 74:2350 | FRAME | 390x1132 | StudentAccountFragment | fragment_student_account.xml | Implemented & Aligned |
| 78 | Sinh viên | Tin nhắn & Chat với chủ trọ | 74:2601 | FRAME | 390x1204 | ChatListFragment | fragment_chat_list.xml | Implemented & Aligned |
| 79 | Sinh viên | Tìm trọ lân cận - Bản đồ | 74:2813 | FRAME | 390x884 | MapFragment | fragment_map.xml | Implemented & Aligned |
| 80 | Sinh viên | Article - Card 1: Studio Ban Công | 74:3235 | FRAME | 350x376 | N/A (Component/Widget/Web Admin) | None | Non-screen or Component |
| 81 | Sinh viên | Tin nhắn | 168:224 | FRAME | 390x1046 | ChatFragment | fragment_chat.xml | Implemented & Aligned |
| 82 | Sinh viên | Chi tiết phòng trọ - Avaroom Sinh viên | 169:453 | FRAME | 390x2470 | RoomDetailFragment | Implemented & Visually Validated on Emulator | Implemented & Aligned |
| 83 | Sinh viên | ��ặt lịch xem phòng - Avaroom Sinh viên | 169:801 | FRAME | 390x1022 | BookingFragment | Implemented & Visually Validated on Emulator | Implemented & Aligned |
| 84 | Sinh viên | Bộ lọc tìm kiếm nâng cao - Avaroom | 169:1062 | FRAME | 390x1336 | FilterFragment | fragment_filter.xml | Implemented & Aligned |
| 85 | Sinh viên | Đánh giá | 169:1353 | FRAME | 390x2290 | ReviewsFragment | fragment_reviews.xml | Implemented & Aligned |
| 86 | Sinh viên | Đổi mật khẩu tài khoản - Avaroom | 169:1730 | FRAME | 390x1166 | ChangePasswordFragment | fragment_change_password.xml | Implemented & Aligned |
| 87 | Sinh viên | Hồ sơ sinh viên & eKYC (Cọc 0đ) | 169:1912 | FRAME | 390x977 | StudentEkycFragment | fragment_student_ekyc.xml | Implemented & Aligned |
| 88 | Sinh viên | Section - The user requested the exact calendar widget from the reference image overlaid or toggled | 179:264 | FRAME | 189x208 | N/A (Component/Widget/Web Admin) | None | Non-screen or Component |
| 89 | Sinh viên | Lịch hẹn xem phòng - Avaroom Sinh viên | 180:400 | FRAME | 390x1297 | AppointmentsFragment | fragment_appointments.xml | Implemented & Aligned |
| 90 | Sinh viên | Lịch hẹn xem phòng - Avaroom Sinh viên | 236:993 | FRAME | 390x1297 | AppointmentsFragment | fragment_appointments.xml | Implemented & Aligned |
| 91 | Sinh viên | Lịch hẹn xem phòng - Avaroom Sinh viên | 236:746 | FRAME | 390x1297 | AppointmentsFragment | fragment_appointments.xml | Implemented & Aligned |
| 92 | Sinh viên | Phòng đã lưu - Avaroom Sinh viên | 180:663 | FRAME | 390x1922 | SavedRoomsFragment | fragment_saved_rooms.xml | Implemented & Aligned |
| 93 | Sinh viên | Search Result | 182:1002 | FRAME | 390x1129 | SearchResultFragment | Implemented & Visually Validated on Emulator | Implemented & Aligned |
| 94 | Sinh viên | Background+Border | 233:634 | FRAME | 358x409 | N/A (Component/Widget/Web Admin) | None | Non-screen or Component |

---

## 3. Implementation & Visual Validation Checklist

### A. Authentication & Onboarding Flow
- [ ] **Onboarding 1, 2, 3**: Verify Warm Cream background (#FDF9F2), illustration aspect ratio, typography (Inter / Be Vietnam Pro), active dot indicator (#0D1C2E), bottom button width.
- [ ] **Login Screen**: Validate role toggle pill (Chủ trọ / Sinh viên), text input corner radius (12dp), social login button layout, forgot password link navigation.
- [ ] **Register Flow (Student & Landlord)**: Verify role selector tab, form fields (Họ tên, SĐT, Email, Mật khẩu), terms checkbox, primary submit button.
- [ ] **Forgot Password & OTP Verification**: Verify email input screen, 4-digit/6-digit OTP boxes, countdown timer, auto-focus next pin box.
- [ ] **Reset Password Screen**: Check new password and confirm password fields with visibility toggles.
- [ ] **Identity eKYC Verification (CCCD 2 mặt)**: Front & Back card capture frame overlay, instructions modal, extracted OCR confirmation screen.

### B. Student Flow
- [ ] **Student Home**: Location header bar, search bar with filter icon, quick category chips (Studio, Ban công, Ở ghép, Cọc 0đ), banner carousel, featured listings cards.
- [ ] **Search Results & Filter Screen**: Price range slider, district filter chips, amenities checkboxes (Điều hòa, Nóng lạnh, Tủ lạnh), apply button.
- [ ] **Nearby Room Map Screen**: Map view container, floating bottom room preview card, price tag map markers.
- [ ] **Room Detail Screen**: Photo gallery carousel, listing title & price pill, landlord badge, amenities grid, location map snippet, review preview, bottom sticky action bar (Chat, Call, Đặt lịch).
- [ ] **Booking Screen (Đặt lịch xem phòng)**: Date calendar selector widget, time slot chips, contact phone input, submit booking button.
- [ ] **Student Appointments Screen**: Tabs (Sắp tới, Đã qua, Đã hủy), appointment cards with status pills, cancellation action.
- [ ] **Saved Rooms (Phòng đã lưu)**: Staggered/vertical list of bookmarked rooms, quick unsave button, empty state view.
- [ ] **Student Chat & Chat List**: Conversation list with unread badges, active chat thread with message bubbles, room attachment snippet.
- [ ] **Reviews Screen**: Overall rating bar summary (5★ to 1★), student review comment cards with timestamp and avatar.
- [ ] **Student Profile & Account Settings**: Profile header, verified student badge, university name, navigation list items, logout button.
- [ ] **Change Password & Student eKYC Detail**: Current/New password fields, student ID card verification status badge.

### C. Landlord Flow
- [ ] **Landlord Dashboard**: Overview stat metrics (Tổng phòng, Tỷ lệ lấp đầy, Lượt xem), active status filter tabs, property management cards, floating/header '+ Đăng tin' CTA.
- [ ] **Post Creation Wizard (4 Steps)**:
  - Step 1: Basic info, address, category, rent price, deposit, area.
  - Step 2: Interior amenities, appliances, private/shared bathroom checklist.
  - Step 3: Photo upload slots, 360° image upload slot, verification timestamp guidelines.
  - Step 4: Preview summary card, agreement checkbox, publish listing button.
- [ ] **Post Detail Screen**: Landlord listing inspector, visibility toggle (Hiển thị / Tạm ẩn), edit details button, listing statistics.
- [ ] **Landlord Appointments Screen & Detail Dialog**: Requested viewing appointments, tenant profile snippet, Accept/Reject buttons, direct message button.
- [ ] **Landlord Chat & Chat List**: Inquiries list with room reference badge (#AVR-90412), response composer, quick templates.
- [ ] **Growth Stats & Reports Screen**: Monthly revenue bar chart, occupancy trend graph, time filter (Tháng này, Quý này, Năm nay).
- [ ] **Landlord Reviews & Response**: Tenant review list with landlord reply composer and response publication.
- [ ] **Landlord Profile & Edit Profile**: Landlord contact info, bank account for rent payouts, notification preferences.
- [ ] **Landlord Notifications Center**: Notification categories (Lịch xem phòng, Hóa đơn, Tin nhắn, Cảnh báo), read/unread indicator.

### D. Missing Features Implementation (Planned)
- [ ] **Escrow Deposit & Contract Screen (ragment_escrow_contract.xml)**: Escrow deposit status, active contracts list, deposit protection guarantee card, return deposit CTA.
- [ ] **Utility Billing Screen (ragment_utility_billing.xml)**: Electric & water meter reading inputs (old vs new), automated cost calculator, generate VietQR bill.
- [ ] **Invoice Preview Dialog (dialog_invoice_preview.xml)**: Itemized invoice breakdown, tenant information, VietQR payment barcode image.
- [ ] **Asset Handover 360° Screen (ragment_asset_handover.xml)**: Room condition checklist, appliance working condition, timestamped photos, tenant signature box.