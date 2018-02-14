package com.example.cerki.top50list;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.cerki.top50list.PlayerActivity.PlayerActivity;
import com.example.cerki.top50list.data.PlayersDbHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Array;
import java.sql.SQLInput;
import java.util.ArrayList;
import java.util.List;

public class PlayersListActivity extends AppCompatActivity {
    ListView mList;
    PlayerAdapter mAdapter;
    SwipeRefreshLayout mRefreshLayout;
    public static final String BASE_URL = "https://osu.ppy.sh";
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mList = (ListView)this.findViewById(R.id.superview);
        mAdapter = new PlayerAdapter(this,R.layout.list_item1);
        mList.setAdapter(mAdapter);
        mRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.list_refresh);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new PlayerListBackground(getApplicationContext(), mAdapter,mRefreshLayout).execute(BASE_URL+"/p/pp");
            }
        });
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Player p = (Player)adapterView.getAdapter().getItem(i);
                Intent intent = new Intent(getApplicationContext(),PlayerActivity.class);
                intent.putExtra("USER_URL",p.getUrl());
                startActivity(intent);
            }
        });
        SQLiteDatabase db = new PlayersDbHelper(this).getWritableDatabase();
        List<Player> players = loadPlayersFromDb(db);
        mAdapter.addAll(players);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        // Get the SearchView and set the searchable configuration
        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                new AsyncTask<String, Void, List<Player>>() {
                    @Override
                    protected List<Player> doInBackground(String... strings) {
                        String query = strings[0];
                        ArrayList<Player> players = new ArrayList<Player>();
                        try {
                            JSONObject jsonObject = getJSONObjectFromURL(query);
                            JSONArray suggestions = jsonObject.getJSONArray("suggestions");
                            Log.v("WTF",Integer.toString(suggestions.length()));
                            for(int i = 0 ; i < suggestions.length();i++){
                                String username = suggestions.getString(i);
                                Player player = new Player(username,null,null,null);
                                player.setUrl("https://osu.ppy.sh/u/" + username);
                                players.add(player);
                            }
                            return players;
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return players;
                    }

                    @Override
                    protected void onPostExecute(List<Player> list) {
                        mAdapter.clear();
                        mAdapter.addAll(list);
                        super.onPostExecute(list);
                    }
                }.execute("https://osu.ppy.sh/p/profile?check=1&query="+s);
                return true;
            }
        });
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        return true;
    }
    public static JSONObject getJSONObjectFromURL(String urlString) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );
        urlConnection.setDoOutput(true);
        urlConnection.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();

        String jsonString = sb.toString();
        System.out.println("JSON: " + jsonString);

        return new JSONObject(jsonString);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                SearchView v = (SearchView) item.getActionView();
                v.requestFocus();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static List<Player> loadPlayersFromDb(SQLiteDatabase db){
        Cursor c  = db.query(PlayersDbHelper.TABLE_NAME,null,null,null,null,null,null);
        ArrayList<Player> players = new ArrayList<Player>();
        while(c.moveToNext()){
            Player p = new Player();
            p.setUsername(c.getString(PlayersDbHelper.COLUMN_USERNAME_ID));
            p.setAccuracy(Float.toString(c.getFloat(PlayersDbHelper.COLUMN_ACC_ID)));
            p.setRank(Integer.toString(c.getInt(PlayersDbHelper.COLUMN_RANK_ID)));
            p.setUrl(c.getString(PlayersDbHelper.COLUMN_URL_ID));
            p.setPp(Integer.toString(c.getInt(PlayersDbHelper.COLUMN_PP_ID)));
            p.setDifferenceFromDbValues(new ContentValues());
            players.add(p);
        }
    return players;

    }
}
