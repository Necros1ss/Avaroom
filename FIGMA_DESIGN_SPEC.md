# FIGMA DESIGN SPECIFICATION: AVAROOM

> Native Android Design System & Pixel-Accurate Screen Specifications extracted from Figma (`DiHZEesg49LjjcnbgmGttP`).

---

## 1. Global Design Tokens

### 1.1 Color Palette

| Token Name | Hex Code | Android Color Resource | Role / Usage |
|---|---|---|---|
| **Surface / Background Warm Cream** | `#FDF9F2` | `@color/bg_warm_cream` | Primary app screen background (Theme default) |
| **Surface White** | `#FFFFFF` | `@color/bg_white` | Card backgrounds, inputs, modal containers |
| **Primary Navy** | `#0D1C2E` | `@color/primary_blue` | Primary buttons, active tab indicators, headers |
| **Primary Dark Black** | `#040505` | `@color/primary_dark` | High-emphasis text, brand title, role toggles |
| **Dark Neutral (Heading/Body)** | `#1A202C` | `@color/primary_black` | Headings, card titles, bold body text |
| **Subtle Neutral / Border** | `#45474C` | `@color/text_secondary` | Secondary body copy, icon tints, subtitles |
| **Muted Neutral / Placeholder** | `#718096` / `#76777C` | `@color/text_muted` | Form placeholders, inactive captions |
| **Divider / Border Subtle** | `#E6E2DB` / `#EDF2F7` | `@color/border_light` | Card outlines, row dividers, list separators |
| **Brand Red / Price Tag** | `#B51822` / `#E53E3E` | `@color/accent_red` | Room rental prices, required stars, error states |
| **Light Red Badge Background** | `#FFDAD7` | `@color/accent_red_light` | Urgent alerts, cancellation pills |
| **Accent Blue Tint** | `#EFF4FF` / `#E5EEFF` | `@color/accent_blue_light` | Information badge backgrounds, selected pill fills |
| **Accent Green (Verified)** | `#10B981` / `#005944` | `@color/accent_green` | Verified Landlord/Student badges, available room tags |
| **Accent Green Light** | `#DCFCE7` | `@color/accent_green_light` | Verified tag background pills |
| **Star Yellow / Rating** | `#F59E0B` / `#FBBF24` | `@color/accent_yellow` | Rating stars, featured ribbons |

### 1.2 Typography System

- **Primary Font Families:** `Inter`, `Be Vietnam Pro`, `Google Sans Flex`
- **Fallback Native Font:** `sans-serif` (Roboto / Android System Default)

| Style Hierarchy | Font Family | Weight | Size (sp) | Line Height (dp) | Letter Spacing | Primary Usage |
|---|---|---|---|---|---|---|
| **Display / Header 1** | Inter | 700 (Bold) | 24sp | 32dp | -0.2px | Onboarding headlines, screen top headers |
| **Headline 2** | Inter | 600 (SemiBold) | 18sp | 24dp | -0.1px | Section headers, card titles (Room names) |
| **Title Medium** | Inter / Be Vietnam Pro | 600 (SemiBold) | 16sp | 22dp | 0px | Modal headers, subheadings |
| **Body Bold** | Inter | 600 (SemiBold) | 13sp-14sp | 18dp-20dp | +0.13px | Button labels, price callouts, metric values |
| **Body Regular** | Inter / Be Vietnam Pro | 400 (Regular) | 14sp | 20dp | 0px | Form field inputs, descriptions, chat text |
| **Body Small** | Inter | 400 (Regular) | 12sp | 16dp | +0.12px | Secondary metadata, addresses, amenities labels |
| **Caption Medium** | Inter / Be Vietnam Pro | 500-600 | 11sp | 14dp-17dp | +0.44px | Category chips, status badges, timestamps |
| **Micro Caption** | Inter | 500-600 | 10sp | 12dp | +0.60px | Bottom navigation labels, micro tags |

### 1.3 Radii & Elevation Tokens

| Token | Measurement | Android Resource / Implementation | Usage |
|---|---|---|---|
| **Radius XS** | 4dp / 6dp | `@drawable/bg_tile_rounded` | Small badges, image corner overlays |
| **Radius SM** | 8dp / 10dp | `@drawable/bg_tile_rounded` | Amenity icons, compact action buttons |
| **Radius MD** | 12dp / 14dp | `@drawable/bg_input_field` | Text inputs, dropdown selectors |
| **Radius LG** | 16dp / 20dp | `@drawable/bg_card_rounded` | Room listing cards, stat overview cards |
| **Radius Pill** | 9999dp | `@drawable/bg_chip_pill` | Category filters, role toggles, status pills |
| **Elevation None** | 0dp | `android:elevation="0dp"` | Flat surface cards on Warm Cream |
| **Elevation Low** | 2dp | `android:elevation="2dp"` | Card elements, bottom dock |
| **Elevation Floating** | 8dp | `android:elevation="8dp"` | Modal bottom sheets, sticky action docks |

---

## 2. Screen-Level Specifications

### 2.1 Authentication & Onboarding Flow

#### Onboarding 1, 2, 3 (`56:5505`, `56:5299`, `56:5392`)
- **Frame Dimensions:** 390 x 840 dp
- **Background:** `#FDF9F2` (Warm Cream)
- **Illustration Container:** Centered, 320 x 280 dp, corner radius 20dp
- **Indicator Dots:** 8dp diameter, active dot width 24dp (pill), color `#0D1C2E`, inactive dot `#E6E2DB`
- **Primary Button:** Height 52dp, background `#0D1C2E`, corner radius 12dp, text `#FFFFFF` 14sp SemiBold

#### Login (`57:5625`)
- **Frame Dimensions:** 390 x 840 dp
- **Role Toggle Pill:** 180 x 44 dp, container `#EDF2F7`, active segment `#040505` with white text
- **Input Fields:** Height 52dp, background `#FFFFFF`, stroke 1dp `#E2E8F0`, corner radius 12dp, hint text `#718096` 14sp
- **Social Buttons:** Height 48dp, border 1dp `#E2E8F0`, corner radius 12dp, Google / Facebook icons

#### Register (Student & Landlord) (`57:5774`, `57:5962`)
- **Frame Dimensions:** 390 x 1145 dp / 393 x 1197 dp
- **Header:** Back button 24dp, Title "Tạo tài khoản" 20sp Bold
- **Form Inputs:** Họ và tên, Số điện thoại, Email, Mật khẩu, Xác nhận mật khẩu
- **Terms & Conditions Checkbox:** 20 x 20 dp, text 12sp Regular with clickable links

### 2.2 Student Experience Flow

#### Student Home (`74:1978`, `251:634`)
- **Frame Dimensions:** 390 x 1162 dp
- **Top App Bar:** Greeting header 18sp Bold, Location picker, Notification bell with badge
- **Search Input:** Height 48dp, corner radius 12dp, magnifying glass icon, trailing filter button (`#0D1C2E`)
- **Category Quick Chips:** Horizontal scroll, height 36dp, corner radius 999dp, padding horizontal 16dp
- **Featured Card (Article):** Width 350dp, height 376dp, corner radius 16dp, image height 200dp, price tag in `#B51822` 16sp Bold
- **Bottom Navigation:** Height 64dp, 4 tabs (Khám phá, Đã lưu, Lịch hẹn, Tài khoản)

#### Room Detail (`169:453`, `251:1741`)
- **Frame Dimensions:** 390 x 2470 dp
- **Media Gallery:** Height 280dp, horizontal pager, photo counter pill "1/8" at bottom right
- **Price & Area Row:** Price in `#B51822` 20sp Bold, Deposit text, Area text
- **Landlord Card:** Avatar 48dp, name, verified green checkmark, rating 4.9 star
- **Amenities Grid:** 4 columns, icon inside rounded container, label 11sp
- **Bottom Action Dock:** Sticky height 72dp, white container with top shadow, Outlined Chat button (120dp), Filled Navy Booking button

#### Booking Form (`169:801`, `251:2068`)
- **Frame Dimensions:** 390 x 992 dp / 390 x 1022 dp
- **Calendar Selector:** Monthly inline calendar, active selected day circle `#0D1C2E` with white text
- **Time Slot Chips:** 09:00, 10:30, 14:00, 16:30, 18:00 (height 36dp, radius 8dp)
- **Notes Input:** Multi-line height 90dp, corner radius 12dp

### 2.3 Landlord Operational Flow

#### Landlord Dashboard (`79:224`, `261:7`)
- **Frame Dimensions:** 390 x 1165 dp
- **Stats Summary Cards (3 Columns):** Tổng phòng, Tỷ lệ lấp đầy, Lượt xem
- **Status Filter Tabs:** Tất cả, Còn trống, Đã cọc, Chờ duyệt
- **Listing Management Card:** Thumbnail 80 x 80 dp (radius 10dp), title 14sp SemiBold, address, price, toggle switch
- **CTA Button:** "+ Đăng tin mới" navy button (`#0D1C2E`)

#### Create Post Step 1-4 Wizard (`184:1375`, `188:1277`, `188:1012`, `261:2835`)
- **Frame Dimensions:** 390 x 1333 dp to 390 x 2128 dp
- **Step Indicator Bar:** 4 segments, active steps filled `#0D1C2E`, pending steps `#E2E8F0`
- **Step 1:** Room title, category selector, university cluster dropdown, exact address, area, rent price, deposit
- **Step 2:** Amenities checkboxes
- **Step 3:** 360 degree image upload slot with preview circular viewer, timestamp verification instructions
- **Step 4:** Review summary listing card with simulated badges, publish CTA button

#### Stats & Growth Report (`187:437`, `261:1793`)
- **Frame Dimensions:** 390 x 1921 dp
- **Time Period Segment:** Tháng này / Quý này / Năm nay (height 36dp, radius 8dp)
- **Revenue Chart:** Bar chart showing monthly income, max height 220dp, bar color `#0D1C2E`
- **Inquiry & Conversion Cards:** Conversion rate cards with percentage progress indicator

---

## 3. Discrepancy & Consistency Analysis

1. **Cross-Page Mirrored Frames:**
   - 35 mobile screens are mirrored between **Page 1 (Landing Page & Web Admin)** and **Page 2 (Chủ trọ) / Page 3 (Sinh viên)**.
   - Page 1 acts as an aggregated master canvas, while Page 2 and Page 3 organize the flows by user persona.
2. **Missing Screens in Current Android Project:**
   - **Escrow Contract & Deposit Protection** (`184:1901`)
   - **Utility Meter Billing & Calculation** (`184:2077`)
   - **Invoice Preview with VietQR Banking Code** (`188:1761`)
   - **Asset Handover 360° Inspection Report** (`188:2041`)
3. **Visual Inconsistencies to Rectify in Android:**
   - **Background:** Update fragment layouts to strictly use `@color/bg_warm_cream` (`#FDF9F2`) rather than pure white.
   - **Buttons & Active Highlights:** Update primary action buttons from generic blacks to Figma Navy Blue `@color/primary_blue` (`#0D1C2E`).
   - **Corner Radii:** Standardize all form inputs to `12dp` (`@drawable/bg_input_field`) and cards to `16dp` (`@drawable/bg_card_rounded`).
