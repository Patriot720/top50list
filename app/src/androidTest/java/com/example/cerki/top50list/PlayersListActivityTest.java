package com.example.cerki.top50list;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.SearchView;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.junit.Assert.*;

/**
 * Created by cerki on 04-Nov-17.
 */
@RunWith(AndroidJUnit4.class)
public class PlayersListActivityTest {
    @Test
    public void searchTest(){
        SearchView view = new SearchView(getTargetContext());
        final int[] count = {0};
        view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                count[0]++;
                return true;
            }
        });
        view.setQuery("Vassa",false);
        assertEquals(1,count[0]);
    }
}