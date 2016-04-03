package com.example.administrator.security;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.dao.BlcakNumberDao;
import com.example.administrator.entity.BlackContactInfo;

public class AddBlackNumberActivity extends AppCompatActivity implements OnClickListener{
    private CheckBox mySmsCB;
    private CheckBox myTelCB;
    private EditText myNumET;
    private EditText mynameET;
    private BlcakNumberDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_black_number);
        dao = new BlcakNumberDao(this);
        intiView();
    }

    private void intiView() {
        findViewById(R.id.rl_titlebar).setBackgroundColor(Color.parseColor("#FF00FF"));
        ((TextView) findViewById(R.id.tv_title)).setText("添加黑名单");
        ImageView myLeftImgv= (ImageView) findViewById(R.id.imgv_leftbtn);
        myLeftImgv.setOnClickListener(this);
        myLeftImgv.setImageResource(R.drawable.back);
        mySmsCB = (CheckBox) findViewById(R.id.cb_blacknumber_sms);
        myTelCB = (CheckBox) findViewById(R.id.cb_blacknumber_tel);
        myNumET = (EditText) findViewById(R.id.et_blacknumber);
        mynameET = (EditText) findViewById(R.id.et_blackname);
        findViewById(R.id.bt_blacknumber).setOnClickListener(this);
        findViewById(R.id.bt_fromcontact).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.bt_blacknumber:
                String number = myNumET.getText().toString().trim();
                String name = mynameET.getText().toString().trim();
                if(TextUtils.isEmpty(number)||TextUtils.isEmpty(name)){
                    Toast.makeText(this,"电话和名称不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    BlackContactInfo info = new BlackContactInfo();
                    info.phoneName = name;
                    info.contactNumber = number;
                    if(mySmsCB.isChecked()&&myTelCB.isChecked()){
                        info.mode = 3;
                    }else if(mySmsCB.isChecked()&&!myTelCB.isChecked()){
                        //拦截短信
                        info.mode = 2;
                    }else if(myTelCB.isChecked()&&!mySmsCB.isChecked()){
                        //拦截电话
                        info.mode = 1;
                    }else {
                        Toast.makeText(this,"请选择拦截模式",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(!dao.IsNumberExist(info.contactNumber)){
                        dao.add(info);
                        Toast.makeText(this,"添加成功！",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(this,"该号码已经在黑名单中",Toast.LENGTH_SHORT).show();
                    }
//                    finish();
                }
                break;
            case R.id.bt_fromcontact:
                startActivityForResult(new Intent(this,ContactSelectActivity.class),0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            String phone = data.getStringExtra("phone" +
                    "");
            String name = data.getStringExtra("name");
            mynameET.setText(name);
            myNumET.setText(phone);
        }
    }
}
