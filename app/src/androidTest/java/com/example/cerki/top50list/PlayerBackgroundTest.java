package com.example.cerki.top50list;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.runner.AndroidJUnit4;

import com.example.cerki.top50list.PlayerActivity.PlayerActivity;
import com.example.cerki.top50list.data.PlayersDbHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.junit.Assert.*;

/**
 * Created by cerki on 30-Oct-17.
 */
@RunWith(AndroidJUnit4.class)
public class PlayerBackgroundTest {
    public List<Player> getPlayers() {
        try {
            PlayerListBackground task = new PlayerListBackground(getTargetContext());
            List<Player> players = task.execute("https://osu.ppy.sh/p/pp").get();
            Field f[] = players.get(0).getClass().getFields();
            return players;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        ArrayList<Player> players = new ArrayList<Player>();
        return players;
    }
    @Test
    public void should_return_players_list(){
            List<Player> players = getPlayers();
            assertAllFieldsAreOk(players.get(0));
            assertEquals(50,players.size());

    }
    @Test
    public void player_difference_test(){
        PlayerListBackground task = new PlayerListBackground(getTargetContext());
        ContentValues cv = new ContentValues();
        cv.put("username","Cookiezi");
        cv.put("rank",28);
        cv.put(PlayersDbHelper.COLUMN_ACC,32);
        cv.put(PlayersDbHelper.COLUMN_PP,344);
        long res = task.mDb.insertWithOnConflict(PlayersDbHelper.TABLE_NAME,null,cv,SQLiteDatabase.CONFLICT_FAIL);
        assertNotEquals(res,-1);
        List<Player> players = getPlayers();
        ContentValues diff = players.get(0).getDifferenceFromDbValues();
        assertEquals(players.get(0).getDifferenceFromDbValues().get("rank"),-27);

    }
    @Before
    public void setUp() throws Exception {
        getTargetContext().deleteDatabase(PlayersDbHelper.DATABASE_NAME);
    }

    @After
    public void tearDown() throws Exception {
        setUp();
    }
    @Test
    public void loadPlayersFromDb() throws Exception {
        PlayerListBackground task = new PlayerListBackground(getTargetContext());
        insertFakeDataIn(task.mDb);
        List<Player> players = PlayersListActivity.loadPlayersFromDb(task.mDb);
        assertTrue(players.size() == 1 );
        assertAllFieldsAreOk(players.get(0));
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
        PlayerListBackground task = new PlayerListBackground(getTargetContext());
        insertFakeDataIn(task.mDb);
        Player player = new Player("Cookiezi","#6","24.48%","14500");
        ContentValues values = task.getPlayerDiff(player);
        assertEquals("2",values.getAsString("rank"));
        assertEquals("200",values.getAsString("pp"));

    }
    @Test
    public void compare_to_db_no_user_test(){
        PlayerListBackground task = new PlayerListBackground(getTargetContext());
        Player player = new Player("Cookiezi","#6","24.48%","14500");
        ContentValues values = task.getPlayerDiff(player);
        assertTrue(values.size() == 0);
    }
    @Test
    public void update_db_test_empty_db(){
        PlayerListBackground task = new PlayerListBackground(getTargetContext());
        Player player = new Player("Cookiezi","#6","24.48%","14500");
        task.updatePlayer(player);
        Cursor cursor = task.getPlayerFromDb("Cookiezi");
        assertTrue(cursor.moveToNext());
    }
    @Test
    public void update_db_test_filled_db(){
    PlayerListBackground task = new PlayerListBackground(getTargetContext());
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