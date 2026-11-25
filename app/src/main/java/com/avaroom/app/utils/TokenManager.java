package com.avaroom.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {
    // Phải dùng đúng tên pref và key này để tương thích với LoginFragment (origin/main)
    private static final String PREF_NAME = "AvaroomAuth";
    private static final String KEY_ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String KEY_REFRESH_TOKEN = "REFRESH_TOKEN";
    private static final String KEY_USER_ROLE = "USER_ROLE";
    private static final String KEY_USER_NAME = "USER_NAME";
    private static final String KEY_USER_EMAIL = "USER_EMAIL";
    private static final String KEY_USER_ID = "USER_ID";

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Trả về "Bearer <token>" để dùng trực tiếp trong @Header("Authorization").
     * Trả về null nếu chưa đăng nhập.
     */
    public static String getToken(Context context) {
        String token = getAccessToken(context);
        if (token == null || token.isEmpty()) return null;
        return "Bearer " + token;
    }

    public static String getAccessToken(Context context) {
        return getPrefs(context).getString(KEY_ACCESS_TOKEN, null);
    }

    public static String getRefreshToken(Context context) {
        return getPrefs(context).getString(KEY_REFRESH_TOKEN, null);
    }

    public static String getUserRole(Context context) {
        return getPrefs(context).getString(KEY_USER_ROLE, null);
    }

    public static String getUserName(Context context) {
        return getPrefs(context).getString(KEY_USER_NAME, null);
    }

    public static String getUserEmail(Context context) {
        return getPrefs(context).getString(KEY_USER_EMAIL, null);
    }

    public static long getUserId(Context context) {
        return getPrefs(context).getLong(KEY_USER_ID, -1L);
    }

    /**
     * Lưu session sau khi đăng nhập thành công.
     * Dùng cùng key với LoginFragment trên origin/main.
     */
    public static void saveSession(Context context, String accessToken,
                                   String refreshToken, String role) {
        saveSession(context, -1L, accessToken, refreshToken, role, null, null);
    }

    public static void saveSession(Context context, String accessToken,
                                   String refreshToken, String role,
                                   String fullName, String email) {
        saveSession(context, -1L, accessToken, refreshToken, role, fullName, email);
    }

    public static void saveSession(Context context, long userId,
                                   String accessToken, String refreshToken,
                                   String role, String fullName, String email) {
        SharedPreferences.Editor editor = getPrefs(context).edit()
                .putLong(KEY_USER_ID, userId)
                .putString(KEY_ACCESS_TOKEN, accessToken)
                .putString(KEY_REFRESH_TOKEN, refreshToken)
                .putString(KEY_USER_ROLE, role);
        if (fullName != null) editor.putString(KEY_USER_NAME, fullName);
        if (email != null) editor.putString(KEY_USER_EMAIL, email);
        editor.apply();
    }

    /** Xóa toàn bộ session khi đăng xuất. */
    public static void clearSession(Context context) {
        getPrefs(context).edit().clear().apply();
    }

    /** Kiểm tra user đã đăng nhập chưa (access token tồn tại và không rỗng). */
    public static boolean isLoggedIn(Context context) {
        String token = getAccessToken(context);
        return token != null && !token.isEmpty();
    }
}
