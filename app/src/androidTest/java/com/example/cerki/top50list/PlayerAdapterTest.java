package com.example.cerki.top50list;

import android.content.ContentValues;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.junit.Assert.*;

/**
 * Created by cerki on 04-Nov-17.
 */
@RunWith(AndroidJUnit4.class)
public class PlayerAdapterTest {
    @Test
    public void getView() throws Exception {
        PlayerAdapter adapter = new PlayerAdapter(getTargetContext(),R.layout.list_item);
        ContentValues diff = new ContentValues();
        diff.put("rank", 1);
        diff.put("pp",2400);
        diff.put("acc",1.48);
        Player p = new Player("c","#2","243","44",null,diff);
        adapter.add(p);
        View m = View.inflate(getTargetContext(),R.layout.list_item,null);
        View v = adapter.getView(0,m,null);
        TextView acc_diff = (TextView)v.findViewById(R.id.acc_diff);
        TextView rank_diff = (TextView)v.findViewById(R.id.rank_diff);
        TextView pp_diff = (TextView)v.findViewById(R.id.pp_diff);
        assertEquals(acc_diff.getText(),"1.48");
        assertEquals("2400",pp_diff.getText());

    }

}