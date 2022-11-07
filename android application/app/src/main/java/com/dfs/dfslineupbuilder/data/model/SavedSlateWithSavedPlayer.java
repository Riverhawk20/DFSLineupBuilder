package com.dfs.dfslineupbuilder.data.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class SavedSlateWithSavedPlayer {
    @Embedded
    public SavedSlate saved_slate;
    @Relation(
            parentColumn = "SlateId",
            entityColumn = "savedSlateId"
    )
    public List<SavedPlayer> players;
}
