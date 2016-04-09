package com.example.administrator.security;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageStats;

import com.example.administrator.adapter.CacheCleanAdapter;
import com.example.administrator.entity.CacheInfo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class ClearCache extends AppCompatActivity implements View.OnClickListener{
    protected static final int SCANNING = 100;
    protected static final int FINISH = 101;
    private AnimationDrawable animation;
    /**建议清理*/
    private TextView myRecomandTV;
    /**可清理*/
    private TextView myCanCleanTV;
    private long cacheMemory;
    private List<CacheInfo> cacheInfos = new ArrayList<CacheInfo>();
    private List<CacheInfo> myCacheInfos = new ArrayList<CacheInfo>();
    private PackageManager pm;
    private CacheCleanAdapter adapter;
    private ListView myCacheLV;
    private Button myCancleBT;
    private Handler handler = new Handler(){
      public void handleMessage(Message msg){
          switch (msg.what){
              case SCANNING:
                  PackageInfo info = (PackageInfo) msg.obj;
                  myRecomandTV.setText("正在扫描:"+info.packageName);
//                  myRecomandTV.setText("Test");
                  myCanCleanTV.setText("已扫描缓存:"+ Formatter.formatFileSize(ClearCache.this,cacheMemory));
                  //在主线程添加变化后的集合
                  myCacheInfos.clear();
                  myCacheInfos.addAll(cacheInfos);
                  //ListView 更新
                  adapter.notifyDataSetChanged();
                  myCacheLV.setSelection(myCacheInfos.size());
                  break;
              case FINISH:
                  //扫描结束，停止动画
                  animation.stop();
                  if(cacheMemory>0){
                      myCancleBT.setEnabled(true);
                  }else {
                      myCancleBT.setEnabled(false);
                      Toast.makeText(ClearCache.this,"没有发现缓存",Toast.LENGTH_SHORT).show();
                  }
                  break;
          }
      };
    };
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_clear_cache);
        pm = getPackageManager();
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        findViewById(R.id.rl_titlebar).setBackgroundColor(Color.parseColor("#EE3B3B"));
        ImageView myLeftImg = (ImageView) findViewById(R.id.imgv_leftbtn);
        myLeftImg.setOnClickListener(this);
        myLeftImg.setImageResource(R.drawable.back);
        ((TextView) findViewById(R.id.tv_title)).setText("缓存扫描");
        myRecomandTV = (TextView) findViewById(R.id.tv_clearnow);
        myCanCleanTV = (TextView) findViewById(R.id.tv_clear_total);
        myCacheLV = (ListView) findViewById(R.id.lv_clear_scan);
        myCancleBT = (Button) findViewById(R.id.bt_clear_cancle);
        myCancleBT.setOnClickListener(this);
        animation = (AnimationDrawable) findViewById(R.id.iv_clearcache_broom).getBackground();
        animation.setOneShot(false);
        adapter = new CacheCleanAdapter(this,myCacheInfos);
        myCacheLV.setAdapter(adapter);
        fillData();
    }

    private void fillData() {
        thread = new Thread(){
            public void run(){
                //遍历手机所有应用
                cacheInfos.clear();
                List<PackageInfo> infos = pm.getInstalledPackages(0);
                for(PackageInfo info : infos){
                    getCacheSize(info);
                    try{
                       Thread.sleep(500);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    Message msg = Message.obtain();
                    msg.obj = info;//这里忘写了导致空指针异常
                    msg.what = SCANNING;
                    handler.sendMessage(msg);
                }
                Message msg = Message.obtain();
                msg.what = FINISH;
                handler.sendMessage(msg);
            };
        };
        thread.start();
    }

    private void getCacheSize(PackageInfo info) {
        try{
            Method method = PackageManager.class.getDeclaredMethod("getPackageSizeInfo", String.class,
                    IPackageStatsObserver.class);
            method.invoke(pm,info.packageName,new MyPackObserver(info));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        animation.stop();
        if(thread!=null){
            thread.interrupt();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.bt_clear_cancle:
                if(cacheMemory>0){
                    //跳转到清理缓存界面Activity
                    Intent intent = new Intent(this,CleanCacheActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
        }
    }
    private class MyPackObserver extends android.content.pm.IPackageStatsObserver.Stub{
        private PackageInfo info;
        public MyPackObserver(PackageInfo info) {
            this.info = info;
        }

        @Override
        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
            long cachesize = pStats.cacheSize;
            if(cachesize>0){
                CacheInfo cacheInfo = new CacheInfo();
                cacheInfo.cacheSize = cachesize;
                cacheInfo.packagename = info.packageName;
                cacheInfo.appName = info.applicationInfo.loadLabel(pm).toString();
                cacheInfo.appIcon = info.applicationInfo.loadIcon(pm);
                cacheInfos.add(cacheInfo);
                cacheMemory+=cachesize;
            }
        }
    }
}
