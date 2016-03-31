package com.example.administrator.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.entity.ContactInfo;
import com.example.administrator.security.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Administrator on 2016-03-31.
 */
public class ContactAdapter extends BaseAdapter {
    private List<ContactInfo> contactInfos;
    private Context  context;
    public ContactAdapter(List<ContactInfo> contactInfos,Context context) {
        super();
        this.contactInfos = contactInfos;
        this.context = context;
    }

    @Override
    public int getCount() {
        return contactInfos.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            convertView = View.inflate(context, R.layout.item_list_contact_select,null);
            holder = new ViewHolder();
            holder.myNameTv = (TextView) convertView.findViewById(R.id.tv_contact_name);
            holder.myPhoneTv = (TextView) convertView.findViewById(R.id.tv_contact_phoneNumber);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.myNameTv.setText(contactInfos.get(position).name);
        holder.myPhoneTv.setText(contactInfos.get(position).phoneNumber);
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return contactInfos.get(position);
    }
    class ViewHolder{
        TextView myNameTv;
        TextView myPhoneTv;
    }
}
