package com.gbq.library.callback;

import com.gbq.library.okgo.callback.AbsCallback;
import com.gbq.library.okgo.convert.FileConvert;

import java.io.File;

import okhttp3.Response;

/**
 * 类说明：文件的回调下载进度监听
 * Author: Kuzan
 * Date: 2017/5/27 13:51.
 */
public abstract class FileCallback extends AbsCallback<File> {
    private FileConvert convert;    //文件转换类

    public FileCallback() {
        this(null);
    }

    public FileCallback(String destFileName) {
        this(null, destFileName);
    }

    public FileCallback(String destFileDir, String destFileName) {
        convert = new FileConvert(destFileDir, destFileName);
        convert.setCallback(this);
    }

    @Override
    public File convertSuccess(Response response) throws Exception {
        File file = convert.convertSuccess(response);
        response.close();
        return file;
    }
}
