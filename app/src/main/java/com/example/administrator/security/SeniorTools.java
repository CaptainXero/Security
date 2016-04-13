package com.example.administrator.security;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SeniorTools extends AppCompatActivity implements View.OnClickListener{
    private RelativeLayout rl_numLocation;
    private RelativeLayout rl_smsCopy;
    private RelativeLayout rl_smsBack;
    private RelativeLayout rl_appLock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_senior_tools);
        initView();
    }

    private void initView() {
        findViewById(R.id.rl_titlebar).setBackgroundColor(Color.parseColor("#EE2C2C"));
        ((TextView) findViewById(R.id.tv_title)).setText("高级工具");
        ImageView myLeftBTN = (ImageView) findViewById(R.id.imgv_leftbtn);
        myLeftBTN.setOnClickListener(this);
        rl_numLocation = (RelativeLayout) findViewById(R.id.rl_tools_numblongs);
        rl_numLocation.setOnClickListener(this);
        rl_smsCopy = (RelativeLayout) findViewById(R.id.rl_tools_copySMS);
        rl_smsCopy.setOnClickListener(this);
        rl_smsBack = (RelativeLayout) findViewById(R.id.rl_tools_backSMS);
        rl_smsBack.setOnClickListener(this);
        rl_appLock = (RelativeLayout) findViewById(R.id.rl_tools_appLock);
        rl_appLock.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.rl_tools_numblongs:
                //进入归属地查询界面
                mystartActivtiy(numLocationActivity.class);
                break;
            case R.id.rl_tools_appLock:
                //程序锁界面
                mystartActivtiy(appLockAvtivity.class);
                break;
            case R.id.rl_tools_copySMS:
                //短信备份界面
                mystartActivtiy(smsCopyActivity.class);
                break;
            case R.id.rl_tools_backSMS:
                //短信恢复界面
                mystartActivtiy(smsBackActivity.class);
                break;

        }
    }
    /**
     * 开启新的Activity不关闭自己
     */
    public void mystartActivtiy(Class<?> cls){
        Intent intent = new Intent(this,cls);
        startActivity(intent);
    }
}
