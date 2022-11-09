package com.dfs.dfslineupbuilder.data.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dfs.dfslineupbuilder.data.EntityRoomDatabase;
import com.dfs.dfslineupbuilder.data.dao.SlateDao;
import com.dfs.dfslineupbuilder.data.model.Player;
import com.dfs.dfslineupbuilder.data.model.Slate;
import com.dfs.dfslineupbuilder.data.model.SlateWithPlayers;
import com.dfs.dfslineupbuilder.retrofit.APIClient;
import com.dfs.dfslineupbuilder.retrofit.APIInterface;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SlateRepository {

    private static EntityRoomDatabase db;
    private LiveData<List<Slate>> allSlates;
    private LiveData<List<Player>> allPlayers;

    public SlateRepository(Application application){
        db = EntityRoomDatabase.getDatabase(application.getApplicationContext());
        allSlates = db.slateDao().getAll();
    }

    public void insert(List<Slate> slateList){
        new InsertAsyncTask(db).execute(slateList);
    }

    public Slate getSlate(int slateId){
        Slate s = null;
        try {
            s = (new GetAsyncTask(db)).execute(slateId).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return s;
    }
    public void delete(Slate slate){
        Log.d("delete slate","deleting a task");
        new DeleteAsyncTask(db).execute(slate);
    }

    public LiveData<List<Slate>> getSlates(){
        Log.i("slate repo", "got slates: "+allSlates.getValue());
        return allSlates;
    }

    public LiveData<List<SlateWithPlayers>> getPlayers(int SlateId){
        Log.i("player", "getting player");
        return db.slateDao().getSlateWithPlayer(SlateId);
    }

    static class InsertAsyncTask extends AsyncTask<List<Slate>,Void,Void> {
        private SlateDao slateDao;
        private final WeakReference<EntityRoomDatabase> entityRoomDatabaseWeakReference;
        InsertAsyncTask(EntityRoomDatabase entityRoomDatabase)
        {
            this.entityRoomDatabaseWeakReference = new WeakReference<>(entityRoomDatabase);
            slateDao= entityRoomDatabaseWeakReference.get().slateDao();
        }
        @Override
        protected Void doInBackground(List<Slate>... lists) {
            slateDao.insert(lists[0]);
            return null;
        }
    }

    static class DeleteAsyncTask extends AsyncTask<Slate,Void,Void> {
        private SlateDao slateDao;
        private final WeakReference<EntityRoomDatabase> entityRoomDatabaseWeakReference;
        DeleteAsyncTask(EntityRoomDatabase entityRoomDatabase)
        {
            this.entityRoomDatabaseWeakReference = new WeakReference<>(entityRoomDatabase);
            slateDao= entityRoomDatabaseWeakReference.get().slateDao();
        }
        @Override
        protected Void doInBackground(Slate... lists) {
            slateDao.delete((lists[0]));
            return null;
        }
    }

    static class GetAsyncTask extends AsyncTask<Integer,Void,Slate> {
        private SlateDao slateDao;
        private final WeakReference<EntityRoomDatabase> entityRoomDatabaseWeakReference;
        GetAsyncTask(EntityRoomDatabase entityRoomDatabase)
        {
            this.entityRoomDatabaseWeakReference = new WeakReference<>(entityRoomDatabase);
            slateDao= entityRoomDatabaseWeakReference.get().slateDao();
        }
        @Override
        protected Slate doInBackground(Integer... integers) {
            return slateDao.getSlate(integers[0]);
        }
    }

    static class DeleteAsyncTask2 extends AsyncTask<Slate,Void,Void> {
        private SlateDao slateDao;
        private final WeakReference<EntityRoomDatabase> entityRoomDatabaseWeakReference;
        DeleteAsyncTask2(EntityRoomDatabase entityRoomDatabase)
        {
            this.entityRoomDatabaseWeakReference = new WeakReference<>(entityRoomDatabase);
            slateDao= entityRoomDatabaseWeakReference.get().slateDao();
        }
        @Override
        protected Void doInBackground(Slate... lists) {
            slateDao.delete((lists[0]));
            return null;
        }
    }

}

