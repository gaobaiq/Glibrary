package com.gbq.library.callback;

import com.gbq.library.beans.BaseResponse;
import com.gbq.library.beans.UploadFileBean;
import com.gbq.library.network.JsonConvert;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.callback.AbsCallback;

import java.lang.reflect.Type;

import okhttp3.Response;

/**
 * 类说明：上传文件回调
 *          这个是根据不同公司框架定义的
 * Author: Kuzan
 * Date: 2017/9/7 16:41.
 */
public abstract class UploadFileCallback extends AbsCallback<UploadFileBean> {
    private JsonConvert<BaseResponse<UploadFileBean>> convert;

    public UploadFileCallback() {
        Type type = new TypeToken<BaseResponse<UploadFileBean>>(){}.getType();
        convert = new JsonConvert<>(type);
    }

    @Override
    public UploadFileBean convertResponse(Response response) throws Throwable {
        BaseResponse<UploadFileBean> bean = convert.convertResponse(response);
        response.close();
        if (bean != null) {
            if (bean.getStatus() == 200) {
                return bean.getData();
            } else {
                throw new IllegalStateException(bean.getMessage()==null?"上传文件失败":bean.getMessage());
            }
        } else {
            throw new IllegalStateException("上传文件失败");
        }
    }
}
