package com.example.parkedcardriver.Model.APIService;

import com.example.parkedcardriver.Model.data.auth.LoginRequest;
import com.example.parkedcardriver.Model.data.auth.LoginResponse;
import com.example.parkedcardriver.Model.data.auth.SignupRequest;
import com.example.parkedcardriver.Model.data.auth.SignupResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {
    @POST("/auth/login/")
    Call<LoginResponse> login(@Body LoginRequest loginReq);

    @POST("/register/")
    Call<SignupResponse> signup(@Body SignupRequest signupReq);

}
