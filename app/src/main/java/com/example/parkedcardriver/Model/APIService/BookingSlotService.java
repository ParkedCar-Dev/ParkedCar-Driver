package com.example.parkedcardriver.Model.APIService;

import com.example.parkedcardriver.Model.BookingSlotModel;
import com.example.parkedcardriver.Model.BookingSlotRequestModel;
import com.example.parkedcardriver.Model.PaymentStatusModel;
import com.example.parkedcardriver.Model.RequestBody.BookingGeneralRequestBody;
import com.example.parkedcardriver.Model.RequestBody.BookingSlotRequestRequestBody;
import com.example.parkedcardriver.Model.data.BookingListRequest;
import com.example.parkedcardriver.Model.data.BookingListResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface BookingSlotService {
    @POST("booking/request")
    Call<BookingSlotRequestModel> sendBookingSlotRequest(@Body BookingSlotRequestRequestBody bookingSlotRequestRequestBody);

    @POST("booking/getBooking")
    Call<BookingSlotModel> getBooking(@Body BookingGeneralRequestBody bookingGeneralRequestBody);

    @POST("booking/getDriverBookings")
    Call<BookingListResponse> getBookingList(@Body BookingListRequest request);

    @POST("booking/payFare")
    Call<BookingSlotModel> payBooking(@Body BookingGeneralRequestBody bookingGeneralRequestBody);

    @POST("booking/paymentStatus")
    Call<PaymentStatusModel> getPaymentStatus(@Body BookingGeneralRequestBody bookingGeneralRequestBody);

    @POST("booking/status")
    Call<BookingSlotModel> getBookingStatus(@Body BookingGeneralRequestBody bookingGeneralRequestBody);

    @POST("booking/cancel")
    Call<BookingSlotModel> getCancelStatus(@Body BookingGeneralRequestBody bookingGeneralRequestBody);
}
