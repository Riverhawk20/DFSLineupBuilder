package com.dfs.dfslineupbuilder.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.dfs.dfslineupbuilder.data.model.Slate;
import com.dfs.dfslineupbuilder.data.model.User;

import java.util.List;

@Dao
public interface SlateDao {

    @Query("SELECT * FROM slate")
    LiveData<List<Slate>> getAll();

    @Query("SELECT * FROM slate WHERE SlateId IN (:slateIds)")
    List<Slate> loadAllByIds(int[] slateIds);

    @Insert
    void insert(List<Slate> slateList);

    @Delete
    void delete(Slate slate);
}
