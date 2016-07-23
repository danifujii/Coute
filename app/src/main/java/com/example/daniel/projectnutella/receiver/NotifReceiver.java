package com.example.daniel.projectnutella.receiver;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.daniel.projectnutella.R;
import com.example.daniel.projectnutella.SettingsActivity;
import com.example.daniel.projectnutella.time.AlarmManager;

/**
 * Created by Daniel on 23/7/2016.
 */
public class NotifReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmManager.disableAlarm(context);
        ((NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE))
                .cancel(intent.getIntExtra("NOTIF_ID",0));
    }
}
