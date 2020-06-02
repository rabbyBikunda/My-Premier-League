package com.example.rabby_mobile_ca2;

public class Item {

    private String mTeamId;
    private String mImageUrl;
    private String mTeamName;
    private String mWins;
    private String mDraws;
    private String mLosses;
    private String mPoints;

    public Item(String teamId, String imageUrl, String teamName, String wins, String draws, String losses, String points)
    {

        mTeamId = teamId;
        mImageUrl = imageUrl;
        mTeamName= teamName;
        mWins = wins;
        mDraws = draws;
        mLosses = losses;
        mPoints = points;
    }

    public Item(String imageUrl, String teamName)
    {

        mImageUrl = imageUrl;
        mTeamName= teamName;

    }

    public Item(String teamId, String imageUrl, String teamName)
    {
        mTeamId = teamId;
        mImageUrl = imageUrl;
        mTeamName= teamName;

    }


    public String getTeamId() {
        return mTeamId;
    }

    public  String getImageUrl() {
        return mImageUrl;
    }

    public String getTeamName() {
        return mTeamName;
    }

    public String getWins(){
        return mWins;
    }

    public String getDraws(){
        return mDraws;
    }

    public String getLosses(){
        return mLosses;
    }

    public String getPoints() {
        return mPoints;
    }
}
