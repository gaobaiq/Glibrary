package com.gbq.library.callback;

import com.lzy.okgo.model.Progress;

import java.io.File;

import io.reactivex.annotations.NonNull;

/**
 * 类说明：下载文件回调
 * Author: Kuzan
 * Date: 2017/7/31 17:26.
 */
public interface DownloadCallback {
    /** 正在下载中...*/
    void onAccept(String acceptStr);

    /** 下载进度 */
    void onProgress(@NonNull Progress progress);

    /** 下载出错 */
    void onError(String errMsg);

    /**下载完成 */
    void onComplete(String completeMsg);

    /**
     * 下载成功*/
    void onSuccess(File file);
}
