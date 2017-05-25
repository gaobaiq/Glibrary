package com.gbq.library.okgo.adapter;

import com.gbq.library.okgo.callback.AbsCallback;
import com.gbq.library.okgo.model.Response;
import com.gbq.library.okgo.request.BaseRequest;

/**
 * 类说明：请求的包装类
 * Author: Kuzan
 * Date: 2017/5/25 14:00.
 */
public interface Call<T> {
    /**
     *  同步执行
     *  */
    Response<T> execute() throws Exception;

    /**
     * 异步回调执行
     * */
    void execute(AbsCallback<T> callback);

    /**
     * 是否已经执行
     * */
    boolean isExecuted();

    /**
     * 取消
     * */
    void cancel();

    /**
     * 是否取消
     * */
    boolean isCanceled();

    Call<T> clone();

    BaseRequest getBaseRequest();
}
