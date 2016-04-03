package com.example.administrator.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.example.administrator.entity.AppInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-04-03.
 * 获取手机内所有应用程序
 */
public class AppInfoParser {
    public static List<AppInfo> getAppInfos(Context context){
        //获得一个包管理器
        PackageManager pm = context.getPackageManager();
        List<AppInfo> appInfos = new ArrayList<AppInfo>();
        List<PackageInfo> packageInfos = pm.getInstalledPackages(0);
        for(PackageInfo packinfo:packageInfos){
            AppInfo appInfo = new AppInfo();
            String packageName = packinfo.packageName;
            appInfo.packageName = packageName;
            Drawable icon = packinfo.applicationInfo.loadIcon(pm);
            appInfo.icon = icon;
            String appName = packinfo.applicationInfo.loadLabel(pm).toString();
            appInfo.appName = appName;
            String apkPath = packinfo.applicationInfo.sourceDir;
            appInfo.apkpath = apkPath;
            File file = new File(apkPath);
            long appSize = file.length();//apk大小
            appInfo.appSize = appSize;
            int flags = packinfo.applicationInfo.flags;
            if((ApplicationInfo.FLAG_EXTERNAL_STORAGE & flags)!=0){
                //外部存储
                appInfo.isInRoom = false;
            }else {
                //手机存储
                appInfo.isInRoom = true;
            }
            if((ApplicationInfo.FLAG_SYSTEM &flags)!=0){
                //系统应用
                appInfo.isUserApp = false;
            }else {
                //用户应用
                appInfo.isUserApp = true;
            }
            appInfos.add(appInfo);
            appInfo = null;
        }
        return appInfos;
    }
}
