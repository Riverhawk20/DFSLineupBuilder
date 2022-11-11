package com.dfs.dfslineupbuilder.retrofit;

import com.dfs.dfslineupbuilder.data.model.Regulation;
import com.dfs.dfslineupbuilder.data.model.Slate;
import com.dfs.dfslineupbuilder.data.model.SlateTest;
import com.dfs.dfslineupbuilder.data.model.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface APIInterface {

    @Headers("x-api-key: FrtvVA8CqB3kl1s2GxSCY2PB4GUQzGIL7cfN56IS")
    @GET()
    Call<List<SlateTest>> getAllSlates(@Url String fullUrl);

    @Headers("x-api-key: irABhs2FmCaxjfDH8KbN34YQcGTeFaJI9JXioL5Q")
    @GET()
    Call<List<Regulation>> getStateRegulation(@Url String fullUrl);

    @Headers("x-api-key: 0Iuzbfhyye2hcDlgoStch6DOKiWIbw57RTCCMLb0")
    @POST()
    Call<ResponseBody> postUser(@Url String fullUrl, @Body User user);

    @Headers("x-api-key: kFAjHwmMLA8Z2DIcVTtA3BOOS53ofYN9reGBPJL8")
    @GET()
    Call<User> getUserByEmail(@Url String fullUrl, @Query("Email") String email);

}
