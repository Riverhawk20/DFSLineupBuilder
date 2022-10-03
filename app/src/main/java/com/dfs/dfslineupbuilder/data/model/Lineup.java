package com.dfs.dfslineupbuilder.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Lineup {
    @PrimaryKey
    public int LineupId;
    public int FantasyPoints;
    public int TotalSalary;

    public Lineup(int salary, int fantasyPoints){
        this.TotalSalary = salary;
        this.FantasyPoints =fantasyPoints;
    }
}
