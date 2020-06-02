package com.example.rabby_mobile_ca2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.rabby_mobile_ca2.FixtureContract.*;

import java.util.ArrayList;
import java.util.List;


public class FixtureDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "fixtures.db";
    private static final int DATABASE_VERSION = 42;

    //hold reference to database
    private SQLiteDatabase db;


    //ONLY need to pass context when you create instance of fixturedbhelper
    public FixtureDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //will be called first time u try to access database
    @Override
    public void onCreate(SQLiteDatabase db) {
        //save reference in db
         this.db = db;

         final String SQL_CREATE_FIXTURES_TABLE =  "CREATE TABLE " +
              FixturesTable.TABLE_NAME + " ( " +
              FixturesTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                 FixturesTable.COLUMN_DATE + " TEXT, " +
                 FixturesTable.COLUMN_HOME_TEAM + " TEXT, " +
                 FixturesTable.COLUMN_AWAY_TEAM + " TEXT, " +
                 FixturesTable.COLUMN_HOME_TEAM_BADGE + " TEXT, " +
                 FixturesTable.COLUMN_AWAY_TEAM_BADGE + " TEXT " + ")";

         db.execSQL(SQL_CREATE_FIXTURES_TABLE);
         fillFixturesTable();


    }

    //called if u make changes to database - to do this increment DATABASE_VERSION by 1
    //in onUpgrade u define how u will update database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FixturesTable.TABLE_NAME);
        onCreate(db);
    }

    private void fillFixturesTable(){
        //create instance of fixtures classs

        Fixture f1 = new Fixture("2019-12-01","AWAY TEAM NAME","HOME TEAM BADGE","HOME TEAM NAME","AWAYTEAMBADGE");
        addFixture(f1);
    }

    private void addFixture(Fixture fixture){
        //key value pairs where key is column and value is value in column
        ContentValues cv = new ContentValues();
        cv.put(FixturesTable.COLUMN_DATE, fixture.getDate());
        cv.put(FixturesTable.COLUMN_HOME_TEAM, fixture.getHomeTeamName());
        cv.put(FixturesTable.COLUMN_AWAY_TEAM, fixture.getAwayTeamName());
        cv.put(FixturesTable.COLUMN_HOME_TEAM_BADGE, fixture.getHomeTeamBadge());
        cv.put(FixturesTable.COLUMN_AWAY_TEAM_BADGE, fixture.getAwayTeamBadge());
        db.insert(FixturesTable.TABLE_NAME,null,cv);
    }

    public List<Fixture> getAllFixtures () {
        List<Fixture> fixtureList = new ArrayList<>();
        //when this is called the first time it creates a database
        db = getReadableDatabase();

        //cursor to query database
        Cursor c = db.rawQuery("SELECT * FROM " + FixturesTable.TABLE_NAME, null);

        //move cursor to first entry if it exits
        if (c.moveToFirst()) {
            do {//query entry
                Fixture fixture = new Fixture();
                //get index of the cursor in the table
                fixture.setmDate(c.getString(c.getColumnIndex(FixturesTable.COLUMN_DATE)));
                fixture.setmHomeTeamName(c.getString(c.getColumnIndex(FixturesTable.COLUMN_HOME_TEAM)));
                fixture.setmAwayTeamName(c.getString(c.getColumnIndex(FixturesTable.COLUMN_AWAY_TEAM)));
                fixture.setHomeTeamBadge(c.getString(c.getColumnIndex(FixturesTable.COLUMN_HOME_TEAM_BADGE)));
                fixture.setHomeTeamBadge(c.getString(c.getColumnIndex(FixturesTable.COLUMN_AWAY_TEAM_BADGE)));


                //Add fixture object to arraylist
                fixtureList.add(fixture);

            } while (c.moveToNext());//move to next entry only if it exists
        }
        c.close();
        return fixtureList;

    }
}
