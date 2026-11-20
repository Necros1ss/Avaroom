package com.avaroom.app.data.model.response;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class AppointmentResponse implements Serializable {

    @SerializedName("id")
    private long id;

    @SerializedName("appointment_date")
    private String appointmentDate;

    @SerializedName("start_time")
    private String startTime;

    @SerializedName("end_time")
    private String endTime;

    @SerializedName("status")
    private String status; // PENDING, CONFIRMED, COMPLETED, CANCELLED, REJECTED

    @SerializedName("student_note")
    private String studentNote;

    @SerializedName("rejection_reason")
    private String rejectionReason;

    @SerializedName("cancellation_reason")
    private String cancellationReason;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("room")
    private AppointmentRoom room;

    @SerializedName("landlord")
    private AppointmentLandlord landlord;

    public static class AppointmentRoom implements Serializable {
        @SerializedName("id")
        private long id;

        @SerializedName("title")
        private String title;

        @SerializedName("price")
        private double price;

        @SerializedName("address")
        private String address;

        @SerializedName("thumbnail_url")
        private String thumbnailUrl;

        public long getId() { return id; }
        public void setId(long id) { this.id = id; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }

        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }

        public String getThumbnailUrl() { return thumbnailUrl; }
        public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }
    }

    public static class AppointmentLandlord implements Serializable {
        @SerializedName("id")
        private long id;

        @SerializedName("full_name")
        private String fullName;

        @SerializedName("phone_number")
        private String phoneNumber;

        @SerializedName("avatar_url")
        private String avatarUrl;

        public long getId() { return id; }
        public void setId(long id) { this.id = id; }

        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }

        public String getPhoneNumber() { return phoneNumber; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

        public String getAvatarUrl() { return avatarUrl; }
        public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    }

    public static class OccupiedSlotResponse implements Serializable {
        @SerializedName("start_time")
        private String startTime;

        @SerializedName("end_time")
        private String endTime;

        @SerializedName("status")
        private String status;

        public String getStartTime() { return startTime; }
        public void setStartTime(String startTime) { this.startTime = startTime; }

        public String getEndTime() { return endTime; }
        public void setEndTime(String endTime) { this.endTime = endTime; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    public static class OccupiedSlotsData implements Serializable {
        @SerializedName("room_id")
        private long roomId;

        @SerializedName("date")
        private String date;

        @SerializedName("occupied_slots")
        private List<OccupiedSlotResponse> occupiedSlots;

        public long getRoomId() { return roomId; }
        public void setRoomId(long roomId) { this.roomId = roomId; }

        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }

        public List<OccupiedSlotResponse> getOccupiedSlots() { return occupiedSlots; }
        public void setOccupiedSlots(List<OccupiedSlotResponse> occupiedSlots) { this.occupiedSlots = occupiedSlots; }
    }

    public static class AppointmentsListData implements Serializable {
        @SerializedName("total")
        private int total;

        @SerializedName("page")
        private int page;

        @SerializedName("totalPages")
        private int totalPages;

        @SerializedName("data")
        private List<AppointmentResponse> data;

        public int getTotal() { return total; }
        public void setTotal(int total) { this.total = total; }

        public int getPage() { return page; }
        public void setPage(int page) { this.page = page; }

        public int getTotalPages() { return totalPages; }
        public void setTotalPages(int totalPages) { this.totalPages = totalPages; }

        public List<AppointmentResponse> getData() { return data; }
        public void setData(List<AppointmentResponse> data) { this.data = data; }
    }

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(String appointmentDate) { this.appointmentDate = appointmentDate; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getStudentNote() { return studentNote; }
    public void setStudentNote(String studentNote) { this.studentNote = studentNote; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }

    public String getCancellationReason() { return cancellationReason; }
    public void setCancellationReason(String cancellationReason) { this.cancellationReason = cancellationReason; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public AppointmentRoom getRoom() { return room; }
    public void setRoom(AppointmentRoom room) { this.room = room; }

    public AppointmentLandlord getLandlord() { return landlord; }
    public void setLandlord(AppointmentLandlord landlord) { this.landlord = landlord; }
}
