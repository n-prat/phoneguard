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

    private int NOTIFICATION = R.string.service_started;

    public SecurityService() {
    }

    //TODO remove binder?
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    //TODO add a Context?
    //TODO foreground? notification?
    @Override
    public void onCreate() {
        Log.d(tag, "onCreate ");
        //mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        Toast.makeText(this, "Phoneguard service starting", Toast.LENGTH_SHORT).show();

        //startForeground(FOREGROUND_ID,buildForegroundNotification(filename));

        // Display a notification about us starting.  We put an icon in the status bar.
        //showNotification();
    }

    @Override
    public void onDestroy() {
        Log.d(tag, "onDestroy ");
        // Cancel the persistent notification.
        //mNM.cancel(NOTIFICATION);

        //stopForeground(true);

        // Tell the user we stopped.
        Toast.makeText(this, "Phoneguard service stopped", Toast.LENGTH_SHORT).show();
    }

    private void showNotification() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = getText(R.string.service_started);
        Log.d(tag, "showNotification "+text);

        // Set the icon, scrolling text and timestamp
        Notification notification = new Notification(R.drawable.stat_sample, text,
                System.currentTimeMillis());

        // Send the notification.
        mNM.notify(NOTIFICATION, notification);
    }

    private Notification buildForegroundNotification(String filename) {
        NotificationCompat.Builder b=new NotificationCompat.Builder(this);

        b.setOngoing(true);

        b.setContentTitle(getString(R.string.foreground_service))
                .setContentText(filename)
                .setSmallIcon(android.R.drawable.stat_sys_download)
                .setTicker(getString(R.string.foreground_service));

        return(b.build());
    }
}
