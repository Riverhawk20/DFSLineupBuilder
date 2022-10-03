package com.dfs.dfslineupbuilder.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.dfs.dfslineupbuilder.data.dao.LineupDao;
import com.dfs.dfslineupbuilder.data.dao.PlayerDao;
import com.dfs.dfslineupbuilder.data.dao.SlateDao;
import com.dfs.dfslineupbuilder.data.dao.UserDao;
import com.dfs.dfslineupbuilder.data.model.Lineup;
import com.dfs.dfslineupbuilder.data.model.Player;
import com.dfs.dfslineupbuilder.data.model.Slate;
import com.dfs.dfslineupbuilder.data.model.User;

@Database(entities = {User.class, Lineup.class, Slate.class, Player.class}, version = 1)
public abstract class EntityRoomDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract PlayerDao playerDao();
    public abstract SlateDao slateDao();
    public abstract LineupDao lineupDao();
}