package com.example.administrator.utils;

import android.content.Context;

/**
 * Created by Administrator on 2016-04-04.
 * dip转px
 */
public class DensityUtil {
    public static int dip2px(Context context,float dpValue){
        try {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue*scale+0.5f);
        }catch (Exception e){
            e.printStackTrace();
        }
        return (int) dpValue;
    }
    /**
     * px转dip
     */
    public  static int pxx2dip(Context context,float pxValue){
        try{
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (pxValue/scale+0.5f);
        }catch (Exception e){
            e.printStackTrace();
        }
        return (int) pxValue;
    }
}
