package com.gbq.library.callback;

import com.gbq.library.beans.UploadFileBean;
import com.lzy.okgo.model.Progress;

import io.reactivex.annotations.NonNull;

/**
 * 类说明：上传文件回调
 * Author: Kuzan
 * Date: 2017/9/7 19:00.
 */
public interface UploadCallback {
    /** 正在上传中... */
    void onAccept(String acceptStr);

    /** 上传进度 */
    void onProgress(@NonNull Progress progress);

    /** 上传出错 */
    void onError(String errMsg);

    /** 上传完成 */
    void onComplete(String completeMsg);

    /** 上传成功 */
    void onSuccess(UploadFileBean bean);
}
