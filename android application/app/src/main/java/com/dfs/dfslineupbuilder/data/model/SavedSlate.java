package com.dfs.dfslineupbuilder.data.model;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.List;

@Entity(tableName = "saved_slate")
public class SavedSlate {
    @PrimaryKey
    public int SlateId;

    public String SeasonYear;

    public int Week;

    public String StartDate;

    public String SlateName;

    public SavedSlate(int SlateId, String SeasonYear, String SlateName, String StartDate, int Week ){
        this.SlateId = SlateId;
        this.SeasonYear = SeasonYear;
        this.Week = Week;
        this.StartDate = StartDate;
        this.SlateName = SlateName;
    }
}
