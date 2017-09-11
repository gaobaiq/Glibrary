package com.gbq.library.utils;

import android.text.TextUtils;
import android.util.Log;

import com.gbq.library.network.OkGoRequest;


/**
 * 类说明：app token工具类
 * Author: Kuzan
 * Date: 2017/5/25 15:12.
 */
public class TokenUtil {
    private static final String TAG = TokenUtil.class.getSimpleName();
    // 保存本次登录的token用的
    public static final String TOKEN = "app_token";

    /**
     * 保存token
     * */
    public static void saveToken(final String token) {
        String str = Base64Util.StringToBase64(token);
        if (StringUtils.isNotEmptyString(str)) {
            ConstantUtil.writeString(TOKEN, str);
            OkGoRequest.setToken(token);
        }
    }

    /**
     * 清掉token
     * */
    public static void clearToken() {
        Log.e(TAG, "clearToken");
        ConstantUtil.writeString(TOKEN, "");
        OkGoRequest.setToken("");
    }

    /**
     * 获取token
     * */
    public static String getToken() {
        String userToken = Base64Util.Base64ToString(ConstantUtil.getString(TOKEN));
        return TextUtils.isEmpty(userToken) ? "" : userToken;
    }

}
