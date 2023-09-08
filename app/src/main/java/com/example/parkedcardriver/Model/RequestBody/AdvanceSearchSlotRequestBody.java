package com.example.parkedcardriver.Model.RequestBody;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AdvanceSearchSlotRequestBody {
    @SerializedName("latitude")
    private Double latitude;
    @SerializedName("longitude")
    private Double longitude;
    @SerializedName("city")
    private String city;
    @SerializedName("from")
    private Long from;
    @SerializedName("to")
    private Long to;
    @SerializedName("price")
    private Integer price;
    @SerializedName("distance")
    private Integer distance;
    @SerializedName("security_measures")
    private ArrayList<String> securityMeasures;
    @SerializedName("auto_approve")
    private Boolean autoApprove;

    public AdvanceSearchSlotRequestBody(Double latitude, Double longitude, String city, Long from, Long to,
                                        Integer price, Integer distance, ArrayList<String> securityMeasures, Boolean autoApprove) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.from = from;
        this.to = to;
        this.price = price;
        this.distance = distance;
        this.securityMeasures = securityMeasures;
        this.autoApprove = autoApprove;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public ArrayList<String> getSecurityMeasures() {
        return securityMeasures;
    }

    public void setSecurityMeasures(ArrayList<String> securityMeasures) {
        this.securityMeasures = securityMeasures;
    }

    public Boolean getAutoApprove() {
        return autoApprove;
    }

    public void setAutoApprove(Boolean autoApprove) {
        this.autoApprove = autoApprove;
    }
}
