package com.dfs.dfslineupbuilder.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dfs.dfslineupbuilder.LoggedInUser;
import com.dfs.dfslineupbuilder.data.model.Player;
import com.dfs.dfslineupbuilder.data.model.SavedPlayer;
import com.dfs.dfslineupbuilder.data.model.SavedSlate;
import com.dfs.dfslineupbuilder.data.model.UserLineup;
import com.dfs.dfslineupbuilder.data.model.UserLineupResponse;
import com.dfs.dfslineupbuilder.data.repository.SavedPlayerRepository;
import com.dfs.dfslineupbuilder.data.repository.SavedSlateRepository;
import com.dfs.dfslineupbuilder.data.repository.SlateRepository;
import com.dfs.dfslineupbuilder.data.model.Slate;
import com.dfs.dfslineupbuilder.retrofit.APIClient;
import com.dfs.dfslineupbuilder.retrofit.APIInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SlateViewModel extends AndroidViewModel {

    private SlateRepository slateRepository;
    private LiveData<List<Slate>> allSlates;
    private SavedSlateRepository savedSlateRepository;
    private APIInterface apiInterface;
    private SavedPlayerRepository savedPlayerRepository;

    public SlateViewModel(@NonNull Application application) {
        super(application);
        slateRepository=new SlateRepository(application);
        allSlates=slateRepository.getSlates();
        savedSlateRepository = new SavedSlateRepository(application);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        savedPlayerRepository = new SavedPlayerRepository(application);
        try{
            fetchSavedLineups();
        }catch (Exception e){
            Log.d("Network","Error Fetching saved lineups");
        }

    }

    public void insert(List<Slate> list)
    {
        slateRepository.insert(list);
    }

    public void remove(Slate slate){
        slateRepository.delete(slate);
    }

    public LiveData<List<Slate>> getSlates()
    {
        return allSlates;
    }

    private void fetchSavedLineups(){
        Call<UserLineupResponse> call = apiInterface.getLineupByUser("https://d4hc2g4i31.execute-api.us-east-1.amazonaws.com/Prod/getuserlineup", LoggedInUser.getLoggedInUser(getApplication()));
        call.enqueue(new Callback<UserLineupResponse>() {
            @Override
            public void onResponse(Call<UserLineupResponse> call, Response<UserLineupResponse> response) {
                if(response.isSuccessful()) {
                    for (UserLineup lineup : response.body().Lineups) {
                        SavedSlate s = new SavedSlate(lineup.LineupId, lineup.TotalSalary, "SavedSlate", "", 0, lineup.UserId);
                        for (SavedPlayer player : lineup.players) {
                            player.savedSlateId = lineup.LineupId;
                        }

                        savedPlayerRepository.insert(lineup.players);
                        List<SavedSlate> l = new ArrayList<SavedSlate>();
                        l.add(s);
                        savedSlateRepository.insert(l);
                    }
                }else{
                    Log.i("Slate View Model", "saved lineup network call failure");
                }
            }

            @Override
            public void onFailure(Call<UserLineupResponse> call, Throwable t) {
                Log.d("Error","failed to get saved lineups");
            }
        });
    }
}
