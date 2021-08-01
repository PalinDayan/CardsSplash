package com.example.game;

import android.content.Context;
import android.media.MediaPlayer;

public class MusicManger {
    public static MediaPlayer player;
    public static void SoundPlayer(Context ctx, int raw_id){
        player = MediaPlayer.create(ctx, raw_id);
        player.setLooping(true);
        player.start();
    }
    }