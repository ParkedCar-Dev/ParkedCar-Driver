package com.example.parkedcardriver.Model.data.auth;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("token")
    private String token;
    @SerializedName("refreshToken")
    private String refreshToken;

    public LoginResponse(String status, String message, String token, String refreshToken) {
        this.status = status;
        this.message = message;
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public String toString(){
        return "LoginResponse{" +
                "status='" + status + '\n' +
                ", message='" + message + '\n' +
                ", token='" + token + '\n' +
                ", refreshToken='" + refreshToken + '\n' +
                '}';
    }
}
