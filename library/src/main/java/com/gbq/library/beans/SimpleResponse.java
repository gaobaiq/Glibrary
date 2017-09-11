package com.gbq.library.beans;

import java.io.Serializable;

/**
 * 类说明：简单响应数据类型
 * Author: Kuzan
 * Date: 2017/5/27 10:05.
 */
public class SimpleResponse implements Serializable {
    private int code;
    private String msg;

    public BaseResponse toBaseResponse() {
        BaseResponse response = new BaseResponse();
        response.setStatus(code);
        response.setMessage(msg);
        return response;
    }

    @Override
    public String toString() {
        return "SimpleResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
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
}
