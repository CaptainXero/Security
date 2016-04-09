package com.example.administrator.adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.entity.CacheInfo;
import com.example.administrator.security.R;

import java.util.List;

/**
 * Created by Administrator on 2016-04-09.
 */
public class CacheCleanAdapter extends BaseAdapter {
    private Context context;
    private List<CacheInfo> cacheInfos;

    public CacheCleanAdapter(Context context,List<CacheInfo> cacheInfos) {
        super();
        this.cacheInfos = cacheInfos;
        this.context = context;
    }

    @Override
    public int getCount() {
        return cacheInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return cacheInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_cleancache_list,null);
            holder.myAppicon = (ImageView) convertView.findViewById(R.id.iv_appicon_cleancache);
            holder.myAppnameTV = (TextView) convertView.findViewById(R.id.tv_appname_cleanCache);
            holder.myCacheSizeTV = (TextView) convertView.findViewById(R.id.tv_appsize_cleancache);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        CacheInfo cacheInfo = cacheInfos.get(position);
        holder.myAppicon.setImageDrawable(cacheInfo.appIcon);
        holder.myAppnameTV.setText(cacheInfo.appName);
        holder.myCacheSizeTV.setText(Formatter.formatFileSize(context,cacheInfo.cacheSize));
        return convertView;
    }
    static class ViewHolder{
        ImageView myAppicon;
        TextView myAppnameTV;
        TextView myCacheSizeTV;
    }
}
