package com.gbq.library.utils;

import android.graphics.Color;

/**
 * 类说明：颜色工具类
 * Author: Kuzan
 * Date: 2017/8/11 16:04.
 */
public class ColorUtil {

    /**
     * 获取颜色的RGB值
     *
     * @param color 颜色值 Color.BLACK
     * */
    public static String toHexFromColor(int color){
        String r,g,b;
        StringBuilder su = new StringBuilder();
        r = Integer.toHexString(Color.red(color));
        g = Integer.toHexString(Color.green(color));
        b = Integer.toHexString(Color.blue(color));
        r = r.length() == 1 ? "0" + r : r;
        g = g.length() ==1 ? "0" + g : g;
        b = b.length() == 1 ? "0" + b : b;
        r = r.toUpperCase();
        g = g.toUpperCase();
        b = b.toUpperCase();
        su.append("0x");
        su.append(r);
        su.append(g);
        su.append(b);
        //0x000000
        return su.toString();
    }
}
