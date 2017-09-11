package com.gbq.library.network;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpMethod;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.request.base.Request;
import com.lzy.okrx2.adapter.ObservableBody;

import java.lang.reflect.Type;

import io.reactivex.Observable;

/**
 * 类说明：Rx请求数据
 * Author: Kuzan
 * Date: 2017/7/1 15:31.
 */
public class RxRequest {

    public static <T> Observable<T> request(HttpMethod method, Object tag, String url, Type type) {
        return request(method, tag, url, type, null);
    }

    public static <T> Observable<T> request(HttpMethod method, Object tag, String url, Type type, HttpParams params) {
        return request(method, tag, url, type, params, null);
    }

    public static <T> Observable<T> request(HttpMethod method, Object tag, String url, Type type, HttpParams params, HttpHeaders headers) {
        return request(method, tag, url, type, null, params, headers);
    }

    public static <T> Observable<T> request(HttpMethod method, Object tag, String url, Class<T> clazz) {
        return request(method, tag, url, clazz, null);
    }

    public static <T> Observable<T> request(HttpMethod method, Object tag, String url, Class<T> clazz, HttpParams params) {
        return request(method, tag, url, clazz, params, null);
    }

    public static <T> Observable<T> request(HttpMethod method, Object tag, String url, Class<T> clazz, HttpParams params, HttpHeaders headers) {
        return request(method, tag, url, null, clazz, params, headers);
    }

    /**
     * 网络请求
     *
     * @param method
     *          请求方法
     * @param tag
     *          请求标签
     * @param url
     *          请求链接
     * @param type
     *          返回数据对象的类型
     * @param clazz
     *          返回数据对象有class类型
     * @param params
     *          请求参数
     * @param headers
     *          请求头
     *
     * @time 2017/7/1 16:03
     */
    public static <T> Observable<T> request(HttpMethod method, Object tag, String url, Type type, Class<T> clazz, HttpParams params, HttpHeaders headers) {
        Request<T, ? extends Request> request;
        if (method == HttpMethod.GET) request = OkGo.get(url);
        else if (method == HttpMethod.POST) request = OkGo.post(url);
        else if (method == HttpMethod.PUT) request = OkGo.put(url);
        else if (method == HttpMethod.DELETE) request = OkGo.delete(url);
        else if (method == HttpMethod.HEAD) request = OkGo.head(url);
        else if (method == HttpMethod.PATCH) request = OkGo.patch(url);
        else if (method == HttpMethod.OPTIONS) request = OkGo.options(url);
        else if (method == HttpMethod.TRACE) request = OkGo.trace(url);
        else request = OkGo.get(url);

        if (tag != null) request.tag(tag);

        request.headers(headers);
        request.params(params);
        if (type != null) {
            request.converter(new JsonConvert<T>(type));
        } else if (clazz != null) {
            request.converter(new JsonConvert<T>(clazz));
        } else {
            request.converter(new JsonConvert<T>());
        }
        return request.adapt(new ObservableBody<T>());
    }
}
