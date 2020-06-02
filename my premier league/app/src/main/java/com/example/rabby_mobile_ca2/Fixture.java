package com.example.rabby_mobile_ca2;

public class Fixture {

    private String mDate;
    private String mHomeTeamName;
    private String mAwayTeamName;
    private String mHomeTeamBadge;
    private String mAwayTeamBadge;

    public Fixture(){};


    public Fixture(String date, String homeTeamName,String homeTeamBadge, String awayTeamName,String awayTeamBadge)
    {
        mDate= date;
        mHomeTeamName = homeTeamName;
        mAwayTeamName = awayTeamName;
        mHomeTeamBadge = homeTeamBadge;
        mAwayTeamBadge = awayTeamBadge;
    }

    public Fixture(String date, String homeTeamName, String awayTeamName)
    {

        mDate= date;
        mHomeTeamName = homeTeamName;
        mAwayTeamName = awayTeamName;
    }

    public String getDate(){
        return mDate;
    }

    public String getHomeTeamName(){
        return mHomeTeamName;
    }

    public String getAwayTeamName(){
        return mAwayTeamName;
    }

    public String getHomeTeamBadge(){
        return mHomeTeamBadge;
    }

    public String getAwayTeamBadge(){
        return mAwayTeamBadge;
    }

    public void setHomeTeamBadge(String mBadgeHome) {
        this.mHomeTeamBadge = mBadgeHome;
    }

    public void setAwayTeamBadge(String mBadgeAway) {
        this.mAwayTeamBadge = mBadgeAway;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public void setmHomeTeamName(String mHomeTeamName) {
        this.mHomeTeamName = mHomeTeamName;
    }

    public void setmAwayTeamName(String mAwayTeamName) {
        this.mAwayTeamName = mAwayTeamName;
    }
}
