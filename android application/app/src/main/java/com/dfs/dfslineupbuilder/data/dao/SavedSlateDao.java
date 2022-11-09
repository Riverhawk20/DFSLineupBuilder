package com.dfs.dfslineupbuilder.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.dfs.dfslineupbuilder.data.model.SavedPlayer;
import com.dfs.dfslineupbuilder.data.model.SavedSlate;
import com.dfs.dfslineupbuilder.data.model.SavedSlateWithSavedPlayer;
import com.dfs.dfslineupbuilder.data.model.SlateWithPlayers;

import java.util.List;

@Dao
public interface SavedSlateDao {

    @Query("SELECT * FROM saved_slate WHERE userId = :userId")
    LiveData<List<SavedSlate>> getAll(String userId);

    @Query("SELECT * FROM saved_slate WHERE SlateId IN (:slateIds)")
    List<SavedSlate> loadAllByIds(int[] slateIds);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<SavedSlate> slateList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPlayer(SavedPlayer player);

    @Delete
    void delete(SavedSlate slate);

    @Query("SELECT * FROM saved_slate WHERE SlateId = :SlateId")
    LiveData<List<SavedSlateWithSavedPlayer>> getSavedSlateWithSavedPlayer(int SlateId);
}
