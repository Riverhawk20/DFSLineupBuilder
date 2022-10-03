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

    public Player(String name, String position, String team, String opponent, int salary, int fantasyPoints){
        this.Name = name;
        this.Position =position;
        this.Team = team;
        this.Opponent = opponent;
        this.Salary = salary;
        this.FantasyPoints = fantasyPoints;
    }
}
