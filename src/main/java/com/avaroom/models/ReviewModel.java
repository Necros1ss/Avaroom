package com.avaroom.models;

public class ReviewModel {
    private String authorName;
    private String university;
    private int stars;
    private String date;
    private String comment;
    private String hostReply;

    public ReviewModel(String authorName, String university, int stars, String date, String comment, String hostReply) {
        this.authorName = authorName;
        this.university = university;
        this.stars = stars;
        this.date = date;
        this.comment = comment;
        this.hostReply = hostReply;
    }

    public String getAuthorName() { return authorName; }
    public String getUniversity() { return university; }
    public int getStars() { return stars; }
    public String getDate() { return date; }
    public String getComment() { return comment; }
    public String getHostReply() { return hostReply; }
    public void setHostReply(String hostReply) { this.hostReply = hostReply; }
}
