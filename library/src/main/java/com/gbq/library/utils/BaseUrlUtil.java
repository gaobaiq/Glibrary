package com.gbq.library.utils;

import android.util.Log;

import com.gbq.library.FrameConfig;

/**
 * 类说明：
 * Author: Kuzan
 * Date: 2017/5/27 10:09.
 */
public class BaseUrlUtil {
    private static final String TAG = BaseUrlUtil.class.getSimpleName();
    // 保存服务器地址
    private static final String BASE_URL = "base_url";
    //保存是否开启后门的标志。
    private static final String PASTERN = "postern";

    private static String baseUrl;

    /**
     * 设置并保存baseUrl
     *
     * @param url
     */
    public static void setBaseUrl(String url) {
        if (StringUtils.isNotEmptyString(url)) {
            baseUrl = url;
            if (isPastern()) {
                ConstantUtil.writeString(BASE_URL, url);
            }
        } else {
            Log.e(TAG, "url is empty");
        }
    }

    /**
     * 获取baseUrl
     *
     * @return
     */
    public static String getBaseUrl() {

        if (StringUtils.isNotEmptyString(baseUrl)) {
            return baseUrl;
        }

        if (isPastern()) {
            String url = ConstantUtil.getString(BASE_URL);
            if (StringUtils.isNotEmptyString(url)) {
                baseUrl = url;
                return url;
            }
        }

        if (StringUtils.isNotEmptyString(FrameConfig.server)) {
            setBaseUrl(FrameConfig.server);
            return FrameConfig.server;
        }

        return null;
    }


    /**
     * 保存是否开启后门的标志
     *
     * @param isPastern
     */
    public static void setPastern(boolean isPastern) {
        if (FrameConfig.isPastern) {
            ConstantUtil.writeBoolean(PASTERN, isPastern);
        }
    }

    /**
     * 获取是否开启后门的标志
     *
     * @return
     */
    public static boolean isPastern() {
        return FrameConfig.isPastern && ConstantUtil.getBoolean(PASTERN, false);
    }
}
