package com.example.administrator.entity;

/**
 * Created by Administrator on 2016-04-01.
 * 联系人实体
 */
public class BlackContactInfo {
    public String phoneName;
    public String contactNumber;
    public int mode;
    public String getModeString(int mode){
        switch (mode){
            case 1:
                return "电话拦截";
            case 2:
                return "短信拦截";
            case 3:
                return "电话/短信拦截";
        }
        return "";
    }
}
