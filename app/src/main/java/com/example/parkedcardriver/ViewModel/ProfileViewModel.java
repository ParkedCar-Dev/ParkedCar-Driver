package com.example.parkedcardriver.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.parkedcardriver.Model.data.Booking;
import com.example.parkedcardriver.Repository.ProfileRepository;

import java.util.ArrayList;
import java.util.List;

public class ProfileViewModel extends ViewModel {
    private MutableLiveData<List<Booking>> currentBookings, previousBookings;
    private MutableLiveData<String> paymentResponseStatus;
    ProfileRepository repository;
    public ProfileViewModel(){
        repository = ProfileRepository.getInstance();
        currentBookings = new MutableLiveData<>(new ArrayList<>());
        previousBookings = new MutableLiveData<>(new ArrayList<>());
        paymentResponseStatus = new MutableLiveData<>("failed");
    }
    public void fetchCurrentBookings() {
        repository.fetchCurrentBookings(currentBookings);
    }
    public void fetchPreviousBookings(){
        repository.fetchPreviousBookings(previousBookings);
    }

    public void payBooking(int bookingId) {
        repository.payBooking(bookingId, paymentResponseStatus);
    }

    public MutableLiveData<String> getPaymentResponseStatus() {
        return paymentResponseStatus;
    }

    public MutableLiveData<List<Booking>> getCurrentBookings() {
        return currentBookings;
    }

    public MutableLiveData<List<Booking>> getPreviousBookings() {
        return previousBookings;
    }
}
