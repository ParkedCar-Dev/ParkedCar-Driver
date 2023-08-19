package com.example.parkedcardriver.Model.RequestBody;

import com.google.gson.annotations.SerializedName;

public class SearchSlotRequestBody {
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("city")
    private String city;

    public SearchSlotRequestBody(Double latitude, Double longitude, String city) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
