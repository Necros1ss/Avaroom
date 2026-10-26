package com.avaroom.app.data.model.response;

public class AuthResponse {
    private UserData user;
    private TokenData tokens;
    public UserData getUser() { return user; }
    public TokenData getTokens() { return tokens; }
    public static class UserData {
        private long id;
        private String email;
        private String full_name;
        private String role; // "STUDENT" hoặc "LANDLORD"
        public long getId() { return id; }
        public String getEmail() { return email; }
        public String getFullName() { return full_name; }
        public String getRole() { return role; }
    }
    public static class TokenData {
        private String access_token;
        private String refresh_token;
        public String getAccessToken() { return access_token; }
        public String getRefreshToken() { return refresh_token; }
    }
}
