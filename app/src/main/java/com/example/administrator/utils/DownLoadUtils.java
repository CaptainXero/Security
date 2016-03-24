package com.example.administrator.utils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import java.io.File;

/**
 * Download APK.
 */
public class DownLoadUtils {
    public void downapk(String url,String targerFile,final MycallBack mycallBack){
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.download(url, targerFile, new RequestCallBack<File>() {
            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                mycallBack.onSuccess(responseInfo);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                mycallBack.onFailure(e,s);

            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
                mycallBack.onLoading(total,current,isUploading);
            }
        });
    }
    interface MycallBack{
        void onSuccess(ResponseInfo<File> arg0);
        void onFailure(HttpException arg0,String arg1);
        void onLoading(long total,long current,boolean isUploading);
    }
}
