package com.example.administrator.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Entity;
import android.content.Intent;
import android.widget.Toast;
import android.os.Handler;
import android.app.AlertDialog;

import com.example.administrator.security.HomeActivity;
import com.example.administrator.entity.VersionEntity;
import com.example.administrator.security.R;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.client.HttpRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

/**
 * Update APK.
 */
public class VersionUpdateUtils {
    private static final int MESSAGE_NET_ERROR=101;
    private static final int MESSAGE_IO_ERROR=102;
    private static final int MESSAGE_JSON_ERROR=103;
    private static final int MESSAGE_SHOW_DIALOG=104;
    private static final int MESSGE_ENTERHOME=105;
    /***
     * Update UI
     */
    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message message){
            switch (message.what){
                case MESSAGE_IO_ERROR:
                    Toast.makeText(context,"IO ERROR",Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MESSAGE_JSON_ERROR:
                    Toast.makeText(context,"JSON ERROR",Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MESSAGE_NET_ERROR:
                    Toast.makeText(context,"NET ERROR",Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MESSAGE_SHOW_DIALOG:
                    showUpdateDialog(versionEntity);
                    break;
                case MESSGE_ENTERHOME:
                    Intent intent = new Intent(context, HomeActivity.class);
                    context.startActivity(intent);
                    context.finish();
                    break;
            }
        };
    };

    /**
     * Loacl Version
     */
    private String mVersion;
    private Activity context;
    private ProgressDialog mProgressDialog;
    private VersionEntity versionEntity;
    public VersionUpdateUtils(String Version,Activity activity){
        mVersion = Version;
        context = activity;
    }

    /**
     * get service's Version
     */
    public void getCloudVersion(){
        try {
            HttpClient client = new DefaultHttpClient();
            /*request time out*/
            HttpConnectionParams.setSoTimeout(client.getParams(), 5000);
            /*connection time out*/
            HttpConnectionParams.setConnectionTimeout(client.getParams(),5000);
            HttpGet httpGet = new HttpGet("http://10.10.10.28:8080/test/demo.html");
            HttpResponse execute = client.execute(httpGet);
            if(execute.getStatusLine().getStatusCode()==200){
                //request&response is successful
                HttpEntity entity = execute.getEntity();
                String result = EntityUtils.toString(entity,"gbk");//IO ERROR
                //Create JSON Object
                JSONObject jsonObject = new JSONObject(result);
                versionEntity = new VersionEntity();
                String code = jsonObject.getString("code");
                versionEntity.versioncode = code;
                String des = jsonObject.getString("des");
                versionEntity.description = des;
                String apkurl = jsonObject.getString("apkurl");
                versionEntity.apkurl = apkurl;
                if(!mVersion.equals(versionEntity.versioncode)){
                    //Version not euqal
                    handler.sendEmptyMessage(MESSAGE_SHOW_DIALOG);
                }

            }
        }catch (ClientProtocolException e){
            handler.sendEmptyMessage(MESSAGE_NET_ERROR);
            e.printStackTrace();
        }catch (IOException e){
            handler.sendEmptyMessage(MESSAGE_IO_ERROR);
            e.printStackTrace();
        }catch (JSONException e){
            handler.sendEmptyMessage(MESSAGE_JSON_ERROR);
            e.printStackTrace();
        }
    }

    /**
     * Show Dialog
     */
    private void showUpdateDialog(final VersionEntity versionEntity){
        //creat Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("有新版本:"+versionEntity.versioncode);
        builder.setMessage(versionEntity.description);
        builder.setCancelable(false);
        builder.setIcon(R.drawable.ic_launcher);
        //Updata click Linister
        builder.setPositiveButton("立即升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                initProgressDialog();
                downloadNewApk(versionEntity.apkurl);
            }
        });
        //set pass optation
        builder.setNegativeButton("暂不升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                enterHome();
            }
        });
        builder.show();
    }
//down New Version
    protected void downloadNewApk(String apkurl) {
        DownLoadUtils downLoadUtils = new DownLoadUtils();
        downLoadUtils.downapk(apkurl, "/mnt/sdcard/mobilesafe2.0.apk", new DownLoadUtils.MycallBack() {
            @Override
            public void onSuccess(ResponseInfo<File> arg0) {
                mProgressDialog.dismiss();
                MyUtils.installApk(context);
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                mProgressDialog.setMessage("下载失败");
                mProgressDialog.dismiss();
                enterHome();
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                mProgressDialog.setMax((int)total);
                mProgressDialog.setMessage("正在下载ing");
                mProgressDialog.setProgress((int)current);
            }
        });
    }
//init ProgressDialog
    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("准备下载ing");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.show();
    }


    private void enterHome(){
        handler.sendEmptyMessageDelayed(MESSGE_ENTERHOME, 2000);
    }

}
