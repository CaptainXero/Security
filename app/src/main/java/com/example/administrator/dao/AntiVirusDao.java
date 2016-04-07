package com.example.administrator.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.InputStream;

/**
 * Created by Administrator on 2016-04-04.
 */
public class AntiVirusDao {
    private static String FullPath;
    public static String checkVirus(String md5,Context context){
        String desc = null;
        File f = context.getDatabasePath("antivirus.db").getParentFile();
        if(f.exists()==false){
            f.mkdirs();
        }
        FullPath = f.getPath()+"/antivirus.db";

        /**打开病毒MD5数据库*/
//        SQLiteDatabase db = SQLiteDatabase.openDatabase
//                (context.getDatabasePath("antivirus.db").toString(),null,SQLiteDatabase.OPEN_READWRITE);
        SQLiteDatabase db = SQLiteDatabase.openDatabase
                (FullPath,null,SQLiteDatabase.OPEN_READWRITE);
        Cursor cursor = db.rawQuery("select desc from datable where md5=?",new String[]{md5});
        if(cursor.moveToNext()){
            desc = cursor.getString(0);
        }
        cursor.close();
//        db.endTransaction();
        db.close();
        return desc;
    }
}
