package com.example.administrator.security;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.content.SharedPreferences.Editor;

import com.example.administrator.action.BaseSetUpAvtivity;

import org.w3c.dom.Text;

public class SetUpStep4 extends BaseSetUpAvtivity{
    private TextView tv_status;
    private ToggleButton toggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step4);
        initView();
    }

    private void initView() {
        ((RadioButton) findViewById(R.id.rb_forth)).setChecked(true);
        tv_status = (TextView) findViewById(R.id.tv_step4_mid);
        toggleButton = (ToggleButton) findViewById(R.id.tb_securityfunction);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    tv_status.setText("防盗保护已经开启");
                }else {
                    tv_status.setText("防盗保护没有开启");
                }
                Editor editor = sp.edit();
                editor.putBoolean("protecting",isChecked);
                editor.commit();
            }
        });
        boolean protecting = sp.getBoolean("protecting",true);
        if(protecting){
            tv_status.setText("防盗保护已经开启");
            toggleButton.setChecked(true);
        }else {
            tv_status.setText("防盗保护没有开启");
            toggleButton.setChecked(false);
        }
    }

    @Override
    public void showPre() {
        startActivityAndFinishSelf(SetUpStep3.class);
    }

    @Override
    public void showNext() {
        Editor editor = sp.edit();
        editor.putBoolean("isSetup", true);
        editor.commit();
        startActivityAndFinishSelf(LostFindActivity.class);

    }
}
