package com.example.parkedcardriver.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.parkedcardriver.Model.BookingModel;
import com.example.parkedcardriver.Model.SlotModel;
import com.example.parkedcardriver.Repository.BookingSlotRepository;
import com.example.parkedcardriver.Repository.SearchSlotRepository;

import java.util.ArrayList;

public class BookingRequestViewModel extends ViewModel {
    MutableLiveData<Integer> booking_id;
    MutableLiveData<BookingModel> bookingData;
    MutableLiveData<String> bookingStatus;
    MutableLiveData<String> paymentResponseStatus;
    MutableLiveData<String> currentPaymentStatus;
    MutableLiveData<String> cancellationStatus;

    BookingSlotRepository bookingSlotRepository;

    public BookingRequestViewModel(){
        booking_id = new MutableLiveData<>();
        bookingData = new MutableLiveData<>();
        bookingStatus = new MutableLiveData<>();
        paymentResponseStatus = new MutableLiveData<>();
        currentPaymentStatus = new MutableLiveData<>();
        cancellationStatus = new MutableLiveData<>();
    }

    public void setBookingSlotRepository(BookingSlotRepository bookingSlotRepository) {
        this.bookingSlotRepository = bookingSlotRepository;
    }

    public MutableLiveData<Integer> getBookingId() {
        return booking_id;
    }

    public MutableLiveData<BookingModel> getBookingDetails() {
        return bookingData;
    }

    public MutableLiveData<String> getBookingStatus() {
        return bookingStatus;
    }

    public MutableLiveData<String> getPaymentResponseStatus() {
        return paymentResponseStatus;
    }

    public MutableLiveData<String> getPaymentStatus() {
        return currentPaymentStatus;
    }

    public MutableLiveData<String> getCancellationStatus() {
        return cancellationStatus;
    }

    public void sendBookingSlotRequest(int space_id, long from_time, long to_time){
        bookingSlotRepository.sendBookingSlotRequest(space_id, from_time, to_time, booking_id);
    }

    public void getBookingDataRequest(int booking_id){
        bookingSlotRepository.getBooking(booking_id, bookingData);
    }

    public void getBookingRequestStatus(int booking_id){
        bookingSlotRepository.getBookingStatus(booking_id, bookingStatus);
    }

    public void payBooking(int bookingId) {
        bookingSlotRepository.payBooking(bookingId, paymentResponseStatus);
    }

    public void paymentStatus(int bookingId) {
        bookingSlotRepository.getPaymentStatus(bookingId, currentPaymentStatus);
    }

    public void cancelStatus(int bookingId) {
        bookingSlotRepository.getCancelStatus(bookingId, cancellationStatus);
    }

    public void clearComposite(){
        bookingSlotRepository.clearComposite();
    }
}
