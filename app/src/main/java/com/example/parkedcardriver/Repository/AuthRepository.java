package com.example.parkedcardriver.Repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.parkedcardriver.Model.APIService.AuthService;
import com.example.parkedcardriver.Model.data.auth.LoginRequest;
import com.example.parkedcardriver.Model.data.auth.LoginResponse;
import com.example.parkedcardriver.Model.data.auth.SignupRequest;
import com.example.parkedcardriver.Model.data.auth.SignupResponse;
import com.example.parkedcardriver.ViewModel.Retrofit.RetrofitClient;
import com.example.parkedcardriver.view.auth.LoggedInUser;
import com.example.parkedcardriver.view.auth.SignupResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {
    private static final String TAG = "LOGIN_REPOSITORY";
    private static AuthRepository loginRepository;
    private AuthRepository(){}
    public static AuthRepository getInstance(){
        if(loginRepository == null) loginRepository = new AuthRepository();
        return loginRepository;
    }
    public void login(String email, String password, MutableLiveData<LoggedInUser> result){
        try{
            AuthService api = RetrofitClient.getInstance().create(AuthService.class);

            LoginRequest request = new LoginRequest(email, password);
            Call<LoginResponse> call = api.login(request);

            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if(response.isSuccessful()){
                        Log.d(TAG, "on successful response: " + response.body().toString());
                        result.postValue(new LoggedInUser(response.body()));
                    }else{
                        Log.d(TAG, "on failed response: " + response.body().toString());
                        result.postValue(new LoggedInUser(response.body().getMessage()));
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Log.d(TAG, "on failure: " + t.getMessage());
                    result.postValue(new LoggedInUser(t.getMessage()));
                }
            });
        }catch (Exception e){

        }
    }

    public void signup(String name, String email, String phone, String password, MutableLiveData<SignupResult> result){
        try{
            Log.d(TAG, "signup: " + name + " " + email + " " + phone + " " + password);
            AuthService api = RetrofitClient.getInstance().create(AuthService.class);

            SignupRequest request = new SignupRequest(name, email, phone, password);
            Call<SignupResponse> call = api.signup(request);

            call.enqueue(new Callback<SignupResponse>() {
                @Override
                public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                    if(response.isSuccessful()){
                        Log.d(TAG, "on successful response: " + response.body().toString());
                        result.postValue(new SignupResult(response.body()));
                    }else{
                        Log.d(TAG, "on failed response: " + response.body().toString());
                        result.postValue(new SignupResult(response.body()));
                    }
                }

                @Override
                public void onFailure(Call<SignupResponse> call, Throwable t) {
                    Log.d(TAG, "on failure: " + t.getMessage());
                    result.postValue(new SignupResult(t));
                }
            });
        }catch (Exception e){
            Log.d(TAG, "signup: " + e.getMessage());
        }
    }
}
