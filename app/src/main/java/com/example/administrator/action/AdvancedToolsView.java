package com.example.administrator.action;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.security.R;

/**
 * Created by Administrator on 2016-04-13.
 */
public class AdvancedToolsView extends RelativeLayout {
    private TextView myDesripitionTV;
    private String desc="";
    private Drawable drawable;
    private ImageView myLeftImgv;

    public AdvancedToolsView(Context context) {
        super(context);
        init(context);
    }


    public AdvancedToolsView(Context context,AttributeSet attrs) {
        super(context,attrs);
        //获取属性对象
        TypedArray mytypedArray = context.obtainStyledAttributes(attrs, R.styleable.AdvanceToolsView);
        desc = mytypedArray.getString(R.styleable.AdvanceToolsView_desc);
        //获取src属性，与attrs.xml中定义的属性绑定
        drawable = mytypedArray.getDrawable(R.styleable.AdvanceToolsView_android_src);
        //回收属性对象
        mytypedArray.recycle();
        init(context);
    }

    public AdvancedToolsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 初始化控件
     * @param context
     */
    private void init(Context context) {
        View view = View.inflate(context,R.layout.ui_advancedtools_view,null);
        this.addView(view);
        myDesripitionTV = (TextView) findViewById(R.id.tv_tools_decription);
        myLeftImgv = (ImageView) findViewById(R.id.iv_tools_left);
        myDesripitionTV.setText(desc);
        if(drawable!=null){
            myLeftImgv.setImageDrawable(drawable);
        }
    }
}
