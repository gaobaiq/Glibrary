package com.gbq.library.okgo.model;

import okhttp3.Headers;

/**
 * 类说明：响应体的包装类
 * Author: Kuzan
 * Date: 2017/5/25 11:34.
 */
public final class Response<T> {
    private final okhttp3.Response rawResponse;
    private final T body;

    private Response(okhttp3.Response rawResponse, T body) {
        this.rawResponse = rawResponse;
        this.body = body;
    }

    public okhttp3.Response raw() {
        return rawResponse;
    }

    public int code() {
        return rawResponse.code();
    }

    public String message() {
        return rawResponse.message();
    }

    public Headers headers() {
        return rawResponse.headers();
    }

    public boolean isSuccessful() {
        return rawResponse.isSuccessful();
    }

    public T body() {
        return body;
    }

    public static <T> Response<T> success(T body, okhttp3.Response rawResponse) {
        if (rawResponse == null) throw new NullPointerException("rawResponse == null");
        if (!rawResponse.isSuccessful()) throw new IllegalArgumentException("rawResponse must be successful response");
        return new Response<>(rawResponse, body);
    }
}
