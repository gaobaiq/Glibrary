package com.gbq.library.network;

import android.util.Log;

import com.gbq.library.beans.BaseResponse;
import com.gbq.library.beans.RxBusEvent;
import com.gbq.library.callback.DataCallback;
import com.gbq.library.rxbus.BusProvider;
import com.gbq.library.utils.StringUtils;
import com.gbq.library.utils.TokenUtil;
import com.google.gson.JsonParseException;
import com.lzy.okgo.exception.HttpException;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 类说明：网络请求
 * Author: Kuzan
 * Date: 2017/7/1 14:15.
 */
public class BaseApi {
    private static final String TAG = BaseApi.class.getSimpleName();

    private static final int NOT_LOGIN_STATUS = 300; // 未登录状态，返回自定义异常代码
    private static final int ERROR_SERVICE = -599; // 接口错误
    private static final int SUCCESS_STATUS = 200;
    private static final int WRONG_SERVICE = 500;
    private static final int OTHER_ERROR = 404;
    private static final int NO_ROOT = 403;
    public static final int NOT_LOGIN_STATUS_TYPE = 300;

    /**
     * 网络请求数据回来后，对数据进行进一步的解析，获取我们真正需要的数据。
     *
     * @time 2017/7/1 14:16
     */
    public static <T> void dispose(Observable<BaseResponse<T>> observable, final DataCallback<T> callback) {
        observable.map(new Function<BaseResponse<T>, BaseResponse<T>>() {
            @Override
            public BaseResponse<T> apply(@NonNull BaseResponse<T> tBaseResponse) throws Exception {
                //Gson gson = new Gson();
                Log.e(TAG, "status = " + tBaseResponse.getStatus());
                Log.e(TAG, "message = " + tBaseResponse.getMessage());
                //Log.e(TAG, gson.toJson(tBaseResponse.getData()));
                return tBaseResponse;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<T>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        // TODO 这里需要添加到订阅管理，当关闭页面时取消订阅，由于这里数据处理是很快的，所以暂不添加
                        Log.e(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull BaseResponse<T> tBaseResponse) {
                        switch (tBaseResponse.getStatus()) {
                            case SUCCESS_STATUS:
                                callback.onSuccess(tBaseResponse.getData());
                                break;
                            case NOT_LOGIN_STATUS:
                            case NO_ROOT:
                                // 清空登录token
                                TokenUtil.clearToken();
                                // 抛登录异常
                                callback.onError(NO_ROOT, "您还未登录");
                                //发送未登录的全局通知。
                                BusProvider.getInstance().post(RxBusEvent.newBuilder(NOT_LOGIN_STATUS_TYPE).build());
                                break;
                            case ERROR_SERVICE:
                            case WRONG_SERVICE:
                                if (StringUtils.isEmptyString(tBaseResponse.getMessage())) {
                                    callback.onError(WRONG_SERVICE, "服务器请求出错");
                                } else {
                                    callback.onError(WRONG_SERVICE, tBaseResponse.getMessage());
                                }
                                break;
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "onError = " + e.getMessage());
                        Log.e(TAG, "onError = " + e.getClass().getName());
                        e.printStackTrace();

                        if (e instanceof IOException || e instanceof HttpException) {
                            callback.onError(WRONG_SERVICE, "网络请求失败");
                        } else if (e instanceof JsonParseException) {
                            callback.onError(OTHER_ERROR, "数据解析出错");
                        } else {
                            Log.e(TAG, "未知错误");
                            callback.onError(OTHER_ERROR, "未知错误");
                        }
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete");
                    }
                });


    }

    public static <T> void dispose2(Observable<T> observable, final DataCallback<T> callback) {
        observable.map(new Function<T, T>() {
            @Override
            public T apply(@NonNull T tBaseResponse) throws Exception {

                return tBaseResponse;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<T>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.e(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull T tBaseResponse) {
                        Log.e(TAG, "onNext");
                        callback.onSuccess(tBaseResponse);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "onError = " + e.getMessage());
                        Log.e(TAG, "onError = " + e.getClass().getName());
                        e.printStackTrace();

                        if (e instanceof IOException || e instanceof HttpException) {
                            callback.onError(WRONG_SERVICE, "网络请求失败");
                        } else if (e instanceof JsonParseException) {
                            callback.onError(OTHER_ERROR, "数据解析出错");
                        } else {
                            Log.e(TAG, "未知错误");
                            callback.onError(OTHER_ERROR, "未知错误");
                        }
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete");
                    }
                });


    }
}
