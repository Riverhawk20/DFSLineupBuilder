package com.dfs.dfslineupbuilder.data.model;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity
public class Slate {
    @PrimaryKey
    public int SlateId;

    @SerializedName("seasonYear")
    public String SeasonYear;

    @SerializedName("week")
    public int Week;

    @SerializedName("startDate")
    public String StartDate;

    @SerializedName("slateName")
    public String SlateName;

    public Slate(String SeasonYear, String SlateName, String StartDate, int Week){
        this.SeasonYear = SeasonYear;
        this.Week =Week;
        this.StartDate = StartDate;
        this.SlateName = SlateName;
    }
}
