package com.skipop.nathan.phoneguard;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

/**
 * Created by nathan on 12/19/14.
 * Nathan Prat
 */
public class Media {
    private final String tag = "PhoneGuard Media";
    MediaPlayer mediaPlayer;

    public Media() {
    }

    public void playAlarm(Context context){
        Log.d(tag, "playAlarm ");

        mediaPlayer = MediaPlayer.create(context, R.raw.sound_alarm);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
    }

    public void stopAlarm(Context context){
        Log.d(tag, "playAlarm ");

        mediaPlayer.release();
        mediaPlayer = null;
    }

}
