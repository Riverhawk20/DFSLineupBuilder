package com.dfs.dfslineupbuilder.viewmodel;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dfs.dfslineupbuilder.LoggedInUser;
import com.dfs.dfslineupbuilder.data.model.Lineup;
import com.dfs.dfslineupbuilder.data.model.LineupPost;
import com.dfs.dfslineupbuilder.data.model.Player;
import com.dfs.dfslineupbuilder.data.model.SavedPlayer;
import com.dfs.dfslineupbuilder.data.model.SavedSlate;
import com.dfs.dfslineupbuilder.data.model.Slate;
import com.dfs.dfslineupbuilder.data.model.User;
import com.dfs.dfslineupbuilder.data.repository.SavedPlayerRepository;
import com.dfs.dfslineupbuilder.data.repository.SavedSlateRepository;
import com.dfs.dfslineupbuilder.data.repository.SlateRepository;
import com.dfs.dfslineupbuilder.retrofit.APIClient;
import com.dfs.dfslineupbuilder.retrofit.APIInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LineUpViewModel extends AndroidViewModel {
    private MutableLiveData<Integer> balanceLiveData = new MutableLiveData<>(50000);
    private MutableLiveData<Integer> positionFilledLiveData = new MutableLiveData<>(0);
    private MutableLiveData<ArrayList<Player>> Players = new MutableLiveData<>(new ArrayList<>());
    private SavedSlateRepository savedSlateRepository;
    private SlateRepository slateRepository;
    private SavedPlayerRepository savedPlayerRepository;
    private int currentSlateId;
    private Slate currentSlate;
    private APIInterface apiInterface;

    public LineUpViewModel(@NonNull Application application) {
        super(application);
        ArrayList<Player> playerList = Players.getValue();
        playerList.add(new Player("Select Player", "RB","","",0,0,0));
        playerList.add(new Player("Select Player", "RB","","",0,0,0));
        playerList.add(new Player("Select Player", "WR","","",0,0,0));
        playerList.add(new Player("Select Player", "WR","","",0,0,0));
        playerList.add(new Player("Select Player", "WR","","",0,0,0));
        playerList.add(new Player("Select Player", "TE","","",0,0,0));
        playerList.add(new Player("Select Player", "FL","","",0,0,0));
        playerList.add(new Player("Select Player", "DST","","",0,0,0));
        savedSlateRepository = new SavedSlateRepository(application);
        savedPlayerRepository = new SavedPlayerRepository(application);
        slateRepository = new SlateRepository(application);
        currentSlate = slateRepository.getSlate(76927);
        apiInterface = APIClient.getClient().create(APIInterface.class);
    }

    public void setSlateId(int id){
        currentSlateId = id;
        currentSlate = slateRepository.getSlate(currentSlateId);
    }

    public MutableLiveData<Integer> getBalanceLiveData(){
        return balanceLiveData;
    }
    public MutableLiveData<ArrayList<Player>> getPlayerLiveData(){
        return Players;
    }

    public MutableLiveData<Integer> getPositionFilledLiveData(){
        return positionFilledLiveData;
    }

    public boolean setPlayerOnLineUp(Player p, int pos){
        int diff = p.Salary - this.Players.getValue().get(pos).Salary;
        int contains = Players.getValue().stream().filter(p1->p1.PlayerId == p.PlayerId).collect(Collectors.toList()).size();
        boolean val = false;
        if(balanceLiveData.getValue() > diff && contains == 0){
            balanceLiveData.setValue(balanceLiveData.getValue() + this.Players.getValue().get(pos).Salary);;
            this.Players.getValue().set(pos,p);
            balanceLiveData.setValue(balanceLiveData.getValue() - p.Salary);;
            val = true;
            Log.d("slate", "here");
            positionFilledLiveData.setValue(Players.getValue().stream().filter(p1->p1.SlateId != 0).collect(Collectors.toList()).size());
        }
        return val;
    }

    public boolean saveLineup(){
        if(this.positionFilledLiveData.getValue() < 9){
            return false;
        }
        int id = new Random().nextInt(9999999);
        List<SavedPlayer> list = new ArrayList<>();
        List<Player> p = Players.getValue();
        int salary = 50000-balanceLiveData.getValue();
        for(Player x: p){
            list.add(new SavedPlayer(x.PlayerId+id, x.Name,x.Position,x.Team,x.Opponent,x.Salary,x.FantasyPoints,x.SlateId,id));
        }
        SavedSlate s = new SavedSlate(id,salary, currentSlate.SlateName, currentSlate.StartDate, currentSlate.Week, LoggedInUser.getLoggedInUser(getApplication()));
        savedPlayerRepository.insert(list);
        List<SavedSlate> l = new ArrayList<>();
        l.add(s);
        savedSlateRepository.insert(l);

        try{
            LineupPost post = new LineupPost(s.SlateId,0, s.TotalSalary, s.userId,list);
            Call<ResponseBody> call = apiInterface.postLineup("https://mrqwl4e43h.execute-api.us-east-1.amazonaws.com/Prod/writelineup",post);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()){
                        Log.i("lineup", "lineup added to dynamoDB");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.i("Error", "lineup not added to dynamoDB");
                }
            });

        }catch (Exception e){
            Log.i("Error", "issue with network");
        }

        return true;
    }

}
