package com.example.parkedcardriver.Repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.parkedcardriver.Model.APIService.SearchSlotService;
import com.example.parkedcardriver.Model.RequestBody.SearchSlotRequestBody;
import com.example.parkedcardriver.Model.SearchSlotModel;
import com.example.parkedcardriver.Model.SlotModel;
import com.example.parkedcardriver.ViewModel.Retrofit.RetrofitClient;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchSlotRepository {

    private Double latitude, longitude;
    private String city;

    private ArrayList<SlotModel> slotModelArrayList = new ArrayList<>();

    private MutableLiveData<ArrayList<SlotModel>> mutableLiveData = new MutableLiveData<>();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public SearchSlotRepository(Double latitude, Double longitude, String city) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
    }

    public void clearComposite(){
        compositeDisposable.clear();
    }

    public MutableLiveData<ArrayList<SlotModel>> getSearchedSlots(){
        SearchSlotService searchSlotService = new RetrofitClient().getSearchedSlotService();

        System.out.println(latitude+" "+longitude+" "+ city);

        // Observable<SearchSlotModel> observable = searchSlotService.getSearchedSlots(new SearchSlotRequestBody(latitude, longitude, city));

//        compositeDisposable.add(
//                observable
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(searchResult -> {
//                    Log.d("Slots", searchResult.getMessage());
//                    if(searchResult.getSpaces() != null && searchResult.getSpaces().size() > 0){
//                        mutableLiveData.setValue(searchResult.getSpaces());
//                    }
//                }, throwable -> Log.e("Error on Search Slots", "Throwable " + throwable.getMessage()))
//        );

        Call<SearchSlotModel> call = searchSlotService.getSearchedSlots(new SearchSlotRequestBody(latitude, longitude, city));

        call.enqueue(new Callback<SearchSlotModel>() {
            @Override
            public void onResponse(Call<SearchSlotModel> call, Response<SearchSlotModel> response) {
                if (response.isSuccessful()) {
                    SearchSlotModel searchSlotModel = response.body();
                    Log.d("Succes or not!", searchSlotModel.getMessage());
                } else {
                    // Handle error responses here
                }
            }

            @Override
            public void onFailure(Call<SearchSlotModel> call, Throwable t) {
                // Handle network errors here
                System.out.println(t.getMessage());
            }
        });

        return mutableLiveData;
    }
}
