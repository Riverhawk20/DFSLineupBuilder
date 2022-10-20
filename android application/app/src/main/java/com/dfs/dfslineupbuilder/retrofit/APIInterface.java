package com.dfs.dfslineupbuilder.retrofit;

import com.dfs.dfslineupbuilder.data.model.Slate;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface APIInterface {

    @Headers("x-api-key: FrtvVA8CqB3kl1s2GxSCY2PB4GUQzGIL7cfN56IS")
    @GET("Prod/getslates")
    Call<List<Slate>> getAllSlates();
}
