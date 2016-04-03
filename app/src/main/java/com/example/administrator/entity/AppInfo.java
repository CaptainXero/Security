package com.example.administrator.entity;

import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2016-04-03.
 */
public class AppInfo {
    public String packageName;
    public Drawable icon;
    public String appName;
    public String apkpath;
    public long appSize;
    public boolean isInRoom;
    public boolean isUserApp;
    public boolean isSelected = false;

    public String getAppLocation(boolean isInRoom){
        if(isInRoom){
            return "手机内存";
        }else {
            return "外部存储";
        }
    }
}
