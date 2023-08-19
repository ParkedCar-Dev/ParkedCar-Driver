package com.example.parkedcardriver.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.parkedcardriver.Model.SlotModel;
import com.example.parkedcardriver.Repository.SearchSlotRepository;

import java.util.ArrayList;

public class SlotViewModel extends ViewModel {

    private Double latitude, longitude;
    private String city;

    SearchSlotRepository searchSlotRepository;

    public void setSearchSlotRepository(SearchSlotRepository searchSlotRepository) {
        this.searchSlotRepository = searchSlotRepository;
    }

    public LiveData<ArrayList<SlotModel>> getSearchedSlots(){
        return searchSlotRepository.getSearchedSlots();
    }

    public void clearComposite(){
        searchSlotRepository.clearComposite();
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
