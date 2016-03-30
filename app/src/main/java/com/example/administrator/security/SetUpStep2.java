package com.example.administrator.security;

import android.content.SharedPreferences.Editor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.administrator.action.BaseSetUpAvtivity;
import com.lidroid.xutils.util.core.LruDiskCache;

public class SetUpStep2 extends BaseSetUpAvtivity implements View.OnClickListener {
    private TelephonyManager telephonyManager;
    private Button bindSimBtn;
    private RadioButton radioButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step2);
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        radioButton = (RadioButton) findViewById(R.id.rb_second);
        initView();
    }

    private void initView() {
        radioButton.setChecked(true);
        bindSimBtn = (Button) findViewById(R.id.bt_bind_sim);
        bindSimBtn.setOnClickListener(this);
        if(isBind()){
            bindSimBtn.setEnabled(false);
        }else {
            bindSimBtn.setEnabled(true);
        }
    }
    private boolean isBind() {
        String simString = sp.getString("sim", null);
        if (TextUtils.isEmpty(simString)) {
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bt_bind_sim:
                //绑定SIM卡
                bindSIM();
                break;
        }
    }

    private void bindSIM() {
        if(!isBind()){
            String simSerialNumber = telephonyManager.getSimSerialNumber();
            Editor edit = sp.edit();
            edit.putString("sim",simSerialNumber);
            edit.commit();
            Toast.makeText(this,"SIM卡绑定成功",Toast.LENGTH_SHORT).show();
            bindSimBtn.setEnabled(false);
        }else {
            Toast.makeText(this,"SIM卡已经绑定",Toast.LENGTH_SHORT).show();
            bindSimBtn.setEnabled(false);
        }
    }

    @Override
    public void showPre() {
        startActivityAndFinishSelf(SetUpStep2.class);
    }

    @Override
    public void showNext() {
        if(!isBind()){
            Toast.makeText(this,"您还没有绑定SIM卡",Toast.LENGTH_SHORT).show();
            return;
        }
        startActivityAndFinishSelf(SetUpStep3.class);
    }
}
