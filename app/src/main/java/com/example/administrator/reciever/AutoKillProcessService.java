package com.example.administrator.reciever;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class AutoKillProcessService extends Service {
    private ScreenLockReceiver receiver;

    @Override
    public void onCreate() {
        super.onCreate();
        receiver = new ScreenLockReceiver();
        registerReceiver(receiver,new IntentFilter(Intent.ACTION_SCREEN_OFF));
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    class ScreenLockReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            for(ActivityManager.RunningAppProcessInfo info : am.getRunningAppProcesses()){
                String packname = info.processName;
                am.killBackgroundProcesses(packname);
            }
        }
    }
}
