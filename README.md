# Avaroom - Mobile App Prototype & UI Architecture (Android Native)

Dự án Android Studio được xây dựng **100% bằng Java Native** phục vụ hiển thị Giao diện (UI) và Luồng tương tác của từng Button (Prototype) chuyển cảnh theo thiết kế Figma **Avaroom**, **hoàn toàn KHÔNG dùng backend**.

---

## 1. Cấu trúc dự án (Architecture)

```text
Avaroom/
├── build.gradle (Root Gradle)
├── settings.gradle
├── app/
│   ├── build.gradle (Android dependencies & ViewBinding)
│   └── src/main/
│       ├── AndroidManifest.xml (Khai báo Activities & Permissions)
│       ├── java/com/avaroom/app/
│       │   ├── MainActivity.java (Entry Point cho Onboarding & Auth)
│       │   ├── ui/
│       │   │   ├── auth/ (Onboarding 1-3, Login, Register, Forgot Password, OTP, Reset Password)
│       │   │   ├── ekyc/ (Xác thực CCCD 2 mặt)
│       │   │   ├── student/ (StudentMainActivity, Trang chủ, Chi tiết, Đặt lịch, Filter, Map, Chat)
│       │   │   └── landlord/ (LandlordMainActivity, Quản lý tin, Đăng tin 4 bước, Lịch hẹn)
│       └── res/
│           ├── layout/ (Các file XML giao diện màn hình chuẩn màu Warm Cream & Typography)
│           ├── navigation/ (nav_graph_auth.xml, nav_graph_student.xml, nav_graph_landlord.xml)
│           ├── values/ (colors.xml, strings.xml, themes.xml)
│           └── drawable/ (Bo góc button, input field, card view)
```

---

## 2. Bảng ánh xạ Prototype & Button Flows đã trích xuất từ Figma (133 interactions)

### A. Phân hệ Onboarding & Authentication (`nav_graph_auth.xml`)
| Màn hình gốc | Tên Button trên Figma | Màn hình đích (Target Screen) |
| :--- | :--- | :--- |
| **Onboarding 1** | Button "Tiếp tục" | `Onboarding 2 - Minh bạch giá & Ảnh thật` |
| **Onboarding 2** | Button "Tiếp tục" | `Onboarding 3 - Đăng nhập & Đăng ký` |
| **Onboarding 3** | Button "Đăng ký tài khoản mới" | `Đăng ký - Avaroom` |
| **Onboarding 3** | Button "Tôi đã có tài khoản • Đăng nhập" | `Đăng nhập - Avaroom` |
| **Đăng nhập** | Button "ĐĂNG NHẬP" (Primary CTA) | `Trang chủ - Tìm trọ sinh viên` (Vào app) |
| **Đăng nhập** | Button "Quên mật khẩu?" | `Form nhập email lấy lại mật khẩu` |
| **Đăng nhập** | Button "Đăng ký ngay" | `Đăng ký - Avaroom` |
| **Đăng ký** | Tab "Chủ trọ" (Role switcher) | Chuyển chế độ đăng ký Chủ trọ |
| **Đăng ký** | Tab "Sinh viên" (Role switcher) | Chuyển chế độ đăng ký Sinh viên |
| **Đăng ký** | Button "TIẾP TỤC" (Sinh viên) | `Xác thực OTP - Avaroom` |
| **Đăng ký** | Button "TIẾP TỤC" (Chủ trọ) | `Xác thực CCCD 2 mặt - Avaroom` |
| **Đăng ký** | Link "Đăng nhập" / "Quay lại" | Quay lại màn `Đăng nhập` |
| **Quên mật khẩu** | Button "GỬI MÃ XÁC THỰC" | `Xác thực OTP - Avaroom` |
| **Xác thực OTP** | Button "XÁC NHẬN" | `Đặt mật khẩu mới - Avaroom` |
| **Đặt mật khẩu mới** | Button "LƯU MẬT KHẨU & ĐĂNG NHẬP" | `Trang chủ - Tìm trọ sinh viên` |
| **Xác thực CCCD** | Button "HOÀN TẤT XÁC THỰC" | `Quản lý tin đăng - Chủ trọ` |

### B. Phân hệ Sinh viên (`nav_graph_student.xml`)
* **Trang chủ**: 
  * Click Card phòng trọ -> Mở màn **Chi tiết phòng trọ** (`Chi tiết phòng trọ - Avaroom Sinh viên`).
  * Click Button "Bộ lọc nâng cao" -> Mở BottomSheet / Dialog **Bộ lọc tìm kiếm nâng cao**.
  * Click Bottom Nav "Bản đồ" -> Chuyển sang màn **Tìm trọ lân cận - Bản đồ**.
  * Click Bottom Nav "Tin nhắn" -> Chuyển sang **Tin nhắn & Chat với chủ trọ**.
  * Click Bottom Nav "Tài khoản" -> Chuyển sang **Tài khoản & Cài đặt sinh viên**.
* **Chi tiết phòng trọ**:
  * Button "Đặt lịch xem phòng" -> Chuyển sang màn **Đặt lịch xem phòng & Calendar**.
  * Button "Chat với chủ trọ" -> Chuyển sang màn **Tin nhắn**.
* **Tài khoản & Cài đặt**:
  * Mục "Phòng đã lưu" -> Mở màn **Phòng đã lưu**.
  * Mục "Lịch hẹn xem phòng" -> Mở màn **Lịch hẹn xem phòng**.
  * Mục "eKYC Sinh viên (Cọc 0đ)" -> Mở màn **Hồ sơ sinh viên & eKYC**.
  * Mục "Đổi mật khẩu" -> Mở màn **Đổi mật khẩu tài khoản**.
  * Button "Đăng xuất" -> Trở về màn **Đăng nhập**.

### C. Phân hệ Chủ trọ (`nav_graph_landlord.xml`)
* **Quản lý tin đăng**:
  * Button "Đăng tin phòng trọ mới" -> Chuyển sang luồng đăng tin 4 bước:
    * Bước 1: Thông tin cơ bản
    * Bước 2: Tiện ích & Nội thất
    * Bước 3: Ảnh 360° & Timestamp
    * Bước 4: Hoàn tất đăng tin
  * Click vào item tin đăng -> Mở màn **Chi tiết tin đăng [DEMO]**.
  * Bottom Navigation: Chuyển đổi giữa **Quản lý tin đăng**, **Lịch hẹn xem phòng & Khảo sát**, **Tin nhắn khách thuê**, **Hồ sơ chủ trọ**.

---

## 3. Cách mở và chạy trên Android Studio
1. Mở **Android Studio**.
2. Chọn **Open** -> Trỏ đến thư mục `c:\Users\Admin\Documents\Code\Avaroom`.
3. Android Studio sẽ tự động đồng bộ Gradle Sync.
4. Chọn máy ảo (Emulator) hoặc cắm thiết bị Android thật và bấm **Run (Shift + F10)** để test tương tác trực tiếp tất cả các nút bấm.
