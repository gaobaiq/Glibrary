package com.gbq.library.network;

import android.util.Log;

import com.gbq.library.FrameConfig;
import com.gbq.library.callback.FileCallback;
import com.gbq.library.callback.JsonCallback;
import com.gbq.library.okgo.OkGo;
import com.gbq.library.okgo.callback.AbsCallback;
import com.gbq.library.okgo.model.HttpHeaders;
import com.gbq.library.okgo.model.HttpParams;
import com.gbq.library.utils.AppParamEncryptUtil;
import com.gbq.library.utils.BaseUrlUtil;
import com.gbq.library.utils.StringUtils;

import java.io.File;
import java.util.List;

/**
 * 类说明：网络请求封装
 * Author: Kuzan
 * Date: 2017/5/26 18:46.
 */
public class OkGoRequest {
    private static final String TAG = OkGoRequest.class.getSimpleName();

    private static final String CLIENT_TYPE_KEY = "qc_client_type";
    private static final String CLIENT_TYPE = "android";
    private static final String FORMAT = "format";
    private static final String APP_STR_KEY = "qc_app_str";
    private static final String APP_SIGN_KEY = "qc_app_sign";
    private static final String APP_TOKEN_KEY = "qc_app_token";
    private static final int NOT_LOGIN_STATUS = 300; // 未登录状态，返回自定义异常代码
    private static final int ERROR_SERVICE = -599; // 接口错误
    private static final int SUCCESS_STATUS = 200;
    public static final int NOT_LOGIN_STATUS_TYPE = 300;

    private static String qc_app_str = AppParamEncryptUtil.encryptCharStr();
    private static String qc_app_sign = AppParamEncryptUtil.signParamStr(qc_app_str, FrameConfig.appSign);
    private static String qc_app_token;

    /**
     * json数据请求
     *
     * @param url 请求地址
     * @param tag 请求标签，方便区分是哪个请求在运行，以便取消请求
     * @param headers 请求头
     * @param params 请求参数
     * @param callback 回调
     * */
    public static <T> void jsonRequest(final String url, final Object tag, HttpHeaders headers, HttpParams params, final AbsCallback<BaseResponse<T>> callback) {
        OkGo.get(BaseUrlUtil.getBaseUrl() + url)
                .tag(tag)
                .headers(headers)
                .params(params)
                .execute(callback);
    }

    /**
     * 文件下载数据请求
     *
     * @param url 请求地址
     * @param tag 请求标签，方便区分是哪个请求在运行，以便取消请求
     * @param headers 请求头
     * @param params 请求参数
     * @param callback 回调
     * */
    public static void downloadRequest(final String url, final Object tag, HttpHeaders headers, HttpParams params, final FileCallback callback) {
        OkGo.get(BaseUrlUtil.getBaseUrl() + url)
                .tag(tag)
                .headers(headers)
                .params(params)
                .execute(callback);
    }

    /**
     * 文件上传
     *
     * @param url 请求地址
     * @param tag 请求标签，方便区分是哪个请求在运行，以便取消请求
     * @param headers 请求头
     * @param params 请求参数
     * @param callback 回调
     * */
    public static <T> void uploadRequest(final String url, final Object tag, HttpHeaders headers, HttpParams params, List<File> files, final JsonCallback<BaseResponse<T>> callback) {
        OkGo.post(BaseUrlUtil.getBaseUrl() + url)
                .tag(tag)
                .headers(headers)
                .params(params)
                .addFileParams("file", files)
                .execute(callback);
    }

    /**
     * 普通的请求数据
     */
    public static HttpParams getDefaultParams() {
        HttpParams params = new HttpParams();
        params.put(FORMAT, true);
        params.put(CLIENT_TYPE_KEY, CLIENT_TYPE);
        params.put(APP_STR_KEY, qc_app_str);
        params.put(APP_SIGN_KEY, qc_app_sign);

        return params;
    }

    /**
     * 所有请求都执行该方法构造基础参数
     */
    public static HttpParams getAppParams() {
        HttpParams params = getDefaultParams();
        if (StringUtils.isEmptyString(qc_app_token)) {
            qc_app_token = "3215sdf13ad1f65asd4f3ads1f";
        }
        params.put(APP_TOKEN_KEY, "3215sdf13ad1f65asd4f3ads1f");

        return params;
    }

    public static void setToken(String token) {
        qc_app_token = token;
        Log.e("token", "token=" + qc_app_token);
    }
}
