package com.gbq.library.rxbus;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.reactivex.Scheduler;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.functions.ObjectHelper;

/**
 * 类说明：自定义Subscriber
 * Author: Kuzan
 * Date: 2017/8/12 9:39.
 */
public class CustomSubscriber<T> extends AbstractSubscriber<T> {
    private final int hashCode;

    private Class<T> eventClass;
    private Consumer<T> receiver;
    private Predicate<T> filter;
    private Scheduler scheduler;

    CustomSubscriber(@NonNull Class<T> eventClass, @NonNull Consumer<T> receiver) {
        this.eventClass = eventClass;
        this.receiver = receiver;

        hashCode = receiver.hashCode();
    }

    @SuppressWarnings("WeakerAccess")
    public CustomSubscriber<T> withFilter(@NonNull Predicate<T> filter) {
        ObjectHelper.requireNonNull(filter, "Filter must not be null.");
        this.filter = filter;
        return this;
    }

    @SuppressWarnings("WeakerAccess")
    public CustomSubscriber<T> withScheduler(@NonNull Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "Scheduler must not be null.");
        this.scheduler = scheduler;
        return this;
    }

    @NonNull
    Class<T> getEventClass() {
        return eventClass;
    }

    @Nullable
    Predicate<T> getFilter() {
        return filter;
    }

    @Nullable
    Scheduler getScheduler() {
        return scheduler;
    }

    @Override
    protected void acceptEvent(T event) throws Exception {
        receiver.accept(event);
    }

    @Override
    protected void release() {
        eventClass = null;
        receiver = null;
        filter = null;
        scheduler = null;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        CustomSubscriber<?> that = (CustomSubscriber<?>) other;

        return receiver.equals(that.receiver);
    }

    @Override
    public int hashCode() {
        return hashCode;
    }
}
