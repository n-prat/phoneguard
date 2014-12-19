package com.skipop.nathan.phoneguard;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class SecurityService extends Service {
    private final String tag = "PhoneGuard Service";
    private NotificationManager mNM;
    //private Context mContext;
    Media player;

    private int NOTIFICATION = R.string.service_started;

    //TODO implement service?
    //TODO (start when security is enabled, restart on boot if necessary)
    //TODO (stop only when phone is found again(unlock, security off message?)

    public SecurityService() {
    }

    //TODO remove binder?
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Log.d(tag, "onCreate ");
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        Toast.makeText(this, "Phoneguard service starting", Toast.LENGTH_SHORT).show();

        //TODO use a Foreground service?
        //startForeground(FOREGROUND_ID,buildForegroundNotification(filename));

        // Display a notification about us starting.  We put an icon in the status bar.
        mNM.notify(NOTIFICATION, buildNotification());

        //TODO play only on demand
        player = new Media();
        player.playAlarm(SecurityService.this);
    }

    @Override
    public void onDestroy() {
        Log.d(tag, "onDestroy ");
        // Cancel the persistent notification.
        mNM.cancel(NOTIFICATION);

        //stopForeground(true);

        player.stopAlarm(SecurityService.this);

        // Tell the user we stopped.
        Toast.makeText(this, "Phoneguard service stopped", Toast.LENGTH_SHORT).show();
    }

    private Notification buildNotification() {
        Log.d(tag, "showNotification ");

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_icon)
                        .setContentTitle(getString(R.string.notication_title))
                        .setContentText(getString(R.string.notification_text));

        //mNM.notify(mId, mBuilder.build());
        return mBuilder.build();
    }

    private Notification buildForegroundNotification(String filename) {
        NotificationCompat.Builder b=new NotificationCompat.Builder(this);

        b.setOngoing(true);

        b.setSmallIcon(R.drawable.ic_icon)
                .setContentTitle(getString(R.string.notication_title))
                .setContentText(getString(R.string.notification_text))
                .setTicker(getString(R.string.foreground_service));

        return(b.build());
    }
}
