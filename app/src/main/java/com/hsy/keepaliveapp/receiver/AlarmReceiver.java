package com.hsy.keepaliveapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.hsy.keepaliveapp.service.GuardService;

/**
 * Created by hsy on 18/4/29.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("AlarmReceiver", "AlarmReceiver-onReceive");
        GuardService.start(context);
    }


}
