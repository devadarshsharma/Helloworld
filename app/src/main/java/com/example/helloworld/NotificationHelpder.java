package com.example.helloworld;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class NotificationHelpder extends ContextWrapper {
    public static final String channelID = "channelID";
    public static final String channelNAme = "Channel Name";
    public String alarmTitle;
    public String alarmContent;

    private NotificationManager mManager;

    public NotificationHelpder(Context base, String aTitle, String aContnet) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
        alarmTitle = aTitle;
        alarmContent = aContnet;
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelNAme, NotificationManager.IMPORTANCE_HIGH);

        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification() {

        Intent resultIntent = new Intent(this, HomeActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle(alarmTitle)
                .setContentText(alarmContent)
                .setSmallIcon(R.drawable.logo)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent);
    }
}
