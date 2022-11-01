package com.dfs.dfslineupbuilder.data.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class SlateWithPlayers {
    @Embedded
    public Slate slate;
    @Relation(
            parentColumn = "SlateId",
            entityColumn = "SlateId"
    )
    public List<Player> players;
}
