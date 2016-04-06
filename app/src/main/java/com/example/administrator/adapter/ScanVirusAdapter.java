package com.example.administrator.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.entity.ScanAppInfo;
import com.example.administrator.security.R;

import java.util.List;

/**
 * Created by Administrator on 2016-04-06.
 */
public class ScanVirusAdapter extends BaseAdapter {
    private List<ScanAppInfo> AppInfos;
    private Context context;

    public ScanVirusAdapter(List<ScanAppInfo> appInfos, Context context) {
        super();
        this.AppInfos = appInfos;
        this.context = context;
    }

    @Override
    public int getCount() {
        return AppInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return AppInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView = View.inflate(context, R.layout.item_list_applock,null);
            holder = new ViewHolder();
            holder.myAppIcon = (ImageView) convertView.findViewById(R.id.iv_appicon);
            holder.myAppname = (TextView) convertView.findViewById(R.id.tv_scan_appname);
            holder.myScanAppIcon = (ImageView) convertView.findViewById(R.id.iv_lock);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        ScanAppInfo scanAppInfo = AppInfos.get(position);
        if(!scanAppInfo.isVirus){
            holder.myScanAppIcon.setBackgroundResource(R.drawable.blue_right_icon);
            holder.myAppname.setTextColor(Color.parseColor("#000000"));
            holder.myAppname.setText(scanAppInfo.appName);
        }else {
            holder.myAppname.setTextColor(Color.parseColor("#EE2C2C"));
            holder.myAppname.setText(scanAppInfo.appName+"("+scanAppInfo.description+")");
        }
        holder.myScanAppIcon.setImageDrawable(scanAppInfo.appicon);
        return convertView;
    }
    class ViewHolder{
        ImageView myAppIcon;
        TextView myAppname;
        ImageView myScanAppIcon;
    }
}
