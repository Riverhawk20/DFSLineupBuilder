package com.dfs.dfslineupbuilder.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.dfs.dfslineupbuilder.data.model.Player;
import com.dfs.dfslineupbuilder.data.model.Slate;
import com.dfs.dfslineupbuilder.data.model.SlateWithPlayers;
import com.dfs.dfslineupbuilder.data.model.User;

import java.util.List;

@Dao
public interface SlateDao {

    @Query("SELECT * FROM slate")
    LiveData<List<Slate>> getAll();

    @Query("SELECT * FROM slate WHERE SlateId IN (:slateIds)")
    List<Slate> loadAllByIds(int[] slateIds);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Slate> slateList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPlayer(Player player);

    @Delete
    void delete(Slate slate);

    @Query("SELECT * FROM slate WHERE SlateId = :SlateId")
    LiveData<List<SlateWithPlayers>> getSlateWithPlayer(int SlateId);
}
