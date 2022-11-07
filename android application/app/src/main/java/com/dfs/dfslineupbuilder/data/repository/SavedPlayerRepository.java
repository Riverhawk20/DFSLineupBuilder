package com.dfs.dfslineupbuilder.data.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dfs.dfslineupbuilder.data.EntityRoomDatabase;
import com.dfs.dfslineupbuilder.data.dao.PlayerDao;
import com.dfs.dfslineupbuilder.data.dao.SavedPlayerDao;
import com.dfs.dfslineupbuilder.data.dao.SavedSlateDao;
import com.dfs.dfslineupbuilder.data.dao.SlateDao;
import com.dfs.dfslineupbuilder.data.model.Player;
import com.dfs.dfslineupbuilder.data.model.SavedPlayer;
import com.dfs.dfslineupbuilder.data.model.Slate;
import com.dfs.dfslineupbuilder.data.model.SlateWithPlayers;
import com.dfs.dfslineupbuilder.retrofit.APIClient;
import com.dfs.dfslineupbuilder.retrofit.APIInterface;

import java.lang.ref.WeakReference;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SavedPlayerRepository {

    private static EntityRoomDatabase db;
    private SavedSlateDao slateDao;

    public SavedPlayerRepository(Application application){
        db = EntityRoomDatabase.getDatabase(application.getApplicationContext());
        slateDao = db.savedSlateDao();
    }

    public void insert(List<SavedPlayer> players){
        new InsertAsyncTask(db).execute(players);
    }

//    public LiveData<List<SlateWithPlayers>> getPlayers(int slate){
//        Log.i("slate", "getting players for slate: "+slate);
//        return slateDao.getSlateWithPlayer(slate);
//    }

    static class InsertAsyncTask extends AsyncTask<List<SavedPlayer>,Void,Void> {
        private SavedPlayerDao playerDao;
        private final WeakReference<EntityRoomDatabase> entityRoomDatabaseWeakReference;
        InsertAsyncTask(EntityRoomDatabase entityRoomDatabase)
        {
            this.entityRoomDatabaseWeakReference = new WeakReference<>(entityRoomDatabase);
            playerDao= entityRoomDatabaseWeakReference.get().savedPlayerDao();
        }
        @Override
        protected Void doInBackground(List<SavedPlayer>... lists) {
            playerDao.insertAll(lists[0]);
            return null;
        }
    }


}

