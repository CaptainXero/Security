package com.example.administrator.security;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class appLockAvtivity extends AppCompatActivity implements View.OnClickListener{

    private ViewPager mAppViewPager;
    List<Fragment> mFragments = new ArrayList<Fragment>();
    private TextView mLockTV;
    private TextView mUnLockTV;
    private View slideLockView;
    private View slideUnLockView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_app_lock_avtivity);
        initView();
        initListener();
    }

    private void initListener() {
        mAppViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                if(arg0 == 0){
                    slideUnLockView.setBackgroundResource(R.drawable.slide_view);
                    slideLockView.setBackgroundColor(Color.parseColor("#00000000"));
                    //未加锁
                    mLockTV.setTextColor(Color.parseColor("#000000"));
                    mUnLockTV.setTextColor(Color.parseColor("#FF4040"));
                }else{
                    slideLockView.setBackgroundResource(R.drawable.slide_view);
                    slideUnLockView.setBackgroundColor(Color.parseColor("#00000000"));
                    //已加锁
                    mLockTV.setTextColor(Color.parseColor("#FF4040"));
                    mUnLockTV.setTextColor(Color.parseColor("#000000"));
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

    }

    private void initView() {
        findViewById(R.id.rl_titlebar).setBackgroundColor(Color.parseColor("#FF4040"));
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        ((TextView) findViewById(R.id.tv_title)).setText("程序锁");
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);
        mAppViewPager = (ViewPager) findViewById(R.id.vp_applock);
        mLockTV = (TextView) findViewById(R.id.tv_lock);
        mUnLockTV = (TextView) findViewById(R.id.tv_unlock);
        mLockTV.setOnClickListener(this);
        mUnLockTV.setOnClickListener(this);
        slideLockView = findViewById(R.id.view_slide_lock);
        slideUnLockView = findViewById(R.id.view_slide_unlock);
        AppUnLockFragment unLock = new AppUnLockFragment();
        AppLockFragment lock = new AppLockFragment();
        mFragments.add(unLock);
        mFragments.add(lock);
        mAppViewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.tv_lock:
                mAppViewPager.setCurrentItem(1);
                break;
            case R.id.tv_unlock:
                mAppViewPager.setCurrentItem(0);
                break;
        }

    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int arg0) {
            return mFragments.get(arg0);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
}
