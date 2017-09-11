package com.gbq.library.rxbus;

import android.support.annotation.NonNull;

import io.reactivex.functions.Consumer;

/**
 * 类说明：事件
 * Author: Kuzan
 * Date: 2017/8/12 9:38.
 */
public interface Bus {
    void register(@NonNull Object observer);

    <T> CustomSubscriber<T> obtainSubscriber(@NonNull Class<T> eventClass, @NonNull Consumer<T> receiver);

    <T> void registerSubscriber(@NonNull Object observer, @NonNull CustomSubscriber<T> subscriber);

    void unregister(@NonNull Object observer);

    void post(@NonNull Object event);
}
