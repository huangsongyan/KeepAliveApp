package com.hsy.keepaliveapp.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hsy.keepaliveapp.receiver.AlarmReceiver;

/**
 * Created by hsy on 18/4/29.
 */
public class AlarmService extends Service {

    private static String TAG = "GuardService";

    public static void start(Context context) {
        Log.i(TAG, "start AlarmService");
        try {
            Intent service = new Intent(context, AlarmService.class);
            context.startService(service);
        } catch (Throwable t) {
            Log.e(TAG, t.getMessage(), t);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //获取提醒管理器
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //这是10s的毫秒数
        int intervalMillis = 10 * 1000;
        //SystemClock.elapsedRealtime()表示1970年1月1日0点至今所经历的时间
        long triggerAtTime = SystemClock.elapsedRealtime();
        //此处设置开启AlarmReceiver这个Service
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        //ELAPSED_REALTIME_WAKEUP表示让定时任务的出发时间从系统开机算起，并且会唤醒CPU。
        manager.setRepeating(AlarmManager.ELAPSED_REALTIME, triggerAtTime, intervalMillis, pi);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //在Service结束后关闭AlarmManager
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.cancel(pi);

    }
}
