package com.gbq.library.okgo.adapter;

/**
 * 类说明：返回值的适配器
 * Author: Kuzan
 * Date: 2017/5/25 14:02.
 */
public interface CallAdapter<T> {
    /**
     *  call执行的代理方法
     *  */
    <R> T adapt(Call<R> call);
}
