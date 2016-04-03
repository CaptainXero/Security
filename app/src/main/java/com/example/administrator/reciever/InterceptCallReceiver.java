package com.example.administrator.reciever;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.example.administrator.dao.BlcakNumberDao;

import java.lang.reflect.Method;

public class InterceptCallReceiver extends BroadcastReceiver {
    public InterceptCallReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        boolean blackNumberStatus = sp.getBoolean("BlackNumStatus",true);
        if(!blackNumberStatus){
            return;
        }
        BlcakNumberDao dao = new BlcakNumberDao(context);
        if(!intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)){
            //如果是来电
            String myIncomingNumber = "";
            TelephonyManager telephonyManager = (TelephonyManager)
                    context.getSystemService(Service.TELEPHONY_SERVICE);
            switch (telephonyManager.getCallState()){
                case TelephonyManager.CALL_STATE_RINGING:
                    myIncomingNumber = intent.getStringExtra("incoming_number");
                    int blackContactMode = dao.getBlackContactMode(myIncomingNumber);
                    if(blackContactMode==1||blackContactMode==3){
                        /**
                         * 观察通话记录应用的数据库状态，
                         * 如果生成了记录，则删除记录
                         */
                        Uri uri = Uri.parse("content://call_log/calls");
                        context.getContentResolver().registerContentObserver
                                (uri, true, new CallLogObserver(new Handler(),myIncomingNumber,context));
                        endCall(context);
                    }
                    break;
            }
        }
    }

    /**
     * 挂断电话（复制两个AIDL）
     */
    private void endCall(Context context) {
        try{
            Class clazz = context.getClassLoader().loadClass("android.os.Service Manager");
            Method method = clazz.getDeclaredMethod("getService", String.class);
            IBinder iBinder = (IBinder) method.invoke(null,Context.TELEPHONY_SERVICE);
            ITelephony itelephony = ITelephony.Stub.asInterface(iBinder);
            itelephony.endCall();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 通过内容观察者监视数据库变化
     */
    private class CallLogObserver extends ContentObserver {
        private String incomingNumber;
        private Context context;
        public CallLogObserver(Handler handler,String incomingNumber,Context context) {
            super(handler);
            this.incomingNumber = incomingNumber;
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            Log.i("CallLogObserver","呼叫记录数据库发生变化");
            context.getContentResolver().unregisterContentObserver(this);
            deleteCallLog(incomingNumber,context);
            super.onChange(selfChange);
        }
    }

    /**
     * 清楚来电记录
     * @param incomingNumber
     * @param context
     */
    private void deleteCallLog(String incomingNumber, Context context) {
        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.parse("content://call_log/calls");
        Cursor cursor = resolver.query(uri,new String[]{"_id"},"number=?",
                new String[]{incomingNumber},"_id desc limit 1");
        if(cursor.moveToNext()){
            String id =cursor.getString(0);
            resolver.delete(uri,"_id=?",new String[]{id});
        }
    }
}
