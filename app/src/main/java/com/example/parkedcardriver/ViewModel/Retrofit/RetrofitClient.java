package com.example.parkedcardriver.ViewModel.Retrofit;

import com.example.parkedcardriver.Model.APIService.SearchSlotService;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private final String BASE_URL = "https://parked-car-driver-dev-api.onrender.com/";

    private Retrofit retrofit;

    public SearchSlotService getSearchedSlotService() {
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit.create(SearchSlotService.class);
    }
}
