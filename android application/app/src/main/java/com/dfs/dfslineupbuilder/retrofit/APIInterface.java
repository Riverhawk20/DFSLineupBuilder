package com.dfs.dfslineupbuilder.retrofit;

import com.dfs.dfslineupbuilder.data.model.Regulation;
import com.dfs.dfslineupbuilder.data.model.Slate;
import com.dfs.dfslineupbuilder.data.model.SlateTest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Url;

public interface APIInterface {

    @Headers("x-api-key: FrtvVA8CqB3kl1s2GxSCY2PB4GUQzGIL7cfN56IS")
    @GET()
    Call<List<SlateTest>> getAllSlates(@Url String fullUrl);

    @Headers("x-api-key: irABhs2FmCaxjfDH8KbN34YQcGTeFaJI9JXioL5Q")
    @GET()
    Call<List<Regulation>> getStateRegulation(@Url String fullUrl);

}
