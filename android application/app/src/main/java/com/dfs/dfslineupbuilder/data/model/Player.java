package com.dfs.dfslineupbuilder.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Player {
    @PrimaryKey
    public int PlayerId;

    public String Name;
    public String Position;
    public String Team;
    public String Opponent;
    public int Salary;
    public int FantasyPoints;
    public int SlateId;

    public Player(String Name, String Position, String Team, String Opponent, int Salary, int FantasyPoints, int SlateId){
        this.Name = Name;
        this.Position =Position;
        this.Team = Team;
        this.Opponent = Opponent;
        this.Salary = Salary;
        this.FantasyPoints = FantasyPoints;
        this.SlateId = SlateId;
    }
}
