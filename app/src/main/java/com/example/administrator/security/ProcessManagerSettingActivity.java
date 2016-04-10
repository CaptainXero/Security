package com.example.administrator.security;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.administrator.reciever.AutoKillProcessService;
import com.example.administrator.utils.SystemInfoUtils;

public class ProcessManagerSettingActivity extends AppCompatActivity implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener{
    private ToggleButton mshowSysAppTgb;
    private ToggleButton mKillProcessTgb;
    private SharedPreferences msp;
    private boolean running;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_process_manager_setting);
        msp = getSharedPreferences("config",MODE_PRIVATE);
        initView();
    }

    private void initView() {
        findViewById(R.id.rl_titlebar).setBackgroundColor(Color.parseColor("#C0FF3E"));
        ImageView mLeftBtn = (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftBtn.setImageResource(R.drawable.back);
        mLeftBtn.setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_title)).setText("进程管理设置");
        mshowSysAppTgb = (ToggleButton) findViewById(R.id.tgb_showsys_process);
        mKillProcessTgb = (ToggleButton) findViewById(R.id.tgb_killprocess_lockscreen);
        mshowSysAppTgb.setChecked(msp.getBoolean("showSystemProcess", true));
        running = SystemInfoUtils.isServiceRunning(this,"com.example.administrator.reciever.AutoKillProcessService");
        mKillProcessTgb.setChecked(running);
        initListener();
    }

    private void initListener() {
        mKillProcessTgb.setOnCheckedChangeListener(this);
        mshowSysAppTgb.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.tgb_showsys_process:
                saveStatus("showSystemProcess",isChecked);
                break;
            case R.id.tgb_killprocess_lockscreen:
                Intent service = new Intent(this, AutoKillProcessService.class);
                if(isChecked){
                    //开启服务
                    startService(service);
                }else {
                    //关闭服务
                    stopService(service);
                }
                break;
        }
    }

    private void saveStatus(String showSystemProcess, boolean isChecked) {
        Editor editor = msp.edit();
        editor.putBoolean(showSystemProcess,isChecked);
        editor.commit();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgv_leftbtn:
                    finish();
                    break;
        }
    }
}
