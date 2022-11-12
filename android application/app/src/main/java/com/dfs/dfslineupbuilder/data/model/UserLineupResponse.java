package com.dfs.dfslineupbuilder.data.model;

import java.util.List;

public class UserLineupResponse {
    public User User;
    public List<UserLineup> Lineups;

    public UserLineupResponse(User User, List<UserLineup> Lineups) {
        this.User = User;
        this.Lineups = Lineups;
    }
}
