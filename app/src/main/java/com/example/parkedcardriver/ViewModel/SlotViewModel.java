package com.example.parkedcardriver.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.parkedcardriver.Model.SlotModel;
import com.example.parkedcardriver.Repository.SearchSlotRepository;

import java.util.ArrayList;

public class SlotViewModel extends ViewModel {

    private Double latitude, longitude;
    private String city;
    MutableLiveData<ArrayList<SlotModel>> quickSearchSlotModelLiveData;
    MutableLiveData<ArrayList<SlotModel>> advanceSearchSlotModelLiveData;

    SearchSlotRepository searchSlotRepository;

    public SlotViewModel(){
        quickSearchSlotModelLiveData = new MutableLiveData<>();
        advanceSearchSlotModelLiveData = new MutableLiveData<>();
    }

    public void setSearchSlotRepository(SearchSlotRepository searchSlotRepository) {
        this.searchSlotRepository = searchSlotRepository;
    }

    public LiveData<ArrayList<SlotModel>> getQuickSearchedSlots() {
//        if(slotModelLiveData == null){
//            slotModelLiveData = new MutableLiveData<>();
//        }
        return quickSearchSlotModelLiveData;
    }

    public LiveData<ArrayList<SlotModel>> getAdvanceSearchedSlots() {
//        if(slotModelLiveData == null){
//            slotModelLiveData = new MutableLiveData<>();
//        }
        return advanceSearchSlotModelLiveData;
    }

    public void setQuickSearchedSlots(ArrayList<SlotModel> slotModelLiveData) {
        this.quickSearchSlotModelLiveData.setValue(slotModelLiveData);
    }

    public void setAdvanceSearchedSlots(ArrayList<SlotModel> slotModelLiveData) {
        this.advanceSearchSlotModelLiveData.setValue(slotModelLiveData);
    }

    public void quickSearchSlots(){
        searchSlotRepository.getQuickSearchedSlots(latitude, longitude, city, quickSearchSlotModelLiveData);
    }

    public void advanceSearchSlots(Double latitude, Double longitude, String city, Long from, Long to, Double price,
                                   Double distance, ArrayList<String> securityMeasures, Boolean autoApprove){
        searchSlotRepository.getAdvanceSearchedSlots(latitude, longitude, city, from, to, price, distance, securityMeasures, autoApprove, advanceSearchSlotModelLiveData);
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
