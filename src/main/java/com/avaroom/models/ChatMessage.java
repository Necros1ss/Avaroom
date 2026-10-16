package com.avaroom.models;

public class ChatMessage {
    private String sender;
    private String text;
    private String time;
    private boolean isFromMe;

    public ChatMessage(String sender, String text, String time, boolean isFromMe) {
        this.sender = sender;
        this.text = text;
        this.time = time;
        this.isFromMe = isFromMe;
    }

    public String getSender() { return sender; }
    public String getText() { return text; }
    public String getTime() { return time; }
    public boolean isFromMe() { return isFromMe; }
}
