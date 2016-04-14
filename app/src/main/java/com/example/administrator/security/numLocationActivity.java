package com.example.administrator.security;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.dao.NumBelongDao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class numLocationActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText myEditText;
    private TextView myResult;
    private Button myButton;
    private String dbName = "address.db";
    private static String DB_PATH;
    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg){
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_num_location);
        initView();
        copyDB(dbName);
    }

    private void initView() {
        findViewById(R.id.rl_titlebar).setBackgroundColor(Color.parseColor("#FF4500"));
        ((TextView) findViewById(R.id.tv_title)).setText("号码归属地查询");
        ImageView myLeft = (ImageView) findViewById(R.id.imgv_leftbtn);
        myLeft.setOnClickListener(this);
        myLeft.setImageResource(R.drawable.back);
        myButton = (Button) findViewById(R.id.bt_numlocation);
        myButton.setOnClickListener(this);
        myEditText = (EditText) findViewById(R.id.et_numlocation);
        myResult = (TextView) findViewById(R.id.tv_numlocation_result);
        myEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //文本变化之后的操作
                String string = s.toString().toString().trim();
                if(string.length()==0){
                    myResult.setText("");
                }
            }
        });
    }

    private void copyDB(final String dbName) {
        /**
         * 复制assete下的db到手机
         */
        new Thread(){
            public void run(){
                try {
                    InputStream inputStream = getAssets().open(dbName);
                    DB_PATH = "/data/data/com.example.administrator.security/databases/";
                    OutputStream outputStream = new FileOutputStream(DB_PATH+dbName);
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = inputStream.read(buffer))>0){
                        outputStream.write(buffer,0,len);
                    }
                    Log.i("test", "拷贝结束");
                    outputStream.flush();
                    inputStream.close();
                    outputStream.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.bt_numlocation:
                //判断edittex中号码是否为空
                //判断数据库是否拷贝完成
                String phonenumber = myEditText.getText().toString().trim();
                if(!TextUtils.isEmpty(phonenumber)){
                    File file = new File(getFilesDir(),dbName);
                    if(!file.exists()||file.length()<=0){
                        //数据库没有复制到手机,复制数据库
                        copyDB(dbName);
                    }
                    //查询数据库
                    String location = NumBelongDao.getLocation(phonenumber);
                    myResult.setText("归属地:"+location);
                }else {
                    Toast.makeText(this,"请输入要查询的号码",Toast.LENGTH_SHORT);
                }
                break;
        }
    }
}
