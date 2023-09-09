package com.example.parkedcardriver.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BookingModel implements Serializable {
    @SerializedName("booking_id")
    private Integer bookingId;
    @SerializedName("space_id")
    private Integer spaceId;
    @SerializedName("driver_id")
    private Integer driverId;
    @SerializedName("from_time")
    private String fromTime;
    @SerializedName("to_time")
    private String toTime;
    @SerializedName("status")
    private String status;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("total_price")
    private Double totalPrice;
    @SerializedName("payment_id")
    private String paymentId;
    @SerializedName("payment_status")
    private String paymentStatus;
    @SerializedName("payment_medium")
    private String paymentMedium;
    @SerializedName("medium_transaction_id")
    private String mediumTransactionId;
    @SerializedName("base_fare")
    private Double baseFare;

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public Integer getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Integer spaceId) {
        this.spaceId = spaceId;
    }

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentMedium() {
        return paymentMedium;
    }

    public void setPaymentMedium(String paymentMedium) {
        this.paymentMedium = paymentMedium;
    }

    public String getMediumTransactionId() {
        return mediumTransactionId;
    }

    public void setMediumTransactionId(String mediumTransactionId) {
        this.mediumTransactionId = mediumTransactionId;
    }

    public Double getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(Double baseFare) {
        this.baseFare = baseFare;
    }
}
