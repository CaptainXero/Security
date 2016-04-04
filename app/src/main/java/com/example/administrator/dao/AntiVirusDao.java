package com.example.administrator.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Administrator on 2016-04-04.
 */
public class AntiVirusDao {
    public static String checkVirus(String md5){
        String desc = null;
        /**打开病毒MD5数据库*/
        SQLiteDatabase db = SQLiteDatabase.openDatabase
                ("/src/main/assets/antivirus.db",null,SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = db.rawQuery("select desc from datable where md5=?",new String[]{md5});
        if(cursor.moveToNext()){
            desc = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return desc;
    }
}
