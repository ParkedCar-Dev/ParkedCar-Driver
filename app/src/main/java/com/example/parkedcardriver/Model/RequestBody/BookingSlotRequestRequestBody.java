package com.example.parkedcardriver.Model.RequestBody;

import com.google.gson.annotations.SerializedName;

public class BookingSlotRequestRequestBody {
    @SerializedName("space_id")
    private Integer space_id;
    @SerializedName("from_time")
    private Long from_time;
    @SerializedName("to_time")
    private Long to_time;

    public BookingSlotRequestRequestBody(Integer space_id, Long from_time, Long to_time) {
        this.space_id = space_id;
        this.from_time = from_time;
        this.to_time = to_time;
    }

    public Integer getSpace_id() {
        return space_id;
    }

    public void setSpace_id(Integer space_id) {
        this.space_id = space_id;
    }

    public Long getFrom_time() {
        return from_time;
    }

    public void setFrom_time(Long from_time) {
        this.from_time = from_time;
    }

    public Long getTo_time() {
        return to_time;
    }

    public void setTo_time(Long to_time) {
        this.to_time = to_time;
    }
}
