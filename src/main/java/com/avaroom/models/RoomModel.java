package com.avaroom.models;

import java.util.ArrayList;
import java.util.List;

public class RoomModel {
    private String id;
    private String title;
    private String address;
    private String university;
    private int distanceMeters;
    private long pricePerMonth;
    private int areaM2;
    private double rating;
    private int reviewCount;
    private String hostName;
    private String hostPhone;
    private boolean verifiedEkyc;
    private boolean hasBalcony;
    private boolean hasMezzanine;
    private boolean freeHours;
    private boolean noOwner;
    private String status; // "Còn trống", "Đã cọc", "Chờ duyệt", "Đã thuê"
    private List<String> amenities = new ArrayList<>();

    public RoomModel(String id, String title, String address, String university, int distanceMeters,
                     long pricePerMonth, int areaM2, double rating, int reviewCount, String hostName,
                     String hostPhone, boolean verifiedEkyc, String status) {
        this.id = id;
        this.title = title;
        this.address = address;
        this.university = university;
        this.distanceMeters = distanceMeters;
        this.pricePerMonth = pricePerMonth;
        this.areaM2 = areaM2;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.hostName = hostName;
        this.hostPhone = hostPhone;
        this.verifiedEkyc = verifiedEkyc;
        this.status = status;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getUniversity() { return university; }
    public int getDistanceMeters() { return distanceMeters; }
    public long getPricePerMonth() { return pricePerMonth; }
    public void setPricePerMonth(long price) { this.pricePerMonth = price; }
    public int getAreaM2() { return areaM2; }
    public void setAreaM2(int area) { this.areaM2 = area; }
    public double getRating() { return rating; }
    public int getReviewCount() { return reviewCount; }
    public String getHostName() { return hostName; }
    public String getHostPhone() { return hostPhone; }
    public boolean isVerifiedEkyc() { return verifiedEkyc; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public List<String> getAmenities() { return amenities; }
    public void addAmenity(String amenity) { this.amenities.add(amenity); }

    public String getFormattedPrice() {
        return String.format("%,d đ", pricePerMonth);
    }
}
