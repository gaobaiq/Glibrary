package com.gbq.library.utils;

import com.google.gson.Gson;

import java.util.Map;

/**
 * 类说明：Gson工具
 * Author: Kuzan
 * Date: 2017/9/6 16:31.
 */
public class GsonUtil {
    /**
     * 将Map转化为Json
     *
     * @param map
     * @return String
     */
    public static <T> String mapToJson(Map<String, T> map) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(map);
        return jsonStr;
    }
}
