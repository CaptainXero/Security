package com.example.administrator.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.dao.BlcakNumberDao;
import com.example.administrator.entity.BlackContactInfo;
import com.example.administrator.security.R;

import java.util.List;

/**
 * Created by Administrator on 2016-04-01.
 */
public class BlackContactAdapter extends BaseAdapter {
    private List<BlackContactInfo> contactInfos;
    private Context context;
    private BlcakNumberDao dao;
    private BlackConactCallBack callBack;
    public void setCallBack(BlackConactCallBack callBack) {
        this.callBack = callBack;

    }
    public BlackContactAdapter(List<BlackContactInfo> systemContacts,Context context) {
        this.contactInfos = systemContacts;
        this.context = context;
        dao = new BlcakNumberDao(context);
    }

    @Override
    public int getCount() {
        return contactInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return contactInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context,
                    R.layout.item_list_blackcontact, null);
            holder = new ViewHolder();
            holder.mNameTV = (TextView) convertView
                    .findViewById(R.id.tv_black_name);
            holder.mModeTV = (TextView) convertView
                    .findViewById(R.id.tv_black_mode);
            holder.mContactImgv = convertView
                    .findViewById(R.id.view_black_icon);
            holder.mDeleteView = convertView
                    .findViewById(R.id.view_black_delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mNameTV.setText(contactInfos.get(position).phoneName + "("
                + contactInfos.get(position).contactNumber + ")");
        holder.mModeTV.setText(contactInfos.get(position).getModeString(
                contactInfos.get(position).mode));
        holder.mNameTV.setTextColor(Color.parseColor("#CD00CD"));
        holder.mModeTV.setTextColor(Color.parseColor("#CD00CD"));
        holder.mContactImgv
                .setBackgroundResource(R.drawable.brightpurple_contact_icon);
        holder.mDeleteView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean datele = dao.delete(contactInfos.get(position));
                if (datele) {
                    contactInfos.remove(contactInfos.get(position));
                    BlackContactAdapter.this.notifyDataSetChanged();
                    // 如果数据库中没有数据了，则执行回调函数
                    if (dao.getTotalNumber() == 0) {
                        callBack.DataSizeChanged();
                    }
                } else{
                    Toast.makeText(context, "删除失败！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return convertView;
    }
    public interface BlackConactCallBack {
        void DataSizeChanged();
    }
    class ViewHolder {
        TextView mNameTV;
        TextView mModeTV;
        View mContactImgv;
        View mDeleteView;
    }
}
