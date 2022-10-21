package com.dfs.dfslineupbuilder.data;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.dfs.dfslineupbuilder.data.dao.LineupDao;
import com.dfs.dfslineupbuilder.data.dao.PlayerDao;
import com.dfs.dfslineupbuilder.data.dao.SlateDao;
import com.dfs.dfslineupbuilder.data.dao.UserDao;
import com.dfs.dfslineupbuilder.data.model.Lineup;
import com.dfs.dfslineupbuilder.data.model.Player;
import com.dfs.dfslineupbuilder.data.model.Slate;
import com.dfs.dfslineupbuilder.data.model.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Lineup.class, Slate.class, Player.class}, version = 1)
public abstract class EntityRoomDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract PlayerDao playerDao();
    public abstract SlateDao slateDao();
    public abstract LineupDao lineupDao();

    //using singleton database
    private static volatile EntityRoomDatabase INSTANCE;
    private static final int numberOfThreads = 2;

    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(numberOfThreads);

    static EntityRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (EntityRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    EntityRoomDatabase.class, "entity_database").addCallback(callback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    static Callback callback=new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
//            db.beginTransaction();
//            db.execSQL("INSERT INTO Slate VALUES(?,?,?,?,?)", new Object[]{1,"2022",5,"Oct 16", "dummy slate"});
//            db.endTransaction();
        }
    };

}