package com.example.game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class diffrent extends AppCompatActivity {
    ImageView card1;
    ImageView card2;
    ImageView card3;
    boolean stop;
    boolean music;
    int size = 4;
    boolean king=false;
    ConstraintLayout bgElement;
    ImageView card4;
    Boolean newGame;
    TextView exp;
    ImageView npc;
    ImageView logo;
    ImageView bossphoto;
    TextView youwon;
    Button nextworld;
    int world;
    boolean traget[] = new boolean[4];
    int place;
    int boss[] = {R.drawable.joker1, R.drawable.joker2, R.drawable.joker3, R.drawable.joker4};
    int diffrent[] = {R.drawable.find1, R.drawable.find2, R.drawable.find3, R.drawable.find4};

    SharedPreferences sp;
    MediaPlayer musicWin;
    Boolean playMusic;
    int sumTime;
    CountDownTimer cTimer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diffrent);
        sp= getSharedPreferences("details", MODE_PRIVATE);
        musicWin = MediaPlayer.create(diffrent.this, R.raw.fight);
        newGame= sp.getBoolean("newGame", true);


        stop = false;
        card1 = findViewById(R.id.card1);
        card2 = findViewById(R.id.card2);
        card3 = findViewById(R.id.card3);
        bgElement = findViewById(R.id.diffrent);
        card4 = findViewById(R.id.card4);
        exp = findViewById(R.id.textView6);
        logo = findViewById(R.id.imageView7);
        npc = findViewById(R.id.npc);
        bossphoto = findViewById(R.id.endphoto);
        youwon = findViewById(R.id.youwon);
        nextworld = findViewById(R.id.nextworld);


        world= sp.getInt("worldNow",1);
        sumTime= sp.getInt("sumTime",0);


        if (world == 1) {
            npc.setImageResource(R.drawable.shub);
            card1.setImageResource(R.drawable.cardstart1);
            card2.setImageResource(R.drawable.cardstart1);
            card3.setImageResource(R.drawable.cardstart1);
            card4.setImageResource(R.drawable.cardstart1);
            bossphoto.setImageResource(R.drawable.boss1end);
        } else if (world == 2) {

            npc.setImageResource(R.drawable.seny);
            card1.setImageResource(R.drawable.cardstart2);
            card2.setImageResource(R.drawable.cardstart2);
            card3.setImageResource(R.drawable.cardstart2);
            card4.setImageResource(R.drawable.cardstart2);
            bossphoto.setImageResource(R.drawable.boss2end);
        } else if (world == 3) {
            npc.setImageResource(R.drawable.monk);
            card1.setImageResource(R.drawable.cardstart3);
            card2.setImageResource(R.drawable.cardstart3);
            card3.setImageResource(R.drawable.cardstart3);
            card4.setImageResource(R.drawable.cardstart3);
            bossphoto.setImageResource(R.drawable.boss3end);
        } else if (world == 4) {
            npc.setImageResource(R.drawable.hilfa);
            card1.setImageResource(R.drawable.cardstart4);
            card2.setImageResource(R.drawable.cardstart4);
            card3.setImageResource(R.drawable.cardstart4);
            card4.setImageResource(R.drawable.cardstart4);
            bossphoto.setImageResource(R.drawable.boss4end);
        }
    }


    public void startfind(View view) {
        exp.setVisibility(View.VISIBLE);
        startTimer(10000);
        if (stop == false) {
            card1.setId(boss[0]);
            card2.setId(boss[1]);
            card3.setId(boss[2]);
            card4.setId(boss[3]);
            for (int i = 0; i < size; i++) {
                traget[i] = false;
            }


            Random rand = new Random();
            place = rand.nextInt(4);
            while (place == 4) {
                rand = new Random();
                place = rand.nextInt(4);
            }

            ImageView friend = findViewById(boss[place]);
            friend.setImageResource(diffrent[world - 1]);
            traget[place] = true;

            for (int i = 0; i < size; i++) {
                if (traget[i] == false) {
                    friend = findViewById(boss[i]);
                    friend.setImageResource(boss[world - 1]);
                    traget[place] = true;
                }
            }
        }
        stop = true;

        for (int i = 0; i < size; i++) {
            traget[i] = false;
        }
        traget[place] = true;
        ImageView great = findViewById(boss[place]);
        great.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelTimer();
                if(playMusic == true) {
                    MediaPlayer musicWiner = MediaPlayer.create(diffrent.this, R.raw.win);
                    musicWiner.start();
                }
                card1.setVisibility(View.GONE);
                card2.setVisibility(View.GONE);
                card3.setVisibility(View.GONE);
                card4.setVisibility(View.GONE);
                logo.setVisibility(View.GONE);
                npc.setVisibility(View.GONE);
                exp.setVisibility(View.GONE);
                bgElement.setBackground(ContextCompat.getDrawable(com.example.game.diffrent.this, android.R.color.black));
                bossphoto.setVisibility(View.VISIBLE);
                youwon.setVisibility(View.VISIBLE);
                nextworld.setVisibility(View.VISIBLE);
            }
        });
        for (int i = 0; i < size; i++) {
            if (traget[i] == false) {
                ImageView notgood = findViewById(boss[i]);
                notgood.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelTimer();

                        AlertDialog.Builder builder = new AlertDialog.Builder(diffrent.this);
                        builder.setCancelable(false);

                        View dialogView = getLayoutInflater().inflate(R.layout.lose_dialogue, null);

                        builder.setView(dialogView).setNegativeButton(
                                R.string.worldback,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        music=false;
                                        Intent gonext = new Intent(diffrent.this, Road.class);
                                        startActivity(gonext);
                                        finish();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();


                    }
                });
            }
        }
    }

    public void world(View view) {
        king=true;
        music=false;
            world++;
        Intent gonexte = new Intent(diffrent.this, ScoreBoard.class);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("worldNow", world);
        editor.putInt("sumTime",sumTime);
        editor.commit();
        startActivity(gonexte);
        finish();
    }

    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("music", playMusic);
        if(newGame)
        {
            if(world > sp.getInt("worldNew",1)) {
                editor.putInt("worldNew", world);
            }
        }
        else {
            if (world > sp.getInt("world", 1)) {
                editor.putInt("world", world);
            }
        }
        editor.putInt("place",world-1);
        editor.commit();
    }

    void startTimer(int time) {
        cTimer = new CountDownTimer(time, 1000) {
            public void onTick(long millisUntilFinished) {
                sumTime++;
            }
            public void onFinish() {

            }
        };
        cTimer.start();
    }

    void cancelTimer() {
        if (cTimer != null)
            cTimer.cancel();
    }

    @Override
    public void finish() {
        super.finish();
        musicWin.stop();
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("music", playMusic);
        if(newGame)
        {
            if(world > sp.getInt("worldNew",1)) {
                editor.putInt("worldNew", world);
            }
        }
        else {
            if (world > sp.getInt("world", 1)) {
                editor.putInt("world", world);
            }
        }
        editor.putInt("place",world-1);
        editor.commit();

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu,menu);
        MenuItem item= menu.findItem(R.id.music);
        playMusic= sp.getBoolean("music", true);
        if(playMusic)
        {
            item.setTitle("on");
            item.setIcon(R.drawable.speker);
            musicWin.start();
        }
        else
        {
            item.setTitle("off");
            item.setIcon(R.drawable.mute);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.music)
        {
            if(!playMusic)
            {
                playMusic= true;
                musicWin = MediaPlayer.create(diffrent.this, R.raw.fight);
                musicWin.start();
                item.setTitle("on");
                item.setIcon(R.drawable.speker);
            }
            else
            {
                playMusic= false;
                item.setTitle("off");
                item.setIcon(R.drawable.mute);
                musicWin.stop();
            }

        }
        else  if(item.getItemId() == R.id.world)
        {
            musicWin.stop();
            cancelTimer();
            Intent gonext = new Intent(diffrent.this, Road.class);
            startActivity(gonext);
            finish();
        }
        else  if(item.getItemId() == R.id.user)
        {
            musicWin.stop();
            cancelTimer();
            Intent gonext = new Intent(diffrent.this, MainActivity.class);
            startActivity(gonext);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
