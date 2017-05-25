package com.gbq.library.okgo.request;

import com.gbq.library.okgo.model.HttpParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 类说明：表示当前请求是否具有请求体
 * Author: Kuzan
 * Date: 2017/5/25 14:22.
 */
public interface HasBody<R> {

    R isMultipart(boolean isMultipart);

    R requestBody(RequestBody requestBody);

    R params(String key, File file);

    R addFileParams(String key, List<File> files);

    R addFileWrapperParams(String key, List<HttpParams.FileWrapper> fileWrappers);

    R params(String key, File file, String fileName);

    R params(String key, File file, String fileName, MediaType contentType);

    R upString(String string);

    R upJson(String json);

    R upJson(JSONObject jsonObject);

    R upJson(JSONArray jsonArray);

    R upBytes(byte[] bs);
}
