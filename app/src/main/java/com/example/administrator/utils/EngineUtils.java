package com.example.administrator.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import com.example.administrator.entity.AppInfo;
import com.stericson.RootTools.RootTools;

/**
 * Created by Administrator on 2016-04-04.
 */
public class EngineUtils {
    /**
     * 分享应用
     */
    public static void shareApplcation(Context context,AppInfo appInfo){
        Intent intent = new Intent("android.intent.action.SEND");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"推荐您使"+appInfo.appName);
        context.startActivity(intent);
    }
    /**
     * 开启应用
     */
    public static void startApp(Context context,AppInfo appInfo){
        //打开App入口Activity
        PackageManager pm = context.getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(appInfo.packageName);
        if(intent!=null){
            context.startActivity(intent);
        }else {
            Toast.makeText(context,"该App无法启动",Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 开启应用设置界面
     */
    public static void settingAppDetail(Context context,AppInfo appInfo){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.ALL_APPS");
        intent.addCategory("Intent.CATEGORY_DEFAULT");
        intent.setData(Uri.parse("package:"+appInfo.packageName));
        context.startActivity(intent);
    }
    /**
     * 卸载应用
     */
    public static void deleteApp(Context context,AppInfo appInfo){
        if(appInfo.isUserApp){
            Intent intent = new Intent();
            intent.setAction("android.intent.action.DELETE");
            intent.setData(Uri.parse("package:" + appInfo.packageName));
            context.startActivity(intent);
        }else {
            //删除系统应用需要root权限,此处导入RootTools.jar
            if(!RootTools.isRootAvailable()){
                Toast.makeText(context,"卸载系统应用需要root权限",Toast.LENGTH_SHORT).show();
                return;
            }
            try{
                if(!RootTools.isAccessGiven()){
                    Toast.makeText(context,"授权以删除App",Toast.LENGTH_SHORT).show();
                    return;
                }
                RootTools.sendShell("mount -o remount ,rw /system", 3000);
                RootTools.sendShell("rm -r "+appInfo.apkpath, 30000);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
