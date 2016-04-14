package com.example.administrator.utils;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by Administrator on 2016-04-14.
 */
public class UIUtils {
    public static void showToast(final Activity context,final String msg){
        if("main".equals(Thread.currentThread().getName())){
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }else{
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}