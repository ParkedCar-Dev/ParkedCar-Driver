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
import retrofit2.Retrofit;

public class SearchSlotRepository {

    private Double latitude, longitude;
    private String city;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public SearchSlotRepository() {
    }

    public void clearComposite(){
        compositeDisposable.clear();
    }

    public void getSearchedSlots(double latitude, double longitude, String city, MutableLiveData<ArrayList<SlotModel>> spaceList){
        Retrofit retrofitClient = new RetrofitClient().getInstance();
        SearchSlotService searchSlotService = retrofitClient.create(SearchSlotService.class);

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
        Log.d("SEARCH_SLOT_REPO", "Before Call");
        Call<SearchSlotModel> call = searchSlotService.getSearchedSlots(new SearchSlotRequestBody(latitude, longitude, city));
        Log.d("SEARCH_SLOT_REPO", "After Call");
        call.enqueue(new Callback<SearchSlotModel>() {
            @Override
            public void onResponse(Call<SearchSlotModel> call, Response<SearchSlotModel> response) {
                if (response.isSuccessful()) {
                    SearchSlotModel searchSlotModel = response.body();
                    Log.d("SEARCH_SLOT_REPO", searchSlotModel.getMessage());
                    Log.d("SEARCH_SLOT_REPO", searchSlotModel.toString());
                    if(searchSlotModel.getSpaces() != null && searchSlotModel.getSpaces().size() > 0){
                        spaceList.setValue(searchSlotModel.getSpaces());
                    }
                } else {
                    // Handle error responses here
                    Log.d("SEARCH_SLOT_REPO", "Error Response: "+response.message());
                    if(response.code() == 401){
                        spaceList.setValue(null);
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchSlotModel> call, Throwable t) {
                // Handle network errors here
                System.out.println(t.getMessage());
                Log.d("SEARCH_SLOT_REPO", "on failure: "+ t.getMessage());
            }
        });

//        return mutableLiveData;
    }
}
