package com.example.cerki.top50list;

import android.content.ContentValues;

/**
 * Created by cerki on 29-Oct-17.
 */

public class Player {
    String username;
    String rank;
    String accuracy;
    String pp;
    public String BASE_URL = "https://osu.ppy.sh/";
    String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    ContentValues mDifferenceFromDbValues;
    public Player(String username, String rank, String accuracy, String pp,String url,ContentValues difference){
        this.username = username;
        this.accuracy = accuracy;
        this.rank = rank;
        this.pp = pp;
        this.url = url;
        mDifferenceFromDbValues = difference;
    }
    public Player(String username,String rank,String accuracy,String pp,String url){
        this(username,rank,accuracy,pp,url,new ContentValues());
    }
    public Player(String username,String rank,String accuracy,String pp){
        this(username,rank,accuracy,pp,null,new ContentValues());
    }

    public void setDifferenceFromDbValues(ContentValues mDifferenceFromDbValues) {
        this.mDifferenceFromDbValues = mDifferenceFromDbValues;
    }

    public ContentValues getDifferenceFromDbValues() {
        return mDifferenceFromDbValues;
    }

    public Player(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRank() {
        return rank;
    }
    public int getRankInt(){
        return Integer.parseInt(rank.replaceAll("[^0-9]",""));
    }
    public int getPpInt(){
        return Integer.parseInt(pp.replaceAll("[^0-9]",""));
    }
    public float getAccFloat(){
        return Float.parseFloat(accuracy.replaceAll("[^0-9.]",""));
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public String getPp() {
        return pp;
    }

    public void setPp(String pp) {
        this.pp = pp;
    }
}
