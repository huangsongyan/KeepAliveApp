/**
 * create by henrikwu
 * 利用JobScheduler拉活进程，在5.0以上设备，这个方法很好用（除了小米）
 */
package com.hsy.keepaliveapp.schedulerjob;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.hsy.keepaliveapp.service.GuardService;

import java.lang.ref.WeakReference;


/**
 * android5.0以上用JobScheduler
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class DaemonJobService extends JobService {
    private static final String TAG = "DaemonJobService";

    private static class JobHandler extends Handler {

        private final WeakReference<JobService> mJobService;

        public JobHandler(JobService jobService) {
            mJobService = new WeakReference<>(jobService);
        }

        @Override
        public void handleMessage(Message msg) {
            JobService service = mJobService.get();
            if (service != null) {
                JobParameters parameters = (JobParameters) msg.obj;
                service.jobFinished(parameters, false);
//                JobScheduler jobScheduler = (JobScheduler) service.getSystemService(Context.JOB_SCHEDULER_SERVICE);
//                jobScheduler.cancel(parameters.getJobId()); //取消任务时会调用onStopJob()方法
                GuardService.start(service);
            }


        }
    }

    private final JobHandler mJobHandler = new JobHandler(this);


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Service created");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Service destroyed");
    }

    /**
     * When the app's MainActivity is created, it starts this service. This is so that the
     * activity and this service can communicate back and forth. See "setUiCalback()"
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i(TAG, "on start job22: " + params.getJobId());
        mJobHandler.sendMessage(Message.obtain(mJobHandler, 1, params));
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i(TAG, "on stop job11: " + params.getJobId());
        mJobHandler.removeMessages(1);
        return false;
    }

    /**
     * Android5.0以上可以使用JobScheduler来拉活进程
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void startScheduleDaemonJob(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        Log.v(TAG, "startScheduleDaemonJob");
        try {
            int jobId = 1;
            JobInfo.Builder builder = new JobInfo.Builder(jobId, new ComponentName(context, DaemonJobService.class));
            builder.setPeriodic(10 * 1000); //间隔10s
            builder.setPersisted(true);//设置设备重启后，是否重新执行任务
            JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            if (jobScheduler.schedule(builder.build()) <= 0) {
                //If something goes wrong
                Log.v(TAG, "jobScheduler_error");
            } else {
                Log.v(TAG, "success");
            }

        } catch (Exception e) {
        }
    }
}
