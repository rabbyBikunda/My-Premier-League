package com.example.rabby_mobile_ca2;

import android.provider.BaseColumns;

//container for different constants that we will need in sqlite operations
//f u change a coumn name u can do it here instead of changing it everywhere throughout your code
public final class FixtureContract {

    //prevents u from creating a subclass or an object of it
    private FixtureContract() {}

    //base columns is an interface that
    public static class FixturesTable implements BaseColumns {
        //want
        public static final String TABLE_NAME = "fixtures";
        public  static final String COLUMN_DATE = "date";
        public  static final String COLUMN_HOME_TEAM = "homeTeam";
        public  static final String COLUMN_AWAY_TEAM = "awayTeam";
        public  static final String COLUMN_HOME_TEAM_BADGE = "homeTeamBadge";
        public  static final String COLUMN_AWAY_TEAM_BADGE = "awayTeamBadge";






    }

}
