package com.dfs.dfslineupbuilder.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dfs.dfslineupbuilder.data.model.SavedSlate;
import com.dfs.dfslineupbuilder.data.repository.SavedSlateRepository;
import com.dfs.dfslineupbuilder.data.repository.SlateRepository;
import com.dfs.dfslineupbuilder.data.model.Slate;

import java.util.List;

public class SavedSlateViewModel extends AndroidViewModel {

    private SavedSlateRepository savedSlateRepository;
    private LiveData<List<SavedSlate>> allSlates;

    public SavedSlateViewModel(@NonNull Application application) {
        super(application);
        savedSlateRepository=new SavedSlateRepository(application);
        allSlates=savedSlateRepository.getSlates();
    }
    public LiveData<List<SavedSlate>> getSlates()
    {
        return allSlates;
    }
}
