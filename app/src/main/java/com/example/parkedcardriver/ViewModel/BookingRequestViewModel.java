package com.example.parkedcardriver.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.parkedcardriver.Model.SlotModel;
import com.example.parkedcardriver.Repository.BookingSlotRepository;
import com.example.parkedcardriver.Repository.SearchSlotRepository;

import java.util.ArrayList;

public class BookingRequestViewModel extends ViewModel {
    MutableLiveData<Integer> booking_id;

    BookingSlotRepository bookingSlotRepository;

    public BookingRequestViewModel(){
        booking_id = new MutableLiveData<>();
    }

    public void setBookingSlotRepository(BookingSlotRepository bookingSlotRepository) {
        this.bookingSlotRepository = bookingSlotRepository;
    }

    public LiveData<Integer> getBookingId() {
        return booking_id;
    }

    public void sendBookingSlotRequest(int space_id, long from_time, long to_time){
        bookingSlotRepository.sendBookingSlotRequest(space_id, from_time, to_time, booking_id);
    }

    public void clearComposite(){
        bookingSlotRepository.clearComposite();
    }
}
