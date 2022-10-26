package com.dfs.dfslineupbuilder.data.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.dfs.dfslineupbuilder.data.EntityRoomDatabase;
import com.dfs.dfslineupbuilder.data.dao.UserDao;
import com.dfs.dfslineupbuilder.data.model.User;

import java.lang.ref.WeakReference;
import java.util.List;

public class UserRepository {

    private UserDao userDao;
    private EntityRoomDatabase db;
    private LiveData<List<User>> allUsers;

    public UserRepository(Application application){
        db = EntityRoomDatabase.getDatabase(application.getApplicationContext());
        userDao = db.userDao();
        allUsers = userDao.getAll();
    }

    public LiveData<List<User>> getAllUsers(){
        return allUsers;
    }

    public void insert(User user){
        new UserRepository.InsertAsyncTask(db).execute(user);
    }

    static class InsertAsyncTask extends AsyncTask<User,Void,Void> {
        private final WeakReference<EntityRoomDatabase> entityRoomDatabaseWeakReference;
        private UserDao userDao;
        InsertAsyncTask(EntityRoomDatabase entityRoomDatabase)
        {
            this.entityRoomDatabaseWeakReference = new WeakReference<>(entityRoomDatabase);
            userDao= entityRoomDatabaseWeakReference.get().userDao();
        }
        @Override
        protected Void doInBackground(User... lists) {
            userDao.insert((lists[0]));
            return null;
        }
    }

}
