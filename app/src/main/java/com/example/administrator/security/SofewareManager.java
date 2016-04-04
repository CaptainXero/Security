package com.example.administrator.security;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Handler;
import android.text.format.Formatter;


import com.example.administrator.adapter.AppManagerAdapter;
import com.example.administrator.entity.AppInfo;
import com.example.administrator.utils.AppInfoParser;

import java.util.ArrayList;
import java.util.List;


public class SofewareManager extends AppCompatActivity implements OnClickListener{
    private TextView mPhoneMemoryTV;
    private TextView mSDMemoryTV;
    private TextView mAppNumTV;
    private ListView mListView;
    private List<AppInfo> appInfos;
    private List<AppInfo> userAppInfos = new ArrayList<AppInfo>();
    private List<AppInfo> systemAppInfos = new ArrayList<AppInfo>();
    private AppManagerAdapter adapter;
    /**接收应用程序卸载成功的广播*/
    private unInstallReceiver receiver;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg){
            switch (msg.what){
                case 10:
                    if(adapter==null){
                        adapter = new AppManagerAdapter
                                (userAppInfos,systemAppInfos,SofewareManager.this);
                    }
                    mListView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    break;
                case 15:
                    adapter.notifyDataSetChanged();
                    break;
            }
        };
    };
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.imgv_leftbtn:
                finish();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sofeware_manager);
        /**注册广播*/
        receiver = new unInstallReceiver();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("Package");
        registerReceiver(receiver, intentFilter);
        initView();
    }

    private void initView() {
        findViewById(R.id.rl_titlebar).setBackgroundColor(Color.parseColor("#FFD700"));
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        ((TextView) findViewById(R.id.tv_title)).setText("软件管家");
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);
        mPhoneMemoryTV = (TextView) findViewById(R.id.tv_phomeMemory_appManager);
        mSDMemoryTV = (TextView) findViewById(R.id.tv_sdmemory_appManager);
        mAppNumTV =  ( TextView) findViewById(R.id.tv_appManager);
        mListView = (ListView) findViewById(R.id.lv_appManager);
        /**获取手机剩余内存和SD卡内存*/
        getMemoryForPhone();
        initData();
        initListener();
    }

    private void initListener() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if(adapter!=null){
                    new Thread(){
                        public void run(){
                            AppInfo mappInfo = (AppInfo) adapter.getItem(position);
                            //记住当前条目状态
                            boolean flag = mappInfo.isSelected;
                            //将集合中所有条目的AppInfo设置为未选中状态
                            for(AppInfo appInfo:userAppInfos){
                                appInfo.isSelected = false;
                            }
                            for(AppInfo appInfo:systemAppInfos){
                                appInfo.isSelected = false;
                            }
                            if(mappInfo!=null){
                                //如果已经选中，则变为未选中
                                if(flag){
                                    mappInfo.isSelected = false;
                                }else {
                                    mappInfo.isSelected = true;
                                }
                                handler.sendEmptyMessage(15);
                            }
                        };
                    }.start();
                }
            }
        });
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem>=userAppInfos.size()+1){
                    mAppNumTV.setText("系统程序:"+systemAppInfos.size()+"个");
                }else {
                    mAppNumTV.setText("用户程序:"+userAppInfos.size()+"个");
                }
            }
        });
    }

    private void getMemoryForPhone() {
        long avail_sd = Environment.getExternalStorageDirectory().getFreeSpace();
        long avail_rom = Environment.getDataDirectory().getFreeSpace();
        //格式化内存
        String str_avail_sd = Formatter.formatFileSize(this,avail_sd);
        String str_avail_rom = Formatter.formatFileSize(this,avail_rom);
        mPhoneMemoryTV.setText("剩余手机内存:"+str_avail_rom);
        mSDMemoryTV.setText("剩余SD卡内存:"+str_avail_sd);
    }


    private void initData() {
        appInfos = new ArrayList<AppInfo>();
        new Thread() {
            public void run() {
                appInfos.clear();
                userAppInfos.clear();
                systemAppInfos.clear();
                appInfos.addAll(AppInfoParser.getAppInfos(SofewareManager.this));
                for (AppInfo appInfo : appInfos) {
                    if (appInfo.isUserApp) {
                        userAppInfos.add(appInfo);
                    } else {
                        systemAppInfos.add(appInfo);
                    }
                }
                handler.sendEmptyMessage(10);
            }
        }.start();
    }

    class unInstallReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            //收到广播
            initData();
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        receiver = null;
        super.onDestroy();
    }
}
