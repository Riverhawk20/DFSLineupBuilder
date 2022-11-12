package com.dfs.dfslineupbuilder.data.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.dfs.dfslineupbuilder.LoggedInUser;
import com.dfs.dfslineupbuilder.data.EntityRoomDatabase;
import com.dfs.dfslineupbuilder.data.dao.SavedSlateDao;
import com.dfs.dfslineupbuilder.data.dao.SlateDao;
import com.dfs.dfslineupbuilder.data.model.Player;
import com.dfs.dfslineupbuilder.data.model.SavedSlate;
import com.dfs.dfslineupbuilder.data.model.SavedSlateWithSavedPlayer;
import com.dfs.dfslineupbuilder.data.model.Slate;
import com.dfs.dfslineupbuilder.data.model.SlateWithPlayers;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SavedSlateRepository {

    private static EntityRoomDatabase db;
    private LiveData<List<SavedSlate>> allSlates;
    private LiveData<List<Player>> allPlayers;

    public SavedSlateRepository(Application application){
        db = EntityRoomDatabase.getDatabase(application.getApplicationContext());
        allSlates = db.savedSlateDao().getAll(LoggedInUser.getLoggedInUser(application.getApplicationContext()));
    }

    public void insert(List<SavedSlate> slateList){
        new InsertAsyncTask(db).execute(slateList);
    }

    public LiveData<List<SavedSlate>> getSlates(){
        Log.i("slate repo", "got slates: "+allSlates.getValue());
        return allSlates;
    }

    public LiveData<List<SavedSlateWithSavedPlayer>> getPlayers(int SlateId){
        Log.i("player", "getting player");
        return db.savedSlateDao().getSavedSlateWithSavedPlayer(SlateId);
    }

    public SavedSlate getSlate(int slateId){
        SavedSlate s = null;
        try {
            s = (new SavedSlateRepository.GetAsyncTask(db)).execute(slateId).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return s;
    }

    static class InsertAsyncTask extends AsyncTask<List<SavedSlate>,Void,Void> {
        private SavedSlateDao slateDao;
        private final WeakReference<EntityRoomDatabase> entityRoomDatabaseWeakReference;
        InsertAsyncTask(EntityRoomDatabase entityRoomDatabase)
        {
            this.entityRoomDatabaseWeakReference = new WeakReference<>(entityRoomDatabase);
            slateDao= entityRoomDatabaseWeakReference.get().savedSlateDao();
        }
        @Override
        protected Void doInBackground(List<SavedSlate>... lists) {
            slateDao.insert(lists[0]);
            return null;
        }
    }

    static class GetAsyncTask extends AsyncTask<Integer,Void,SavedSlate> {
        private SavedSlateDao slateDao;
        private final WeakReference<EntityRoomDatabase> entityRoomDatabaseWeakReference;
        GetAsyncTask(EntityRoomDatabase entityRoomDatabase)
        {
            this.entityRoomDatabaseWeakReference = new WeakReference<>(entityRoomDatabase);
            slateDao= entityRoomDatabaseWeakReference.get().savedSlateDao();
        }
        @Override
        protected SavedSlate doInBackground(Integer... integers) {
            return slateDao.getSlate(integers[0]);
        }
    }
}

