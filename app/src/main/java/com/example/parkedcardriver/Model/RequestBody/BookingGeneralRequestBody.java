package com.example.parkedcardriver.Model.RequestBody;

import com.google.gson.annotations.SerializedName;

public class BookingGeneralRequestBody {
    @SerializedName("booking_id")
    private Integer booking_id;

    public BookingGeneralRequestBody(Integer booking_id) {
        this.booking_id = booking_id;
    }

    public Integer getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(Integer booking_id) {
        this.booking_id = booking_id;
    }
}
