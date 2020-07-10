package com.example.tabuilengilizcenigelitir;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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

public class Words15GameActivity extends AppCompatActivity {

    public final static String TEAM_ONE_SCORE = "TEAM_ONE_SCORE";
    public final static String TEAM_TWO_SCORE = "TEAM_TWO_SCORE";
    public final static String TURN = "TURN";

    static private int team1Score;
    static private int team2Score;
    private int passNum,passNumCopy,wordNum,wordNumCopy ;
    private int raundNum;
    private int raundTime;
    private int currentRound;
    private int size;
    private int language;
    private int timeLeft;

    private boolean turn ;
    boolean startTurn;

    CountDownTimer countDownTimer;
    ProgressBar mProgressBar ;
    int mills;
    TextView time,teamName,guessWord,translateWord,scoreText,usedText;
    ArrayList <Words15> words;
    Button passButton,trueButton,wordButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words15_game3);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.getProgressDrawable().setColorFilter(
                Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);


        words= new ArrayList<Words15>();

        teamName = (TextView) findViewById(R.id.teamName);
        scoreText=(TextView) findViewById(R.id.score);

        startTurn = true;
        turn = true ;
        teamNameText(turn);

        usedText =(TextView) findViewById(R.id.kalanText);

        SharedPreferences sharedPref = this.getSharedPreferences("MyData",MODE_PRIVATE);
        raundNum = sharedPref.getInt("roundNum",10);
        raundTime = sharedPref.getInt("roundTime",60);
        passNum = sharedPref.getInt("pasHakki",3);
        language = sharedPref.getInt("language",1);
        passNumCopy = passNum;
        wordNum=15;
        wordNumCopy=wordNum;
        currentRound =1;
        timeLeft=raundTime;

        team1Score=0;
        team2Score=0;

        updateRoundNum();
        updateScores();
        updateUsedWord();

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
        trueButton = (Button) findViewById(R.id.trueButton);
        wordButton = (Button) findViewById(R.id.wordButton);

        assert passButton != null;
        assert trueButton != null;
        assert wordButton != null;

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
        wordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wordNum--;
                updateUsedWord();
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

    public void updateUsedWord(){
        if(wordNum>0)
            usedText.setText(15-wordNum+" words used");
        else if(wordNum==0){
            usedText.setText(15-wordNum+" words used");
            wordButton.setAlpha((float)0.5);
        }
        else
            wordButton.setAlpha((float)0.5);
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
                updateUsedWord();
            }
        }
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_CANCELED) {
                startTurn = true;
                startRoundTimer(true,raundTime);
                updateScores();
                updateCards();
                updateUsedWord();
            }
        }
    }
    public void updatePas(){
        if(passNum==0)
            passButton.setAlpha((float) 0.5);
    }

    public void startRoundTimer(boolean started,int _raundTime){
        if(started) {
            if (!started)
                mills = _raundTime * 1000 + 1000;
            else
                mills = _raundTime * 1000 + 1000;

            mProgressBar.setMax(_raundTime);

            countDownTimer = new CountDownTimer(mills, 1000) {
                @Override
                public void onTick(long l) {

                    time.setText(String.valueOf(l / 1000));
                    timeLeft = (int) l / 1000;
                    int progress = (int) (l / 1000);
                    mProgressBar.setProgress(progress);
                }


                @Override
                public void onFinish() {
                    if (!turn) {
                        if (raundNum + 1 == currentRound) {
                            Intent intent = new Intent(Words15GameActivity.this, Words15EndOfGameActivity.class);
                            startActivity(intent);
                        }
                        updateRoundNum();
                    }

                    countDownTimer.cancel();
                    passNum = passNumCopy;
                    turn = !turn;
                    startTurn = false;
                    wordNum = 15;
                    timeLeft = raundTime;
                    passButton.setAlpha((float) 1.0);
                    wordButton.setAlpha((float) 1.0);

                    Intent i = new Intent(Words15GameActivity.this, Words15EndOfRoundActivity.class);
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
                input = this.getResources().openRawResource(R.raw.cards15_tr);
                file = new BufferedReader(new InputStreamReader(input));
                break;
            }
            case 2:{
                input = this.getResources().openRawResource(R.raw.cards15_es);
                file = new BufferedReader(new InputStreamReader(input));
                break;
            }
            case 3:{
                input = this.getResources().openRawResource(R.raw.cards15_ger);
                file = new BufferedReader(new InputStreamReader(input));
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + choice);
        }

        int i=0;


        while(file.ready()){
            words.add(new Words15(file.readLine(),file.readLine()));
            i++;
            file.readLine();
        }

        input.close();
        file.close();
    }

    public void updateCards(){
        guessWord= (TextView) findViewById(R.id.guessWord);
        translateWord =(TextView) findViewById(R.id.translateWord);

        size= words.size();
        int random = (int)(Math.random()*size);

        guessWord.setText(""+words.get(random).guessWord);
        translateWord.setText("("+words.get(random).translate+")");
    }
    public void onPause() {

        super.onPause();
        countDownTimer.cancel();
    }
    public void onResume(){

        super.onResume();
        startRoundTimer(true,timeLeft);
    }
}

class Words15 {
    String translate,guessWord;

    public Words15 (String w1,String w2){
        translate=w1;
        guessWord=w2;
    }

}