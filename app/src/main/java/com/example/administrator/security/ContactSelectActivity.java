package com.example.administrator.security;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;



import com.example.administrator.action.ContactInfoParser;
import com.example.administrator.adapter.ContactAdapter;
import com.example.administrator.entity.ContactInfo;

import java.util.List;
import android.os.Handler;
import android.widget.TextView;

import java.util.logging.LogRecord;

public class ContactSelectActivity extends AppCompatActivity implements OnClickListener{
    private ListView mylistView;
    private ContactAdapter adapter;
    private List<ContactInfo> systemContacts;
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg){
            switch (msg.what) {
                case 10:
                    if (systemContacts != null) {
                        adapter = new ContactAdapter(systemContacts, ContactSelectActivity.this);
                        mylistView.setAdapter(adapter);
                    }
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_contact_select);
        initView();
    }

    private void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText("选择联系人");
        ImageView myLeftImg = (ImageView) findViewById(R.id.imgv_leftbtn);
        myLeftImg.setOnClickListener(this);
        myLeftImg.setImageResource(R.drawable.back);
        //导航栏颜色
        findViewById(R.id.rl_titlebar).setBackgroundColor(Color.parseColor("#BF3EFF") );
        mylistView = (ListView) findViewById(R.id.lv_contact);
        new Thread(){
          public void run(){
              systemContacts = ContactInfoParser.getSystemContact(ContactSelectActivity.this);
              systemContacts.addAll(ContactInfoParser.getSimContacts(ContactSelectActivity.this));
              handler.sendEmptyMessage(10);
          };
        }.start();
        mylistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ContactInfo item = (ContactInfo) adapter.getItem(position);
                Intent intent = new Intent();
                intent.putExtra("phoneNumber",item.phoneNumber);
                setResult(0,intent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgv_leftbtn:
                finish();
                break;
        }
    }
}
