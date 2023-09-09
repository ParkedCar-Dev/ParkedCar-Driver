package com.example.parkedcardriver.view.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.parkedcardriver.R;
import com.example.parkedcardriver.view.profile.ProfileActivity;


public class HomeFragment extends Fragment {

    LinearLayout parkingBox, profileBox;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parkingBox = view.findViewById(R.id.parkingBox);
        parkingBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler(view);
            }
        });
        profileBox = view.findViewById(R.id.profileBox);
        profileBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler(view);
            }
        });
    }

    private void clickHandler(View view) {
        switch (view.getId()){
            case R.id.parkingBox:
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_mapsFragment2);
                break;
            case R.id.profileBox:
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
                this.getActivity().finish();
                break;
        }
    }
}