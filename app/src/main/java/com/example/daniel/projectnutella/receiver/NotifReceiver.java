package com.example.daniel.projectnutella.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Daniel on 23/7/2016.
 */
public class NotifReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("HERE","NOTIFICATION ACTIVATED");
    }
}
