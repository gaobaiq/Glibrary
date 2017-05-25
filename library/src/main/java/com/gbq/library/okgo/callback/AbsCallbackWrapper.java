package com.gbq.library.okgo.callback;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 类说明：所有回调的包装类,空实现
 * Author: Kuzan
 * Date: 2017/5/25 14:06.
 */
public class AbsCallbackWrapper<T> extends AbsCallback<T> {
    @Override
    public T convertSuccess(Response value) throws Exception {
        value.close();
        return (T) value;
    }

    @Override
    public void onSuccess(T t, Call call, Response response) {
    }
}
