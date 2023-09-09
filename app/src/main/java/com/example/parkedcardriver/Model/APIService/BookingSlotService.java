package com.example.parkedcardriver.Model.APIService;

import com.example.parkedcardriver.Model.BookingSlotModel;
import com.example.parkedcardriver.Model.RequestBody.BookingSlotRequestRequestBody;
import com.example.parkedcardriver.Model.RequestBody.QuickSearchSlotRequestBody;
import com.example.parkedcardriver.Model.SearchSlotModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface BookingSlotService {
    @POST("booking/request")
    Call<BookingSlotModel> sendBookingSlotRequest(@Body BookingSlotRequestRequestBody bookingSlotRequestRequestBody);
}
