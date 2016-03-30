package com.example.administrator.action;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.Toast;

import com.example.administrator.security.R;

public abstract class BaseSetUpAvtivity extends AppCompatActivity {
    public SharedPreferences sp;
    private GestureDetector myGestureDetector;//手势识别

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_base_set_up_avtivity);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        //初始化手势识别器
        myGestureDetector = new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if(Math.abs(velocityX)<200){
                    Toast.makeText(getApplicationContext(),"快速滑动翻页",Toast.LENGTH_SHORT).show();
                    return true;
                }
                if((e2.getRawX()-e1.getRawX())>200){
                    //从左向右滑动，显示上一个页面
                    showPre();
                    overridePendingTransition(R.transition.pre_in, R.transition.pre_out);
                    return true;
                }
                if((e1.getRawX()-e2.getRawX())>200){
                    //从右向左滑动，显示下一个页面
                    showNext();
                    overridePendingTransition(R.transition.next_in,R.transition.next_out);
                    return true;
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }
    public abstract void showPre();
    public abstract void showNext();
    //利用识别到的手势去识别事件

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //分析手势
        myGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
    /**
     * 开启新的activity并关闭自身
     */
    public void startActivityAndFinishSelf(Class<?>cls){
        Intent intent = new Intent(this,cls);
        startActivity(intent);
        finish();
    }
}
