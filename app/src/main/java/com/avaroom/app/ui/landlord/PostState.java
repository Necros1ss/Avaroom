package com.avaroom.app.ui.landlord;

/**
 * Simple static state holder để thông báo giữa các Landlord Fragment
 * khi tạo bài đăng mới thành công.
 * LandlordDashboardFragment đọc giá trị này khi resume để refresh danh sách.
 */
public class PostState {
    private static boolean newPostAdded = false;

    public static boolean isNewPostAdded() {
        return newPostAdded;
    }

    public static void setNewPostAdded(boolean value) {
        newPostAdded = value;
    }
}
