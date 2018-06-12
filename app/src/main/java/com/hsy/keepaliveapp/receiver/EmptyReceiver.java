package com.hsy.keepaliveapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hsy.keepaliveapp.service.GuardService;

public class EmptyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("EmptyReceiver","EmptyReceiver-onReceive");
        GuardService.start(context);
    }
}
