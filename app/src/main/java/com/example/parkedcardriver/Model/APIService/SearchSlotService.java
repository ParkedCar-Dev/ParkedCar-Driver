package com.example.parkedcardriver.Model.APIService;

import com.example.parkedcardriver.Model.RequestBody.SearchSlotRequestBody;
import com.example.parkedcardriver.Model.SearchSlotModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SearchSlotService {
    @POST("search/quick")
    Call<SearchSlotModel> getSearchedSlots(@Body SearchSlotRequestBody searchSlotRequestBody);
}
