package com.example.parkedcardriver.view.profile.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.TokenWatcher;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.parkedcardriver.R;
import com.example.parkedcardriver.ViewModel.ProfileViewModel;
import com.example.parkedcardriver.utils.TokenManager;
import com.example.parkedcardriver.view.auth.AuthActivity;
import com.example.parkedcardriver.view.profile.adapters.BookingListAdapter;

public class CurrentBookingsFragment extends Fragment {
    ProfileViewModel viewModel;
    public CurrentBookingsFragment() {
        viewModel = new ProfileViewModel();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume(){
        super.onResume();
        viewModel.fetchCurrentBookings();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_current_bookings, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = getView().findViewById(R.id.current_bookings_recycler_view);
        BookingListAdapter adapter = new BookingListAdapter(viewModel);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        viewModel.getCurrentBookings().observe(getViewLifecycleOwner(), (bookings) -> {
            if (bookings != null) {
                adapter.setBookings(bookings);
                adapter.notifyDataSetChanged();
            }else if(bookings == null){
                Intent intent = new Intent(getContext(), AuthActivity.class);
                startActivity(intent);
                this.getActivity().finish();
            }
        });

        viewModel.getPaymentResponseStatus().observe(getViewLifecycleOwner(), (status) -> {
            if(status.equalsIgnoreCase("success")){
                viewModel.fetchCurrentBookings();
                adapter.notifyDataSetChanged();
            }
        });

        viewModel.fetchCurrentBookings();
    }
}