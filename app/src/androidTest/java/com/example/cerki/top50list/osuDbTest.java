package com.example.cerki.top50list;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.cerki.top50list.data.PlayersDbHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.junit.Assert.*;
import static android.support.test.InstrumentationRegistry.getContext;

/**
 * Created by cerki on 01-Nov-17.
 */
@RunWith(AndroidJUnit4.class)
public class osuDbTest {
    PlayersDbHelper mHelper = new PlayersDbHelper(getTargetContext());
    @Before
    public void setUp() throws Exception {
        getTargetContext().deleteDatabase(mHelper.getDatabaseName());
    }

    @After
    public void tearDown() throws Exception {
        getTargetContext().deleteDatabase(mHelper.getDatabaseName());
    }
    @Test
    public void insertion_test(){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PlayersDbHelper.COLUMN_USERNAME,"Cookiezi");
        values.put(mHelper.COLUMN_RANK,50);
        values.put(mHelper.COLUMN_ACC,98);
        long id = db.insertOrThrow(mHelper.TABLE_NAME,null,values);
        assertNotEquals(-1,id);
        Cursor cursor = db.query(mHelper.TABLE_NAME,null,null,null,null,null,null);
        cursor.moveToNext();
        assertEquals(cursor.getCount(),1);
        assertEquals(4,cursor.getColumnCount());
        assertEquals(cursor.getString(PlayersDbHelper.COLUMN_USERNAME_ID),"Cookiezi");
    }

}