package com.gbq.library.utils;

import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

/**
 * 类说明：
 * Author: Kuzan
 * Date: 2017/5/26 18:53.
 */
public class Base64Util {
    public static String StringToBase64(String str) {

        if (StringUtils.isEmptyString(str)) {
            return "";
        }

        String base = "";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            // 创建对象输出流，并封装字节流
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            // 将对象写入字节流
            oos.writeObject(str);
            // 将字节流编码成base64的字符窜
            base = new String(Base64.encode(baos.toByteArray(), 0));
            oos.close();
            baos.close();
        } catch (IOException e) {
        }

        return base;
    }
    
    public static String Base64ToString(String base) {

        if (StringUtils.isEmptyString(base)) {
            return "";
        }

        String str = "";

        byte[] base64;

        //读取字节
        base64 = Base64.decode(base,0);

        if (base64 == null) {
            return "";
        }

        //封装到字节流
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        try {
            //再次封装
            ObjectInputStream bis = new ObjectInputStream(bais);
            try {
                //读取对象
                str = (String) bis.readObject();
                bis.close();
                bais.close();
            } catch (ClassNotFoundException e) {
            }
        } catch (StreamCorruptedException e) {
        } catch (IOException e) {
        }

        return str;

    }
}
