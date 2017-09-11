package com.gbq.library.utils;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * 类说明：统一管理所有的订阅生命周期
 * Author: Kuzan
 * Date: 2017/7/31 20:20.
 */
public class CompositeDisposableUtil {
    private static CompositeDisposable compositeDisposable;

    public static void addDisposable(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    public static void dispose() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
    }

    public static void remove(Disposable disposable) {
        disposable.dispose();
    }
}
