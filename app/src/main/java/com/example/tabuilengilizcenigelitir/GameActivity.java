package com.example.tabuilengilizcenigelitir;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    public final static String TEAM_ONE_SCORE = "TEAM_ONE_SCORE";
    public final static String TEAM_TWO_SCORE = "TEAM_TWO_SCORE";
    public final static String TURN = "TURN";

    static private int team1Score;
    static private int team2Score;
    private int passNum,passNumCopy ;
    private int raundNum;
    private int raundTime;
    private int currentRound;
    private int size;
    private int language;
    private int timeLeft;

    private boolean turn ;
    boolean startTurn;

    CountDownTimer countDownTimer;
    ProgressBar mProgressBar;
    TextView time,teamName,guessWord,translateWord,tabuWord1,tabuWord2,tabuWord3,scoreText;
    ArrayList <Words> words;
    Button passButton,tabuButton,trueButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamenew3);


        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.getProgressDrawable().setColorFilter(
                Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);

        words= new ArrayList<Words>();

        teamName = (TextView) findViewById(R.id.teamName);
        scoreText=(TextView) findViewById(R.id.score);

        startTurn = true;
        turn = true ;
        teamNameText(turn);


        SharedPreferences sharedPref = this.getSharedPreferences("MyData",MODE_PRIVATE);
        raundNum = sharedPref.getInt("roundNum",10);
        raundTime = sharedPref.getInt("roundTime",60);
        passNum = sharedPref.getInt("pasHakki",3);
        language = sharedPref.getInt("language",1);
        passNumCopy = passNum;
        currentRound =1;

        timeLeft=raundTime;

        team1Score=0;
        team2Score=0;

        updateRoundNum();
        updateScores();

        time=(TextView) findViewById(R.id.timeText);

        startRoundTimer(false,raundTime);

        try {
            getCards(language);
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateCards();
        updateScores();

        passButton = (Button) findViewById(R.id.passButton);
        tabuButton = (Button) findViewById(R.id.tabuButton);
        trueButton = (Button) findViewById(R.id.trueButton);

        assert passButton != null;
        assert trueButton != null;
        assert tabuButton != null;

        passButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(passNum>0){
                    updateCards();
                }
                passNum--;
                updatePas();
            }
        });

        tabuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(turn){
                    team1Score--;
                }
                else{
                    team2Score--;
                }
                updateCards();
                updateScores();
            }
        });

        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(turn)
                    team1Score++;
                else
                    team2Score++;
                updateCards();
                updateScores();
            }
        });

    }

    public int getTeam1Score(){
        int _team1Score = this.team1Score;
        return _team1Score;
    }
    public int getTeam2Score(){
        int _team2Score = this.team2Score;
        return _team2Score;
    }

    public void updateRoundNum(){
        TextView roundText = (TextView) findViewById(R.id.roundText);
        roundText.setText("Round:" + currentRound + "/" + raundNum);
        currentRound++;
    }

    public void updateScores(){
            if(turn){
                scoreText.setText(""+team1Score);
            }
            else{
                scoreText.setText(""+team2Score);
            }
    }

    public void teamNameText(boolean t){
        if(t) {
            teamName.setText("Team 1");
        } else
            teamName.setText("Team 2");
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_CANCELED) {
                updateScores();
                updateCards();
            }
        }
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_CANCELED) {
                startTurn = true;
                startRoundTimer(false,raundTime);
                updateScores();
                updateCards();
            }
        }
    }

    public void startRoundTimer(boolean started,int _raundTime) {

        int mills;

        mills = _raundTime * 1000 + 1000;

        mProgressBar.setMax(_raundTime);

        if(started){
        countDownTimer = new CountDownTimer(mills, 1000) {
            @Override
            public void onTick(long l) {
                time.setText(String.valueOf(l / 1000));
                int progress = (int) (l / 1000);
                mProgressBar.setProgress(progress);
                timeLeft = (int) l / 1000;
            }

            @Override
            public void onFinish() {
                if (!turn) {
                    if (raundNum + 1 == currentRound) {
                        Intent intent = new Intent(GameActivity.this, EndOfGameActivity.class);
                        startActivity(intent);
                    }
                    updateRoundNum();
                }
                countDownTimer.cancel();
                passNum = passNumCopy;
                turn = !turn;
                startTurn = false;
                timeLeft = raundTime;
                passButton.setAlpha((float) 1.0);

                Intent i = new Intent(GameActivity.this, EndOfRoundActivity.class);
                i.putExtra(TEAM_ONE_SCORE, team1Score);
                i.putExtra(TEAM_TWO_SCORE, team2Score);
                i.putExtra(TURN, turn);
                startActivityForResult(i, 2);

                if (startTurn) {
                    startRoundTimer(true, raundTime);
                }
                teamNameText(turn);

            }
        }.start();
    }
        else if (countDownTimer != null) {
            countDownTimer.cancel();
    }
    }
    public void getCards(int choice) throws IOException {
        InputStream input;
        BufferedReader file;

        switch (choice){
            case 1:{
                input = this.getResources().openRawResource(R.raw.cards_tr);
                file = new BufferedReader(new InputStreamReader(input));
                break;
            }
            case 2:{
                input = this.getResources().openRawResource(R.raw.cards_es);
                file = new BufferedReader(new InputStreamReader(input));
                break;
            }
            case 3:{
                input = this.getResources().openRawResource(R.raw.cards_ger);
                file = new BufferedReader(new InputStreamReader(input));
                break;
            }

            default:
                throw new IllegalStateException("Unexpected value: " + choice);
        }


        while(file.ready()){
            words.add(new Words(file.readLine(),file.readLine(),file.readLine(),file.readLine(),file.readLine()));
            file.readLine();
        }
        input.close();
        file.close();
    }

    public void updateCards(){
        guessWord= (TextView) findViewById(R.id.guessWord);
        translateWord =(TextView) findViewById(R.id.translateWord);
        tabuWord1= (TextView) findViewById(R.id.tabuWord1);
        tabuWord2= (TextView) findViewById(R.id.tabuWord2);
        tabuWord3= (TextView) findViewById(R.id.tabuWord3);

        size= words.size();
        int random = (int)(Math.random()*size);

        guessWord.setText(""+words.get(random).guessWord);
        translateWord.setText("("+words.get(random).translate+")");
        tabuWord1.setText(""+words.get(random).tabuWord1);
        tabuWord2.setText(""+words.get(random).tabuWord2);
        tabuWord3.setText(""+words.get(random).tabuWord3);
    }

    protected void onPause() {
        super.onPause();
        countDownTimer.cancel();
        startRoundTimer(false,0);
        //stop your timer here...
    }

    protected void onResume() {
        super.onResume();
        startRoundTimer(true,timeLeft);
    }
    @Override
    public void onBackPressed(){
        countDownTimer.cancel();
        super.onBackPressed();

    }


    public void updatePas(){
        if(passNum==0){
            passButton.setAlpha((float) 0.5);
        }

    }
}

class Words {
    String translate,guessWord,tabuWord1,tabuWord2,tabuWord3;

    public Words (String w1,String w2,String w3,String w4,String w5){
        translate=w1;
        guessWord=w2;
        tabuWord1=w3;
        tabuWord2=w4;
        tabuWord3=w5;
    }

}