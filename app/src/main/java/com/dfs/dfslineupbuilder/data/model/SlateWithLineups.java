package com.dfs.dfslineupbuilder.data.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class SlateWithLineups {
    @Embedded
    public Slate slate;
    @Relation(
            parentColumn = "SlateId",
            entityColumn = "LineupId"
    )
    public List<Lineup> lineups;
}
