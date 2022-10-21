package com.dfs.dfslineupbuilder.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.dfs.dfslineupbuilder.data.dao.SlateDao;
import com.dfs.dfslineupbuilder.data.dao.UserDao;
import com.dfs.dfslineupbuilder.data.model.Slate;
import com.dfs.dfslineupbuilder.data.model.User;

import java.util.List;

public class UserRepository {

    private UserDao userDao;
    EntityRoomDatabase db;
    private LiveData<List<User>> allUsers;

    public UserRepository(Application application){
        db = EntityRoomDatabase.getDatabase(application);
        userDao = db.userDao();
        allUsers = userDao.getAll();
    }

    public LiveData<List<User>> getAllUsers(){
        return allUsers;
    }

    public void insert(User newUser){
        EntityRoomDatabase.databaseWriteExecutor.execute(()->userDao.insert(newUser));
    }

}
