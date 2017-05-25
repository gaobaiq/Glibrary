package com.gbq.library.okgo.convert;

import okhttp3.Response;

/**
 * 类说明：字符串的转换器
 * Author: Kuzan
 * Date: 2017/5/25 14:45.
 */
public class StringConvert implements Converter<String> {
    public static StringConvert create() {
        return ConvertHolder.convert;
    }

    private static class ConvertHolder {
        private static StringConvert convert = new StringConvert();
    }

    @Override
    public String convertSuccess(Response value) throws Exception {
        return value.body().string();
    }
}
