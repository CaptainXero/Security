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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

public class LostFindActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView tv_statue;
    private ToggleButton toggleButton;
    private RelativeLayout relativeLayout;
    private SharedPreferences mySharedPreference;
    private TextView myProtectStatusTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_lostfind);
        mySharedPreference = getSharedPreferences("config",MODE_PRIVATE);
        if(!isSetUp()){
            startSetUp1Activity();
        }
        initView();
    }

    private void startSetUp1Activity() {
        Intent intent = new Intent(LostFindActivity.this,SetUpStep1.class);
        startActivity(intent);
        finish();
    }

    private boolean isSetUp(){
        return mySharedPreference.getBoolean("isSetUp",false);
    }

    private void initView() {
        TextView titleTV = (TextView) findViewById(R.id.tv_title);
        titleTV.setText("手机防盗");
        ImageView imageView = (ImageView) findViewById(R.id.imgv_leftbtn);
        imageView.setOnClickListener(this);
        imageView.setImageResource(R.drawable.back);
        findViewById(R.id.rl_titlebar).setBackgroundColor(Color.parseColor("#BF3EFF"));
        tv_statue = (TextView) findViewById(R.id.tv_lostfind_safephone);
        tv_statue.setText(mySharedPreference.getString("safephone",""));
        toggleButton = (ToggleButton) findViewById(R.id.tb_lostfind);
        relativeLayout = (RelativeLayout) findViewById(R.id.rl_lostfind_tosetup);
        relativeLayout.setOnClickListener(this);
        myProtectStatusTV = (TextView) findViewById(R.id.tv_lostfind_protectstauts);
        /**
         * 查询手机防盗是否开启，默认开启
         */
        boolean protecting = mySharedPreference.getBoolean("protecting",true);
        if(protecting){
            myProtectStatusTV.setText("防盗保护已经开启");
            toggleButton.setChecked(true);
        }else {
            myProtectStatusTV.setText("防盗保护没有开启");
            toggleButton.setChecked(true);
        }
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    myProtectStatusTV.setText("防盗保护已经开启");
                }else {
                    myProtectStatusTV.setText("防盗保护没有开启");
                }
             Editor editor = mySharedPreference.edit();
                editor.putBoolean("protecting",isChecked);
                editor.commit();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_lostfind_tosetup:
                //重新进入向导
                startSetUp1Activity();
                break;
            case R.id.imgv_leftbtn:
                finish();
                break;
        }
    }
}
