package com.example.administrator.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.security.R;

import org.w3c.dom.Text;


/**
 * Created by Administrator on 2016-03-27.
 */
public class EnterPasswordDialog extends Dialog implements android.view.View.OnClickListener {
    private TextView tv_enter_title;
    private EditText et_password;
    private MyCallBack myCallBack;
    private Context context;

    public void initView(){
        tv_enter_title = (TextView) findViewById(R.id.tv_enterpwd_title);
        et_password = (EditText) findViewById(R.id.et_enterPwd);
        findViewById(R.id.bt_enter_ok).setOnClickListener(this);
        findViewById(R.id.bt_enter_cancle).setOnClickListener(this);
    }
    public void setCallBack(MyCallBack callBack){
        this.myCallBack = callBack;
    }
    public void setTitle(String title){
        if(!TextUtils.isEmpty(title)){
            tv_enter_title.setText(title);
        }
    }

    public EnterPasswordDialog(Context context){
        super(context, R.style.dialog_custom);
        this.context = context;
    }
    public String getPassword(){
        return et_password.getText().toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.enter_password_dialog);
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_enter_ok:
                myCallBack.confirm();
                break;
            case R.id.bt_enter_cancle:
                myCallBack.cancle();
                break;
        }
    }
    public interface MyCallBack{
        void confirm();
        void cancle();
    }
}
