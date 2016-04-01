package com.example.administrator.security;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.adapter.BlackContactAdapter;
import com.example.administrator.dao.BlcakNumberDao;
import com.example.administrator.entity.BlackContactInfo;
import com.example.administrator.adapter.BlackContactAdapter.BlackConactCallBack;

import java.util.ArrayList;
import java.util.List;

public class SecurityPhone extends AppCompatActivity implements OnClickListener{
    private FrameLayout fl_haveBlackNumber;
    private FrameLayout fl_noBlackNumber;
    private BlcakNumberDao dao;
    private ListView myListView;
    private int pageNumber = 0;
    private int pageSize = 15;
    private int totalNumber;
    private List<BlackContactInfo> pageBlackNumber = new ArrayList<BlackContactInfo>();
    private BlackContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_security_phone);
        initView();
        fillData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(totalNumber!=dao.getTotalNumber()){
            if(dao.getTotalNumber()>0){
                fl_haveBlackNumber.setVisibility(View.VISIBLE);
                fl_noBlackNumber.setVisibility(View.GONE);
            }else {
                fl_haveBlackNumber.setVisibility(View.GONE);
                fl_noBlackNumber.setVisibility(View.VISIBLE);
            }
            pageNumber = 0;
            pageBlackNumber.clear();
            pageBlackNumber.addAll(dao.getPageBlackNumber(pageNumber,pageSize));
            if(adapter!=null){
                adapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 填充数据，刷进界面
     */
    private void fillData(){
        dao = new BlcakNumberDao(SecurityPhone.this);
        totalNumber = dao.getTotalNumber();
        if(totalNumber==0){
            fl_haveBlackNumber.setVisibility(View.GONE);
            fl_noBlackNumber.setVisibility(View.VISIBLE);
        }else if(totalNumber>0){
            fl_haveBlackNumber.setVisibility(View.VISIBLE);
            fl_noBlackNumber.setVisibility(View.GONE);
            pageNumber = 0;
            if(pageBlackNumber.size()>0){
                pageBlackNumber.clear();
            }
            pageBlackNumber.addAll(dao.getPageBlackNumber(pageNumber,pageSize));
            if(adapter==null){
                adapter = new BlackContactAdapter(pageBlackNumber,SecurityPhone.this);
                adapter.setCallBack(new BlackConactCallBack() {
                    @Override
                    public void DataSizeChanged() {
                        fillData();
                    }
                });
                myListView.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        }
    }
    private void initView() {
        findViewById(R.id.rl_titlebar).setBackgroundColor(Color.parseColor("#CD00CD"));
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        ((TextView) findViewById(R.id.tv_title)).setText("通讯卫士");
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);
        fl_haveBlackNumber = (FrameLayout) findViewById(R.id.fl_haveblacknumber);
        fl_noBlackNumber = (FrameLayout) findViewById(R.id.fl_noblacknumber);
        findViewById(R.id.bt_addblacknumber).setOnClickListener(this);
        myListView = (ListView) findViewById(R.id.lv_blacknumbers);
        myListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE: // 没有滑动的状态
                        // 获取最后一个可见条目
                        int lastVisiblePosition = myListView
                                .getLastVisiblePosition();
                        // 如果当前条目是最后一个 增查询更多的数据
                        if (lastVisiblePosition == pageBlackNumber.size() - 1) {
                            pageNumber++;
                            if (pageNumber * pageSize >= totalNumber) {
                                Toast.makeText(SecurityPhone.this,
                                        "没有更多的数据了", Toast.LENGTH_SHORT).show();
                            } else {
                                pageBlackNumber.addAll(dao.getPageBlackNumber(
                                        pageNumber, pageSize));
                                adapter.notifyDataSetChanged();
                            }
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.bt_addblacknumber:
                // 跳转至添加黑名单页面
                startActivity(new Intent(this, AddBlackNumberActivity.class));
                break;
        }
    }
}
