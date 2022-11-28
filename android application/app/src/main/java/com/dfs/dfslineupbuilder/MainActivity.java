package com.dfs.dfslineupbuilder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.dfs.dfslineupbuilder.activity.LoginActivity;
import com.dfs.dfslineupbuilder.activity.SignupActivity;
import com.dfs.dfslineupbuilder.activity.UserLandingPageActivity;
import com.dfs.dfslineupbuilder.data.model.Player;
import com.dfs.dfslineupbuilder.data.model.Slate;
import com.dfs.dfslineupbuilder.data.model.SlateTest;
import com.dfs.dfslineupbuilder.data.repository.PlayerRepository;
import com.dfs.dfslineupbuilder.data.repository.SlateRepository;
import com.dfs.dfslineupbuilder.retrofit.APIClient;
import com.dfs.dfslineupbuilder.retrofit.APIInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private PlayerRepository playerRepository;
    private SlateRepository slateRepository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        slateRepository = new SlateRepository(this.getApplication());
        playerRepository = new PlayerRepository(this.getApplication());
        try{
            networkRequest();
        }catch (Exception e){
            Log.d("Network","Error Fetching saved lineups");
        }
    }

    public void onLoginClick(View v){
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    public void onSignupClick(View v){
        Intent i = new Intent(this, SignupActivity.class);
        startActivity(i);
    }

    private void networkRequest() {

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<List<SlateTest>> call = apiInterface
                .getAllSlates("https://qkjpmd9d09.execute-api.us-east-1.amazonaws.com/Prod/getslates");

        call.enqueue(new Callback<List<SlateTest>>() {
            @Override
            public void onResponse(Call<List<SlateTest>> call, Response<List<SlateTest>> response) {
                if (response.isSuccessful()) {
                    List<Slate> slate = new ArrayList<>();
                    for (SlateTest s : response.body()) {
                        for (Player p : s.Players) {
                            p.SlateId = s.SlateId;
                        }
                        playerRepository.insert(s.Players);
                        Slate sl = new Slate(s.SeasonYear, s.SlateName, s.StartDate, s.Week);
                        sl.SlateId = s.SlateId;
                        slate.add(sl);
                    }
                    slateRepository.insert(slate);
                    Log.d("slate fragment", "onResponse: " + response.body());
                } else {
                    Log.d("slate fragment", "slate network call fail");
                }
            }

            @Override
            public void onFailure(Call<List<SlateTest>> call, Throwable t) {
                Log.e("slate fragment", "onResponse error", t);
            }
        });

    }

}