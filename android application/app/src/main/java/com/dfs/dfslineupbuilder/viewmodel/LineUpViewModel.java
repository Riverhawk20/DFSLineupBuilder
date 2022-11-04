package com.dfs.dfslineupbuilder.viewmodel;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dfs.dfslineupbuilder.data.model.Lineup;
import com.dfs.dfslineupbuilder.data.model.Player;
import com.dfs.dfslineupbuilder.data.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LineUpViewModel extends AndroidViewModel {
    private MutableLiveData<Integer> balanceLiveData = new MutableLiveData<>(50000);
    private MutableLiveData<Integer> positionFilledLiveData = new MutableLiveData<>(0);
    private MutableLiveData<ArrayList<Player>> Players = new MutableLiveData<>(new ArrayList<>());

    public LineUpViewModel(@NonNull Application application) {
        super(application);
        Players.getValue().add(new Player("QuarterBack", "QB","","",0,0,0));
        Players.getValue().add(new Player("RunningBack", "RB","","",0,0,0));
        Players.getValue().add(new Player("RunningBack", "RB","","",0,0,0));
        Players.getValue().add(new Player("Wide Receive", "WR","","",0,0,0));
        Players.getValue().add(new Player("Wide Receive", "WR","","",0,0,0));
        Players.getValue().add(new Player("Wide Receive", "WR","","",0,0,0));
        Players.getValue().add(new Player("Tight Ends", "TE","","",0,0,0));
        Players.getValue().add(new Player("FLEX", "FLEX","","",0,0,0));
        Players.getValue().add(new Player("Defense", "DST","","",0,0,0));
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

}
