package com.example.tabuilengilizcenigelitir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {

    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button playButton = (Button) findViewById(R.id.playButton);
        Button settingsButton = (Button) findViewById(R.id.settingsButton);
        Button words15Button  = (Button) findViewById(R.id.words15Button);
        Button howtoplayButton  = (Button) findViewById(R.id.howtoplayButton);

        assert playButton != null;
        assert settingsButton != null;
        assert words15Button != null;
        assert howtoplayButton != null;

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent play = new Intent(MainActivity.this,GameActivity.class);
                startActivity(play);
            }
        });

        words15Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent words = new Intent(MainActivity.this,Words15GameActivity.class);
                startActivity(words);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settings = new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(settings);
            }
        });

        howtoplayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent howtoplay = new Intent(MainActivity.this,HowToPlayActivity.class);
                startActivity(howtoplay);
            }
        });

        MobileAds.initialize(this,"ca-app-pub-1416395265344990~9079063221");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
