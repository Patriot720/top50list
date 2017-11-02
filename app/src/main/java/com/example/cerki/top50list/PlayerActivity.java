package com.example.cerki.top50list;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class PlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        String text = getIntent().getStringExtra("USER_URL");
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        TextView textView = new TextView(this);
        textView.setText(text);
        ConstraintLayout m = (ConstraintLayout) findViewById(R.id.activity_player);
        m.addView(textView);
    }
}
