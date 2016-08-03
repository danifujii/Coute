package com.example.daniel.projectnutella.time;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.daniel.projectnutella.R;
import com.example.daniel.projectnutella.receiver.AlarmReceiver;
import com.example.daniel.projectnutella.receiver.BootReceiver;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Daniel on 23/7/2016.
 */
public class AlarmManager {

    public static void enableAlarm(Context act, long newValue){
        android.app.AlarmManager manager = (android.app.AlarmManager)act.getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(act,
                0, new Intent(act, AlarmReceiver.class), 0);
        manager.setInexactRepeating(android.app.AlarmManager.RTC_WAKEUP, getTime(newValue).getTimeInMillis(),
                android.app.AlarmManager.INTERVAL_FIFTEEN_MINUTES, alarmIntent);

        //Activate the BootReceiver
        ComponentName receiver = new ComponentName(act, BootReceiver.class);
        PackageManager pm = act.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        //Save the setting, because this method can be called outside the UI
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(act);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(act.getString(R.string.pref_reminder),true);
        editor.apply();
    }

    public static void disableAlarm(Context act){
        android.app.AlarmManager manager = (android.app.AlarmManager)act.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(PendingIntent.getBroadcast(act,0,
                new Intent(act, AlarmReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT));

        //Deactivate the BootReceiver
        ComponentName receiver = new ComponentName(act, BootReceiver.class);
        PackageManager pm = act.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(act);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(act.getString(R.string.pref_reminder),false);
        editor.apply();
    }

    public static Calendar getTime(long time){
        Calendar calendar = Calendar.getInstance();
        Date date = new Date(time);
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minute);

        return calendar;
    }
}
