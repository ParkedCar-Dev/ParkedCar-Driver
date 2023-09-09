package com.example.parkedcardriver.Model.APIService;

import com.example.parkedcardriver.Model.BookingSlotModel;
import com.example.parkedcardriver.Model.BookingSlotRequestModel;
import com.example.parkedcardriver.Model.RequestBody.BookingGeneralRequestBody;
import com.example.parkedcardriver.Model.RequestBody.BookingSlotRequestRequestBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface BookingSlotService {
    @POST("booking/request")
    Call<BookingSlotRequestModel> sendBookingSlotRequest(@Body BookingSlotRequestRequestBody bookingSlotRequestRequestBody);

    @POST("booking/getBooking")
    Call<BookingSlotModel> getBooking(@Body BookingGeneralRequestBody bookingGeneralRequestBody);
}
