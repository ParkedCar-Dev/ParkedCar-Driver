package com.example.parkedcardriver.view.auth;

import android.os.Bundle;
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
import com.google.android.material.textfield.TextInputLayout;

public class SignupFragment extends Fragment {

    private static final String TAG = "SignupFragment";
    private TextInputLayout email, password, confirmPassword, name, phone;
    private Button signupButton, gotoLoginButton;
    AuthViewModel viewModel;
    public SignupFragment() {}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        email = getView().findViewById(R.id.signup_email);
        password = getView().findViewById(R.id.signup_pass);
        confirmPassword = getView().findViewById(R.id.signup_confirm_pass);
        name = getView().findViewById(R.id.signup_name);
        phone = getView().findViewById(R.id.signup_phone);
        signupButton = getView().findViewById(R.id.signup_button);
        gotoLoginButton = getView().findViewById(R.id.signup_to_login);
        viewModel = AuthViewModel.getInstance();

        signupButton.setOnClickListener((v) -> {
            String emailString = email.getEditText().getText().toString();
            String passwordString = password.getEditText().getText().toString();
            String confirmPasswordString = confirmPassword.getEditText().getText().toString();
            String nameString = name.getEditText().getText().toString();
            String phoneString = phone.getEditText().getText().toString();
            if(emailString.isEmpty()){
                email.setError("Email is required");
                return;
            }
            if(passwordString.isEmpty()){
                password.setError("Password is required");
                return;
            }
            if(confirmPasswordString.isEmpty()){
                confirmPassword.setError("Confirm Password is required");
                return;
            }
            if(nameString.isEmpty()){
                name.setError("Name is required");
                return;
            }
            if(phoneString.isEmpty()){
                phone.setError("Phone is required");
                return;
            }
            if(!passwordString.equals(confirmPasswordString)){
                confirmPassword.setError("Password and Confirm Password must be same");
                return;
            }
            viewModel.signup(nameString, emailString, phoneString, passwordString);

        });

        viewModel.getSignupResult().observe(getViewLifecycleOwner(), (result) -> {
            if(result == null) return;
            if(result.getError() != null){
                Toast.makeText(getContext(), result.getError(), Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getContext(), "Signup Successful", Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }
        });
        gotoLoginButton.setOnClickListener((v) -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_signupFragment_to_loginFragment);
        });
    }

    private void tryToSignup() {
        Toast.makeText(getContext(), "Signup", Toast.LENGTH_SHORT).show();
    }
}