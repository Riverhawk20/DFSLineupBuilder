package com.dfs.dfslineupbuilder.data.model;

import androidx.room.Entity;

@Entity(primaryKeys = {"LineupId", "PlayerId"})
public class LineupPlayerCrossRef {
    public int PlayerId;
    public int LineupId;
}
