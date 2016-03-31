package com.example.administrator.security;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.administrator.action.BaseSetUpAvtivity;

public class SetUpStep3 extends BaseSetUpAvtivity implements View.OnClickListener{
    private EditText et_inputNumber;
    private Button bt_addContact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step3);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        ((RadioButton)findViewById(R.id.rb_third)).setChecked(true);
        et_inputNumber = (EditText) findViewById(R.id.et_inputPhoneNO);
        bt_addContact = (Button) findViewById(R.id.bt_step3_addcontace);
        String safePhone = sp.getString("safephone",null);
        if(!TextUtils.isEmpty(safePhone)){
            et_inputNumber.setText(safePhone);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_step3_addcontace:
                startActivityForResult(new Intent(this,ContactSelectActivity.class),0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            String phone = data.getStringExtra("phone");
            et_inputNumber.setText(phone);
        }
    }

    @Override
    public void showNext() {
        String safePhone = et_inputNumber.getText().toString().trim();
        if(TextUtils.isEmpty(safePhone)){
            Toast.makeText(this,"请输入安全电话号码",Toast.LENGTH_SHORT);
            return;
        }
        Editor editor = sp.edit();
        editor.putString("safephone",safePhone);
        editor.commit();
        startActivityAndFinishSelf(SetUpStep4.class);
    }

    @Override
    public void showPre() {
        startActivityAndFinishSelf(SetUpStep2.class);
    }
}
