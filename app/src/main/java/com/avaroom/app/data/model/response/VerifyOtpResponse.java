package com.avaroom.app.data.model.response;

public class VerifyOtpResponse {
    private String reset_token;
    private AuthResponse.UserData user;
    private AuthResponse.TokenData tokens;

    public String getResetToken() { return reset_token; }
    public void setResetToken(String reset_token) { this.reset_token = reset_token; }

    public AuthResponse.UserData getUser() { return user; }
    public void setUser(AuthResponse.UserData user) { this.user = user; }

    public AuthResponse.TokenData getTokens() { return tokens; }
    public void setTokens(AuthResponse.TokenData tokens) { this.tokens = tokens; }
}
