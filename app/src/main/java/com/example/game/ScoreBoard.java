package com.example.game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.StringTokenizer;

public class ScoreBoard extends AppCompatActivity {
    SharedPreferences sp;
    int score [][]=new int [4][10];
    Boolean english;
    String names [][]=new String[4][10];
    String dates [][]=new String[4][10];
    ImageView logo;
    int firstplace=0;
    ConstraintLayout bgElement;
    int dar=1;
    int k=0;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    int world;
    boolean changeLang= false;

    MediaPlayer musicBcg;
    boolean playMusic;
    int sumTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("details", MODE_PRIVATE);
        english= sp.getBoolean("english", false);
        if(english)
        {
            setLocale("hi");
        }
        else {
            setLocale("he");
        }

        setContentView(R.layout.activity_score_board);

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy • HH:mm");
        date = dateFormat.format(calendar.getTime());

        LinearLayout layout = (LinearLayout) findViewById(R.id.linegod);
        logo = findViewById(R.id.logo);
        bgElement = findViewById(R.id.scoresbcg);
        sumTime = sp.getInt("sumTime", 0);
        int world = sp.getInt("worldNow", 1);
        String infoName=sp.getString("user_name", "");
        boolean guest=sp.getBoolean("newGame",false);
        if(guest==true){
            infoName=getString(R.string.Guest);
        }
        if (sumTime == 0) {
            Intent mIntent = getIntent();
            int intValue = sp.getInt("place", 1);
            world = intValue;
        } else {
            world = world - 1;
        }
        int place = 0;
        boolean enough = false;
        String savedString="";
        String savedString_name="";
        String savedDates="";

        if(world==1) {
            savedString = sp.getString("string1", "");
            savedString_name = sp.getString("string1_names", "");
            savedDates = sp.getString("string1_dates", "");
        }
        else if(world==2){
            savedString = sp.getString("string2", "");
            savedString_name = sp.getString("string2_names", "");
            savedDates = sp.getString("string2_dates", "");
        }
        else if(world==3){
            savedString = sp.getString("string3", "");
            savedString_name = sp.getString("string3_names", "");
            savedDates = sp.getString("string3_dates", "");
        }
        else if (world==4){
            savedString = sp.getString("string4", "");
            savedString_name = sp.getString("string4_names", "");
            savedDates = sp.getString("string4_dates", "");
        }
        StringTokenizer st = new StringTokenizer(savedString, ",");
        if(savedString!="") {
            for (int i = 0; i < 10; i++) {
                score[world - 1][i] = Integer.parseInt(st.nextToken());
            }
        }       

        String names_t[]=savedString_name.split(",");
        String date_t[]=savedDates.split(",");


        for(int i=0; i<names_t.length; i++){
            names[world-1][i]=names_t[i];
            dates[world-1][i]=date_t[i];
        }

        if (sumTime > 0) {

           if(score[world-1][0]==0){
               score[world - 1][0] = sumTime;
               names[world-1][0]=infoName;
               dates[world-1][0]=date;
           }

            else if (score[world-1][0]!=0) {
                for (int i = 1; i <= 10 && enough == false; i++) {
                    if (sumTime <= score[world - 1][i-1] && score[world-1][i-1]!=0) {
                        if(sumTime==score[world-1][i-1]){
                            while(sumTime==score[world-1][i-1]){
                                place = i;
                                firstplace = i;
                                i++;
                            }
                            enough = true;
                        }
                        else {
                            place = i - 1;
                            firstplace = i - 1;
                            enough = true;
                        }
                    }
                }

                if(enough==false){
                    for(int i=0; i<10 && enough==false; i++){
                        if(score[world-1][i]==0){
                            place = i;
                            firstplace = i;
                            enough=true;
                        }
                    }
                }

                int archive[] = new int[10 - place];
                String archive_names[]=new String[10-place];
                String archive_dates[]=new String[10-place];

                for (place = place; place < 10; place = place + 1) {
                    if (place == firstplace) {
                        archive[k] = score[world - 1][place];
                        archive_names[k]=names[world-1][place];
                        archive_dates[k]=dates[world-1][place];
                        score[world - 1][place] = sumTime;
                        names[world-1][place]=infoName;
                        dates[world-1][place]=date;

                    } else {
                        archive[k + 1] = score[world - 1][place];
                        archive_names[k + 1] = names[world - 1][place];
                        archive_dates[k+1]=dates[world-1][place];
                        score[world - 1][place] = archive[k];
                        dates[world-1][place]=archive_dates[k];
                        names[world - 1][place] = archive_names[k++];
                    }
                }
            }



        }
        for (int i = 0; i < 10; i++) {
            if(score[world - 1][i]!=0) {
                int d = i + 1;
                TextView dynamicTextView = new TextView(this);
                dynamicTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                dynamicTextView.setText(names[world-1][i] +"\n" +score[world - 1][i]+getString(R.string.seconds)+
                        "\n" +dates[world-1][i] + "\n---------------------------");
                dynamicTextView.setTextColor(getResources().getColor(android.R.color.white));
                dynamicTextView.setTextSize(30);
                dynamicTextView.setGravity(Gravity.CENTER);
                layout.addView(dynamicTextView);
                ImageView blackline = new ImageView(this);


            }
        }
        if (world == 1) {
            logo.setImageResource(R.drawable.score1);
            bgElement.setBackground(ContextCompat.getDrawable(this, R.drawable.blue));

        } else if (world == 2) {
            logo.setImageResource(R.drawable.score2);
            bgElement.setBackground(ContextCompat.getDrawable(this, R.drawable.pink));

        } else if (world == 3) {
            logo.setImageResource(R.drawable.score3);
            bgElement.setBackground(ContextCompat.getDrawable(this, R.drawable.tibet));
        } else if (world == 4) {
            logo.setImageResource(R.drawable.score4);
            bgElement.setBackground(ContextCompat.getDrawable(this, R.drawable.black));
        }
        sumTime = 0;

        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("sumTime", sumTime);

        StringBuilder str_names = new StringBuilder();
        StringBuilder str = new StringBuilder();
        StringBuilder str_dates = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            str.append(score[world-1][i]).append(",");
            str_names.append(names[world-1][i]).append(",");
            str_dates.append(dates[world-1][i]).append(",");
        }

        String result=str.toString();
        String result_names=str_names.toString();
        String result_dates=str_dates.toString();

        if(world==1){
            editor.putString("string1", result);
            editor.putString("string1_names", result_names);
            editor.putString("string1_dates", result_dates);
        }
        else if(world==2){
            editor.putString("string2", result);
            editor.putString("string2_names", result_names);
            editor.putString("string2_dates", result_dates);
        }
        else if(world==3){
            editor.putString("string3", result);
            editor.putString("string3_names", result_names);
            editor.putString("string3_dates", result_dates);
        }
        else if(world==4){
            editor.putString("string4", result);
            editor.putString("string4_names", result_names);
            editor.putString("string4_dates", result_dates);
        }
        editor.commit();


    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }
    public void back(View view) {
        Intent tfd = new Intent(ScoreBoard.this, Road.class);
        startActivity(tfd);
        finish();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.road_menu,menu);
        MenuItem item= menu.findItem(R.id.music);
        MenuItem langh= menu.findItem(R.id.language);
        playMusic= sp.getBoolean("music", true);
        english= sp.getBoolean("english", true);
        if(playMusic)
        {
            item.setTitle("on");
            item.setIcon(R.drawable.speker);
            MusicManger.player.start();
        }
        else
        {
            item.setTitle("off");
            item.setIcon(R.drawable.mute);
            MusicManger.player.pause();
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
                MusicManger.player.start();
                item.setTitle("on");
                item.setIcon(R.drawable.speker);
            }
            else
            {
                playMusic= false;
                item.setTitle("off");
                item.setIcon(R.drawable.mute);
                MusicManger.player.pause();
            }
        }
        else if(item.getItemId() == R.id.language) {
            if(!english)
            {
                english= true;
                item.setIcon(R.drawable.hebrew);
                item.setTitle("Hebrew");
                Intent refresh = new Intent(this, ScoreBoard.class);
                finish();
                startActivity(refresh);

            }
            else
            {
                english= false;
                item.setIcon(R.drawable.english);
                item.setTitle("English");
                Intent refresh = new Intent(this, ScoreBoard.class);
                finish();
                startActivity(refresh);

            }
        }
        else  if(item.getItemId() == R.id.user)
        {
            Intent gonext = new Intent(ScoreBoard.this, MainActivity.class);
            startActivity(gonext);
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor= sp.edit();
        editor.putBoolean("music", playMusic);
        editor.putBoolean("english", english);
        editor.putInt("sumTime",0);
        editor.commit();
        MusicManger.player.pause();
    }

    @Override
    public void finish() {
        super.finish();
        SharedPreferences.Editor editor= sp.edit();
        editor.putBoolean("music", playMusic);
        editor.putBoolean("english", english);
        editor.putInt("sumTime",0);
        editor.commit();
        MusicManger.player.pause();
    }
}