package com.example.helloworld;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String AlarmContent = intent.getStringExtra("AlarmContent");
        String AlarmTitle = intent.getStringExtra("AlarmTitle");

        NotificationHelpder notificationHelper = new NotificationHelpder(context, AlarmTitle, AlarmContent);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
        notificationHelper.getManager().notify(1, nb.build());
    }
}
