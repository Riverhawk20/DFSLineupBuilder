package com.dfs.dfslineupbuilder.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.dfs.dfslineupbuilder.data.model.User;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE UserId IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    @Insert
    void insertAll(User... users);

    @Delete
    void delete(User user);
}
