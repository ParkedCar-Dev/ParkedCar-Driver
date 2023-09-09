package com.example.parkedcardriver.Model;

import com.google.gson.annotations.SerializedName;

public class PaymentStatusModel {
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("payment_status")
    private String payment_status;

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

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }
}
