package com.avaroom.app.data.model.request;

import com.google.gson.annotations.SerializedName;

/**
 * Model request gửi tin nhắn mới (POST /api/chat/conversations/:id/messages)
 */
public class SendMessageRequest {

    @SerializedName("content")
    private String content;

    public SendMessageRequest(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
