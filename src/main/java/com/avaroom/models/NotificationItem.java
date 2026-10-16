package com.avaroom.models;

public class NotificationItem {
    private String id;
    private String title;
    private String content;
    private String time;
    private String type; // "appointment", "billing", "chat", "system"
    private boolean isRead;

    public NotificationItem(String id, String title, String content, String time, String type, boolean isRead) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.time = time;
        this.type = type;
        this.isRead = isRead;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getTime() { return time; }
    public String getType() { return type; }
    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { this.isRead = read; }
}
