package com.gbq.library.okgo.callback;

import android.graphics.Bitmap;

import com.gbq.library.okgo.convert.BitmapConvert;

import okhttp3.Response;

/**
 * 类说明：返回图片的Bitmap，这里没有进行图片的缩放，可能会发生 OOM
 * Author: Kuzan
 * Date: 2017/5/25 14:40.
 */
public abstract class BitmapCallback extends AbsCallback<Bitmap> {
    @Override
    public Bitmap convertSuccess(Response response) throws Exception {
        Bitmap bitmap = BitmapConvert.create().convertSuccess(response);
        response.close();
        return bitmap;
    }
}