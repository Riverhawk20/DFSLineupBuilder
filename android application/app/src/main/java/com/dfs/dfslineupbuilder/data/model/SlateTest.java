package com.dfs.dfslineupbuilder.data.model;

import androidx.room.PrimaryKey;

import java.util.List;

public class SlateTest {
        public int SlateId;

        public String SeasonYear;

        public int Week;

        public String StartDate;

        public String SlateName;

        public List<Player> Players;

        public SlateTest(String SeasonYear, String SlateName, String StartDate, int Week, List<Player> Players ){
            this.SeasonYear = SeasonYear;
            this.Week =Week;
            this.StartDate = StartDate;
            this.SlateName = SlateName;
            this.Players = Players;
        }

}
