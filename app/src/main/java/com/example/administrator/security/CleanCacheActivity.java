package com.example.administrator.security;

import android.content.Intent;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.Random;


public class CleanCacheActivity extends AppCompatActivity implements View.OnClickListener{
    protected static final int CLEANNING = 100;
    private AnimationDrawable animation;
    private long cacheMemory;
    private TextView myMemoryTV;
    private TextView myMemoryUnitTV;
    private PackageManager pm;
    private FrameLayout myCleanCacheFL;
    private FrameLayout myFinishCleanFL;
    private TextView mySezeTV;
    private Handler myHandler = new Handler(){
        public void handleMessage(android.os.Message msg){
            switch (msg.what){
                case CLEANNING:
                    long memory = (Long) msg.obj;
                    formatMemory(memory);
                    if(memory==cacheMemory){
                        animation.stop();
                        myCleanCacheFL.setVisibility(View.GONE);
                        myFinishCleanFL.setVisibility(View.VISIBLE);
                        mySezeTV.setText("成功清理:"+ Formatter.formatFileSize(CleanCacheActivity.this,cacheMemory));
                    }
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_clean_cache);
        initView();
        pm = getPackageManager();
        Intent intent = getIntent();
        cacheMemory = intent.getLongExtra("cacheMemory",0);
        initData();
    }

    private void initView() {
        findViewById(R.id.rl_titlebar).setBackgroundColor(Color.parseColor("#EE3B3B"));
        ((TextView) findViewById(R.id.tv_title)).setText("缓存清理");
        ImageView myLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        myLeftImgv.setOnClickListener(this);
        myLeftImgv.setImageResource(R.drawable.back);
        animation = (AnimationDrawable) findViewById(R.id.iv_trashbin_cacheclean).getBackground();
        animation.setOneShot(false);
        animation.start();
        myMemoryTV = (TextView) findViewById(R.id.tv_cleancache_memory);
        myMemoryUnitTV = (TextView) findViewById(R.id.tv_cleancache_memoryunit);
        myCleanCacheFL = (FrameLayout) findViewById(R.id.fl_cleancache);
        myFinishCleanFL = (FrameLayout) findViewById(R.id.fl_finishclean);
        mySezeTV = (TextView) findViewById(R.id.tv_cleanmemorysize);
        findViewById(R.id.bt_finish_celancache).setOnClickListener(this);
    }

    private void initData() {
        cleanAll();
        new Thread(){
            public void run(){
                long memory = 0;
                while (memory<cacheMemory){
                    try{
                        Thread.sleep(300);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    Random rand = new Random();
                    int i = rand.nextInt();
                    i = rand.nextInt(1024);
                    memory+=1024*1;
                    if(memory>cacheMemory){
                        memory = cacheMemory;
                    }
                    Message message = Message.obtain();
                    message.what = CLEANNING;
                    message.obj = memory;
                    myHandler.sendMessageDelayed(message,200);
                }
            };
        }.start();
    }
class ClearCacheObserver extends android.content.pm.IPackageDataObserver.Stub{
    @Override
    public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {

    }
}
    private void cleanAll() {
        Method[] methods = PackageManager.class.getMethods();
        for(Method method : methods){
            if("freeStorageAndNotify".equals(method.getName())){
                try{
                    method.invoke(pm,Long.MAX_VALUE,new ClearCacheObserver());
                }catch (Exception e){
                    e.printStackTrace();
                }
                return;
            }
        }
        Toast.makeText(this,"清理完成",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.bt_finish_celancache:
                finish();
                break;
        }

    }
    private void formatMemory(long memory) {
        String cacheMemoryStr = Formatter.formatFileSize(this,memory);
        String memoryStr;
        String memoryUnit;
        //根据大小判定单位
        if(memory>900){
            //两位
            memoryStr = cacheMemoryStr.substring(0,cacheMemoryStr.length()-2);
            memoryUnit = cacheMemoryStr.substring(cacheMemoryStr.length()-2,cacheMemoryStr.length());
        }else {
            //一位
            memoryStr = cacheMemoryStr.substring(0,cacheMemoryStr.length()-1);
            memoryUnit = cacheMemoryStr.substring(cacheMemoryStr.length()-1,cacheMemoryStr.length());
        }
        myMemoryTV.setText(memoryStr);
        myMemoryUnitTV.setText(memoryUnit);
    }
}
