package com.dfs.dfslineupbuilder.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dfs.dfslineupbuilder.data.model.Lineup;
import com.dfs.dfslineupbuilder.data.model.User;

import java.util.List;

public class LineUpViewModel extends AndroidViewModel {
    private int balance;
    private int positionFilled;
    private LiveData<List<Lineup>> allLineUps;

    public LineUpViewModel(@NonNull Application application) {
        super(application);

    }

    public void insert(Lineup lineup)
    {

    }

    public LiveData<List<Lineup>> getAllLineups()
    {
        return allLineUps;
    }
}
