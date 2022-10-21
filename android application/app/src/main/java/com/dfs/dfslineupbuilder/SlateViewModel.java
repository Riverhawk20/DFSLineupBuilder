package com.dfs.dfslineupbuilder;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dfs.dfslineupbuilder.data.SlateRepository;
import com.dfs.dfslineupbuilder.data.model.Slate;

import java.util.List;

public class SlateViewModel extends AndroidViewModel {

    private SlateRepository slateRepository;
    private LiveData<List<Slate>> allSlates;

    public SlateViewModel(@NonNull Application application) {
        super(application);
        slateRepository=new SlateRepository(application);
        allSlates=slateRepository.getSlates();
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
}
