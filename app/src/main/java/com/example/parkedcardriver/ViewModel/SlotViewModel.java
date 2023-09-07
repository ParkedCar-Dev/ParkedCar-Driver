package com.example.parkedcardriver.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.parkedcardriver.Common.Common;
import com.example.parkedcardriver.Model.SlotModel;
import com.example.parkedcardriver.Repository.SearchSlotRepository;

import java.util.ArrayList;

public class SlotViewModel extends ViewModel {

    private Double latitude, longitude;
    private String city;
    MutableLiveData<ArrayList<SlotModel>> slotModelLiveData;

    SearchSlotRepository searchSlotRepository;

    public SlotViewModel(){
        slotModelLiveData = new MutableLiveData<>();
    }

    public void setSearchSlotRepository(SearchSlotRepository searchSlotRepository) {
        this.searchSlotRepository = searchSlotRepository;
    }

    public LiveData<ArrayList<SlotModel>> getSearchedSlots() {
//        if(slotModelLiveData == null){
//            slotModelLiveData = new MutableLiveData<>();
//        }
        return slotModelLiveData;
    }

    public void setSearchedSlots(ArrayList<SlotModel> slotModelLiveData) {
        this.slotModelLiveData.setValue(slotModelLiveData);
    }

    public void searchSlots(){
        if(Common.isQuickSearch){
            searchSlotRepository.getSearchedSlots(latitude, longitude, city, slotModelLiveData);
        }
        else{
            /**
             * Advance Search
             */
            searchSlotRepository.getSearchedSlots(latitude, longitude, city, slotModelLiveData);
        }
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
