package com.dk.boosted;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.Arrays;


public class DisplaySummoners extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_summoners);
        Intent intent = getIntent();
        String[] champFulls = intent.getStringArrayExtra("champFulls");
        Log.d("CHAMPFULLS", Arrays.toString(champFulls));
        new DownloadImageTask((ImageView) findViewById(R.id.summoner1))
                .execute("http://ddragon.leagueoflegends.com/cdn/6.16.2/img/champion/" +
                    champFulls[0]);
        new DownloadImageTask((ImageView) findViewById(R.id.summoner2))
                .execute("http://ddragon.leagueoflegends.com/cdn/6.16.2/img/champion/" +
                    champFulls[1]);
        new DownloadImageTask((ImageView) findViewById(R.id.summoner3))
                .execute("http://ddragon.leagueoflegends.com/cdn/6.16.2/img/champion/" +
                        champFulls[2]);
        new DownloadImageTask((ImageView) findViewById(R.id.summoner4))
                .execute("http://ddragon.leagueoflegends.com/cdn/6.16.2/img/champion/" +
                        champFulls[3]);
        new DownloadImageTask((ImageView) findViewById(R.id.summoner5))
                .execute("http://ddragon.leagueoflegends.com/cdn/6.16.2/img/champion/" +
                        champFulls[4]);
        new DownloadImageTask((ImageView) findViewById(R.id.summoner6))
                .execute("http://ddragon.leagueoflegends.com/cdn/6.16.2/img/champion/" +
                        champFulls[5]);
        new DownloadImageTask((ImageView) findViewById(R.id.summoner7))
                .execute("http://ddragon.leagueoflegends.com/cdn/6.16.2/img/champion/" +
                        champFulls[6]);
        new DownloadImageTask((ImageView) findViewById(R.id.summoner8))
                .execute("http://ddragon.leagueoflegends.com/cdn/6.16.2/img/champion/" +
                        champFulls[7]);
        new DownloadImageTask((ImageView) findViewById(R.id.summoner9))
                .execute("http://ddragon.leagueoflegends.com/cdn/6.16.2/img/champion/" +
                        champFulls[8]);
        new DownloadImageTask((ImageView) findViewById(R.id.summoner10))
                .execute("http://ddragon.leagueoflegends.com/cdn/6.16.2/img/champion/" +
                        champFulls[9]);

    }
// show The Image in a ImageView



    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


}
