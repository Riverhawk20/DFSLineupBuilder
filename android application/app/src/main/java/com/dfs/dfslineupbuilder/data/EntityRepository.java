package com.dfs.dfslineupbuilder.data;

import android.app.Application;

import androidx.room.Room;

import com.dfs.dfslineupbuilder.data.dao.LineupDao;
import com.dfs.dfslineupbuilder.data.dao.PlayerDao;
import com.dfs.dfslineupbuilder.data.dao.SlateDao;
import com.dfs.dfslineupbuilder.data.dao.UserDao;

public class EntityRepository {

    private static EntityRoomDatabase entityRoomDatabase;


    public EntityRepository(Application application){
        entityRoomDatabase = Room.databaseBuilder(application.getApplicationContext(),
                EntityRoomDatabase.class, "entity_database").build();

    }


}
