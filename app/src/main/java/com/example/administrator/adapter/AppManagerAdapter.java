package com.example.administrator.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.entity.AppInfo;
import com.example.administrator.security.R;
import com.example.administrator.utils.DensityUtil;
import com.example.administrator.utils.EngineUtils;


import java.util.List;

/**
 * Created by Administrator on 2016-04-04.
 */
public class AppManagerAdapter extends BaseAdapter {
    public List<AppInfo> UserAppInfos;
    public List<AppInfo> SystemAppInfos;
    private Context context;

    public AppManagerAdapter(List<AppInfo> userAppInfos,List<AppInfo> systemAppInfos
            ,Context context) {
        super();
        UserAppInfos = userAppInfos;
        SystemAppInfos = systemAppInfos;
        this.context = context;
    }

    @Override
    public int getCount() {
        return UserAppInfos.size()+SystemAppInfos.size()+2;
    }

    @Override
    public Object getItem(int position) {
        if(position==0){
            return null;
        }else if(position==(UserAppInfos.size()+1)){
            return null;
        }
        AppInfo appInfo;
        if(position<(UserAppInfos.size()+1)){
            appInfo = UserAppInfos.get(position-1);
        }else{
            int location = position-UserAppInfos.size()-2;
            appInfo = SystemAppInfos.get(location);
        }
        return appInfo;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(position==0){
            TextView tv = getTextView();
            tv.setText("用户程序:"+UserAppInfos.size()+"个");
            return tv;
        }else if(position==(UserAppInfos.size()+1)){
            TextView tv = getTextView();
            tv.setText("系统程序:"+SystemAppInfos.size()+"个");
            return tv;
        }
        //获取当前App对象
        AppInfo appInfo;
        if(position<(UserAppInfos.size()+1)){
            //position 0 为textView
            appInfo = UserAppInfos.get(position-1);
        }else {
            appInfo = SystemAppInfos.get(position-UserAppInfos.size()-2);
        }
        ViewHolder viewHolder = null;
        if(convertView!=null & convertView instanceof LinearLayout){
            viewHolder = (ViewHolder) convertView.getTag();
        }else {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_appmanager_list,null);
            viewHolder.mAppIconImgv = (ImageView) convertView.findViewById(R.id.imgv_appicon);
            viewHolder.mAppLocationTV = (TextView) convertView.findViewById(R.id.tv_appisroom);
            viewHolder.mAppSizeTV = (TextView) convertView.findViewById(R.id.tv_appsize);
            viewHolder.mAppNameTV = (TextView) convertView.findViewById(R.id.tv_appname);
            viewHolder.mLuanchAppTV = (TextView) convertView.findViewById(R.id.tv_launch_app);
            viewHolder.mSettingAppTV = (TextView) convertView.findViewById(R.id.tv_setting_app);
            viewHolder.mShareAppTV = (TextView) convertView.findViewById(R.id.tv_share_app);
            viewHolder.mUninstallTV = (TextView) convertView.findViewById(R.id.tv_uninstall_app);
            viewHolder.mAppOptionLL = (LinearLayout) convertView.findViewById(R.id.ll_option_app);
            convertView.setTag(viewHolder);
        }
        if(appInfo!=null){
            viewHolder.mAppLocationTV.setText(appInfo.getAppLocation(appInfo.isInRoom));
            viewHolder.mAppIconImgv.setImageDrawable(appInfo.icon);
            viewHolder.mAppSizeTV.setText(Formatter.formatFileSize(context,appInfo.appSize));
            viewHolder.mAppNameTV.setText(appInfo.appName);
            if(appInfo.isSelected){
                viewHolder.mAppOptionLL.setVisibility(View.VISIBLE);
            }else {
                viewHolder.mAppOptionLL.setVisibility(View.GONE);
            }
        }
        MyClickListener listener = new MyClickListener(appInfo);
        viewHolder.mLuanchAppTV.setOnClickListener(listener);
        viewHolder.mSettingAppTV.setOnClickListener(listener);
        viewHolder.mShareAppTV.setOnClickListener(listener);
        viewHolder.mUninstallTV.setOnClickListener(listener);
        return convertView;
    }
    class MyClickListener implements View.OnClickListener{
        private AppInfo appInfo;
        public MyClickListener(AppInfo appInfo) {
            super();
            this.appInfo = appInfo;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_launch_app:
                    EngineUtils.startApp(context,appInfo);
                    break;
                case R.id.tv_share_app:
                    EngineUtils.shareApplcation(context,appInfo);
                    break;
                case R.id.tv_setting_app:
                    EngineUtils.settingAppDetail(context,appInfo);
                    break;
                case R.id.tv_uninstall_app:
                    if(appInfo.packageName.equals(context.getPackageName())){
                        Toast.makeText(context,"您没有权限卸载此App",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    EngineUtils.deleteApp(context,appInfo);
                    break;
            }
        }
    }
    static class ViewHolder {
        /** 启动App */
        TextView mLuanchAppTV;
        /** 卸载App */
        TextView mUninstallTV;
        /** 分享App */
        TextView mShareAppTV;
        /** 设置App */
        TextView mSettingAppTV;
        /** app 图标 */
        ImageView mAppIconImgv;
        /** app位置 */
        TextView mAppLocationTV;
        /** app大小 */
        TextView mAppSizeTV;
        /** app名称 */
        TextView mAppNameTV;
        /** 操作App的线性布局 */
        LinearLayout mAppOptionLL;
    }

    private TextView getTextView() {
        TextView tv = new TextView(context);
        tv.setBackgroundColor(Color.parseColor("#555555"));
        tv.setPadding(DensityUtil.dip2px(context, 5), DensityUtil.dip2px(context, 5), DensityUtil
                .dip2px(context, 5), DensityUtil.dip2px(context, 5));
        tv.setTextColor(Color.parseColor("#000000"));
        return tv;
    }
}
