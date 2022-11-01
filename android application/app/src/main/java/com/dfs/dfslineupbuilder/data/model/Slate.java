package com.dfs.dfslineupbuilder.data.model;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.List;

@Entity
public class Slate {
    @PrimaryKey
    public int SlateId;

    public String SeasonYear;

    public int Week;

    public String StartDate;

    public String SlateName;

//    public List<Player> Players;

    public Slate(String SeasonYear, String SlateName, String StartDate, int Week ){
        this.SeasonYear = SeasonYear;
        this.Week =Week;
        this.StartDate = StartDate;
        this.SlateName = SlateName;
//        this.Players = Players;
    }
}
