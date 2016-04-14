package com.example.administrator.security;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.action.MyCircleProgress;
import com.example.administrator.utils.SmsReducitionUtils;
import com.example.administrator.utils.UIUtils;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.ObjectStreamConstants;

public class smsBackActivity extends AppCompatActivity implements View.OnClickListener{
    private MyCircleProgress mProgressButton;
    private boolean flag = false;
    private SmsReducitionUtils smsReducitionUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sms_back);
        initView();
        smsReducitionUtils = new SmsReducitionUtils();
    }
    private void initView() {
        findViewById(R.id.rl_titlebar).setBackgroundColor(Color.parseColor("#EE0000"));
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        ((TextView) findViewById(R.id.tv_title)).setText("短信还原");
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);

        mProgressButton = (MyCircleProgress) findViewById(R.id.mcp_reducition);
        mProgressButton.setOnClickListener(this);
    }
    @Override
    protected void onDestroy() {
        flag = false;
        smsReducitionUtils.setFlag(flag);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.mcp_reducition:
                if(flag){
                    flag = false;
                    mProgressButton.setText("一键还原");
                }else{
                    flag = true;
                    mProgressButton.setText("取消还原");
                }
                smsReducitionUtils.setFlag(flag);
                new Thread(){

                    public void run() {
                        try {
                            smsReducitionUtils.reducitionSms(smsBackActivity.this, new SmsReducitionUtils.SmsReducitionCallBack() {

                                @Override
                                public void onSmsReducition(int process) {
                                    mProgressButton.setProcess(process);
                                }
                                @Override
                                public void beforeSmsReducition(int size) {
                                    mProgressButton.setMax(size);
                                }
                            });
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                            UIUtils.showToast(smsBackActivity.this, "文件格式错误");
                        } catch (IOException e) {
                            e.printStackTrace();
                            UIUtils.showToast(smsBackActivity.this, "读写错误");
                        }
                    }
                }.start();
                break;
        }
    }

}
