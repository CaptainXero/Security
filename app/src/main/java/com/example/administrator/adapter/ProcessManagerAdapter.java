package com.example.administrator.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.entity.TaskInfo;
import com.example.administrator.security.R;
import com.example.administrator.utils.DensityUtil;

import java.util.List;

/**
 * Created by Administrator on 2016-04-09.
 */
public class ProcessManagerAdapter extends BaseAdapter {
    private Context context;
    private List<TaskInfo> myUsertaskInfos;
    private List<TaskInfo> mySystaskInfos;
    private SharedPreferences mysp;

    public ProcessManagerAdapter(Context context, List<TaskInfo> myUsertaskInfos, List<TaskInfo> mySystaskInfos) {
        super();
        this.context = context;
        this.myUsertaskInfos = myUsertaskInfos;
        this.mySystaskInfos = mySystaskInfos;
        mysp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
    }

    @Override
    public int getCount() {
        if(mySystaskInfos.size()>0 & mysp.getBoolean("showSystemProcess",true)){
            return myUsertaskInfos.size()+mySystaskInfos.size()+2;
        }else {
            return myUsertaskInfos.size()+1;
        }
    }

    @Override
    public Object getItem(int position) {
        if(position==0||position==myUsertaskInfos.size()+1){
            return null;
        }else if (position<=myUsertaskInfos.size()){
            //用户进程
            return myUsertaskInfos.get(position-1);
        }else {
            //系统进程
            return mySystaskInfos.get(position-myUsertaskInfos.size()-2);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(position==0){
            TextView tv = getTextView();
            tv.setText("用户进程:"+myUsertaskInfos.size()+"个");
            return tv;
        }else if(position==myUsertaskInfos.size()+1){
            TextView tv = getTextView();
            if(mySystaskInfos.size()>0){
                tv.setText("系统进程:"+mySystaskInfos.size()+"个");
                return tv;
            }
        }
        //获取TaskInfo对象
        TaskInfo taskInfo = null;
        if(position<=myUsertaskInfos.size()){
            taskInfo = myUsertaskInfos.get(position-1);
        }else if (mySystaskInfos.size()>0){
            taskInfo = mySystaskInfos.get(position-myUsertaskInfos.size()-2);
        }
        ViewHolder holder =null;
        if(convertView!=null&&convertView instanceof RelativeLayout){//判断其左边对象是否为其右边类的实例
            holder = (ViewHolder) convertView.getTag();
        }else {
            convertView = View.inflate(context, R.layout.item_processmanager_list,null);
            holder = new ViewHolder();
            holder.myAppIcon = (ImageView) convertView.findViewById(R.id.iv_appicon_processmana);
            holder.myAppMemoryTv = (TextView) convertView.findViewById(R.id.tv_appmemory_processmana);
            holder.myAppNameTV = (TextView) convertView.findViewById(R.id.tv_appname_processmana);
            holder.myCheckBox = (CheckBox) convertView.findViewById(R.id.cb_process);
            convertView.setTag(holder);
        }
        if(taskInfo!=null){
            holder.myAppNameTV.setText(taskInfo.appName);
            holder.myAppMemoryTv.setText("占用内存:"+ Formatter.formatFileSize(context,taskInfo.appMemory));
            holder.myAppIcon.setImageDrawable(taskInfo.appIcon);
            if(taskInfo.packageName.equals(context.getPackageName())){
                holder.myCheckBox.setVisibility(View.GONE);
            }else {
                holder.myCheckBox.setVisibility(View.VISIBLE);
            }
            holder.myCheckBox.setChecked(taskInfo.isChecked);
        }
        return convertView;
    }

    private TextView getTextView() {
        TextView tv = new TextView(context);
        tv.setBackgroundColor(Color.parseColor("#D3D3D3"));
        tv.setPadding(DensityUtil.dip2px(context, 5), DensityUtil.dip2px(context, 5),
                DensityUtil.dip2px(context, 5), DensityUtil.dip2px(context, 5));
        tv.setTextColor(Color.parseColor("#000000"));
        return tv;
    }

    static  class ViewHolder{
        ImageView myAppIcon;
        TextView myAppNameTV;
        TextView myAppMemoryTv;
        CheckBox myCheckBox;
    }
}
