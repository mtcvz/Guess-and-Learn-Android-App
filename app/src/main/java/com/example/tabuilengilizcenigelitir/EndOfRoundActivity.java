package com.example.tabuilengilizcenigelitir;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.w3c.dom.Text;

public class EndOfRoundActivity extends AppCompatActivity {
        TextView scoreText;
        Button continueButton;
        int score1,score2;

    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_of_round);

        Intent intent = getIntent();
        GameActivity game = new GameActivity();
        scoreText = (TextView) findViewById(R.id.scoreText);
        score1 = game.getTeam1Score();
        score2 = game.getTeam2Score();
        scoreText.setText(""+score1+"  "+score2);

        continueButton = (Button) findViewById(R.id.continueButton);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });

        MobileAds.initialize(this,"ca-app-pub-1416395265344990~9079063221");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
