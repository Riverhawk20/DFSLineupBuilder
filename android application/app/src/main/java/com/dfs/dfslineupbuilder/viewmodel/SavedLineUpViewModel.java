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
import com.dfs.dfslineupbuilder.data.model.SavedPlayer;
import com.dfs.dfslineupbuilder.data.model.SavedSlate;
import com.dfs.dfslineupbuilder.data.model.SavedSlateWithSavedPlayer;
import com.dfs.dfslineupbuilder.data.model.Slate;
import com.dfs.dfslineupbuilder.data.model.SlateWithPlayers;
import com.dfs.dfslineupbuilder.data.model.User;
import com.dfs.dfslineupbuilder.data.repository.SavedPlayerRepository;
import com.dfs.dfslineupbuilder.data.repository.SavedSlateRepository;
import com.dfs.dfslineupbuilder.data.repository.SlateRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class SavedLineUpViewModel extends AndroidViewModel {
    private MutableLiveData<Integer> balanceLiveData = new MutableLiveData<>(50000);
    private LiveData<List<SavedSlateWithSavedPlayer>> Players;
    private SavedSlateRepository slateRepository;
    private int currentSlateId;
    private Slate currentSlate;

    public SavedLineUpViewModel(@NonNull Application application) {
        super(application);
        slateRepository = new SavedSlateRepository(application);
    }

    public void setSlateId(int id){
        currentSlateId = id;
        Players = slateRepository.getPlayers(currentSlateId);
    }

    public MutableLiveData<Integer> getBalanceLiveData(){
        return balanceLiveData;
    }
    public LiveData<List<SavedSlateWithSavedPlayer>> getPlayerLiveData(){
        return Players;
    }
}
