package com.hsy.keepaliveapp.onepx;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/**
 * 锁屏开屏监听
 */

public class OnepxReceiver extends BroadcastReceiver {
    private static OnepxReceiver receiver;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            Intent it = new Intent(context, KeepAliveActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(it);
            Log.d(getClass().getName(), "1px--screen off-");
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            context.sendBroadcast(new Intent("finish activity"));
            Log.d(getClass().getName(), "1px--screen on-");
        } else {
//            Intent it = new Intent(context, KeepAliveActivity.class);
//            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(it);
//            Log.d(getClass().getName(), "1px--home");
        }
    }

    public static void register1pxReceiver(Context context) {
        if (receiver == null) {
            receiver = new OnepxReceiver();
        }
        context.registerReceiver(receiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
        context.registerReceiver(receiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
        //       context.registerReceiver(receiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }

    public static void unregister1pxReceiver(Context context) {
        context.unregisterReceiver(receiver);
    }
}