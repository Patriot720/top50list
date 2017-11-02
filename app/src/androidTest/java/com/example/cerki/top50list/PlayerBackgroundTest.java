package com.example.cerki.top50list;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.runner.AndroidJUnit4;

import com.example.cerki.top50list.data.PlayersDbHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.junit.Assert.*;

/**
 * Created by cerki on 30-Oct-17.
 */
@RunWith(AndroidJUnit4.class)
public class PlayerBackgroundTest {
    @Test
    public void should_return_players_list(){
    PlayerBackground task = new PlayerBackground(getTargetContext());
        try {
            List<Player> players = task.execute("https://osu.ppy.sh/p/pp").get();
            Field f[] = players.get(0).getClass().getFields();
            assertAllFieldsAreOk(players.get(0));
            assertEquals(50,players.size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void setUp() throws Exception {
        getTargetContext().deleteDatabase(PlayersDbHelper.DATABASE_NAME);
    }

    @After
    public void tearDown() throws Exception {
        setUp();
    }
    public void insertFakeDataIn(SQLiteDatabase db){
        ContentValues cv = new ContentValues();
        cv.put(PlayersDbHelper.COLUMN_USERNAME,"Cookiezi");
        cv.put(PlayersDbHelper.COLUMN_PP,"14300");
        cv.put(PlayersDbHelper.COLUMN_ACC,23.48);
        cv.put(PlayersDbHelper.COLUMN_RANK,4);
        db.insert(PlayersDbHelper.TABLE_NAME,null,cv);
    }
    @Test
    public void compare_to_db_test(){
        PlayerBackground task = new PlayerBackground(getTargetContext());
        insertFakeDataIn(task.mDb);
        Player player = new Player("Cookiezi","#6","24.48%","14500");
        ContentValues values = task.getPlayerDiff(player);
        assertEquals(-2,values.get("rank"));
        assertEquals(200,values.get("pp"));

    }
    @Test
    public void compare_to_db_no_user_test(){
        PlayerBackground task = new PlayerBackground(getTargetContext());
        Player player = new Player("Cookiezi","#6","24.48%","14500");
        ContentValues values = task.getPlayerDiff(player);
        assertTrue(values.size() == 0);
    }
    @Test
    public void update_db_test_empty_db(){
        PlayerBackground task = new PlayerBackground(getTargetContext());
        Player player = new Player("Cookiezi","#6","24.48%","14500");
        task.updatePlayer(player);
        Cursor cursor = task.getPlayerFromDb("Cookiezi");
        assertFalse(cursor.moveToNext());
    }
    @Test
    public void update_db_test_filled_db(){
    PlayerBackground task = new PlayerBackground(getTargetContext());
    insertFakeDataIn(task.mDb);
    Player player = new Player("Cookiezi","#2","22.48","14500");
    task.updatePlayer(player);
    Cursor cursor = task.getPlayerFromDb("Cookiezi");
    assertTrue(cursor.moveToNext());
    assertEquals(cursor.getInt(PlayersDbHelper.COLUMN_RANK_ID),2);
    assertEquals(cursor.getInt(PlayersDbHelper.COLUMN_PP_ID),14500);
    }

    private void assertAllFieldsAreOk(Player player) {
        assertTrue(player.username.length() > 0);
        assertTrue(player.rank.length() > 0);
        assertTrue(player.accuracy.length() > 0);
        assertTrue(player.pp.length() > 0);
    }
}