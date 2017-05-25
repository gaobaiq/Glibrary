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
 * Date: 2017/5/25 14:31.
 */
public class DeleteRequest extends BaseBodyRequest<DeleteRequest> {

    public DeleteRequest(String url) {
        super(url);
        method = "DELETE";
    }

    @Override
    public Request generateRequest(RequestBody requestBody) {
        try {
            headers.put(HttpHeaders.HEAD_KEY_CONTENT_LENGTH, String.valueOf(requestBody.contentLength()));
        } catch (IOException e) {
            OkLogger.e(e);
        }
        Request.Builder requestBuilder = HttpUtils.appendHeaders(headers);
        return requestBuilder.delete(requestBody).url(url).tag(tag).build();
    }
}
