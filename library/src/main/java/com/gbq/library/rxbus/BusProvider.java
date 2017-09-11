package com.gbq.library.rxbus;

/**
 * 类说明：
 * Author: Kuzan
 * Date: 2017/8/12 9:48.
 */
public final class BusProvider {
    private BusProvider() {
    }

    public static Bus getInstance() {
        return BusHolder.INSTANCE;
    }

    private static final class BusHolder {
        final static Bus INSTANCE = new RxBus();
    }
}
