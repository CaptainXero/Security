package com.example.administrator.utils;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

/**
 * Created by Administrator on 2016-04-04.
 * 获取文件MD5值
 */
public class MD5Utils {
    public static String getFileMD5(String path){
        try{
            MessageDigest digest = MessageDigest.getInstance("md5");
            File file = new File(path);
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = fis.read(buffer))!=-1){
                digest.update(buffer,0,len);
            }
            byte[] result = digest.digest();
            StringBuilder sb = new StringBuilder();
            for(byte b:result){
                int number = b & 0xff;
                String hex = Integer.toHexString(number);
                if(hex.length()==1){
                    sb.append("0"+hex);
                }else {
                    sb.append(hex);
                }
            }
            return sb.toString();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
