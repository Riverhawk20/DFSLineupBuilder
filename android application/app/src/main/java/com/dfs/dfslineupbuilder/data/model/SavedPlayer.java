package com.dfs.dfslineupbuilder.data.model;


import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "saved_player")
public class SavedPlayer {
    @PrimaryKey
    public int PlayerId;

    public String Name;
    public String Position;
    public String Team;
    public String Opponent;
    public int Salary;
    public int FantasyPoints;
    public int SlateId;
    public int savedSlateId;

    public SavedPlayer(int PlayerId, String Name, String Position, String Team, String Opponent, int Salary, int FantasyPoints, int SlateId, int savedSlateId){
        this.PlayerId = PlayerId;
        this.Name = Name;
        this.Position =Position;
        this.Team = Team;
        this.Opponent = Opponent;
        this.Salary = Salary;
        this.FantasyPoints = FantasyPoints;
        this.SlateId = SlateId;
        this.savedSlateId = savedSlateId;
    }
}