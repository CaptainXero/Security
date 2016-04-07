package com.example.administrator.security;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AntiVirus extends AppCompatActivity implements View.OnClickListener{
    private TextView myLastTime;
    private SharedPreferences sp;
    private static String DB_PATH ;
    private static String DB_NAME = "antivirus.db";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_anti_virus);
        sp = getSharedPreferences("config",MODE_PRIVATE);
        copyDB("antivirus.db");
        intiView();
    }

    @Override
    protected void onResume() {
        String string = sp.getString("lastVirusScan","您还没有查杀过病毒！");
        myLastTime.setText(string);
        super.onResume();
    }

    private void copyDB(final String dbname) {
        new Thread(){
            public void run(){
                try{
//                    File file = new File(getFilesDir(),dbname);
//                    if(file.exists() && file.length()>0){
//                        Log.i("VirusScanActivity", "数据库已存在");
//                        return;
//                    }
                    InputStream inputStream = getAssets().open(dbname);
                    DB_PATH = "/data/data/com.example.administrator.security/databases/";
                    String outFileName = DB_PATH + DB_NAME;
                    OutputStream outputStream = new FileOutputStream(DB_PATH+DB_NAME);
//                    FileOutputStream outputStream = openFileOutput(dbname, MODE_PRIVATE);
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = inputStream.read(buffer))>0){
                        outputStream.write(buffer,0,len);
                    }
                    Log.i("test", "拷贝结束");
                    outputStream.flush();
                    inputStream.close();
                    outputStream.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            };
        }.start();
    }

    private void intiView() {
        findViewById(R.id.rl_titlebar).setBackgroundColor(Color.parseColor("#0000CD"));
        ImageView iv_leftimage = (ImageView) findViewById(R.id.imgv_leftbtn);
        ((TextView) findViewById(R.id.tv_title)).setText("病毒查杀");
        iv_leftimage.setOnClickListener(this);
        iv_leftimage.setImageResource(R.drawable.back);
        myLastTime = (TextView) findViewById(R.id.tv_lastscantime);
        findViewById(R.id.rl_allscanvirus).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.rl_allscanvirus:
                startActivity(new Intent(this, VirusScanSpeedActivity.class));
                break;
        }
    }
}
