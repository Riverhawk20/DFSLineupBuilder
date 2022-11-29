package com.dfs.dfslineupbuilder.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class BottomBarViewModel extends AndroidViewModel {
    private int last = 1;
    public BottomBarViewModel(@NonNull Application application){
        super(application);
    }
    public int getLast() {
        return this.last;
    }

    public void setLast(int last) {
        this.last = last;
    }

}
