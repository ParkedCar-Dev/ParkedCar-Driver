package com.example.parkedcardriver.Repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.parkedcardriver.Model.APIService.BookingSlotService;
import com.example.parkedcardriver.Model.BookingSlotModel;
import com.example.parkedcardriver.Model.RequestBody.BookingGeneralRequestBody;
import com.example.parkedcardriver.Model.data.Booking;
import com.example.parkedcardriver.Model.data.BookingListRequest;
import com.example.parkedcardriver.Model.data.BookingListResponse;
import com.example.parkedcardriver.ViewModel.Retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileRepository {
    private static ProfileRepository instance;
    private ProfileRepository() { }
    public static ProfileRepository getInstance() {
        if(instance == null) {
            instance = new ProfileRepository();
        }
        return instance;
    }

    private void genericFetchBookings(MutableLiveData<List<Booking>> bookings, String status) {
        Retrofit retrofitClient = new RetrofitClient().getInstance();
        BookingSlotService bookingSlotService = retrofitClient.create(BookingSlotService.class);

        Call<BookingListResponse> call = bookingSlotService.getBookingList(new BookingListRequest(-1, status));
        call.enqueue(new Callback<BookingListResponse>() {
            @Override
            public void onResponse(Call<BookingListResponse> call, Response<BookingListResponse> response) {
                Log.d("BookingListResponse", "onResponse: " + response.body());
                if(response.isSuccessful()) {
                    bookings.setValue(response.body().getBookings());
                }else  if(response.code() == 401){
                    bookings.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<BookingListResponse> call, Throwable t) {
                Log.d("BookingListResponse", "onFailure: " + t.getMessage());
            }
        });
    }

    public void fetchCurrentBookings(MutableLiveData<List<Booking>> bookings) {
        genericFetchBookings(bookings, "current");
    }

    public void fetchPreviousBookings(MutableLiveData<List<Booking>> bookings) {
        genericFetchBookings(bookings, "past");
    }

    public void payBooking(int bookingId, MutableLiveData<String> paymentResponseStatus) {
        Retrofit retrofitClient = new RetrofitClient().getInstance();
        BookingSlotService bookingSlotService = retrofitClient.create(BookingSlotService.class);

        Call<BookingSlotModel> call = bookingSlotService.payBooking(new BookingGeneralRequestBody(bookingId));
        call.enqueue(new Callback<BookingSlotModel>() {
            @Override
            public void onResponse(Call<BookingSlotModel> call, Response<BookingSlotModel> response) {
                Log.d("BookingListResponse", "onResponse: " + response.body());
                if(response.isSuccessful() && response.body() != null) {
                    paymentResponseStatus.setValue(response.body().getStatus());
                }
            }

            @Override
            public void onFailure(Call<BookingSlotModel> call, Throwable t) {
                Log.d("BookingListResponse", "onFailure: " + t.getMessage());
            }
        });
    }
}
