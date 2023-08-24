package com.example.parkedcardriver.view.auth;

import com.example.parkedcardriver.Model.data.auth.LoginResponse;

public class LoggedInUser {
    private String displayName;
    private String email;
    private String error_message;
    private String token;
    private String refreshToken;
    public LoggedInUser(String error_message) {
        this.error_message = error_message;
    }
    public String getDisplayName() {
        return displayName;
    }
    public String getError(){
        return error_message;
    }
    public String getToken() {
        return token;
    }
    public String getRefreshToken() {
        return refreshToken;
    }
    public LoggedInUser(LoginResponse response){
        setResponse(response);
    }
    public void setResponse(LoginResponse response) {
        if (response.getStatus().equals("success")) {
            this.displayName = "User";
            this.error_message = null;
            this.token = response.getToken();
            this.refreshToken = response.getRefreshToken();
        } else {
            this.displayName = null;
            this.error_message = response.getMessage();
        }
    }
}
