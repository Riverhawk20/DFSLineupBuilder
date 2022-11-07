package com.dfs.dfslineupbuilder.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.dfs.dfslineupbuilder.data.model.Player;
import com.dfs.dfslineupbuilder.data.model.SavedPlayer;
import com.dfs.dfslineupbuilder.data.model.Slate;
import com.dfs.dfslineupbuilder.data.model.User;

import java.util.List;

@Dao
public interface SavedPlayerDao {

    @Query("SELECT * FROM saved_player")
    List<SavedPlayer> getAll();

    @Query("SELECT * FROM saved_player WHERE PlayerId IN (:playerIds)")
    List<SavedPlayer> loadAllByIds(int[] playerIds);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<SavedPlayer> playerList);

    @Delete
    void delete(SavedPlayer player);
}
