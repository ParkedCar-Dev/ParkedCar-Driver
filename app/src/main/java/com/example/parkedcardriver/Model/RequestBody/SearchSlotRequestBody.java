package com.example.parkedcardriver.Model.RequestBody;

public class SearchSlotRequestBody {
    private Double latitude;
    private Double longitude;
    private String city;

    public SearchSlotRequestBody(Double latitude, Double longitude, String city) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
    }
}
