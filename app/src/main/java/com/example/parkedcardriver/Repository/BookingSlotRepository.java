package com.example.parkedcardriver.Repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.parkedcardriver.Model.APIService.BookingSlotService;
import com.example.parkedcardriver.Model.BookingModel;
import com.example.parkedcardriver.Model.BookingSlotModel;
import com.example.parkedcardriver.Model.BookingSlotRequestModel;
import com.example.parkedcardriver.Model.RequestBody.BookingGeneralRequestBody;
import com.example.parkedcardriver.Model.RequestBody.BookingSlotRequestRequestBody;
import com.example.parkedcardriver.ViewModel.Retrofit.RetrofitClient;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BookingSlotRepository {
    private static BookingSlotRepository bookingSlotRepository;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static BookingSlotRepository getInstance(){
        if(bookingSlotRepository == null){
            bookingSlotRepository = new BookingSlotRepository();
        }
        return bookingSlotRepository;
    }

    public void clearComposite(){
        compositeDisposable.clear();
    }

    public void sendBookingSlotRequest(int space_id, long from_time, long to_time, MutableLiveData<Integer> booking_id){
        Retrofit retrofitClient = new RetrofitClient().getInstance();
        BookingSlotService bookingSlotService = retrofitClient.create(BookingSlotService.class);

        Call<BookingSlotRequestModel> call = bookingSlotService.sendBookingSlotRequest(new BookingSlotRequestRequestBody(space_id, from_time, to_time));
        call.enqueue(new Callback<BookingSlotRequestModel>() {
            @Override
            public void onResponse(Call<BookingSlotRequestModel> call, Response<BookingSlotRequestModel> response) {
                if (response.isSuccessful()) {
                    BookingSlotRequestModel bookingSlotRequestModel = response.body();
                    Log.d("SEARCH_SLOT_REPO", bookingSlotRequestModel.getMessage());
                    Log.d("SEARCH_SLOT_REPO", bookingSlotRequestModel.toString());
                    booking_id.setValue(bookingSlotRequestModel.getBooking_id());
                } else {
                    // Handle error responses here
                    Log.d("SEARCH_SLOT_REPO", "Error Response: "+response.message());
                    if(response.code() == 401){
                        booking_id.setValue(null);
                    }
                }
            }

            @Override
            public void onFailure(Call<BookingSlotRequestModel> call, Throwable t) {
                // Handle network errors here
                System.out.println(t.getMessage());
                Log.d("SEARCH_SLOT_REPO", "on failure: "+ t.getMessage());
            }
        });
    }

    public void getBooking(int booking_id, MutableLiveData<BookingModel> bookingModelMutableLiveData){
        Retrofit retrofitClient = new RetrofitClient().getInstance();
        BookingSlotService bookingSlotService = retrofitClient.create(BookingSlotService.class);

        Call<BookingSlotModel> call = bookingSlotService.getBooking(new BookingGeneralRequestBody(booking_id));
        call.enqueue(new Callback<BookingSlotModel>() {
            @Override
            public void onResponse(Call<BookingSlotModel> call, Response<BookingSlotModel> response) {
                if (response.isSuccessful()) {
                    BookingSlotModel bookingSlotModel = response.body();
                    Log.d("SEARCH_SLOT_REPO", bookingSlotModel.getMessage());
                    bookingModelMutableLiveData.setValue(bookingSlotModel.getBooking());
                } else {
                    // Handle error responses here
                    Log.d("SEARCH_SLOT_REPO", "Error Response: "+response.message());
                    if(response.code() == 401){
                        bookingModelMutableLiveData.setValue(null);
                    }
                }
            }

            @Override
            public void onFailure(Call<BookingSlotModel> call, Throwable t) {
                // Handle network errors here
                System.out.println(t.getMessage());
                Log.d("SEARCH_SLOT_REPO", "on failure: "+ t.getMessage());
            }
        });
    }
}
