package com.gbq.library.base;

/**
 * 类说明：Presenter基类
 * Author: Kuzan
 * Date: 2017/8/1 9:34.
 */
public abstract class BasePresenter<T> {
    public T mView;

    public void attach(T mView) {
        this.mView = mView;
    }

    public void detach() {
        mView = null;
    }
}
