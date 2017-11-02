package com.example.cerki.top50list;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cerki.top50list.data.PlayersDbHelper;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class PlayersListActivity extends AppCompatActivity {
    ListView mList;
    PlayerAdapter mAdapter;
    SwipeRefreshLayout mRefreshLayout;
    public static final String BASE_URL = "https://osu.ppy.sh";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mList = (ListView)this.findViewById(R.id.superview);
        mAdapter = new PlayerAdapter(this,R.layout.list_item);
        mList.setAdapter(mAdapter);
        mRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.list_refresh);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new PlayerBackground(getApplicationContext(), mAdapter,mRefreshLayout).execute(BASE_URL+"/p/pp");
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
    }
}
