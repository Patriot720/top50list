package com.example.cerki.top50list;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.example.cerki.top50list.data.PlayersDbHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by cerki on 30-Oct-17.
 */

public class PlayerListBackground extends AsyncTask<String,Void,List<Player>> {
    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;
    SQLiteDatabase mDb;
    PlayerAdapter mAdapter;
    SwipeRefreshLayout mRefreshLayout;
    PlayerListBackground(Context context, PlayerAdapter adapter, SwipeRefreshLayout layout){
    mDb = new PlayersDbHelper(context).getWritableDatabase();
    mAdapter = adapter;
    mRefreshLayout = layout;
    }
    PlayerListBackground(Context context){
        this(context,null,null);
    }
    PlayerListBackground(){this(null,null,null);}
    public Player extractPlayerFrom(Element row){
        Elements elt = row.select("b");
        Player player = new Player();
        String rank = row.select("b").text();
        String username = row.select("a").text();
        String pp = row.child(4).children().get(0).text();
        String acc = row.child(2).text();
        String url_tmp = row.attr("onclick");
        String url = PlayersListActivity.BASE_URL + url_tmp.substring(url_tmp.indexOf('"')+1,url_tmp.lastIndexOf('"'));
        player.setRank(rank);
        player.setUsername(username);
        player.setAccuracy(acc);
        player.setPp(pp);
        player.setUrl(url);
        ContentValues playerDiff = getPlayerDiff(player);
        player.setDifferenceFromDbValues(playerDiff);
        updatePlayer(player);
        return player;
    }
    @Override
    protected List<Player> doInBackground(String... urls) {
        String url = urls[0];
        List<Player> players = new ArrayList<Player>();
        try{
             Document doc = Jsoup.connect(url).get();
            Elements playersTable = doc.select("tbody .row1p, tbody .row2p");
            for(Element row : playersTable){
                players.add(this.extractPlayerFrom(row));
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return players;
    }
    @Override
    protected void onPostExecute(List<Player> list){
        if(mAdapter != null) {
            mAdapter.clear();
            mAdapter.addAll(list);
            mRefreshLayout.setRefreshing(false);
        }
    }
    Cursor getPlayerFromDb(String username){
        String selection = PlayersDbHelper.COLUMN_USERNAME + "='" + username + "'";
        Cursor cursor = mDb.query(PlayersDbHelper.TABLE_NAME,null,selection,null,null,null,null);
        return cursor;
    }
    ContentValues getPlayerDiff(Player player) {
        Cursor cursor = getPlayerFromDb(player.username);
        ContentValues cv = new ContentValues();
        if(cursor.moveToNext()) {
            float acc = player.getAccFloat();
            int rank = player.getRankInt();
            int pp = player.getPpInt();
            float oldAcc = cursor.getFloat(PlayersDbHelper.COLUMN_ACC_ID);
            int oldPp = cursor.getInt(PlayersDbHelper.COLUMN_PP_ID);
            int oldRank = cursor.getInt(PlayersDbHelper.COLUMN_RANK_ID);
            cv.put("acc",acc-oldAcc);
            cv.put("rank",rank-oldRank);
            cv.put("pp",pp-oldPp);
        }

        return cv;
    }

    public void updatePlayer(Player player) {
        ContentValues cv = new ContentValues();
        cv.put(PlayersDbHelper.COLUMN_USERNAME,player.getUsername());
        cv.put(PlayersDbHelper.COLUMN_URL,player.getUrl());
        cv.put(PlayersDbHelper.COLUMN_RANK,player.getRankInt());
        cv.put(PlayersDbHelper.COLUMN_ACC,player.getAccFloat());
        cv.put(PlayersDbHelper.COLUMN_PP,player.getPpInt());
        long result = mDb.insertWithOnConflict(PlayersDbHelper.TABLE_NAME,null,cv,SQLiteDatabase.CONFLICT_IGNORE);
        if(result == -1)mDb.update(PlayersDbHelper.TABLE_NAME, cv, "username='" + player.username + "'", null);
    }
}
