package com.gbq.library.rxbus;

import android.support.annotation.NonNull;

import java.lang.reflect.Method;

/**
 * 类说明：注释Subscriber
 * Author: Kuzan
 * Date: 2017/8/12 9:47.
 */
public class AnnotatedSubscriber<T> extends AbstractSubscriber<T> {
    private final int hashCode;

    private Object observer;
    private Method method;

    AnnotatedSubscriber(@NonNull Object observer, @NonNull Method method) {
        this.observer = observer;
        this.method = method;

        hashCode = 31 * observer.hashCode() + method.hashCode();
    }

    @Override
    protected void acceptEvent(T event) throws Exception {
        method.invoke(observer, event);
    }

    @Override
    protected void release() {
        observer = null;
        method = null;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        AnnotatedSubscriber<?> that = (AnnotatedSubscriber<?>) other;

        return observer.equals(that.observer) && method.equals(that.method);
    }

    @Override
    public int hashCode() {
        return hashCode;
    }
}
