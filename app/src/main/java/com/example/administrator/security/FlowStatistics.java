package com.example.administrator.security;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class FlowStatistics extends AppCompatActivity implements View.OnClickListener{
    private Spinner mySelectSP;
    private String[] operater = {"中国移动","中国联通","中国电信"};
    private ArrayAdapter mySelectAdapter;
    private SharedPreferences mysp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_flow_statistics);
        mysp = getSharedPreferences("config",MODE_PRIVATE);
        initView();
    }

    private void initView() {
        findViewById(R.id.rl_titlebar).setBackgroundColor(Color.parseColor("#7CFC00"));
        ImageView myLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        myLeftImgv.setImageResource(R.drawable.back);
        myLeftImgv.setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_title)).setText("运营商设置");
        mySelectSP = (Spinner) findViewById(R.id.sp_operator_select);
        mySelectAdapter = new ArrayAdapter(this,R.layout.item_spinner_operatorset,R.id.tv_provice,operater);
        mySelectSP.setAdapter(mySelectAdapter);
        findViewById(R.id.bt_operator_finish).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Editor editor = mysp.edit();
        switch (v.getId()){
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.bt_operator_finish:
                editor.putInt("operator",mySelectSP.getSelectedItemPosition()+1);
                editor.putBoolean("isset_operator", true);
                editor.commit();
                startActivity(new Intent(this,TrafficMonitoringActivity.class));
                finish();
                break;
        }
    }
}
