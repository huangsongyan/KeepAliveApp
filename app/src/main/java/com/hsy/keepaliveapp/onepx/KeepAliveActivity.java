package com.hsy.keepaliveapp.onepx;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.hsy.keepaliveapp.receiver.AlarmReceiver;

/**
 * 1像素activity用于提高系统优先级,提高app存活性.
 * <p>
 * 方案设计思想：监控手机锁屏解锁事件，在屏幕锁屏时启动1个像素的 Activity，在用户解锁时将 Activity 销毁掉。注意该 Activity 需设计成用户无感知。
 * <p>
 * 通过该方案，可以使进程的优先级在屏幕锁屏时间由4提升为最高优先级1。
 * <p>
 * 方案适用范围：
 * <p>
 * 适用场景：本方案主要解决第三方应用及系统管理工具在检测到锁屏事件后一段时间（一般为5分钟以内）内会杀死后台进程，已达到省电的目的问题。
 * <p>
 * 适用版本：适用于所有的 Android 版本。
 */

public class KeepAliveActivity extends Activity {

    private BroadcastReceiver mFinishReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = 0;
        params.y = 0;
        params.height = 1;
        params.width = 1;
        window.setAttributes(params);
        mFinishReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        };
        registerReceiver(mFinishReceiver, new IntentFilter("finish activity"));
        checkScreenOn("onCreate");
    }

    //检查app是否在当前屏幕,是的话关掉一像素activity
    private void checkScreenOn(String methodName) {
        Log.d(getClass().getName(), "from call method: " + methodName);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();
        Log.d(getClass().getName(), "isScreenOn: " + isScreenOn);
        if (isScreenOn) {
            finish();
        }

    }

    @Override
    protected void onDestroy() {
        Log.d(getClass().getName(), "===onDestroy===");
        try {
            unregisterReceiver(mFinishReceiver);
        } catch (IllegalArgumentException e) {
            Log.e(getClass().getName(), "receiver is not resisted: " + e);
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkScreenOn("onResume");
    }


}
