package com.example.administrator.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.security.R;

import org.w3c.dom.Text;


/**
 * Created by Administrator on 2016-03-27.
 */
public class SetUpPasswordDialog extends Dialog implements android.view.View.OnClickListener{
    private TextView tv_title;
    public EditText et_firstPwd;
    public EditText et_secondPwd;
    private MyCallBack myCallBack;
    public SetUpPasswordDialog(Context context) {
        super(context, R.style.dialog_custom);
    }

    public void setCallBack(MyCallBack mycallBack) {
        this.myCallBack = mycallBack;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.setup_password_dialog);
        super.onCreate(savedInstanceState);
        initView();
    }
    private void initView(){
        tv_title = (TextView) findViewById(R.id.tv_setup_title);
        et_firstPwd = (EditText) findViewById(R.id.et_firstpsw);
        et_secondPwd = (EditText) findViewById(R.id.et_secondpsw);
        findViewById(R.id.bt_ok).setOnClickListener(this);
        findViewById(R.id.bt_cancle).setOnClickListener(this);
    }
    public void setTitle(String title){
        if(!TextUtils.isEmpty(title)){
            tv_title.setText(title);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_ok:
                myCallBack.ok();
                break;
            case R.id.bt_cancle:
                myCallBack.cancle();
                break;
        }
    }
    public interface MyCallBack{
        void ok();
        void cancle();
    }
}

