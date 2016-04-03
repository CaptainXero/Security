package com.example.administrator.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsMessage;

import com.example.administrator.dao.BlcakNumberDao;

public class InterceptSmsReciever extends BroadcastReceiver {
    public InterceptSmsReciever() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        boolean blackNumberStatus = sp.getBoolean("BlackNumStatus",true);
        if(!blackNumberStatus){
            return;
        }
        BlcakNumberDao dao = new BlcakNumberDao(context);
        Object[] objs = (Object[]) intent.getExtras().get("pdus");
        for (Object obj:objs){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
            String sender = smsMessage.getOriginatingAddress();
            String body = smsMessage.getMessageBody();
            if(sender.startsWith("+86")){
                sender = sender.substring(3,sender.length());
            }
            int mode = dao.getBlackContactMode(sender);
            if(mode==2||mode==3){
                abortBroadcast();
            }
        }
    }
}
