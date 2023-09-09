package com.example.parkedcardriver.Model.data;

//{
//    "booking_id": 71,
//    "from_time": "1691677246179",
//    "to_time": "1691763706179",
//    "status": "completed",
//    "base_fare": 100,
//    "total_fare": 580,
//    "payment_id": "123",
//    "payment_status": "unpaid",
//    "payment_medium": "null",
//    "address": "New Address 8",
//    "city": "Sunnyvale",
//    "rating": 0,
//    "time_fare": 480
//}

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Booking implements Serializable {
    @SerializedName("booking_id")
    private int bookingId;

    @SerializedName("from_time")
    private long fromTime;

    @SerializedName("to_time")
    private long toTime;

    @SerializedName("status")
    private String status;

    @SerializedName("base_fare")
    private double baseFare;

    @SerializedName("total_fare")
    private double totalPrice;

    @SerializedName("payment_id")
    private String paymentId;

    @SerializedName("payment_status")
    private String paymentStatus;

    @SerializedName("payment_medium")
    private String paymentMedium;

    @SerializedName("address")
    private String locationAddress;

    @SerializedName("city")
    private String city;

    @SerializedName("rating")
    private double rating;

    @SerializedName("time_fare")
    private double timeFare;

    public Booking() {
        this.bookingId = 73;
        this.fromTime = 1694270178869L;
        this.toTime = 1694273778869L;
        this.status = "declined";
        this.baseFare = 100;
        this.totalPrice = 120;
        this.paymentId = "123";
        this.paymentStatus = "unpaid";
        this.paymentMedium = "credit card";
        this.locationAddress = "123 Main Street";
        this.city = "Sunnyvale";
        this.rating = 0;
        this.timeFare = 20;
    }

    public int getBookingId() {
        return bookingId;
    }

    String unixToDate(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yy hh:mm a");
        return sdf.format(date);
    }

    public String getFromTime() {
        return unixToDate(fromTime);
    }

    public String getToTime() {
        return unixToDate(toTime);
    }

    public String getStatus() {
        return status;
    }

    public double getBaseFare() {
        return baseFare;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public String getPaymentMedium() {
        return paymentMedium;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public String getCity() {
        return city;
    }

    public double getRating() {
        return rating;
    }

    public double getTimeFare() {
        return timeFare;
    }

    public String getBookingStatus() {
        return status;
    }
}
