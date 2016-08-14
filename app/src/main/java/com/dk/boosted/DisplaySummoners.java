package com.dk.boosted;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONObject;


public class DisplaySummoners extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_summoners);
        Intent intent = getIntent();
        String summonerName = intent.getStringExtra("SummonerName");
    }



}
