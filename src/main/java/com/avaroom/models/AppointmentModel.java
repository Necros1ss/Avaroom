package com.avaroom.models;

public class AppointmentModel {
    private String id;
    private String roomId;
    private String roomTitle;
    private String studentName;
    private String studentPhone;
    private String hostName;
    private String date;
    private String timeSlot;
    private String status; // "Sắp tới", "Đã xác nhận", "Hoàn tất", "Đã hủy"
    private String note;

    public AppointmentModel(String id, String roomId, String roomTitle, String studentName, String studentPhone,
                            String hostName, String date, String timeSlot, String status, String note) {
        this.id = id;
        this.roomId = roomId;
        this.roomTitle = roomTitle;
        this.studentName = studentName;
        this.studentPhone = studentPhone;
        this.hostName = hostName;
        this.date = date;
        this.timeSlot = timeSlot;
        this.status = status;
        this.note = note;
    }

    public String getId() { return id; }
    public String getRoomId() { return roomId; }
    public String getRoomTitle() { return roomTitle; }
    public String getStudentName() { return studentName; }
    public String getStudentPhone() { return studentPhone; }
    public String getHostName() { return hostName; }
    public String getDate() { return date; }
    public String getTimeSlot() { return timeSlot; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getNote() { return note; }
}
