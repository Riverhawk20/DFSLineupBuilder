package com.dfs.dfslineupbuilder.data.model;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity
public class Slate {
    @PrimaryKey
    public int SlateId;

    public String SeasonYear;
    public int Week;
    public Date StartDate;
    public String SlateName;

    public Slate(String seasonYear, String slateName, Date startDate, int week){
        this.SeasonYear = seasonYear;
        this.Week =week;
        this.StartDate = startDate;
        this.SlateName = slateName;
    }
}
