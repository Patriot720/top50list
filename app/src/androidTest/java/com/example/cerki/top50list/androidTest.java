package com.example.cerki.top50list;

import android.content.ContentValues;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Created by cerki on 04-Nov-17.
 */
@RunWith(AndroidJUnit4.class)
public class androidTest {
    @Test
    public void contentValuesToStringTest(){
        ContentValues cv = new ContentValues();
        cv.put("wtf",23.48);
        assertEquals(cv.getAsString("wtf"),"23.48");
    }
}
