package com.example.tabuilengilizcenigelitir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;


public class SettingsActivity extends AppCompatActivity {
    SeekBar raundTime,raundNum,pasHakki;
    private int pasHakki_progress;
    private int raundNum_progress;
    private int raundTime_progress;
    private int selected,temp;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    RadioGroup language;
    RadioGroup tr,es;

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        MobileAds.initialize(this, "ca-app-pub-1416395265344990~9079063221");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-1416395265344990/2847700949");
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



        Context context = this;
        sharedPref = SettingsActivity.this.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        raundTime = (SeekBar) findViewById(R.id.raundTime);
        raundNum = (SeekBar) findViewById(R.id.raundNum);
        pasHakki = (SeekBar) findViewById(R.id.pasHakki);

        final TextView raundTimeText = (TextView) findViewById(R.id.raundTimeText);
        raundTimeText.setText("Round Time:  " + (raundTime.getProgress()));

        final TextView raundNumText = (TextView) findViewById(R.id.raundNumText);
        raundNumText.setText("Round Number:  " + (raundNum.getProgress()));

        final TextView pasHakkiText = (TextView) findViewById(R.id.pasHakkiText);
        pasHakkiText.setText("Pass Number:  " + (pasHakki.getProgress()));

       raundTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                raundTimeText.setText("Round Time:  " + (progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                saveValues();
            }
        });

         raundNum.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
             @Override
             public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                 raundNumText.setText("Round Number:  " + (i));
             }

             @Override
             public void onStartTrackingTouch(SeekBar seekBar) {

             }

             @Override
             public void onStopTrackingTouch(SeekBar seekBar) {
                saveValues();
             }
         });

         pasHakki.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
             @Override
             public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                 pasHakkiText.setText("Pass Number:  " + (i));
             }

             @Override
             public void onStartTrackingTouch(SeekBar seekBar) {

             }

             @Override
             public void onStopTrackingTouch(SeekBar seekBar) {
                saveValues();
             }
         });

         language = (RadioGroup) findViewById(R.id.languageGroup);


        final Button saveButton = (Button) findViewById(R.id.save_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nextActivity();
            }
        });

    }

    public void saveValues(){
        SharedPreferences sharedPref;
        SharedPreferences.Editor editor;
        sharedPref = getSharedPreferences("MyData",MODE_PRIVATE);
        editor = sharedPref.edit();

        selected = language.getCheckedRadioButtonId();
        switch (selected){
            case R.id.tr:{
                temp= 1;
                break;
            }
            case R.id.es:{
                temp=2;
                break;
            }
            case R.id.ger:{
                temp=3;
                break;
            }

        }

        raundNum_progress= raundNum.getProgress();
        raundTime_progress= raundTime.getProgress();
        pasHakki_progress=pasHakki.getProgress();

        editor.putInt("roundNum",raundNum_progress);
        editor.putInt("roundTime",raundTime_progress);
        editor.putInt("pasHakki",pasHakki_progress);
        editor.putInt("language",temp);
        editor.commit();

    }
    private void nextActivity(){
        Intent intent = new Intent(SettingsActivity.this,MainActivity.class);
        startActivity(intent);
        saveValues();
    }
}
