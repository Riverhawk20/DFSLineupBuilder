package com.dfs.dfslineupbuilder.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.dfs.dfslineupbuilder.data.model.Lineup;

import java.util.List;

@Dao
public interface LineupDao {

    @Query("SELECT * FROM lineup")
    List<Lineup> getAll();

    @Query("SELECT * FROM lineup WHERE LineupId IN (:lineupIds)")
    List<Lineup> loadAllByIds(int[] lineupIds);

    @Insert
    void insertAll(Lineup... lineups);

    @Delete
    void delete(Lineup lineup);
}
