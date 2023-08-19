package com.example.parkedcardriver.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SearchSlotModel {
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("spaces")
    private ArrayList<SlotModel> spaces;

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

    public ArrayList<SlotModel> getSpaces() {
        return spaces;
    }

    public void setSpaces(ArrayList<SlotModel> spaces) {
        this.spaces = spaces;
    }

    @Override
    public String toString() {
        return "SearchSlotModel{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", spaces=" + spaces.size() +
                '}';
    }
}
