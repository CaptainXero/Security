package com.example.administrator.security;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.administrator.action.BaseSetUpAvtivity;

public class SetUpStep1 extends BaseSetUpAvtivity{
    private RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step1);
        initView();
    }

    private void initView() {
        radioButton = (RadioButton) findViewById(R.id.rb_first);
        radioButton.setChecked(true);
    }

    @Override
    public void showPre() {
        Toast.makeText(this,"当前页面已经是第一页",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNext() {
        startActivityAndFinishSelf(SetUpStep2.class);
    }
}
