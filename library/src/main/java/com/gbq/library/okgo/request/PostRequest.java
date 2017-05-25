package com.gbq.library.okgo.request;

import com.gbq.library.okgo.model.HttpHeaders;
import com.gbq.library.okgo.utils.HttpUtils;
import com.gbq.library.okgo.utils.OkLogger;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 类说明：Post请求的实现类，注意需要传入本类的泛型
 * Author: Kuzan
 * Date: 2017/5/25 14:20.
 */
public class PostRequest extends BaseBodyRequest<PostRequest> {
    public PostRequest(String url) {
        super(url);
        method = "POST";
    }

    @Override
    public Request generateRequest(RequestBody requestBody) {
        try {
            headers.put(HttpHeaders.HEAD_KEY_CONTENT_LENGTH, String.valueOf(requestBody.contentLength()));
        } catch (IOException e) {
            OkLogger.e(e);
        }
        Request.Builder requestBuilder = HttpUtils.appendHeaders(headers);
        return requestBuilder.post(requestBody).url(url).tag(tag).build();
    }
}
