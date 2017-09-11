package com.gbq.library.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.view.View;

/**
 * 类说明：高斯模糊背景
 * Author: Kuzan
 * Date: 2017/8/30 13:44.
 */
public class BlurBehindUtil {
    private static final String KEY_CACHE_BLURRED_BACKGROUND_IMAGE = "KEY_CACHE_BLURRED_BACKGROUND_IMAGE";
    private static final int CONSTANT_BLUR_RADIUS = 12;
    private static final int CONSTANT_DEFAULT_ALPHA = 100;

    private static final LruCache<String, Bitmap> mImageCache = new LruCache<String, Bitmap>(1);
    private static CacheBlurBehindAndExecuteTask cacheBlurBehindAndExecuteTask;

    private int mAlpha = CONSTANT_DEFAULT_ALPHA;
    private int mFilterColor = -1;

    private enum State {
        READY,
        EXECUTING
    }

    private State mState = State.READY;

    private static BlurBehindUtil mInstance;

    public static BlurBehindUtil getInstance() {
        if (mInstance == null) {
            mInstance = new BlurBehindUtil();
        }
        return mInstance;
    }

    public void execute(Activity activity, OnBlurCompleteListener onBlurCompleteListener) {
        if (mState.equals(State.READY)) {
            mState = State.EXECUTING;
            cacheBlurBehindAndExecuteTask = new CacheBlurBehindAndExecuteTask(activity, onBlurCompleteListener);
            cacheBlurBehindAndExecuteTask.execute();
        }
    }

    public BlurBehindUtil withAlpha(int alpha) {
        this.mAlpha = alpha;
        return this;
    }

    public BlurBehindUtil withFilterColor(int filterColor) {
        this.mFilterColor = filterColor;
        return this;
    }

    public void setBackground(Activity activity) {
        if (mImageCache.size() != 0) {
            BitmapDrawable bd = new BitmapDrawable(activity.getResources(), mImageCache.get(KEY_CACHE_BLURRED_BACKGROUND_IMAGE));
            bd.setAlpha(mAlpha);
            if (mFilterColor != -1) {
                bd.setColorFilter(mFilterColor, PorterDuff.Mode.DST_ATOP);
            }
            activity.getWindow().setBackgroundDrawable(bd);
            mImageCache.remove(KEY_CACHE_BLURRED_BACKGROUND_IMAGE);
            cacheBlurBehindAndExecuteTask = null;
        }
    }

    private class CacheBlurBehindAndExecuteTask extends AsyncTask<Void, Void, Void> {
        private Activity activity;
        private OnBlurCompleteListener onBlurCompleteListener;

        private View decorView;
        private Bitmap image;

        public CacheBlurBehindAndExecuteTask(Activity activity, OnBlurCompleteListener onBlurCompleteListener) {
            this.activity = activity;
            this.onBlurCompleteListener = onBlurCompleteListener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            decorView = activity.getWindow().getDecorView();
            decorView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
            decorView.setDrawingCacheEnabled(true);
            decorView.buildDrawingCache();

            image = decorView.getDrawingCache();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Bitmap blurredBitmap = BlurUtil.apply(activity, image, CONSTANT_BLUR_RADIUS);
            mImageCache.put(KEY_CACHE_BLURRED_BACKGROUND_IMAGE, blurredBitmap);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            decorView.destroyDrawingCache();
            decorView.setDrawingCacheEnabled(false);

            activity = null;

            onBlurCompleteListener.onBlurComplete();

            mState = State.READY;
        }
    }

    /**
     * 回调
     * */
    public interface OnBlurCompleteListener {
        void onBlurComplete();
    }
}
