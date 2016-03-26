package com.example.administrator.security;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.example.administrator.adapter.HomeAdapter;

public class HomeActivity extends AppCompatActivity {
    private GridView gv_home;
    private long mExitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        //init GridView
        gv_home = (GridView) findViewById(R.id.gv_home);
        gv_home.setAdapter(new HomeAdapter(HomeActivity.this));
        //set item click listener
        gv_home.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0://手机防盗
                        startActivity(AntiTheft.class);
                        break;
                    case 1://通讯卫士
                        startActivity(SecurityPhone.class);
                        break;
                    case 2://软件管家
                        startActivity(SofewareManager.class);
                        break;
                    case 3://病毒查杀
                        startActivity(AntiVirus.class);
                        break;
                    case 4://清理缓存
                        startActivity(ClearCache.class);
                        break;
                    case 5://进程管理
                        startActivity(ProcessManager.class);
                        break;
                    case 6://流量统计
                        startActivity(FlowStatistics.class);
                        break;
                    case 7://高级工具
                        startActivity(SeniorTools.class);
                        break;
                    case 8://设置中心
                        startActivity(Setting.class);
                        break;
                }
            }
        });
    }

    /**
     *开启新的Activity不关闭自己
     */
    public void startActivity(Class<?> cls){
        Intent intent = new Intent(HomeActivity.this,cls);
        startActivity(intent);
    }
    /**
     * 按两次返回退出程序
     */
    public  boolean OnKeyDown(int keyCode,KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if((System.currentTimeMillis()-mExitTime)<2000){
                System.exit(0);
            }else {
                Toast.makeText(this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            }
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }
}
