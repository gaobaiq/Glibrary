package com.gbq.library.okgo.request;

import com.gbq.library.okgo.utils.HttpUtils;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 类说明：Get请求的实现类，注意需要传入本类的泛型
 * Author: Kuzan
 * Date: 2017/5/25 14:19.
 */
public class GetRequest extends BaseRequest<GetRequest> {

    public GetRequest(String url) {
        super(url);
        method = "GET";
    }

    @Override
    public RequestBody generateRequestBody() {
        return null;
    }

    @Override
    public Request generateRequest(RequestBody requestBody) {
        Request.Builder requestBuilder = HttpUtils.appendHeaders(headers);
        url = HttpUtils.createUrlFromParams(baseUrl, params.urlParamsMap);
        return requestBuilder.get().url(url).tag(tag).build();
    }
}