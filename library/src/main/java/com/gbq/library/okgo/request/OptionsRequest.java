package com.gbq.library.okgo.request;

import com.gbq.library.okgo.model.HttpHeaders;
import com.gbq.library.okgo.utils.HttpUtils;
import com.gbq.library.okgo.utils.OkLogger;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 类说明：
 * Author: Kuzan
 * Date: 2017/5/25 14:32.
 */
public class OptionsRequest extends BaseBodyRequest<OptionsRequest> {

    public OptionsRequest(String url) {
        super(url);
        method = "OPTIONS";
    }

    @Override
    public Request generateRequest(RequestBody requestBody) {
        try {
            headers.put(HttpHeaders.HEAD_KEY_CONTENT_LENGTH, String.valueOf(requestBody.contentLength()));
        } catch (IOException e) {
            OkLogger.e(e);
        }
        Request.Builder requestBuilder = HttpUtils.appendHeaders(headers);
        return requestBuilder.method("OPTIONS", requestBody).url(url).tag(tag).build();
    }
}
