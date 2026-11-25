package com.avaroom.app.data.model.request;

import com.google.gson.annotations.SerializedName;

/**
 * Model request tạo hoặc lấy cuộc hội thoại với chủ trọ (POST /api/chat/conversations)
 */
public class CreateConversationRequest {

    @SerializedName("landlord_id")
    private long landlordId;

    @SerializedName("room_id")
    private Long roomId; // Có thể null nếu chat trực tiếp không gắn phòng

    public CreateConversationRequest(long landlordId) {
        this.landlordId = landlordId;
        this.roomId = null;
    }

    public CreateConversationRequest(long landlordId, Long roomId) {
        this.landlordId = landlordId;
        this.roomId = roomId;
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
}
