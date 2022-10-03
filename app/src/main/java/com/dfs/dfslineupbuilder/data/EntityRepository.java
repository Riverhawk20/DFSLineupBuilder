package com.dfs.dfslineupbuilder.data;

import android.app.Application;

import androidx.room.Room;

public class EntityRepository {
    private static EntityRoomDatabase entityRoomDatabase;
    public EntityRepository(Application application){
        entityRoomDatabase = Room.databaseBuilder(application.getApplicationContext(),
                EntityRoomDatabase.class, "entity_database").build();
    }
}
