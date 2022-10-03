package com.dfs.dfslineupbuilder.data.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class UserWithLineups {
    @Embedded
    public User user;
    @Relation(
            parentColumn = "UserId",
            entityColumn = "LineupId"
    )
    public List<Lineup> lineups;
}
