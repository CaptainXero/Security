package com.example.administrator.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.io.File;

/**
 * get APK Version.
 * install APK.
 */
public class MyUtils {
    public static String getVersion(Context context){
        //PackageManager: get all INFO from JSON
        PackageManager packageManager = context.getPackageManager();
        try {
            //get PackageName
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(),0);
            return packageInfo.versionName;
        }catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }
    public static void installApk(Activity activity){
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(Uri.fromFile(new File("/mnt/sdcard/mobilesafe2.0.apk")),
                "application/vnd.android.package-archive");
        activity.startActivityForResult(intent,0);
    }
}
