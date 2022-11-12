package com.dfs.dfslineupbuilder.data.model;

import java.util.ArrayList;
import java.util.List;

public class LineupPost {
    public int LineupId;
    public int FantasyPoints;
    public int TotalSalary;
    public String UserId;
    public List<SavedPlayer> players;

    public LineupPost(int lineupId, int fantasyPoints, int totalSalary, String userId, List<SavedPlayer> players) {
        LineupId = lineupId;
        FantasyPoints = fantasyPoints;
        TotalSalary = totalSalary;
        UserId = userId;
        this.players = players;
    }
}
