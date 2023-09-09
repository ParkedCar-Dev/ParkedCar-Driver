package com.example.parkedcardriver.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BookingSlotModel {
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("booking_id")
    private int booking_id;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(int booking_id) {
        this.booking_id = booking_id;
    }

    @Override
    public String toString() {
        return "SearchSlotModel{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", booking_id=" + booking_id +
                '}';
    }
}
