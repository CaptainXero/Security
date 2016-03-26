package com.example.administrator.security;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.example.administrator.utils.MyUtils;
import com.example.administrator.utils.VersionUpdateUtils;

public class MainActivity extends AppCompatActivity {
    private TextView versionTextView;
    private String localVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        localVersion = MyUtils.getVersion(getApplicationContext());
        initView();
        final VersionUpdateUtils updateUtils = new VersionUpdateUtils
                (localVersion,MainActivity.this);
        new Thread(){
            public void run(){
                //get Service Version
                updateUtils.getCloudVersion();
            };
        }.start();

    }

    private void initView() {
        versionTextView = (TextView)findViewById(R.id.tv_splash_version);
        versionTextView.setText("版本号 "+localVersion);
    }
}
