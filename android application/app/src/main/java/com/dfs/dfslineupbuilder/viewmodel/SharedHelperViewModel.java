package com.dfs.dfslineupbuilder.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dfs.dfslineupbuilder.data.model.Player;

public class SharedHelperViewModel extends ViewModel {
    private MutableLiveData<Integer> index = new MutableLiveData<Integer>(-1);
    private MutableLiveData<Player> selectedPlayer = new MutableLiveData<>();

    public MutableLiveData<Integer> getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index.setValue(index);
    }

    public MutableLiveData<Player> getSelectedPlayer() {
        return selectedPlayer;
    }

    public void setSelectedPlayer(Player selectedPlayer) {
        this.selectedPlayer.setValue(selectedPlayer);
    }
}