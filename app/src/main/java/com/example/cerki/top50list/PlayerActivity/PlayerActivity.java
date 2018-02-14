package com.example.cerki.top50list.PlayerActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cerki.top50list.Player;
import com.example.cerki.top50list.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class PlayerActivity extends AppCompatActivity {
    ImageView mPlayerAvatar;
    private String mAvatarUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
        String url;
        url = getIntent().getStringExtra("USER_URL");
        loadPage(url);
    }

    private void loadPage(String url) {
        loadText(url);
        loadAvatar(url);
    }

    private void loadAvatar(String url) {
        mPlayerAvatar = findViewById(R.id.player_avatar);
        new PlayerBackground().execute(url);
    }


    private void loadText(String url) {
        TextView textView = new TextView(this);
        textView.setText(url);
        LinearLayout m = (LinearLayout) findViewById(R.id.activity_player);
        m.addView(textView);
    }


    public class PlayerBackground extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                String url = urls[0];
                Document playerPage = Jsoup.connect(url).get();
                String avatar_url = "http:"  + playerPage.select(".avatar-holder img").attr("src");
                return avatar_url;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            mAvatarUrl = result;
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(mAvatarUrl,mPlayerAvatar);
        }
    }
}
