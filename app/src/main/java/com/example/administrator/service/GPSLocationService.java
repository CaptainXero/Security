package com.example.administrator.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;

public class GPSLocationService extends Service {
    private LocationManager lm;
    private MyListener listener;
    public GPSLocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        listener = new MyListener();
        /**
         * criteria 查询条件
         * true 仅返回可同的位置提供器
         */
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);//获取准确位置
        criteria.setCostAllowed(true);//允许产生开销
        String name = lm.getBestProvider(criteria,true);
        System.out.println("最好的位置提供器"+name);
        /**
         * 下面代码还没实现动态授权
         */
        lm.requestLocationUpdates(name, 0, 0, listener);

    }

    private class MyListener implements LocationListener{
        @Override
        public void onLocationChanged(Location location) {
            StringBuilder sb = new StringBuilder();
            sb.append("accuracy:"+location.getAccuracy()+"\n");
            sb.append("speed:"+location.getSpeed()+"\n");
            sb.append("jingdu:"+location.getLongitude()+"\n");
            sb.append("weidu:"+location.getLatitude()+"\n");
            String result = sb.toString();
            SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
            String safenumber = sp.getString("safephone", "");
            SmsManager.getDefault().sendTextMessage(safenumber, null, result, null, null);
            stopSelf();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        lm.removeUpdates(listener);
        listener = null;
    }
}

