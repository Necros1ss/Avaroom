package com.avaroom.app.data.model.response;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Model dữ liệu chi tiết một cuộc trò chuyện giữa Sinh viên và Chủ trọ
 */
public class ConversationResponse implements Serializable {

    @SerializedName("id")
    private long id;

    @SerializedName("student_id")
    private long studentId;

    @SerializedName("landlord_id")
    private long landlordId;

    @SerializedName("room_id")
    private Long roomId; // Có thể null

    @SerializedName("partner")
    private PartnerInfo partner;

    @SerializedName("room")
    private RoomInfo room; // Có thể null

    @SerializedName("last_message")
    private MessageResponse lastMessage; // Có thể null

    @SerializedName("unread_count")
    private int unreadCount;

    @SerializedName("last_message_at")
    private String lastMessageAt;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public long getLandlordId() {
        return landlordId;
    }

    public void setLandlordId(long landlordId) {
        this.landlordId = landlordId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public PartnerInfo getPartner() {
        return partner;
    }

    public void setPartner(PartnerInfo partner) {
        this.partner = partner;
    }

    public RoomInfo getRoom() {
        return room;
    }

    public void setRoom(RoomInfo room) {
        this.room = room;
    }

    public MessageResponse getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(MessageResponse lastMessage) {
        this.lastMessage = lastMessage;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getLastMessageAt() {
        return lastMessageAt;
    }

    public void setLastMessageAt(String lastMessageAt) {
        this.lastMessageAt = lastMessageAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Thông tin đối phương trong cuộc hội thoại (Chủ trọ đối với Sinh viên)
     */
    public static class PartnerInfo implements Serializable {

        @SerializedName("id")
        private long id;

        @SerializedName("full_name")
        private String fullName;

        @SerializedName("avatar_url")
        private String avatarUrl;

        @SerializedName("phone_number")
        private String phoneNumber;

        @SerializedName("role")
        private String role;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }

    /**
     * Tóm tắt thông tin phòng trọ đính kèm cuộc hội thoại
     */
    public static class RoomInfo implements Serializable {

        @SerializedName("id")
        private long id;

        @SerializedName("title")
        private String title;

        @SerializedName("price")
        private long price;

        @SerializedName("district")
        private String district;

        @SerializedName("city")
        private String city;

        @SerializedName("address")
        private String address;

        @SerializedName("thumbnail_url")
        private String thumbnailUrl;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public long getPrice() {
            return price;
        }

        public void setPrice(long price) {
            this.price = price;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getThumbnailUrl() {
            return thumbnailUrl;
        }

        public void setThumbnailUrl(String thumbnailUrl) {
            this.thumbnailUrl = thumbnailUrl;
        }
    }
}
