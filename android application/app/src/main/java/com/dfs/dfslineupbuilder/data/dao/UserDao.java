package com.dfs.dfslineupbuilder.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.dfs.dfslineupbuilder.data.model.User;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    LiveData<List<User>> getAll();

    @Query("SELECT * FROM user WHERE UserId IN (:userIds)")
    List<User> loadAllByIds(String[] userIds);

    @Query("SELECT * FROM user WHERE email IS (:email) AND passwordHash IS (:password)")
    LiveData<User> getUserByLogin(String email, String password);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Delete
    void delete(User user);
}
