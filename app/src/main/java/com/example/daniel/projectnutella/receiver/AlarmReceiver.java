package com.example.daniel.projectnutella.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.example.daniel.projectnutella.MainActivity;
import com.example.daniel.projectnutella.R;

/**
 * Created by Daniel on 22/7/2016.
 */
public class AlarmReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context,NotifReceiver.class);
        i.putExtra("NOTIF_ID",0);
        PendingIntent pIntent = PendingIntent.getBroadcast(context,0,
                i ,0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notif)
                .setContentTitle(context.getString(R.string.notif_title))
                .setContentText(context.getString(R.string.notif_detail))
                .setAutoCancel(true)
                .addAction(R.drawable.ic_block_white_18dp, context.getString(R.string.notif_mute),
                        pIntent);
        if (Build.VERSION.SDK_INT >= 23)
                mBuilder.setColor(context.getColor(R.color.colorPrimary));
        Intent resultIntent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotifManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        //0 in the ID, this is used for updating the notif if necesary. Change later if necessary
        mNotifManager.notify(0, mBuilder.build());
    }
}
