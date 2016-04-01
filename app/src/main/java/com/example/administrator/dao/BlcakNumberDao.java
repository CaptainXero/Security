package com.example.administrator.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;

import com.example.administrator.entity.BlackContactInfo;
import com.example.administrator.helper.BlackNumberOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-04-01.
 */
public class BlcakNumberDao {
    private BlackNumberOpenHelper blackNumberOpenHelper;

    public BlcakNumberDao(Context context) {
        //初始化（创建）数据库
        super();
        blackNumberOpenHelper = new BlackNumberOpenHelper(context);
    }
    /**
     *插入数据
     */
    public boolean add(BlackContactInfo blackContactInfo) {
        SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (blackContactInfo.contactNumber.startsWith("+86")) {
            blackContactInfo.contactNumber = blackContactInfo.contactNumber
                    .substring(3, blackContactInfo.contactNumber.length());
        }
        values.put("number", blackContactInfo.contactNumber);
        values.put("name", blackContactInfo.phoneName);
        values.put("mode", blackContactInfo.mode);
        long rowid = db.insert("blacknumber", null, values);
        if (rowid == -1) {
            //插入数据失败
            return false;
        } else {
            return true;
        }
    }
    /**
     * 删除数据
     */
    public boolean delete(BlackContactInfo blackContactInfo){
        SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
        int rownumber = db.delete("blacknumber", "number=?",
                new String[]{blackContactInfo.contactNumber});
        if(rownumber==0){
            //删除失败
            return  false;
        }else {
            return true;
        }
    }
    /**
     * 分页查询数据库记录
     */
    public List<BlackContactInfo> getPageBlackNumber(int pagenumber,int pagesize){
        SQLiteDatabase db = blackNumberOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select number,mode,name from blacknumber limit ? offset ?",
                new String[]{String.valueOf(pagesize), String.valueOf(pagenumber)});
        List<BlackContactInfo> blackContactInfos = new ArrayList<BlackContactInfo>();
        while (cursor.moveToNext()){
            BlackContactInfo info = new BlackContactInfo();
            info.contactNumber = cursor.getString(0);
            info.mode = cursor.getInt(1);
            info.phoneName = cursor.getString(2);
            blackContactInfos.add(info);
        }
        cursor.close();
        db.close();
        SystemClock.sleep(30);
        return blackContactInfos;
    }
    /**
     * 判断号码是否在黑名单中
     */
    public boolean IsNumberExist(String number){
        SQLiteDatabase db = blackNumberOpenHelper.getReadableDatabase();
        Cursor cursor = db.query("blacknumber", null, "number=?", new String[]{number}, null, null, null);
        if(cursor.moveToNext()){
            cursor.close();;
            db.close();
            return true;
        }
        cursor.close();
        db.close();
        return false;
    }
    /**
     * 根据号码查询黑名单信息
     */
    public int getBlackContactMode(String number){
        SQLiteDatabase db = blackNumberOpenHelper.getReadableDatabase();
        Cursor cursor = db.query("blacknumber", new String[]{"mode"}, "number=?",
                new String[]{number}, null, null, null);
        int mode = 0;
        if(cursor.moveToNext()){
            mode = cursor.getInt(cursor.getColumnIndex("mode"));
        }
        cursor.close();
        db.close();
        return mode;
    }
    /**
     * 获取数据库总条目数
     */
    public int getTotalNumber(){
        SQLiteDatabase db = blackNumberOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from blacknumber",null);
        cursor.moveToNext();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count;
    }
}
