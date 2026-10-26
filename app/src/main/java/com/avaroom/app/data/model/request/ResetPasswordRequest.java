package com.avaroom.app.data.model.request;

public class ResetPasswordRequest {
    private String reset_token;
    private String new_password;
    public ResetPasswordRequest(String reset_token, String new_password) {
        this.reset_token = reset_token;
        this.new_password = new_password;
    }
    public String getResetToken() { return reset_token; }
    public String getNewPassword() { return new_password; }
}
