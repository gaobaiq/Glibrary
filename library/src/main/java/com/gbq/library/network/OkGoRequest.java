package com.gbq.library.network;

import android.util.Log;

import com.gbq.library.FrameConfig;
import com.gbq.library.beans.UploadFileBean;
import com.gbq.library.callback.DownloadCallback;
import com.gbq.library.callback.UploadCallback;
import com.gbq.library.callback.UploadFileCallback;
import com.gbq.library.utils.AppParamEncryptUtil;
import com.gbq.library.utils.BaseUrlUtil;
import com.gbq.library.utils.CompositeDisposableUtil;
import com.gbq.library.utils.StringUtils;
import com.gbq.library.utils.TokenUtil;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpMethod;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;

import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static com.gbq.library.network.RxRequest.request;

/**
 * 类说明：网络请求封装
 * Author: Kuzan
 * Date: 2017/5/26 18:46.
 */
public class OkGoRequest {

    private static final String CLIENT_TYPE_KEY = "qc_client_type";
    private static final String CLIENT_TYPE = "android";
    private static final String FORMAT = "format";
    private static final String APP_STR_KEY = "qc_app_str";
    private static final String APP_SIGN_KEY = "qc_app_sign";
    private static final String APP_TOKEN_KEY = "qc_app_token";

    private static String qc_app_str = AppParamEncryptUtil.encryptCharStr();
    private static String qc_app_sign = AppParamEncryptUtil.signParamStr(qc_app_str, FrameConfig.appSign);
    private static String qc_app_token;

    private static String baseUrl;
    private static Type baseType;

    /**
     * 使用请求前先调用此方法
     * */
    public synchronized static void createService() {
        baseUrl = BaseUrlUtil.getBaseUrl();
        baseType = new TypeToken<JSONObject>(){}.getType();
    }

    /**
     * GET请求
     *
     * @param url
     * */
    public static <T> Observable<T> getRequest(String url) {

        return request(HttpMethod.GET, null, baseUrl + url, baseType);
    }

    public static <T> Observable<T> getRequest(Object tag, String url) {

        return request(HttpMethod.GET, tag, baseUrl + url, baseType);
    }

    public static <T> Observable<T> getRequest(String url, Type type) {

        return request(HttpMethod.GET, null, baseUrl + url, type);
    }

    public static <T> Observable<T> getRequest(String url, Class<T> clazz) {
        return request(HttpMethod.GET, null, baseUrl + url, clazz);
    }

    public static <T> Observable<T> getRequest(Object tag, String url, Type type) {

        return request(HttpMethod.GET, tag, baseUrl + url, type);
    }

    public static <T> Observable<T> getRequest(Object tag, String url, Class<T> clazz) {

        return request(HttpMethod.GET, tag, baseUrl + url, clazz);
    }

    public static <T> Observable<T> getRequest(String url, HttpParams params) {

        return request(HttpMethod.GET, null, baseUrl + url, baseType, params);
    }

    public static <T> Observable<T> getRequest(Object tag, String url, HttpParams params) {

        return request(HttpMethod.GET, tag, baseUrl + url, baseType, params);
    }

    public static <T> Observable<T> getRequest(String url, Type type, HttpParams params) {

        return request(HttpMethod.GET, null, baseUrl + url, type, params);
    }

    public static <T> Observable<T> getRequest(String url, Class<T> clazz, HttpParams params) {
        return request(HttpMethod.GET, null, baseUrl + url, clazz, params);
    }

    public static <T> Observable<T> getRequest(Object tag, String url, Type type, HttpParams params) {

        return request(HttpMethod.GET, tag, baseUrl + url, type, params);
    }

    public static <T> Observable<T> getRequest(Object tag, String url, Class<T> clazz, HttpParams params) {

        return request(HttpMethod.GET, tag, baseUrl + url, clazz, params);
    }

    public static <T> Observable<T> getRequest(String url, HttpParams params, HttpHeaders headers) {

        return request(HttpMethod.GET, null, baseUrl + url, baseType, params, headers);
    }

    public static <T> Observable<T> getRequest(Object tag, String url, HttpParams params, HttpHeaders headers) {

        return request(HttpMethod.GET, tag, baseUrl + url, baseType, params, headers);
    }

    public static <T> Observable<T> getRequest(String url, Type type, HttpParams params, HttpHeaders headers) {

        return request(HttpMethod.GET, null, baseUrl + url, type, params, headers);
    }

    public static <T> Observable<T> getRequest(String url, Class<T> clazz, HttpParams params, HttpHeaders headers) {
        return request(HttpMethod.GET, null, baseUrl + url, clazz, params, headers);
    }

    /**
     * GET请求
     *
     * @param tag
     *          请求标签
     * @param url
     *          请求链接
     * @param type
     *          返回对象类型
     * @param params
     *          请求参数
     * @param headers
     *          请求头
     * */
    public static <T> Observable<T> getRequest(Object tag, String url, Type type, HttpParams params, HttpHeaders headers) {

        return request(HttpMethod.GET, tag, baseUrl + url, type, params, headers);
    }

    /**
     * GET请求
     *
     * @param tag
     *          请求标签
     * @param url
     *          请求链接
     * @param clazz
     *          返回对象类型
     * @param params
     *          请求参数
     * @param headers
     *          请求头
     * */
    public static <T> Observable<T> getRequest(Object tag, String url, Class<T> clazz, HttpParams params, HttpHeaders headers) {

        return request(HttpMethod.GET, tag, baseUrl + url, clazz, params, headers);
    }


    /**
     * POST请求
     *
     * @param url
     * */
    public static <T> Observable<T> postRequest(String url) {

        return request(HttpMethod.POST, null, baseUrl + url, baseType);
    }

    public static <T> Observable<T> postRequest(Object tag, String url) {

        return request(HttpMethod.POST, tag, baseUrl + url, baseType);
    }

    public static <T> Observable<T> postRequest(String url, Type type) {

        return request(HttpMethod.POST, null, baseUrl + url, type);
    }

    public static <T> Observable<T> postRequest(String url, Class<T> clazz) {
        return request(HttpMethod.POST, null, baseUrl + url, clazz);
    }

    public static <T> Observable<T> postRequest(Object tag, String url, Type type) {

        return request(HttpMethod.POST, tag, baseUrl + url, type);
    }

    public static <T> Observable<T> postRequest(Object tag, String url, Class<T> clazz) {

        return request(HttpMethod.POST, tag, baseUrl + url, clazz);
    }

    public static <T> Observable<T> postRequest(String url, HttpParams params) {

        return request(HttpMethod.POST, null, baseUrl + url, baseType, params);
    }

    public static <T> Observable<T> postRequest(Object tag, String url, HttpParams params) {

        return request(HttpMethod.POST, tag, baseUrl + url, baseType, params);
    }

    public static <T> Observable<T> postRequest(String url, Type type, HttpParams params) {

        return request(HttpMethod.POST, null, baseUrl + url, type, params);
    }

    public static <T> Observable<T> postRequest(String url, Class<T> clazz, HttpParams params) {
        return request(HttpMethod.POST, null, baseUrl + url, clazz, params);
    }

    public static <T> Observable<T> postRequest(Object tag, String url, Type type, HttpParams params) {

        return request(HttpMethod.POST, tag, baseUrl + url, type, params);
    }

    public static <T> Observable<T> postRequest(Object tag, String url, Class<T> clazz, HttpParams params) {

        return request(HttpMethod.POST, tag, baseUrl + url, clazz, params);
    }

    public static <T> Observable<T> postRequest(String url, HttpParams params, HttpHeaders headers) {

        return request(HttpMethod.POST, null, baseUrl + url, baseType, params, headers);
    }

    public static <T> Observable<T> postRequest(Object tag, String url, HttpParams params, HttpHeaders headers) {

        return request(HttpMethod.POST, tag, baseUrl + url, baseType, params, headers);
    }

    public static <T> Observable<T> postRequest(String url, Type type, HttpParams params, HttpHeaders headers) {

        return request(HttpMethod.POST, null, baseUrl + url, type, params, headers);
    }

    public static <T> Observable<T> postRequest(String url, Class<T> clazz, HttpParams params, HttpHeaders headers) {
        return request(HttpMethod.POST, null, baseUrl + url, clazz, params, headers);
    }

    /**
     * POST请求
     *
     * @param tag
     *          请求标签
     * @param url
     *          请求链接
     * @param type
     *          返回对象类型
     * @param params
     *          请求参数
     * @param headers
     *          请求头
     * */
    public static <T> Observable<T> postRequest(Object tag, String url, Type type, HttpParams params, HttpHeaders headers) {

        return request(HttpMethod.POST, tag, baseUrl + url, type, params, headers);
    }

    /**
     * POST请求
     *
     * @param tag
     *          请求标签
     * @param url
     *          请求链接
     * @param clazz
     *          返回对象类型
     * @param params
     *          请求参数
     * @param headers
     *          请求头
     * */
    public static <T> Observable<T> postRequest(Object tag, String url, Class<T> clazz, HttpParams params, HttpHeaders headers) {

        return request(HttpMethod.POST, tag, baseUrl + url, clazz, params, headers);
    }


    /**
     * 文件下载数据请求
     *
     * @param tag
     *          请求标签，方便区分是哪个请求在运行，以便取消请求
     * @param url
     *          请求地址
     * @param params
     *          请求参数
     * @param callback
     *          回调
     * */
    public static void downloadRequest(final Object tag, final String url, final HttpParams params, final DownloadCallback callback) {
        Observable.create(new ObservableOnSubscribe<Progress>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<Progress> e) throws Exception {
                OkGo.<File>get(baseUrl + url)   // 请求链接
                        .params(params) // 请求参数
                        .tag(tag)
                        .execute(new FileCallback() {
                            @Override
                            public void onSuccess(Response<File> response) {
                                callback.onSuccess(response.body());
                            }

                            @Override
                            public void onError(Response<File> response) {
                                e.onError(response.getException());
                            }

                            @Override
                            public void downloadProgress(Progress progress) {
                                e.onNext(progress);
                            }
                        });
            }
        })
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        callback.onAccept("正在下载中...");
                    }
                })//
                .observeOn(AndroidSchedulers.mainThread())//
                .subscribe(new Observer<Progress>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        // 添加到订阅管理，在activity的onDestroy里添加CompositeDisposableUtil.disposable();
                        CompositeDisposableUtil.addDisposable(d);
                    }

                    @Override
                    public void onNext(@NonNull Progress progress) {
                        Log.d("download", "progress = " + progress);
                        // 文件下载了多少
                        //String downloadLength = Formatter.formatFileSize(context, progress.currentSize);
                        // 文件大小
                        //String totalLength = Formatter.formatFileSize(context, progress.totalSize);
                        // 下载速度100 kb/s
                        //String speed = Formatter.formatFileSize(context, progress.speed);
                        //Log.e("download", "speed = " + String.format("%s/s", speed))
                        // 已下载的占总大小的百分比
                        //Log.e("download", "progress = " + NumberFormat.getPercentInstance().format(progress.fraction.toDouble()));
                        // 将max设为10000
                        //pbProgress.setMax(10000);
                        // 已下载的文件大小
                        // int progress = (int) (progress.fraction * 10000)

                        callback.onProgress(progress);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        callback.onError("下载出错");
                    }

                    @Override
                    public void onComplete() {
                        callback.onComplete("下载完成");
                    }
                });
    }

    /**
     * 文件上传
     *
     * @param tag
     *          请求标签，方便区分是哪个请求在运行，以便取消请求
     * @param url
     *          请求地址
     * @param params
     *          请求参数
     * @param files
     *          文件列表
     * @param callback
     *          回调
     * */
    public static void uploadRequest(final Object tag, final String url, final HttpParams params, final List<File> files, final UploadCallback callback) {
        Observable.create(new ObservableOnSubscribe<Progress>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<Progress> e) throws Exception {
                OkGo.<UploadFileBean>post(baseUrl + url)
                        .tag(tag)       // 请求标签
                        .params(params) // 请求参数
                        .addFileParams("file", files)   // 文件路径集合
                        .execute(new UploadFileCallback() {

                            @Override
                            public void onSuccess(Response<UploadFileBean> response) {
                                callback.onSuccess(response.body());
                            }

                            @Override
                            public void onError(Response<UploadFileBean> response) {
                                e.onError(response.getException());
                            }

                            @Override
                            public void uploadProgress(Progress progress) {
                                e.onNext(progress);
                            }
                        });
            }
        })
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        callback.onAccept("正在上传中...");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Progress>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        // 添加到订阅管理，在activity的onDestroy里添加CompositeDisposableUtil.disposable();
                        CompositeDisposableUtil.addDisposable(d);
                    }

                    @Override
                    public void onNext(@NonNull Progress progress) {
                        Log.d("upload", "progress = " + progress);
                        callback.onProgress(progress);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        callback.onError("上传出错");
                    }

                    @Override
                    public void onComplete() {
                        callback.onComplete("上传完成");
                    }
                });
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
            qc_app_token = TokenUtil.getToken();
        }
        params.put(APP_TOKEN_KEY, TokenUtil.getToken());

        return params;
    }

    public static void setToken(String token) {
        qc_app_token = token;
        Log.e("TOKEN", "token=" + qc_app_token);
    }
}
