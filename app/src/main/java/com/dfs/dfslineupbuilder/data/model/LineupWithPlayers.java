package com.dfs.dfslineupbuilder.data.model;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class LineupWithPlayers {
    @Embedded
    public Lineup lineup;
    @Relation(
            parentColumn = "LineupId",
            entityColumn = "PlayerId",
            associateBy = @Junction(LineupPlayerCrossRef.class)
    )
    public List<Player> players;
}
