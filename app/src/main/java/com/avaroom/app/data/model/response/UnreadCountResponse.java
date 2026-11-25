package com.avaroom.app.data.model.response;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Model phản hồi tổng số tin nhắn chưa đọc của người dùng (GET /api/chat/unread-count)
 */
public class UnreadCountResponse implements Serializable {

    @SerializedName("unread_count")
    private int unreadCount;

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }
}
