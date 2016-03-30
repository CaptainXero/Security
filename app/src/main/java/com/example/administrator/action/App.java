package com.example.administrator.action;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Administrator on 2016-03-30.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        correctSIM();
    }

    public void correctSIM() {
        //检查SIM卡状态
        SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        //防盗保护状态
        boolean protecting = sp.getBoolean("protecting",true);
        if(protecting){
            //获取绑定的SIM卡号
            String bindSIM = sp.getString("sim","");
            //获取当前SIM卡号
            TelephonyManager telephonyManager = (TelephonyManager)
                    getSystemService(Context.TELEPHONY_SERVICE);
            String realSIM = telephonyManager.getSimSerialNumber();
            if(bindSIM.equals(realSIM)){
                Log.i("","Sim卡未发生变化");
            }else {
                Log.i("","SIM发生变化");
                String safeNumber = sp.getString("safenumber","");
                if(!TextUtils.isEmpty(safeNumber)){
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(safeNumber,null,"您的亲友手机SIM卡被更换！",null,null);
                }
            }
        }
    }
}
