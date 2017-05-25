package com.gbq.library.okgo.exception;

/**
 * 类说明：异常封装
 * Author: Kuzan
 * Date: 2017/5/25 14:17.
 */
public class OkGoException extends Exception {
    public static OkGoException INSTANCE(String msg) {
        return new OkGoException(msg);
    }

    public OkGoException() {
        super();
    }

    public OkGoException(String detailMessage) {
        super(detailMessage);
    }

    public OkGoException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public OkGoException(Throwable throwable) {
        super(throwable);
    }
}
