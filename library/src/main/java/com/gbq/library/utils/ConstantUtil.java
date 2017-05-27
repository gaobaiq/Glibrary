package com.gbq.library.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * 类说明：用SharedPreferences 存储一些简单属性 .
 * Author: Kuzan
 * Date: 2017/5/26 18:53.
 */
public class ConstantUtil {
    private static volatile SharedPreferences mSharedPreferences;

    public static synchronized SharedPreferences initSharedPreferences(Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return mSharedPreferences;
    }

    /**
     * 写入一个String值
     */
    public static void writeString(final String key, final String value) {
        final SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(final String key) {
        return mSharedPreferences.getString(key, "");
    }

    /**
     * 根据key获取一个值，若不存在就使用默认值做返回值 .
     *
     * @param key          key
     * @param defaultValue 默认值
     * @return
     */
    public static String getString(final String key, final String defaultValue) {
        return mSharedPreferences.getString(key, defaultValue);
    }

    /**
     * 对全局变量指定写入一个int值 . <br>
     *
     * @param key   KEY
     * @param value 值
     */
    public static void writeInt(final String key, final int value) {
        final SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * 获取指定的一个的Key值 . <br>
     *
     * @param key key
     * @return 返回对应的值
     */
    public static int getInt(final String key) {
        return mSharedPreferences.getInt(key, 0);
    }

    /**
     * 对全局变量指定写入一个boolean .
     *
     * @param key
     * @param value
     */
    public static void writeBoolean(final String key, final boolean value) {
        final SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBoolean(final String key) {
        return mSharedPreferences.getBoolean(key, false);
    }

    public static boolean getBoolean(final String key,boolean defaultValue) {
        return mSharedPreferences.getBoolean(key, defaultValue);
    }

    /**
     * 对全局变量指定写入一个long值 . <br>
     *
     * @param key   KEY
     * @param value 值
     */
    public static void writeLong(final String key, final long value) {
        final SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static Long getLong(final String key) {
        return mSharedPreferences.getLong(key, 0);
    }

//    private static SharedPreferences getSharedPreferences() {
//        return PreferenceManager.getDefaultSharedPreferences(BaseApplication.getBaseApplication());
//    }
}
