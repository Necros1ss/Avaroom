package com.avaroom.app.data.model.response;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

/**
 * Model dữ liệu một phòng trọ trả về từ API GET /api/rooms
 */
public class RoomResponse implements Serializable {

    @SerializedName("id")
    private long id;

    @SerializedName("landlord_id")
    private long landlordId;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("address")
    private String address;

    @SerializedName("district")
    private String district;

    @SerializedName("city")
    private String city;

    @SerializedName("price")
    private long price;

    @SerializedName("area")
    private float area;

    @SerializedName("room_type")
    private String roomType;

    @SerializedName("max_occupants")
    private int maxOccupants;

    @SerializedName("has_wifi")
    private boolean hasWifi;

    @SerializedName("has_ac")
    private boolean hasAc;

    @SerializedName("has_parking")
    private boolean hasParking;

    @SerializedName("has_private_bathroom")
    private boolean hasPrivateBathroom;

    @SerializedName("has_kitchen")
    private boolean hasKitchen;

    @SerializedName("has_security")
    private boolean hasSecurity;

    @SerializedName("thumbnail_url")
    private String thumbnailUrl;

    @SerializedName("image_urls")
    private List<String> imageUrls;

    @SerializedName("status")
    private String status;

    @SerializedName("average_rating")
    private Float averageRating;

    @SerializedName("review_count")
    private int reviewCount;

    @SerializedName("landlord")
    private LandlordInfo landlord;

    @SerializedName("created_at")
    private String createdAt;

    // ── Getters ────────────────────────────────────────────────────────────────

    public long getId() { return id; }
    public long getLandlordId() { return landlordId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getAddress() { return address; }
    public String getDistrict() { return district; }
    public String getCity() { return city; }
    public long getPrice() { return price; }
    public float getArea() { return area; }
    public String getRoomType() { return roomType; }
    public int getMaxOccupants() { return maxOccupants; }
    public boolean isHasWifi() { return hasWifi; }
    public boolean isHasAc() { return hasAc; }
    public boolean isHasParking() { return hasParking; }
    public boolean isHasPrivateBathroom() { return hasPrivateBathroom; }
    public boolean isHasKitchen() { return hasKitchen; }
    public boolean isHasSecurity() { return hasSecurity; }
    public String getThumbnailUrl() { return thumbnailUrl; }
    public List<String> getImageUrls() { return imageUrls; }
    public String getStatus() { return status; }
    public Float getAverageRating() { return averageRating; }
    public int getReviewCount() { return reviewCount; }
    public LandlordInfo getLandlord() { return landlord; }
    public String getCreatedAt() { return createdAt; }

    /**
     * Helper: Format giá tiền hiển thị "2.500.000 đ/tháng"
     */
    public String getFormattedPrice() {
        if (price <= 0) return "Liên hệ";
        java.text.NumberFormat fmt = java.text.NumberFormat.getNumberInstance(new java.util.Locale("vi", "VN"));
        return fmt.format(price) + " đ/tháng";
    }

    /**
     * Helper: Format diện tích "25 m²"
     */
    public String getFormattedArea() {
        if (area <= 0) return "N/A";
        return (area == Math.floor(area)) ? ((int) area) + " m²" : area + " m²";
    }

    /**
     * Helper: Lấy rating dạng string "4.5 ⭐" hoặc "Chưa có đánh giá"
     */
    public String getFormattedRating() {
        if (averageRating == null || averageRating <= 0) return "Chưa có đánh giá";
        return String.format(java.util.Locale.getDefault(), "%.1f ⭐ (%d)", averageRating, reviewCount);
    }

    // ── Nested class: Thông tin chủ trọ ───────────────────────────────────────

    public static class LandlordInfo implements Serializable {
        @SerializedName("id")
        private long id;

        @SerializedName("full_name")
        private String fullName;

        @SerializedName("avatar_url")
        private String avatarUrl;

        @SerializedName("phone_number")
        private String phoneNumber;

        public long getId() { return id; }
        public String getFullName() { return fullName; }
        public String getAvatarUrl() { return avatarUrl; }
        public String getPhoneNumber() { return phoneNumber; }
    }

    // ── Nested class: Phân trang ──────────────────────────────────────────────

    public static class PaginationInfo implements Serializable {
        @SerializedName("total")
        private int total;

        @SerializedName("page")
        private int page;

        @SerializedName("limit")
        private int limit;

        @SerializedName("total_pages")
        private int totalPages;

        public int getTotal() { return total; }
        public int getPage() { return page; }
        public int getLimit() { return limit; }
        public int getTotalPages() { return totalPages; }
    }

    // ── Wrapper response (data chứa rooms + pagination) ───────────────────────

    public static class RoomsData implements Serializable {
        @SerializedName("rooms")
        private List<RoomResponse> rooms;

        @SerializedName("pagination")
        private PaginationInfo pagination;

        public List<RoomResponse> getRooms() { return rooms; }
        public PaginationInfo getPagination() { return pagination; }
    }
}

