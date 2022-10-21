package com.dfs.dfslineupbuilder.data;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dfs.dfslineupbuilder.data.dao.SlateDao;
import com.dfs.dfslineupbuilder.data.model.Slate;
import com.dfs.dfslineupbuilder.retrofit.APIClient;
import com.dfs.dfslineupbuilder.retrofit.APIInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SlateRepository {

    private static EntityRoomDatabase db;
    private LiveData<List<Slate>> allSlates;

    public SlateRepository(Application application){
        db = EntityRoomDatabase.getDatabase(application);
        allSlates = db.slateDao().getAll();
    }

    public void insert(List<Slate> slateList){
        new InsertAsyncTask(db).execute(slateList);
    }

    public void delete(Slate slate){
        Log.d("delete slate","deleting a task");
        new DeleteAsyncTask(db).execute(slate);
    }

    public LiveData<List<Slate>> getSlates(){
        Log.i("slate repo", "got slates: "+allSlates.getValue());
        return allSlates;
    }

    static class InsertAsyncTask extends AsyncTask<List<Slate>,Void,Void> {
        private SlateDao slateDao;
        InsertAsyncTask(EntityRoomDatabase entityRoomDatabase)
        {
            slateDao= entityRoomDatabase.slateDao();
        }
        @Override
        protected Void doInBackground(List<Slate>... lists) {
            slateDao.insert(lists[0]);
            return null;
        }
    }

    static class DeleteAsyncTask extends AsyncTask<Slate,Void,Void> {
        private SlateDao slateDao;
        DeleteAsyncTask(EntityRoomDatabase entityRoomDatabase)
        {
            slateDao= entityRoomDatabase.slateDao();
        }
        @Override
        protected Void doInBackground(Slate... lists) {
            slateDao.delete((lists[0]));
            return null;
        }
    }

}

