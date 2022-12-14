package com.dfs.dfslineupbuilder.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.dfs.dfslineupbuilder.data.model.Player;
import com.dfs.dfslineupbuilder.data.model.Slate;
import com.dfs.dfslineupbuilder.data.model.User;

import java.util.List;

@Dao
public interface PlayerDao {

    @Query("SELECT * FROM player")
    List<Player> getAll();

    @Query("SELECT * FROM player WHERE PlayerId IN (:playerIds)")
    List<Player> loadAllByIds(int[] playerIds);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Player> playerList);

    @Delete
    void delete(Player player);
}
