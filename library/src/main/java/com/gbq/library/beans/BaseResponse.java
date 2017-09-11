package com.gbq.library.beans;

import java.io.Serializable;

/**
 * 类说明：基本的请求响应数据
 * Author: Kuzan
 * Date: 2017/5/27 9:59.
 */
public class BaseResponse<T> implements Serializable {
    private int status;
    private String message;
    private String url;
    private T data;

    @Override
    public String toString() {
        return "BaseResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", url='" + url + '\'' +
                ", data=" + data +
                '}';
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
