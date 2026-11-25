package com.avaroom.app.data.model.response;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Model dữ liệu một tin nhắn trong hệ thống Chat (dùng chung cho REST và Socket.IO)
 */
public class MessageResponse implements Serializable {

    @SerializedName("id")
    private long id;

    @SerializedName("conversation_id")
    private long conversationId;

    @SerializedName("sender_id")
    private long senderId;

    @SerializedName("content")
    private String content;

    @SerializedName("is_read")
    private boolean isRead;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("is_mine")
    private Boolean isMine; // Có trong REST, hoặc client tự tính khi nhận từ Socket

    public MessageResponse() {}

    public MessageResponse(long id, long conversationId, long senderId, String content, boolean isRead, String createdAt, Boolean isMine) {
        this.id = id;
        this.conversationId = conversationId;
        this.senderId = senderId;
        this.content = content;
        this.isRead = isRead;
        this.createdAt = createdAt;
        this.isMine = isMine;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getConversationId() {
        return conversationId;
    }

    public void setConversationId(long conversationId) {
        this.conversationId = conversationId;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isMine() {
        return isMine != null && isMine;
    }

    public void setIsMine(boolean mine) {
        isMine = mine;
    }
}
