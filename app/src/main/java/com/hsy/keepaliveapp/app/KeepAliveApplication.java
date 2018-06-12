package com.hsy.keepaliveapp.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.hsy.keepaliveapp.receiver.EmptyReceiver;
import com.hsy.keepaliveapp.receiver.GuardReceiver;
import com.hsy.keepaliveapp.service.EmptyService;
import com.hsy.keepaliveapp.service.GuardService;
import com.marswin89.marsdaemon.DaemonClient;
import com.marswin89.marsdaemon.DaemonConfigurations;

/**
 * Created by hsy on 18/4/28.
 */
public class KeepAliveApplication extends Application {

    private DaemonClient mDaemonClient;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        mDaemonClient = new DaemonClient(createDaemonConfigurations());
        mDaemonClient.onAttachBaseContext(base);
    }



    private DaemonConfigurations createDaemonConfigurations(){
        DaemonConfigurations.DaemonConfiguration configuration1 = new DaemonConfigurations.DaemonConfiguration(
                "com.hsy.keepaliveapp.service.GuardService:service",
                GuardService.class.getCanonicalName(),
                GuardReceiver.class.getCanonicalName());
        DaemonConfigurations.DaemonConfiguration configuration2 = new DaemonConfigurations.DaemonConfiguration(
                "com.hsy.keepaliveapp.service.EmptyService:emptyService",
                EmptyService.class.getCanonicalName(),
                EmptyReceiver.class.getCanonicalName());
        DaemonConfigurations.DaemonListener listener = new MyDaemonListener();
        return new DaemonConfigurations(configuration1, configuration2, listener);
    }


    class MyDaemonListener implements DaemonConfigurations.DaemonListener{
        @Override
        public void onPersistentStart(Context context) {
            Log.v("TAG","onPersistentStart---");
        }

        @Override
        public void onDaemonAssistantStart(Context context) {
            Log.v("TAG","onDaemonAssistantStart---");
        }

        @Override
        public void onWatchDaemonDaed() {
            Log.v("TAG","onWatchDaemonDaed---");
        }
    }
}
