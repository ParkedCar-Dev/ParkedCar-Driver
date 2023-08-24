package com.example.parkedcardriver.ViewModel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.parkedcardriver.Model.APIService.AuthService;
import com.example.parkedcardriver.Model.data.auth.SignupRequest;
import com.example.parkedcardriver.Model.data.auth.SignupResponse;
import com.example.parkedcardriver.Repository.AuthRepository;
import com.example.parkedcardriver.ViewModel.Remote.RetrofitClient;
import com.example.parkedcardriver.view.auth.LoggedInUser;
import com.example.parkedcardriver.view.auth.SignupResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthViewModel extends ViewModel {
    private static final String TAG = "SIGNUP_REPOSITORY";
    private MutableLiveData<LoggedInUser> loginResult = new MutableLiveData<>();
    private AuthRepository authRepository;
    private static AuthViewModel authViewModel;
    private MutableLiveData<SignupResult> signupResult = new MutableLiveData<>();

    private AuthViewModel(AuthRepository repository) {
        this.authRepository = repository;
    }

    public static AuthViewModel getInstance() {
        if (authViewModel == null)
            authViewModel = new AuthViewModel(AuthRepository.getInstance());
        return authViewModel;
    }

    public void login(String email, String password){
        Log.d("LOGIN_VIEW_MODEL", "login: " + email + " " + password);
        authRepository.login(email, password, loginResult);
    }

    public MutableLiveData<LoggedInUser> getLoginResult() {
        return loginResult;
    }

    public void signup(String name, String email, String phone, String password) {
        authRepository.signup(name, email, phone, password, signupResult);
    }

    public MutableLiveData<SignupResult> getSignupResult() {
        return signupResult;
    }

    public void signup(String name, String email, String phone, String password, MutableLiveData<SignupResult> result){
        try{
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

        }
    }
}
