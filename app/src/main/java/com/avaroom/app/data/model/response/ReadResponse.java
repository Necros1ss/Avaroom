package com.avaroom.app.data.model.response;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Model phản hồi kết quả đánh dấu đã đọc (PATCH /api/chat/conversations/:id/read)
 */
public class ReadResponse implements Serializable {

    @SerializedName("conversation_id")
    private long conversationId;

    @SerializedName("updated_count")
    private int updatedCount;

    public long getConversationId() {
        return conversationId;
    }

    public void setConversationId(long conversationId) {
        this.conversationId = conversationId;
    }

    public int getUpdatedCount() {
        return updatedCount;
    }

    public void setUpdatedCount(int updatedCount) {
        this.updatedCount = updatedCount;
    }
}
