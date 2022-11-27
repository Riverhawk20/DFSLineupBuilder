package com.dfs.dfslineupbuilder.data;

import android.content.Context;
import android.os.AsyncTask;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.dfs.dfslineupbuilder.data.dao.LineupDao;
import com.dfs.dfslineupbuilder.data.dao.PlayerDao;
import com.dfs.dfslineupbuilder.data.dao.SavedPlayerDao;
import com.dfs.dfslineupbuilder.data.dao.SavedSlateDao;
import com.dfs.dfslineupbuilder.data.dao.SlateDao;
import com.dfs.dfslineupbuilder.data.dao.UserDao;
import com.dfs.dfslineupbuilder.data.model.Lineup;
import com.dfs.dfslineupbuilder.data.model.Player;
import com.dfs.dfslineupbuilder.data.model.SavedPlayer;
import com.dfs.dfslineupbuilder.data.model.SavedSlate;
import com.dfs.dfslineupbuilder.data.model.Slate;
import com.dfs.dfslineupbuilder.data.model.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
@Config(sdk = 28)
public class EntityRoomDatabaseTest {
    private EntityRoomDatabase db;
    private UserDao userDao;
    private LineupDao lineupDao;
    private PlayerDao playerDao;
    private SlateDao slateDao;
    private SavedPlayerDao savedPlayerDao;
    private SavedSlateDao savedSlateDao;

    @Before
    public void setUp() throws Exception {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, EntityRoomDatabase.class).allowMainThreadQueries().build();
        userDao = db.userDao();
        playerDao = db.playerDao();
        slateDao = db.slateDao();
        lineupDao=db.lineupDao();
        savedPlayerDao = db.savedPlayerDao();
        savedSlateDao=db.savedSlateDao();
    }

    @After
    public void tearDown() throws Exception {
        db.close();
    }

    @Test
    public void writeUserAndReadInList() throws Exception {
        User user = new User("randomemail@email.com", "123");
        userDao.insert(user);
        List<User> res = userDao.loadAllByIds(new String[]{user.UserId});
        assert res.get(0).UserId.equals(user.UserId);
    }

    @Test
    public void writeLineupAndReadInList() throws Exception {
        Lineup lineup = new Lineup(1, 123);
        lineupDao.insertAll(lineup);
        List<Lineup> res = lineupDao.loadAllByIds(new int[]{lineup.LineupId});
        assert res.get(0).LineupId == lineup.LineupId;
    }

    @Test
    public void writePlayerAndReadInList() throws Exception {
        Player player = new Player("test", "QB", "CLE", "PIT", 123, 1, 1234);
        ArrayList<Player> playerList = new ArrayList<Player>();
        playerList.add(player);
        playerDao.insertAll(playerList);
        List<Player> res = playerDao.loadAllByIds(new int[]{player.PlayerId});
        assert res.get(0).PlayerId == player.PlayerId;
    }

    @Test
    public void writeSlateAndReadInList() throws Exception {
        Slate slate = new Slate("2022", "Test Name", "2022-10-10", 1);
        ArrayList<Slate> slateArrayList = new ArrayList<Slate>();
        slateArrayList.add(slate);
        slateDao.insert(slateArrayList);
        List<Slate> res = slateDao.loadAllByIds(new int[]{slate.SlateId});
        assert res.get(0).SlateId == slate.SlateId;
    }

    @Test
    public void writeSavedPlayerAndReadInList() throws Exception {
        SavedPlayer savedPlayer = new SavedPlayer(123, "Test Name", "QB", "CLE", "PIT",7000,55,333,123);
        ArrayList<SavedPlayer> savedPlayerArrayList = new ArrayList<>();
        savedPlayerArrayList.add(savedPlayer);
        savedPlayerDao.insertAll(savedPlayerArrayList);
        List<SavedPlayer> res = savedPlayerDao.loadAllByIds(new int[]{savedPlayer.PlayerId});
        assert res.get(0).PlayerId == savedPlayer.PlayerId;
    }

    @Test
    public void writeSavedSlateAndReadInList() throws Exception {
        SavedSlate savedSlate = new SavedSlate(123, 48520, "Test Name", "2022-10-15", 1,"55");
        ArrayList<SavedSlate> savedSlateArrayList = new ArrayList<>();
        savedSlateArrayList.add(savedSlate);
        savedSlateDao.insert(savedSlateArrayList);
        List<SavedSlate> res = savedSlateDao.loadAllByIds(new int[]{savedSlate.SlateId});
        assert res.get(0).SlateId == savedSlate.SlateId;
    }

}