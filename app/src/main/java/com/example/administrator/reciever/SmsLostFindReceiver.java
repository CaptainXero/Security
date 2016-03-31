package com.example.administrator.reciever;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

import com.example.administrator.security.R;
import com.example.administrator.service.GPSLocationService;


public class SmsLostFindReceiver extends BroadcastReceiver {
    private static final  String TAG = SmsLostFindReceiver.class.getSimpleName();
    private SharedPreferences sharedPreferences;

    public SmsLostFindReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        sharedPreferences = context.getSharedPreferences("config", Activity.MODE_PRIVATE);
        boolean protecting = sharedPreferences.getBoolean("protecting",true);
        if (protecting) {
            // 防盗保护开启
            Object[] objs = (Object[]) intent.getExtras().get("pdus");
            //String format = intent.getStringExtra("format");
            // 获取超级管理员
            DevicePolicyManager dpm = (DevicePolicyManager)
                    context.getSystemService(Context.DEVICE_POLICY_SERVICE);
            for (Object obj : objs) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
                String sender = smsMessage.getOriginatingAddress();
                if(sender.startsWith("+86")){
                    sender = sender.substring(3, sender.length());
                }
                String body = smsMessage.getMessageBody();
                String safephone = sharedPreferences.getString("safephone",null);
                //如果该短信是安全号码发送的
                if (!TextUtils.isEmpty(safephone) & sender.equals(safephone)) {
                    if ("#*location*#".equals(body)) {
                        Log.i(TAG, "返回位置信息.");
                        // 获取位置,在服务里实现。
                        Intent service = new Intent(context,
                                GPSLocationService.class);
                        context.startService(service);
                        abortBroadcast();
                    } else if ("#*alarm*#".equals(body)) {
                        Log.i(TAG, "播放报警音乐.");
                        MediaPlayer player = MediaPlayer.create(context,
                                R.raw.ylzs);
                        player.setVolume(1.0f, 1.0f);
                        player.start();
                        abortBroadcast();
                    } else if ("#*wipedata*#".equals(body)) {
                        Log.i(TAG, "远程清除数据.");
                        dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
                        abortBroadcast();
                    } else if ("#*lockScreen*#".equals(body)) {
                        Log.i(TAG, "远程锁屏.");
                        dpm.resetPassword("123", 0);
                        dpm.lockNow();
                        abortBroadcast();
                    }
                }
            }
        }
    }
}
