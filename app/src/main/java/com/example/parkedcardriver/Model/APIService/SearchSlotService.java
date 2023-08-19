package com.example.parkedcardriver.Model.APIService;

import com.example.parkedcardriver.Model.RequestBody.SearchSlotRequestBody;
import com.example.parkedcardriver.Model.SearchSlotModel;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SearchSlotService {
    @GET("search/quick")
    Call<SearchSlotModel> getSearchedSlots(
            @Body SearchSlotRequestBody searchSlotRequestBody
    );
}
