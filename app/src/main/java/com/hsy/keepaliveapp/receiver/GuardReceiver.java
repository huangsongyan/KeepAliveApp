package com.hsy.keepaliveapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hsy.keepaliveapp.service.GuardService;

/**
 * Created by hsy on 18/4/28.
 */
public class GuardReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("GuardReceiver", "GuardReceiver-onReceive");
        GuardService.start(context);
    }
}
