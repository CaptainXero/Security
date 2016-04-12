package com.example.administrator.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.administrator.action.App;
import com.example.administrator.service.TrafficMonitoringService;
import com.example.administrator.utils.SystemInfoUtils;

public class BootCompleteReciever extends BroadcastReceiver {
    private static final String TAG = BootCompleteReciever.class.getSimpleName();
    public BootCompleteReciever() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(SystemInfoUtils.isServiceRunning(context,
                "com.example.administrator.service.TrafficMonitoringService")){
            //开启服务
            context.startService(new Intent(context, TrafficMonitoringService.class));
        }
        ((App) context.getApplicationContext()).correctSIM();
    }
}
