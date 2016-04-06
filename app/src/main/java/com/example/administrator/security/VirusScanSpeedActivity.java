package com.example.administrator.security;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.dao.AntiVirusDao;
import com.example.administrator.utils.MD5Utils;
import com.example.administrator.adapter.ScanVirusAdapter;
import com.example.administrator.entity.ScanAppInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Date;

public class VirusScanSpeedActivity extends AppCompatActivity implements View.OnClickListener{
    protected static final int SCAN_BEGIN = 100;
    protected static final int SCANNING = 101;
    protected static final int SCAN_FINISH = 102;
    private int total;
    private int process;
    private TextView myProcess;
    private PackageManager pm;
    private boolean flag;
    private boolean isStop;
    private TextView myScanApp;
    private Button cancleBtn;
    private ImageView myScanningTcon;
    private RotateAnimation rani;
    private ListView myScanningListView;
    private ScanVirusAdapter adapter;
    private List<ScanAppInfo> myScanAppInfos = new ArrayList<ScanAppInfo>();
    private SharedPreferences mysp;
    private Handler myHandler = new Handler(){
        public void handleMessage(android.os.Message msg){
            switch (msg.what){
                case SCAN_BEGIN:
                    myScanApp.setText("初始化杀毒引擎中……");
                    break;
                case SCANNING:
                    ScanAppInfo info = (ScanAppInfo) msg.obj;
                    myScanApp.setText("正在扫描："+info.appName);
                    int speed = msg.arg1;
                    myProcess.setText((speed * 100 / total) + "%");
                    myScanAppInfos.add(info);
                    adapter.notifyDataSetChanged();
                    myScanningListView.setSelection(myScanAppInfos.size());
                    break;
                case SCAN_FINISH:
                    myScanApp.setText("扫描完成！");
                    myScanningTcon.clearAnimation();//停止旋转动画
                    cancleBtn.setBackgroundResource(R.drawable.scan_complete);
                    saveScanTime();
                    break;
            }
        }
        private void saveScanTime() {
            Editor editor = mysp.edit();
            SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd HH:mm:ss", Locale.getDefault());
            String currentTime =format.format(new Date());
            currentTime = "上次查杀:"+currentTime;
            editor.putString("lastVirusScan",currentTime);
            editor.commit();
        };
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_virus_scan_speed);
        pm = getPackageManager();
        mysp = getSharedPreferences("config", MODE_PRIVATE);
        initView();
        scanVirus();
    }

    private void initView() {
        findViewById(R.id.rl_titlebar).setBackgroundColor(Color.parseColor("#00FFFF"));
        ImageView myLeftView = (ImageView) findViewById(R.id.imgv_leftbtn);
        ((TextView)findViewById(R.id.tv_title)).setText("病毒查杀进度");
        myLeftView.setOnClickListener(this);
        myLeftView.setImageResource(R.drawable.back);
        myProcess = (TextView) findViewById(R.id.tv_scanprocess);
        myScanningListView = (ListView) findViewById(R.id.lv_scanapps);
        myScanApp = (TextView) findViewById(R.id.tv_scan_appname);
        cancleBtn = (Button) findViewById(R.id.bt_canclescan);
        cancleBtn.setOnClickListener(this);
        adapter = new ScanVirusAdapter(myScanAppInfos,this);
        myScanningListView.setAdapter(adapter);
        myScanningTcon = (ImageView) findViewById(R.id.iv_scanningicon);
        startAnim();
    }

    /**
     * 动画效果（旋转）
     */
    private void startAnim() {
        if(rani == null){
            rani = new RotateAnimation(0,360, Animation.RELATIVE_TO_PARENT,
                    0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        }
        rani.setRepeatCount(Animation.INFINITE);
        rani.setDuration(2000);
        myScanningTcon.startAnimation(rani);
    }

    /**
     * 扫描病毒
     */
    private void scanVirus() {
        flag = true;
        isStop = false;
        process = 0;
        myScanAppInfos.clear();
        new Thread(){
            public void run(){
                Message msg = Message.obtain();
                msg.what = SCAN_BEGIN;
                myHandler.sendMessage(msg);//sengEmptyMessage只能存放整形数据,sendMessage可以存放其他类型
                List<PackageInfo> installedPackage = pm.getInstalledPackages(0);
                total = installedPackage.size();
                for(PackageInfo info : installedPackage){
                    if(!flag){
                        isStop = true;
                        return;
                    }
                    String apkpath = info.applicationInfo.sourceDir;
                    String md5info = MD5Utils.getFileMD5(apkpath);
                    String result = AntiVirusDao.checkVirus(md5info);
                    msg = Message.obtain();
                    msg.what = SCANNING;
                    ScanAppInfo scanAppInfo = new ScanAppInfo();
                    if(result==null){
                        scanAppInfo.description = "您的手机安全";
                        scanAppInfo.isVirus = false;
                    }else {
                        scanAppInfo.description = result;
                        scanAppInfo.isVirus = true;
                    }
                    process++;
                    scanAppInfo.packageName = info.packageName;
                    scanAppInfo.appName = info.applicationInfo.loadLabel(pm).toString();
                    msg.obj = scanAppInfo;
                    msg.arg1 = process;
                    myHandler.sendMessage(msg);
                    try{
                        Thread.sleep(300);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
                msg = Message.obtain();
                msg.what = SCAN_FINISH;
                myHandler.sendMessage(msg);
            };
        }.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.bt_canclescan:
                if(process==total & process>0) {
                    //扫描已经完成
                    finish();
                }else if (process>0 & process<total & isStop==false){
                    //取消扫描
                    myScanningTcon.clearAnimation();//停止动画效果
                    flag = false;//取消扫描
                    cancleBtn.setBackgroundResource(R.drawable.restart_scan_btn);
                }else if(isStop){
                    //重新扫描
                    scanVirus();
                    cancleBtn.setBackgroundResource(R.drawable.cancle_scan_btn_selector);
            }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        flag = false;
        super.onDestroy();
    }
}
