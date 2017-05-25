package com.gbq.library.okgo.cookie.store;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * 类说明：CookieStore 的公共接口
 * Author: Kuzan
 * Date: 2017/5/25 13:36.
 */
public interface CookieStore {
    /** 保存url对应所有cookie */
    void saveCookie(HttpUrl url, List<Cookie> cookie);

    /** 保存url对应所有cookie */
    void saveCookie(HttpUrl url, Cookie cookie);

    /** 加载url所有的cookie */
    List<Cookie> loadCookie(HttpUrl url);

    /** 获取当前所有保存的cookie */
    List<Cookie> getAllCookie();

    /** 获取当前url对应的所有的cookie */
    List<Cookie> getCookie(HttpUrl url);

    /** 根据url和cookie移除对应的cookie */
    boolean removeCookie(HttpUrl url, Cookie cookie);

    /** 根据url移除所有的cookie */
    boolean removeCookie(HttpUrl url);

    /** 移除所有的cookie */
    boolean removeAllCookie();
}
