package com.avaroom.services;

import com.avaroom.models.*;
import java.util.*;

public class DataService {
    private static DataService instance;

    private final List<RoomModel> rooms = new ArrayList<>();
    private final Set<String> savedRoomIds = new HashSet<>();
    private final List<AppointmentModel> appointments = new ArrayList<>();
    private final List<ChatMessage> studentChatMessages = new ArrayList<>();
    private final List<ChatMessage> landlordChatMessages = new ArrayList<>();
    private final List<NotificationItem> notifications = new ArrayList<>();
    private final List<ReviewModel> reviews = new ArrayList<>();

    // User Session
    private String currentUserRole = "STUDENT"; // "STUDENT" or "LANDLORD"
    private String currentUserName = "Nguyễn Thanh Tùng";
    private String currentUserPhone = "0912 345 678";
    private String currentUserEmail = "tung.nt225431@sis.hust.edu.vn";
    private String currentUniversity = "ĐH Bách Khoa Hà Nội (HUST)";
    private boolean isEkycVerified = true;

    // Landlord profile
    private String landlordName = "Cô Hoàng Thị Ánh Tuyết";
    private String landlordPhone = "0988 765 432";
    private String landlordBank = "Techcombank • 19036789999012";

    public static synchronized DataService getInstance() {
        if (instance == null) {
            instance = new DataService();
        }
        return instance;
    }

    private DataService() {
        initMockData();
    }

    private void initMockData() {
        // Rooms
        RoomModel r1 = new RoomModel(
                "AVR-90412",
                "Studio ban công thoáng mát gần ĐH Bách Khoa",
                "Số 42 Ngõ 187 Tạ Quang Bửu, P. Bách Khoa, Hai Bà Trưng, Hà Nội",
                "ĐH Bách Khoa Hà Nội",
                250,
                4200000L,
                28,
                4.8,
                42,
                "Cô Hoàng Thị Ánh Tuyết",
                "0988 765 432",
                true,
                "Còn trống"
        );
        r1.addAmenity("Điều hòa Inverter");
        r1.addAmenity("Tủ lạnh 2 cánh");
        r1.addAmenity("Máy giặt riêng ban công");
        r1.addAmenity("Bình nóng lạnh 30L");
        r1.addAmenity("Sạc xe điện thông minh");
        r1.addAmenity("Camera an ninh 24/7");
        r1.addAmenity("Khóa vân tay hai lớp");
        r1.addAmenity("Không chung chủ");
        rooms.add(r1);

        RoomModel r2 = new RoomModel(
                "AVR-89210",
                "Phòng gác lửng cao ráo ban công riêng Xuân Thủy",
                "Số 15 Ngõ 130 Xuân Thủy, Cầu Giấy, Hà Nội",
                "ĐH Sư Phạm Hà Nội",
                700,
                2400000L,
                22,
                4.9,
                41,
                "Chú Nguyễn Văn Dũng",
                "0977 123 999",
                true,
                "Còn trống"
        );
        r2.addAmenity("Gác đúc cao 2m");
        r2.addAmenity("Giờ giấc tự do 24/7");
        r2.addAmenity("Không chung chủ");
        r2.addAmenity("Bình nóng lạnh");
        r2.addAmenity("Wifi Mesh tốc độ cao");
        rooms.add(r2);

        RoomModel r3 = new RoomModel(
                "AVR-91045",
                "Căn hộ mini khép kín full đồ phố Chùa Láng",
                "Ngõ 84 Chùa Láng, Láng Thượng, Đống Đa, Hà Nội",
                "ĐH Ngoại Thương",
                450,
                4000000L,
                35,
                4.7,
                28,
                "Bác Lê Thu Hà",
                "0904 555 777",
                true,
                "Chờ duyệt"
        );
        r3.addAmenity("Full nội thất cao cấp");
        r3.addAmenity("Thang máy thẻ từ");
        r3.addAmenity("Bếp nấu riêng biệt");
        rooms.add(r3);

        RoomModel r4 = new RoomModel(
                "AVR-92011",
                "Phòng trọ sinh viên tiện nghi Trần Đại Nghĩa",
                "Số 8 Ngõ 195 Trần Đại Nghĩa, Hai Bà Trưng, Hà Nội",
                "ĐH Kinh Tế Quốc Dân (NEU)",
                300,
                2800000L,
                20,
                4.9,
                35,
                "Cô Hoàng Thị Ánh Tuyết",
                "0988 765 432",
                true,
                "Đã thuê"
        );
        rooms.add(r4);

        savedRoomIds.add("AVR-90412");

        // Appointments
        appointments.add(new AppointmentModel(
                "APT-001",
                "AVR-90412",
                "Studio ban công Tạ Quang Bửu",
                "Nguyễn Thanh Tùng",
                "0912 345 678",
                "Cô Hoàng Thị Ánh Tuyết",
                "14/10/2026",
                "16:30",
                "Đã xác nhận",
                "Cháu là Tùng K66 Bách Khoa, cháu đi cùng bạn qua xem lúc 16:30 ạ."
        ));
        appointments.add(new AppointmentModel(
                "APT-002",
                "AVR-89210",
                "Phòng gác lửng Xuân Thủy",
                "Trần Minh Quang",
                "0987 654 321",
                "Chú Nguyễn Văn Dũng",
                "16/10/2026",
                "10:00",
                "Sắp tới",
                "Em qua xem phòng vào buổi sáng trước giờ đi học ạ."
        ));

        // Chat
        studentChatMessages.add(new ChatMessage("Cô Ánh Tuyết", "Chào cháu Tùng, phòng 301 ban công vẫn còn trống nhé!", "14:20", false));
        studentChatMessages.add(new ChatMessage("Nguyễn Thanh Tùng", "Dạ cô ơi, phòng này tiền điện nước tính như nào ạ?", "14:22", true));
        studentChatMessages.add(new ChatMessage("Cô Ánh Tuyết", "Điện 3.5k/kWh theo công tơ riêng từng phòng, nước 100k/người cháu nhé, có hợp đồng Avaroom bảo lãnh rõ ràng.", "14:25", false));
        studentChatMessages.add(new ChatMessage("Nguyễn Thanh Tùng", "Dạ vâng, chiều nay 16:30 cháu qua xem phòng cô nhé!", "14:26", true));
        studentChatMessages.add(new ChatMessage("Cô Ánh Tuyết", "Ok cháu, đến ngõ 187 gọi cô bấm khóa cổng cho vào nhé.", "14:28", false));

        landlordChatMessages.addAll(studentChatMessages);

        // Notifications
        notifications.add(new NotificationItem("N1", "Lịch hẹn xem phòng", "Sinh viên Nguyễn Thanh Tùng đã đặt lịch hẹn lúc 16:30 ngày 14/10/2026.", "10 phút trước", "appointment", false));
        notifications.add(new NotificationItem("N2", "Hóa đơn điện nước tháng 10", "Chủ trọ Cô Ánh Tuyết đã gửi bảng chốt điện nước phòng #AVR-90412.", "2 giờ trước", "billing", false));
        notifications.add(new NotificationItem("N3", "Xác thực eKYC thành công", "Hồ sơ sinh viên Đại học Bách Khoa Hà Nội được cấp quyền Cọc 0đ bảo đảm.", "1 ngày trước", "system", true));

        // Reviews
        reviews.add(new ReviewModel(
                "Lê Hoàng Nam",
                "ĐH Bách Khoa • K65",
                5,
                "02/10/2026",
                "Phòng rất mới, sạch sẽ thoáng mát đúng như ảnh 360 trên app. Cô chủ trọ cực kỳ nhiệt tình, có bình chữa cháy và thang thoát hiểm chuẩn PCCC nên rất yên tâm.",
                "Cảm ơn cháu Nam nhé! Chúc cháu học tốt tại Bách Khoa."
        ));
        reviews.add(new ReviewModel(
                "Phạm Thu Thảo",
                "ĐH Kinh Tế Quốc Dân • K64",
                5,
                "28/09/2026",
                "Đi bộ ra cổng Parabol trường chỉ mất 3 phút, an ninh cực tốt với khóa vân tay hai lớp. Điện nước tính chuẩn giá nhà nước không bị chặt chém.",
                "Cảm ơn cháu gái nhiều nhé!"
        ));
        reviews.add(new ReviewModel(
                "Trần Quốc Bảo",
                "ĐH Xây Dựng • K66",
                4,
                "15/09/2026",
                "Phòng đẹp, internet wifi mesh nhanh xem bài giảng online mượt. Chỉ có ngõ vào hơi nhỏ một chút nhưng an ninh cao.",
                null
        ));
    }

    public List<RoomModel> getAllRooms() { return rooms; }

    public RoomModel getRoomById(String id) {
        for (RoomModel r : rooms) {
            if (r.getId().equals(id)) return r;
        }
        return rooms.isEmpty() ? null : rooms.get(0);
    }

    public void addRoom(RoomModel room) {
        rooms.add(0, room);
    }

    public boolean isRoomSaved(String roomId) {
        return savedRoomIds.contains(roomId);
    }

    public void toggleSaveRoom(String roomId) {
        if (savedRoomIds.contains(roomId)) {
            savedRoomIds.remove(roomId);
        } else {
            savedRoomIds.add(roomId);
        }
    }

    public List<RoomModel> getSavedRooms() {
        List<RoomModel> list = new ArrayList<>();
        for (RoomModel r : rooms) {
            if (savedRoomIds.contains(r.getId())) list.add(r);
        }
        return list;
    }

    public List<AppointmentModel> getAppointments() { return appointments; }

    public void addAppointment(AppointmentModel appointment) {
        appointments.add(0, appointment);
    }

    public List<ChatMessage> getStudentChatMessages() { return studentChatMessages; }

    public void addStudentChatMessage(String text, boolean fromMe) {
        studentChatMessages.add(new ChatMessage(fromMe ? currentUserName : "Cô Ánh Tuyết", text, "Vừa xong", fromMe));
    }

    public List<NotificationItem> getNotifications() { return notifications; }

    public int getUnreadNotificationCount() {
        int count = 0;
        for (NotificationItem n : notifications) {
            if (!n.isRead()) count++;
        }
        return count;
    }

    public void markAllNotificationsRead() {
        for (NotificationItem n : notifications) {
            n.setRead(true);
        }
    }

    public List<ReviewModel> getReviews() { return reviews; }

    public void addReviewReply(int index, String reply) {
        if (index >= 0 && index < reviews.size()) {
            reviews.get(index).setHostReply(reply);
        }
    }

    public String getCurrentUserRole() { return currentUserRole; }
    public void setCurrentUserRole(String role) { this.currentUserRole = role; }
    public String getCurrentUserName() { return currentUserName; }
    public void setCurrentUserName(String name) { this.currentUserName = name; }
    public String getCurrentUserPhone() { return currentUserPhone; }
    public void setCurrentUserPhone(String phone) { this.currentUserPhone = phone; }
    public String getCurrentUserEmail() { return currentUserEmail; }
    public void setCurrentUserEmail(String email) { this.currentUserEmail = email; }
    public String getCurrentUniversity() { return currentUniversity; }
    public boolean isEkycVerified() { return isEkycVerified; }
    public void setEkycVerified(boolean verified) { this.isEkycVerified = verified; }

    public String getLandlordName() { return landlordName; }
    public void setLandlordName(String name) { this.landlordName = name; }
    public String getLandlordPhone() { return landlordPhone; }
    public void setLandlordPhone(String phone) { this.landlordPhone = phone; }
    public String getLandlordBank() { return landlordBank; }
    public void setLandlordBank(String bank) { this.landlordBank = bank; }
}
