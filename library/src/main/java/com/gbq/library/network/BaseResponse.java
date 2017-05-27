package com.gbq.library.network;

import java.io.Serializable;

/**
 * 类说明：基本的请求响应数据
 * Author: Kuzan
 * Date: 2017/5/27 9:59.
 */
public class BaseResponse<T> implements Serializable {
    private int code;
    private String msg;
    private T data;

    @Override
    public String toString() {
        return "BaseResponseBean{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
