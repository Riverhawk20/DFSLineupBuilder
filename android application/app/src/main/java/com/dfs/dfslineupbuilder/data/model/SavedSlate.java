package com.dfs.dfslineupbuilder.data.model;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.List;

@Entity(tableName = "saved_slate")
public class SavedSlate {
    @PrimaryKey
    public int SlateId;

    public int TotalSalary;

    public int Week;

    public String StartDate;

    public String SlateName;

    public String userId;

    public SavedSlate(int SlateId, int TotalSalary, String SlateName, String StartDate, int Week, String userId ){
        this.SlateId = SlateId;
        this.TotalSalary = TotalSalary;
        this.Week = Week;
        this.StartDate = StartDate;
        this.SlateName = SlateName;
        this.userId = userId;
    }
}
