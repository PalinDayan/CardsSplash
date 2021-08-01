package com.example.game;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class Login extends AppCompatActivity {

    Button startGame;
    Button notMe;
    EditText inputName;
    SharedPreferences sp;
    TextView textStart;
    Boolean playMusic;
    Boolean english;
    Boolean newGame = false;
    String info_name;
    int world;
    MediaPlayer musicBcg;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp= getSharedPreferences("details", MODE_PRIVATE);

        english= sp.getBoolean("english", false);
        if(english)
        {
            setLocale("hi");
        }
        else {
            setLocale("he");
        }
        setContentView(R.layout.activity_login);

        startGame= findViewById(R.id.startGame);
        notMe= findViewById(R.id.notMe);
        inputName= findViewById(R.id.user_name);


        info_name =sp.getString("user_name","");

        if(info_name == "")
        {

        }
        else
        {
            notMe.setVisibility(View.VISIBLE);
            inputName.setText(info_name);
            inputName.setEnabled(false);
            inputName.setHintTextColor(Color.BLACK);
            world= 1;
        }

        notMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputName.setText("");
                notMe.setVisibility(View.INVISIBLE);
                inputName.setEnabled(true);
                newGame = true;
            }
        });


        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputName.getText().toString().matches(""))
                    inputName.setHintTextColor(Color.RED);
                else {
                    Intent gonext = new Intent(Login.this, Road.class);
                    startActivity(gonext);
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.start_menu,menu);
        MenuItem music= menu.findItem(R.id.music);
        MenuItem langh= menu.findItem(R.id.language);
        playMusic= sp.getBoolean("music", true);
        english= sp.getBoolean("english", true);
        if(playMusic)
        {
            music.setTitle("on");
            music.setIcon(R.drawable.speker);
            MusicManger.player.start();
        }
        else
        {
            music.setTitle("off");
            music.setIcon(R.drawable.mute);
        }
        if(english)
        {
            langh.setTitle("Hebrew");
            langh.setIcon(R.drawable.hebrew);
        }
        else
        {
            langh.setTitle("English");
            langh.setIcon(R.drawable.english);
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
                item.setIcon(R.drawable.speker);
                item.setTitle("on");
                MusicManger.player.start();
            }
            else
            {
                playMusic= false;
                item.setIcon(R.drawable.mute);
                item.setTitle("off");
                MusicManger.player.pause();
            }

        }
        else if(item.getItemId() == R.id.language)
        {
            if(!english)
            {
                english= true;
                item.setIcon(R.drawable.hebrew);
                setLocale("he");
                item.setTitle("Hebrew");
                Intent refresh = new Intent(this, Login.class);
                finish();
                startActivity(refresh);

            }
            else
            {
                english= false;
                item.setIcon(R.drawable.english);
                setLocale("hi");
                item.setTitle("English");
                Intent refresh = new Intent(this, Login.class);
                finish();
                startActivity(refresh);

            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }


    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor= sp.edit();
        MusicManger.player.pause();
        if(newGame)
        {
            editor.putInt("world",1);
        }
        editor.putString("user_name", inputName.getText().toString());
        editor.putBoolean("music", playMusic);
        editor.putBoolean("english", english);
        editor.putBoolean("newGame", false);
        editor.commit();
    }

    @Override
    public void finish() {
        super.finish();
        MusicManger.player.pause();
        SharedPreferences.Editor editor= sp.edit();
        if(newGame)
        {
            editor.putInt("world",1);
        }
        editor.putString("user_name", inputName.getText().toString());
        editor.putBoolean("music", playMusic);
        editor.putBoolean("english", english);
        editor.putBoolean("newGame", false);
        editor.commit();
    }
}