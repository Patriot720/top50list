package com.example.cerki.top50list.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.cerki.top50list.Player;

/**
 * Created by cerki on 01-Nov-17.
 */

public class PlayersDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "PlayersDbHelper";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "players";

    public static final String COLUMN_USERNAME = "username";
    public static final int COLUMN_USERNAME_ID = 0;
    public static final String COLUMN_ACC = "accuracy";
    public static final int COLUMN_ACC_ID = 1;
    public static final String COLUMN_RANK = "rank";
    public static final int COLUMN_RANK_ID = 2;
    public static final String COLUMN_PP = "pp";
    public static final int COLUMN_PP_ID = 3;
    public static final String COLUMN_URL = "url";
    public static final int COLUMN_URL_ID = 4;
    public static final String COLUMN_ISFOLLOWED = "isfollowed";
    public static final int COLUMN_ISFOLLOWED_ID = 5;
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_USERNAME  + " TEXT PRIMARY KEY," +
            COLUMN_ACC + " FLOAT," +
            COLUMN_RANK + " INTEGER," +
            COLUMN_PP + " INTEGER," +
            COLUMN_URL + " TEXT," +
            COLUMN_ISFOLLOWED + " TINYINT(1))"
            ;


    public PlayersDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
