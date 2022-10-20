package com.dfs.dfslineupbuilder.retrofit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static Retrofit retrofit;

    public static Retrofit getClient(){

        String baseURl = "https://qkjpmd9d09.execute-api.us-east-1.amazonaws.com/";
        OkHttpClient client = new OkHttpClient.Builder().build();

        retrofit = new Retrofit.Builder().baseUrl(baseURl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }
}
