package com.example.tabuilengilizcenigelitir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class EndOfGameActivity extends AppCompatActivity {
    TextView scores,winner;
    Button menu;
    private InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_of_game);

        MobileAds.initialize(this, "ca-app-pub-1416395265344990~9079063221");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-1416395265344990/3081620769");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                mInterstitialAd.show();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }
        });

        GameActivity game = new GameActivity();
        scores=(TextView) findViewById(R.id.scoreText);
        winner = (TextView) findViewById(R.id.winnerText);
        scores.setText(""+game.getTeam1Score()+"  "+game.getTeam2Score());

        if(game.getTeam1Score()> game.getTeam2Score())
            winner.setText("TEAM 1");
        else if(game.getTeam2Score()>game.getTeam1Score())
            winner.setText("TEAM 2");
        else
            winner.setText("NONE");


        menu = (Button) findViewById(R.id.menuButton);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EndOfGameActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

}
