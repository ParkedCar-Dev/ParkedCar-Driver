package com.example.parkedcardriver.Model.APIService;

import com.example.parkedcardriver.Model.RequestBody.AdvanceSearchSlotRequestBody;
import com.example.parkedcardriver.Model.RequestBody.QuickSearchSlotRequestBody;
import com.example.parkedcardriver.Model.SearchSlotModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SearchSlotService {
    @POST("search/quick")
    Call<SearchSlotModel> getQuickSearchedSlots(@Body QuickSearchSlotRequestBody quickSearchSlotRequestBody);

    @POST("search/advanced")
    Call<SearchSlotModel> getAdvanceSearchedSlots(@Body AdvanceSearchSlotRequestBody advanceSearchSlotRequestBody);
}
