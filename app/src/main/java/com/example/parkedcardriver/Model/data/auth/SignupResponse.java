package com.example.parkedcardriver.Model.data.auth;

import com.google.gson.annotations.SerializedName;

public class SignupResponse {
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;

    public SignupResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public SignupResponse(String message) {
        this.message = message;
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

    @Override
    public String toString(){
        return "SignupResponse{" +
                "status='" + status + '\n' +
                ", message='" + message + '\n' +
                '}';
    }
}
