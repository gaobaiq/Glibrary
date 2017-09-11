package com.gbq.library.rxbus;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 类说明：封装自定义Subscriber
 * Author: Kuzan
 * Date: 2017/8/12 9:39.
 */
public abstract class AbstractSubscriber<T> implements Consumer<T>, Disposable {
    private volatile boolean disposed;

    @Override
    public void accept(T event) {
        try {
            acceptEvent(event);
        } catch (Exception e) {
            throw new RuntimeException("Could not dispatch event: " + event.getClass(), e);
        }
    }

    @Override
    public void dispose() {
        if (!disposed) {
            disposed = true;
            release();
        }
    }

    @Override
    public boolean isDisposed() {
        return disposed;
    }

    protected abstract void acceptEvent(T event) throws Exception;

    protected abstract void release();
}
