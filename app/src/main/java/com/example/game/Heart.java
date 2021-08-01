package com.example.game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Random;

public class Heart extends AppCompatActivity {
    TextView name_preview;
    TextView circle_level;
    ImageView bossNPC;
    TextView timer;
    ImageView mainCardsBack;
    ImageView timerStart;
    TextView matchEXP;
    TextView timeStartTxt;
    boolean level6exp;
    TextView timeAll;
    boolean newGame;
    ImageView bossPhoto;
    TextView explain;
    ImageView levelexp;
    TextView levelNow;
    ConstraintLayout bgElement;

    int world ;
    int level = 1;
    int limit = 4;
    int linecards = 1;
    boolean big = false;
    int time;
    boolean flip = false;
    int sizecard;
    int nRand;
    Random rand;
    Random boss;
    int openNow=0;
    boolean openTogether= false;
    int leftCards;
    int master;
    int timeLevel;
    int findMate;
    LinearLayout linearCards;
    CountDownTimer cTimer = null;
    String textWin;
    String textLevel;
    int sumTime=0;
    boolean inFlip= true;
    boolean[] cardsWas;
    boolean levelEnd = false;
    int[] inGame;
    int[] cards = {R.drawable.card1, R.drawable.card2, R.drawable.card3, R.drawable.card4, R.drawable.card5, R.drawable.card6, R.drawable.card7, R.drawable.card8, R.drawable.joker1, R.drawable.card9, R.drawable.card10, R.drawable.card11, R.drawable.card12, R.drawable.card13, R.drawable.card14, R.drawable.card15, R.drawable.card16,R.drawable.joker2, R.drawable.card17 ,R.drawable.card18,R.drawable.card19,R.drawable.card20,R.drawable.card21,R.drawable.card22,R.drawable.card23,R.drawable.card24, R.drawable.joker3,R.drawable.card25,R.drawable.card26,R.drawable.card27,R.drawable.card28,R.drawable.card29,R.drawable.card30,R.drawable.card31,R.drawable.card32,R.drawable.joker4};
    int[] bosses={R.drawable.boss1a,R.drawable.boss1b,R.drawable.boss1c,R.drawable.boss1cc,R.drawable.boss3a,R.drawable.boss3b,R.drawable.boss3c,R.drawable.boss3c,R.drawable.boss2a,R.drawable.boss2b,R.drawable.boss2cc,R.drawable.boss2c,R.drawable.boss4a,R.drawable.boss4b,R.drawable.boss4c,R.drawable.boss4cc};
    boolean[] dontCountUsAll;
    int[] savenames;
    int[] change;
    boolean [] level6;
    boolean [] level6Cards;

    SharedPreferences sp;
    MediaPlayer music;
    boolean playMusic;
    boolean english;
    boolean inFive= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp= getSharedPreferences("details", MODE_PRIVATE);



        english= sp.getBoolean("english", false);
        MusicManger.player.pause();
        if(english)
        {
            setLocale("hi");
        }
        else {
            setLocale("he");
        }

        setContentView(R.layout.activity_heart);

        bossNPC=(ImageView) findViewById(R.id.bossnpc);
        bgElement = (ConstraintLayout) findViewById(R.id.heart);
        bossPhoto = (ImageView) findViewById(R.id.bossPhoto);
        levelexp=(ImageView) findViewById(R.id.levelexp);
        matchEXP=findViewById(R.id.matchexp);
        mainCardsBack = findViewById(R.id.maincardback);
        explain= findViewById(R.id.Explain);


        sp= getSharedPreferences("details", MODE_PRIVATE);
        world= sp.getInt("worldNow",1);
        newGame= sp.getBoolean("newGame",true);


        sizecard = cards.length;
        cardsWas = new boolean[cards.length];
        inGame = new int[cards.length];
        restartWas();
        savenames = new int[world * 4];
        change = new int[world * 4];

        name_preview = findViewById(R.id.name);
        if(newGame==false){
            String info_name =sp.getString("user_name","");
            name_preview.setText(name_preview.getText().toString() + " " + info_name);
        }

        if(newGame==true){
            name_preview.setText(name_preview.getText().toString() + " " + getString(R.string.Guest));

        }
        timer = findViewById(R.id.CircleTimerNum);
        timerStart = findViewById(R.id.circlestartgame);
        timeStartTxt = findViewById(R.id.textstartgame);
        linearCards = findViewById(R.id.linnar_cards);
        circle_level = findViewById(R.id.circle_level);
        level6exp=true;
        timeAll= findViewById(R.id.totalTime);

        createCards();
        if(level == 6)
        {
            levelSix();
        }
        else {
            mainCardsBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    explain.setVisibility(View.INVISIBLE);
                    if (flip == false && level < 6) {
                        startTimer(3000, timeStartTxt);
                        timerStart.setVisibility(View.VISIBLE);
                        timeStartTxt.setVisibility(View.VISIBLE);
                        timer.setText(""+(timeLevel/1000-1));
                        rand = new Random();
                        master = rand.nextInt(world * 9);

                        FlipCard(mainCardsBack, cards[master]);
                        rand = new Random();
                        int keyMaster = rand.nextInt(linecards * limit) + 1;
                        inGame[keyMaster] = cards[master];
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                int p = 0;
                                for (int j = 0; j < linecards; j++) {
                                    for (int i = 0; i < limit; i++) {
                                        ImageView imageView = findViewById(j * limit + i + 1);
                                        if (imageView.getId() == keyMaster) {
                                            FlipCard(imageView, cards[master]);
                                            imageView.setId(cards[master]);
                                            change[p++] = cards[master];
                                            cardsWas[master] = true;
                                        } else {
                                            rand = new Random();
                                            nRand = rand.nextInt(world * 9);
                                            while (cardsWas[nRand] || nRand == master) {
                                                rand = new Random();
                                                nRand = rand.nextInt(world * 9);
                                            }
                                            FlipCard(imageView, cards[nRand]);
                                            imageView.setId(cards[nRand]);
                                            change[p++] = cards[nRand];
                                            inGame[j * limit + i + 1] = cards[nRand];
                                            cardsWas[nRand] = true;
                                        }
                                        if (level == 5) {
                                            FlipCard(mainCardsBack, R.drawable.card0);
                                            Handler handler = new Handler();
                                            inFlip = false;
                                            handler.postDelayed(new Runnable() {
                                                public void run() {
                                                    inFlip = true;
                                                }
                                            }, 4500);
                                        }

                                        imageView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (inFlip) {
                                                    inFlip = false;
                                                    Handler handler = new Handler();
                                                    handler.postDelayed(new Runnable() {
                                                        public void run() {
                                                            inFlip = true;
                                                        }
                                                    }, 500);
                                                    if (cards[master] == v.getId()) {
                                                        int boss = rand.nextInt(world * 4);
                                                        while (boss!=(world*4)-1 && boss!=(world*4)-2){
                                                            boss = rand.nextInt(world * 4);
                                                        }
                                                        bossPhoto.setImageResource(bosses[boss]);
                                                        if(playMusic) {
                                                            MediaPlayer musicWin = MediaPlayer.create(Heart.this, R.raw.balon);
                                                            musicWin.start();
                                                        }
                                                        imageView.setVisibility(View.INVISIBLE);
                                                        if (level < 4) {
                                                            levelStart();
                                                        } else if (level == 4) {
                                                            levelFour();
                                                        } else if (level == 5) {
                                                            imageView.setVisibility(View.VISIBLE);
                                                            FlipCard(imageView, cards[master]);
                                                            Handler handler3 = new Handler();
                                                            handler3.postDelayed(new Runnable() {
                                                                public void run() {
                                                                    cancelTimer();
                                                                    linearCards.removeAllViews();
                                                                    restartWas();
                                                                    FlipCard(mainCardsBack, R.drawable.card0);
                                                                    flip = false;
                                                                    dialogWin();
                                                                }
                                                            }, 1000);
                                                        }
                                                        if (levelEnd) {
                                                            cancelTimer();
                                                            linearCards.removeAllViews();
                                                            restartWas();
                                                            FlipCard(mainCardsBack, R.drawable.card0);
                                                            flip = false;
                                                            dialogWin();
                                                        }
                                                    } else {
                                                        if (level == 5) {
                                                            ImageView imageView2= findViewById(cards[master]);
                                                            FlipCard(imageView2,cards[master]);
                                                            FlipCard(imageView,imageView.getId());
                                                            Handler handler3 = new Handler();
                                                            cancelTimer();
                                                            handler3.postDelayed(new Runnable() {
                                                                public void run() {
                                                                    linearCards.removeAllViews();
                                                                    restartWas();
                                                                    dialogLose();
                                                                }
                                                            }, 1000);

                                                        }
                                                        else
                                                        {
                                                            linearCards.removeAllViews();
                                                            cancelTimer();
                                                            restartWas();
                                                            dialogLose();
                                                        }
                                                    }
                                                }
                                            }
                                        });
                                    }
                                }

                            }
                        }, 2500);
                        flip = true;
                    }
                }

            });
        }
    }

    void startTimer(int time, TextView timerNow) {
        if(level==6 && level6exp==true){
            matchEXP.setVisibility(View.VISIBLE);
        }
        cTimer = new CountDownTimer(time, 1000) {
            public void onTick(long millisUntilFinished) {
                timerNow.setText("" + (millisUntilFinished / 1000));
                if(timerNow.getId() == timer.getId()) {
                    sumTime++;
                    timeAll.setText("" + sumTime);
                    if(millisUntilFinished / 1000 < 2)
                    {
                        bossPhoto.setImageResource(bosses[world*4-3]);
                        ImageView imageView= findViewById(R.id.CircleTimer);
                        imageView.setImageResource(R.drawable.circletimered);
                        if(playMusic) {
                            MediaPlayer musicTime = MediaPlayer.create(Heart.this, R.raw.timer);
                            musicTime.start();
                        }
                    }
                    else
                    {
                        ImageView imageView= findViewById(R.id.CircleTimer);
                        imageView.setImageResource(R.drawable.circletime);
                    }

                }
                else if(millisUntilFinished/1000 == 1)
                {
                    if(level==6 && level6exp==true){
                        matchEXP.setVisibility(View.INVISIBLE);
                        level6exp=false;
                    }
                    if(playMusic) {
                        MediaPlayer musicStart = MediaPlayer.create(Heart.this, R.raw.start);
                        musicStart.start();
                    }
                }
            }
            public void onFinish() {
                if (timerNow.getId() == timeStartTxt.getId()) {
                    startTimer(timeLevel, timer);
                    timerStart.setVisibility(View.INVISIBLE);
                    timeStartTxt.setVisibility(View.INVISIBLE);
                } else if (timerNow.getId() == timer.getId()) {
                    if (level == 5 && inFive == false) {
                        for (int k = 0; k < limit * linecards + 1; k++) {
                            if (inGame[k] != 0) {
                                ImageView imageView1 = findViewById(inGame[k]);
                                FlipCard(imageView1, R.drawable.card0);
                            }
                        }
                        startTimer(5000, timer);
                        inFive= true;
                        FlipCard(mainCardsBack, cards[master]);
                    } else {
                        leftCards=2;
                        linearCards.removeAllViews();
                        restartWas();
                        dialogLose();
                    }
                }
            }
        };
        cTimer.start();
    }

    void cancelTimer() {
        if (cTimer != null)
            cTimer.cancel();
    }

    void FlipCard(ImageView cardFlip, int card) {
        final ObjectAnimator oa1 = ObjectAnimator.ofFloat(cardFlip, "scaleX", 1f, 0f);
        final ObjectAnimator oa2 = ObjectAnimator.ofFloat(cardFlip, "scaleX", 0f, 1f);
        oa1.setInterpolator(new DecelerateInterpolator());
        if(cardFlip.getId() == mainCardsBack.getId())
        {
            if(playMusic) {
                MediaPlayer musicFlip = MediaPlayer.create(Heart.this, R.raw.flipcard);
                musicFlip.start();
            }
        }
        oa2.setInterpolator(new AccelerateDecelerateInterpolator());
        oa1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                cardFlip.setImageResource(card);
                oa2.start();
            }
        });
        oa1.start();
    }

    void restartWas() {
        for (int i = 0; i < sizecard; i++) {
            cardsWas[i] = false;
            inGame[i]=0;
        }
    }

    void createCards() {
        if(world==1){
            bgElement.setBackground(ContextCompat.getDrawable(this,R.drawable.blue));
            bossNPC.setImageResource(R.drawable.shub);
            mainCardsBack.setImageResource(R.drawable.cardstart1);
            explain.setBackgroundResource(R.drawable.blue);

        }
        else if(world==2){
            bgElement.setBackground(ContextCompat.getDrawable(this,R.drawable.pink));
            bossNPC.setImageResource(R.drawable.seny);
            mainCardsBack.setImageResource(R.drawable.cardstart2);
            explain.setBackgroundResource(R.drawable.pink);

        }

        else if(world==3){
            bgElement.setBackground(ContextCompat.getDrawable(this,R.drawable.tibet));
            mainCardsBack.setImageResource(R.drawable.cardstart3);
            bossNPC.setImageResource(R.drawable.monk);
            explain.setBackgroundResource(R.drawable.tibet);

        }

        else if(world==4){
            bgElement.setBackground(ContextCompat.getDrawable(this,R.drawable.black));
            bossNPC.setImageResource(R.drawable.hilfasm);
            mainCardsBack.setImageResource(R.drawable.cardstart4);
            explain.setBackgroundResource(R.drawable.black);
        }
        explain.setVisibility(View.VISIBLE);
        bossPhoto.setImageResource(bosses[world*4-4]);
        mainCardsBack.setVisibility(View.VISIBLE);
        ImageView imageView= findViewById(R.id.CircleTimer);
        imageView.setImageResource(R.drawable.circletime);
        inFlip= true;
        linearCards.removeAllViews();
        restartWas();
        levelEnd = false;
        switch (level) {
            case 1: {
                timeLevel = 5000;
                levelexp.setImageResource(R.drawable.s1);
                textWin= (String)getResources().getText(R.string.winLevel2);
                textLevel= (String)getResources().getText(R.string.level1);
                explain.setText(R.string.level1o);
                break;
            }
            case 2: {
                timeLevel = 4000;
                levelexp.setImageResource(R.drawable.s2);
                textWin= (String)getResources().getText(R.string.winLevel3);
                textLevel= (String)getResources().getText(R.string.level2);
                explain.setText(R.string.level2o);

                break;
            }
            case 3: {
                levelexp.setImageResource(R.drawable.s3);
                timeLevel = 3000;
                textWin = (String)getResources().getText(R.string.winLevel4);
                textLevel= (String)getResources().getText(R.string.level3);
                explain.setText(R.string.level3o);
                break;
            }
            case 4: {
                levelexp.setImageResource(R.drawable.s4);
                timeLevel = 2000;
                textWin= (String)getResources().getText(R.string.winLevel5);
                textLevel= (String)getResources().getText(R.string.level4);
                explain.setText(R.string.level4o);
                break;
            }
            case 5: {
                levelexp.setImageResource(R.drawable.s5);
                if(world==1) {
                    timeLevel = 5000;
                }
                else if(world==2){
                    timeLevel =10000;
                }
                else if(world==3){
                    timeLevel =15000;
                }
                else if(world==4){
                    timeLevel=20000;
                }
                inFive= false;
                textWin= (String)getResources().getText(R.string.winLlevel6);
                textLevel= (String)getResources().getText(R.string.level5);
                explain.setText(R.string.level5o);
                break;
            }
            case 6:
            {
                levelexp.setImageResource(R.drawable.s6);
                if(world==1) {
                    timeLevel = 8000;
                }
                else if(world == 2){
                    timeLevel=20000;
                }
                else if(world==3){
                    timeLevel=30000;
                }
                else if(world==4){
                    timeLevel=50000;
                }
                textWin= (String)getResources().getText(R.string.winLevel1);
                textLevel= (String)getResources().getText(R.string.level6);
                explain.setVisibility(View.INVISIBLE);
                break;
            }
        }
        int total = ((world - 1) * 6) + level;
        circle_level.setText("" + total);

        if (world == 1) {
            linecards = 2;
            limit = 2;
            big = true;
        } else if (world != 1) {
            linecards = world;
            limit = 4;
            big= false;
        }
        leftCards = linecards * limit;
        for (int j = 0; j < linecards; j++) {
            LinearLayout linearLayout = new LinearLayout(Heart.this);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            if (big == true) {
                linearLayout.setPadding(100, 50, 50, 10);
            } else {
                linearLayout.setPadding(5, 10, 5, 10);
            }

            for (int i = 0; i < limit; i++) {
                ImageView btn = new ImageView(Heart.this);
                btn.setImageResource(R.drawable.card0);
                btn.setId(j * limit + i + 1);

                if (big == true) {
                    linearLayout.addView(btn, 450, 450);
                } else {
                    linearLayout.addView(btn, 250, 250);
                }
            }
            linearCards.addView(linearLayout);
        }
        if(level==6){
            levelSix();
        }
    }

    void levelSix()
    {
        level6=new boolean [world*4];
        level6Cards=new boolean [sizecard];
        for (int i=0; i<level6.length; i++){
            level6[i]=false;
        }
        for (int i=0; i<world*9; i++){
            level6Cards[i]=false;
        }
        mainCardsBack.setVisibility(View.INVISIBLE);
        startTimer(5000, timeStartTxt);
        timerStart.setVisibility(View.VISIBLE);
        timeStartTxt.setVisibility(View.VISIBLE);
        timer.setText(""+(timeLevel/1000-1));
        for (int i = 0; i < world*2; i++) {
            rand = new Random();
            findMate = rand.nextInt(world * 9);
            while (level6Cards[findMate]==true){
                rand = new Random();
                findMate = rand.nextInt(world * 9);
            }
            level6Cards[findMate] = true;
            for(int k=0; k<2; k++) {
                rand = new Random();
                int findPlace = rand.nextInt(world * 4);
                while (level6[findPlace] == true) {
                    rand = new Random();
                    findPlace = rand.nextInt(world * 4);
                }
                level6[findPlace] = true;
                ImageView imageView = findViewById(findPlace + 1);
                leftCards= world*2;
                inGame[findPlace + 1] = cards[findMate];
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int timeOpen=0;
                                if (inFlip) {
                                    inFlip = false;
                                    if(openTogether)
                                    {
                                        timeOpen= 1000;
                                    }
                                    else
                                    {
                                        timeOpen= 400;
                                    }
                                    Handler handler2 = new Handler();
                                    handler2.postDelayed(new Runnable() {
                                        public void run() {
                                            inFlip = true;
                                        }
                                    }, timeOpen);
                                    if (imageView.getId() != openNow) {
                                        FlipCard(imageView, inGame[imageView.getId()]);
                                        if (openTogether) {
                                            ImageView imageView2 = findViewById(openNow);
                                            Handler handler1 = new Handler();
                                            handler1.postDelayed(new Runnable() {
                                                public void run() {
                                                    if (inGame[imageView.getId()] == inGame[openNow]) {
                                                        if(playMusic) {
                                                            MediaPlayer musicWin = MediaPlayer.create(Heart.this, R.raw.balon);
                                                            musicWin.start();
                                                        }
                                                        leftCards--;
                                                        imageView2.setVisibility(View.INVISIBLE);
                                                        v.setVisibility(View.INVISIBLE);
                                                        if (leftCards == 0) {
                                                            cancelTimer();
                                                            dialogWin();
                                                        }
                                                    } else {
                                                        FlipCard(imageView, R.drawable.card0);
                                                        FlipCard(imageView2, R.drawable.card0);
                                                        if(playMusic) {
                                                            MediaPlayer musicFlip = MediaPlayer.create(Heart.this, R.raw.flipcard);
                                                            musicFlip.start();
                                                        }
                                                    }
                                                    openTogether = false;
                                                    openNow= 0;
                                                }
                                            }, 1000);

                                        } else {
                                            openNow = imageView.getId();
                                            openTogether = true;
                                        }
                                    }
                                }
                            }
                        });
                    }

                }, 5000);
            }

        }
    }

    void levelStart() {
        cardsWas[master] = false;
        leftCards--;
        if (leftCards != 0) {
            rand = new Random();
            master = rand.nextInt(world * 9);
            while (!cardsWas[master]) {
                rand = new Random();
                master = rand.nextInt(world * 9);
            }
            cardsWas[master] = false;
            FlipCard(mainCardsBack, cards[master]);
            cancelTimer();
            startTimer(timeLevel, timer);
        } else {
            levelEnd = true;
        }
    }

    void levelFour() {
        int pop = 0;
        int empty = 50;
        dontCountUsAll = new boolean[sizecard];
        for (int i = 0; i < sizecard; i++) {
            dontCountUsAll[i] = false;
        }
        leftCards--;
        if (leftCards != 0) {
            for (int i = 0; i < world * 4; i++) {

                if (change[i] != cards[master] && change[i] > 500) {
                    ImageView replace = findViewById(change[i]);
                    rand = new Random();
                    int inside = rand.nextInt(world * 9);
                    while (cards[inside] == cards[master] || dontCountUsAll[inside] == true) {
                        rand = new Random();
                        inside = rand.nextInt(world * 9);
                    }
                    replace.setImageResource(cards[inside]);
                    savenames[i] = cards[inside];
                    dontCountUsAll[inside] = true;
                    replace.setId(pop++);
                } else if (change[i] < 500) {
                    ImageView replace = findViewById(change[i]);
                    savenames[i] = empty++;
                    replace.setId(pop++);
                } else if (change[i] == cards[master]) {
                    ImageView replace = findViewById(change[i]);
                    savenames[i] = empty++;
                    replace.setId(pop++);
                }
            }
            pop = 0;
            for (int i = 0; i < world * 4; i++) {
                ImageView replace = findViewById(pop++);
                replace.setId(savenames[i]);
                change[i] = savenames[i];
            }

            rand = new Random();
            master = rand.nextInt(world * 9);
            while (dontCountUsAll[master] == false) {
                rand = new Random();
                master = rand.nextInt(world * 9);
            }
            FlipCard(mainCardsBack, cards[master]);
            cancelTimer();
            startTimer(timeLevel, timer);

        } else {
            levelEnd = true;
        }
    }

    void dialogWin()
    {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("sumTime", sumTime);
        editor.commit();
        AlertDialog.Builder builder = new AlertDialog.Builder(Heart.this);
        builder.setCancelable(false);
        cancelTimer();

        if(playMusic) {
            MediaPlayer musicWin = MediaPlayer.create(Heart.this, R.raw.win);
            musicWin.start();
        }

        View dialogView = getLayoutInflater().inflate(R.layout.win_dialogue, null);

        builder.setView(dialogView).setPositiveButton(
                R.string.nextlevel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(level==6)
                        {

                            level=1;
                            savenames = new int[world * 4];
                            change = new int[world * 4];
                            Intent gonext = new Intent(Heart.this, diffrent.class);
                            startActivity(gonext);
                            finish();

                        }
                        else
                        {
                            level++;
                        }
                        setImageBoss();
                        restartWas();
                        createCards();
                        flip=false;
                    }
                });

        AlertDialog alert1 = builder.create();
        alert1.show();


    }


    void setImageBoss(){
        if(world==1) {
            mainCardsBack.setImageResource(R.drawable.cardstart1);
        }
        else if(world==2){
            mainCardsBack.setImageResource(R.drawable.cardstart2);

        }
        else if(world==3){
            mainCardsBack.setImageResource(R.drawable.cardstart3);

        }
        else if(world==4){
            mainCardsBack.setImageResource(R.drawable.cardstart4);

        }
    }


    void dialogLose()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(Heart.this);
        builder.setCancelable(false);
        if(playMusic) {
            MediaPlayer musicLose = MediaPlayer.create(Heart.this, R.raw.lose);
            musicLose.start();
        }
        View dialogView = getLayoutInflater().inflate(R.layout.lose_dialogue, null);
        builder.setView(dialogView).setPositiveButton(
                R.string.try_again,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mainCardsBack.setImageResource(R.drawable.card0);
                        level=1;
                        restartWas();
                        setImageBoss();;
                        createCards();
                        flip=false;
                        sumTime= 0;
                        timeAll.setText("0");
                    }
                });


        builder.setView(dialogView).setNegativeButton(
                R.string.worldback,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent gonext = new Intent(Heart.this, Road.class);
                        startActivity(gonext);
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu,menu);
        MenuItem item= menu.findItem(R.id.music);
        playMusic= sp.getBoolean("music", true);
        if(playMusic)
        {
            item.setTitle("on");
            item.setIcon(R.drawable.speker);
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
                item.setTitle("on");
                item.setIcon(R.drawable.speker);
            }
            else
            {
                playMusic= false;
                item.setTitle("off");
                item.setIcon(R.drawable.mute);

            }

        }
        else  if(item.getItemId() == R.id.world)
        {
            level=1;
            sumTime= 0;
            timeAll.setText("0");
            cancelTimer();
            Intent gonext = new Intent(Heart.this, Road.class);
            startActivity(gonext);
            finish();
        }
        else if(item.getItemId() == R.id.user)
        {
            level=1;
            sumTime= 0;
            timeAll.setText("0");
            cancelTimer();
            Intent gonext = new Intent(Heart.this, MainActivity.class);
            startActivity(gonext);
            finish();
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
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("music", playMusic);
        if(world > sp.getInt("world",1)) {
            editor.putInt("world", world);
        }
        editor.commit();
    }

    @Override
    public void finish() {
        super.finish();
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("music", playMusic);
        if(world > sp.getInt("world",1)) {
            editor.putInt("world", world);
        }
        editor.commit();
    }
}