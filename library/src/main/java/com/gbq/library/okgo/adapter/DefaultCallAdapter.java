package com.gbq.library.okgo.adapter;

/**
 * 类说明：默认的工厂处理,不对返回值做任何操作
 * Author: Kuzan
 * Date: 2017/5/25 14:01.
 */
public class DefaultCallAdapter<T> implements CallAdapter<Call<T>> {
    public static <T> DefaultCallAdapter<T> create() {
        return new DefaultCallAdapter<>();
    }

    @Override
    public <R> Call<T> adapt(Call<R> call) {
        return (Call<T>) call;
    }
}
