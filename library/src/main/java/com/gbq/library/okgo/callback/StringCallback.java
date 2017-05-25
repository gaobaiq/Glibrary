package com.gbq.library.okgo.callback;

import com.gbq.library.okgo.convert.StringConvert;

import okhttp3.Response;

/**
 * 类说明：返回字符串类型的数据
 * Author: Kuzan
 * Date: 2017/5/25 14:44.
 */
public abstract class StringCallback extends AbsCallback<String> {

    @Override
    public String convertSuccess(Response response) throws Exception {
        String s = StringConvert.create().convertSuccess(response);
        response.close();
        return s;
    }
}
