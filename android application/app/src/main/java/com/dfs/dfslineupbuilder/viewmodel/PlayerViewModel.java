package com.dfs.dfslineupbuilder.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dfs.dfslineupbuilder.data.model.Player;

import java.util.List;

public class PlayerViewModel extends AndroidViewModel {

    private LiveData<List<Player>> allPlayers;

    public PlayerViewModel(@NonNull Application application) {
        super(application);
    }

    public void insert(Player player)
    {

    }

    public LiveData<List<Player>> getAllPlayers()
    {
        return allPlayers;
    }
}
