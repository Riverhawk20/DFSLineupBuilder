package com.dfs.dfslineupbuilder.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dfs.dfslineupbuilder.data.model.Lineup;
import com.dfs.dfslineupbuilder.data.model.User;

import java.util.List;

public class LineUpViewModel extends AndroidViewModel {
    private int balance = 50000;
    private int positionFilled = 0;
    private MutableLiveData<Integer> balanceLiveData = new MutableLiveData<>(balance);
    private MutableLiveData<Integer> positionFilledLiveData = new MutableLiveData<>(positionFilled);

    public LineUpViewModel(@NonNull Application application) {
        super(application);
    }

    public void insert(Lineup lineup)
    {

    }

    public MutableLiveData<Integer> getBalanceLiveData(){
        return balanceLiveData;
    }

    public MutableLiveData<Integer> getPositionFilledLiveData(){
        return positionFilledLiveData;
    }

    public void subtractBalance(int amount){
        balance -= amount;
    }

    public void incrementPositionFilled(){
        positionFilled+=1;
    }

}
