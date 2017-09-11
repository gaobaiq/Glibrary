package com.gbq.library.callback;

/**
 * 类说明：数据解析回调
 * Author: Kuzan
 * Date: 2017/6/19 11:36.
 */
public interface DataCallback<T> {
    void onSuccess(T t);

    void onError(int status, String errMsg);
}
