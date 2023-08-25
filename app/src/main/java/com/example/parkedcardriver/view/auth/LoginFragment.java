package com.example.parkedcardriver.view.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.parkedcardriver.R;
import com.example.parkedcardriver.ViewModel.AuthViewModel;
import com.example.parkedcardriver.utils.TokenManager;
import com.example.parkedcardriver.view.MainActivity;
import com.google.android.material.textfield.TextInputLayout;

public class LoginFragment extends Fragment {
    TextInputLayout email, password;
    Button loginButton, signupButton;
    AuthViewModel viewModel;
    public LoginFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        email = getView().findViewById(R.id.login_email);
        password = getView().findViewById(R.id.login_pass);

        email.getEditText().setText("user1@test.com");
        password.getEditText().setText("123456");

        loginButton = getView().findViewById(R.id.login_button);
        signupButton = getView().findViewById(R.id.login_to_signup);
        viewModel = AuthViewModel.getInstance();
        loginButton.setOnClickListener((v) -> {
            Log.d("LOGIN", "onViewCreated: " + email.getEditText().getText().toString() + " " + password.getEditText().getText().toString());
            String emailString = email.getEditText().getText().toString();
            String passwordString = password.getEditText().getText().toString();
            if(emailString.isEmpty()){
                email.setError("Email is required");
                return;
            }
            if(passwordString.isEmpty()){
                password.setError("Password is required");
                return;
            }
            viewModel.login(emailString, passwordString);
        });

        viewModel.getLoginResult().observe(getViewLifecycleOwner(), (result) -> {
            if(result == null) return;
            if(result.getError() != null){
                Toast.makeText(getContext(), result.getError(), Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                TokenManager.getInstance().setToken(result.getToken());
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        signupButton.setOnClickListener((v) -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_loginFragment_to_signupFragment2);
        });
    }
}