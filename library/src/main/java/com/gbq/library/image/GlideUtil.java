package com.gbq.library.image;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.io.File;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * 类说明：Glide图片加载
 * Author: Kuzan
 * Date: 2017/5/25 14:58.
 */
public class GlideUtil {
    private static final String TAG = GlideUtil.class.getSimpleName();

    /**
     * @param context
     * @param imageView 图片控件
     * @param url       图片地址
     */
    public static void loadImage(Context context, ImageView imageView, String url) {
        loadImage(Glide.with(context), imageView, url, 0, 0, 0, false, false);
    }

    /**
     * @param activity
     * @param imageView 图片控件
     * @param url       图片地址
     */
    public static void loadImage(Activity activity, ImageView imageView, String url) {
        loadImage(Glide.with(activity), imageView, url, 0, 0, 0, false, false);
    }

    /**
     * @param fragment
     * @param imageView 图片控件
     * @param url       图片地址
     */
    public static void loadImage(Fragment fragment, ImageView imageView, String url) {
        loadImage(Glide.with(fragment), imageView, url, 0, 0, 0, false, false);
    }

    /**
     * @param fragment
     * @param imageView 图片控件
     * @param url       图片地址
     */
    public static void loadImage(android.app.Fragment fragment, ImageView imageView, String url) {
        loadImage(Glide.with(fragment), imageView, url, 0, 0, 0, false, false);
    }

    /**
     * @param activity
     * @param imageView 图片控件
     * @param url       图片地址
     */
    public static void loadImage(FragmentActivity activity, ImageView imageView, String url) {
        loadImage(Glide.with(activity), imageView, url, 0, 0, 0, false, false);
    }

    /**
     * @param context
     * @param imageView 图片控件
     * @param url       图片地址
     * @param isNoCache 是否跳过内存缓存
     */
    public static void loadImage(Context context, ImageView imageView, String url, boolean isNoCache) {
        loadImage(Glide.with(context), imageView, url, 0, 0, 0, false, isNoCache);
    }

    /**
     * @param activity
     * @param imageView 图片控件
     * @param url       图片地址
     * @param isNoCache 是否跳过内存缓存
     */
    public static void loadImage(Activity activity, ImageView imageView, String url, boolean isNoCache) {
        loadImage(Glide.with(activity), imageView, url, 0, 0, 0, false, isNoCache);
    }

    /**
     * @param fragment
     * @param imageView 图片控件
     * @param url       图片地址
     * @param isNoCache 是否跳过内存缓存
     */
    public static void loadImage(Fragment fragment, ImageView imageView, String url, boolean isNoCache) {
        loadImage(Glide.with(fragment), imageView, url, 0, 0, 0, false, isNoCache);
    }

    /**
     * @param fragment
     * @param imageView 图片控件
     * @param url       图片地址
     * @param isNoCache 是否跳过内存缓存
     */
    public static void loadImage(android.app.Fragment fragment, ImageView imageView, String url, boolean isNoCache) {
        loadImage(Glide.with(fragment), imageView, url, 0, 0, 0, false, isNoCache);
    }

    /**
     * @param activity
     * @param imageView 图片控件
     * @param url       图片地址
     * @param isNoCache 是否跳过内存缓存
     */
    public static void loadImage(FragmentActivity activity, ImageView imageView, String url, boolean isNoCache) {
        loadImage(Glide.with(activity), imageView, url, 0, 0, 0, false, isNoCache);
    }

    /**
     * @param context
     * @param imageView 图片控件
     * @param url       图片地址
     * @param error     加载失败显示的图片
     * @param isPlace   是否显示加载前的图片
     * @param isNoCache 是否跳过内存缓存
     */
    public static void loadImage(Context context, ImageView imageView, String url, int error, boolean isPlace, boolean isNoCache) {
        loadImage(Glide.with(context), imageView, url, error, 0, 0, isPlace, isNoCache);
    }

    /**
     * @param activity
     * @param imageView 图片控件
     * @param url       图片地址
     * @param error     加载失败显示的图片
     * @param isPlace   是否显示加载前的图片
     * @param isNoCache 是否跳过内存缓存
     */
    public static void loadImage(Activity activity, ImageView imageView, String url, int error, boolean isPlace, boolean isNoCache) {
        loadImage(Glide.with(activity), imageView, url, error, 0, 0, isPlace, isNoCache);
    }

    /**
     * @param fragment
     * @param imageView 图片控件
     * @param url       图片地址
     * @param error     加载失败显示的图片
     * @param isPlace   是否显示加载前的图片
     */
    public static void loadImage(Fragment fragment, ImageView imageView, String url, int error, boolean isPlace) {
        loadImage(Glide.with(fragment), imageView, url, error, 0, 0, isPlace, false);
    }

    /**
     * @param fragment
     * @param imageView 图片控件
     * @param url       图片地址
     * @param error     加载失败显示的图片
     * @param isPlace   是否显示加载前的图片
     */
    public static void loadImage(android.app.Fragment fragment, ImageView imageView, String url, int error, boolean isPlace) {
        loadImage(Glide.with(fragment), imageView, url, error, 0, 0, isPlace, false);
    }

    /**
     * @param activity
     * @param imageView 图片控件
     * @param url       图片地址
     * @param error     加载失败显示的图片
     * @param isPlace   是否显示加载前的图片
     */
    public static void loadImage(FragmentActivity activity, ImageView imageView, String url, int error, boolean isPlace) {
        loadImage(Glide.with(activity), imageView, url, error, 0, 0, isPlace, false);
    }

    /**
     * @param context
     * @param imageView 图片控件
     * @param url       图片地址
     * @param error     加载失败显示的图片
     * @param isPlace   是否显示加载前的图片
     */
    public static void loadImage(Context context, ImageView imageView, String url, int error, boolean isPlace) {
        loadImage(Glide.with(context), imageView, url, error, 0, 0, isPlace, false);
    }

    /**
     * @param activity
     * @param imageView 图片控件
     * @param url       图片地址
     * @param error     加载失败显示的图片
     * @param isPlace   是否显示加载前的图片
     */
    public static void loadImage(Activity activity, ImageView imageView, String url, int error, boolean isPlace) {
        loadImage(Glide.with(activity), imageView, url, error, 0, 0, isPlace, false);
    }

    /**
     * @param fragment
     * @param imageView 图片控件
     * @param url       图片地址
     * @param error     加载失败显示的图片
     * @param isPlace   是否显示加载前的图片
     * @param isNoCache 是否跳过内存缓存
     */
    public static void loadImage(Fragment fragment, ImageView imageView, String url, int error, boolean isPlace, boolean isNoCache) {
        loadImage(Glide.with(fragment), imageView, url, error, 0, 0, isPlace, isNoCache);
    }

    /**
     * @param fragment
     * @param imageView 图片控件
     * @param url       图片地址
     * @param error     加载失败显示的图片
     * @param isPlace   是否显示加载前的图片
     * @param isNoCache 是否跳过内存缓存
     */
    public static void loadImage(android.app.Fragment fragment, ImageView imageView, String url, int error, boolean isPlace, boolean isNoCache) {
        loadImage(Glide.with(fragment), imageView, url, error, 0, 0, isPlace, isNoCache);
    }

    /**
     * @param activity
     * @param imageView 图片控件
     * @param url       图片地址
     * @param error     加载失败显示的图片
     * @param isPlace   是否显示加载前的图片
     * @param isNoCache 是否跳过内存缓存
     */
    public static void loadImage(FragmentActivity activity, ImageView imageView, String url, int error, boolean isPlace, boolean isNoCache) {
        loadImage(Glide.with(activity), imageView, url, error, 0, 0, isPlace, isNoCache);
    }

    /**
     * @param context
     * @param imageView 图片控件
     * @param url       图片地址
     * @param error     加载失败显示的图片
     * @param width     图片宽度
     * @param height    图片高度
     * @param isPlace   是否显示加载前的图片
     * @param isNoCache 是否跳过内存缓存
     */
    public static void loadImage(Context context, ImageView imageView, String url, int error, int width,
                                 int height, boolean isPlace, boolean isNoCache) {
        loadImage(Glide.with(context), imageView, url, error, width, height, isPlace, isNoCache);
    }

    /**
     * @param activity
     * @param imageView 图片控件
     * @param url       图片地址
     * @param error     加载失败显示的图片
     * @param width     图片宽度
     * @param height    图片高度
     * @param isPlace   是否显示加载前的图片
     * @param isNoCache 是否跳过内存缓存
     */
    public static void loadImage(Activity activity, ImageView imageView, String url, int error, int width,
                                 int height, boolean isPlace, boolean isNoCache) {
        loadImage(Glide.with(activity), imageView, url, error, width, height, isPlace, isNoCache);
    }

    /**
     * @param fragment
     * @param imageView 图片控件
     * @param url       图片地址
     * @param error     加载失败显示的图片
     * @param width     图片宽度
     * @param height    图片高度
     * @param isPlace   是否显示加载前的图片
     * @param isNoCache 是否跳过内存缓存
     */
    public static void loadImage(Fragment fragment, ImageView imageView, String url, int error, int width,
                                 int height, boolean isPlace, boolean isNoCache) {
        loadImage(Glide.with(fragment), imageView, url, error, width, height, isPlace, isNoCache);
    }

    /**
     * @param fragment
     * @param imageView 图片控件
     * @param url       图片地址
     * @param error     加载失败显示的图片
     * @param width     图片宽度
     * @param height    图片高度
     * @param isPlace   是否显示加载前的图片
     * @param isNoCache 是否跳过内存缓存
     */
    public static void loadImage(android.app.Fragment fragment, ImageView imageView, String url, int error, int width,
                                 int height, boolean isPlace, boolean isNoCache) {
        loadImage(Glide.with(fragment), imageView, url, error, width, height, isPlace, isNoCache);
    }

    /**
     * @param activity
     * @param imageView 图片控件
     * @param url       图片地址
     * @param error     加载失败显示的图片
     * @param width     图片宽度
     * @param height    图片高度
     * @param isPlace   是否显示加载前的图片
     * @param isNoCache 是否跳过内存缓存
     */
    public static void loadImage(FragmentActivity activity, ImageView imageView, String url, int error, int width,
                                 int height, boolean isPlace, boolean isNoCache) {
        loadImage(Glide.with(activity), imageView, url, error, width, height, isPlace, isNoCache);
    }

    /**
     * @param manager
     * @param imageView 图片控件
     * @param url       图片地址
     * @param error     加载失败显示的图片
     * @param width     图片宽度
     * @param height    图片高度
     * @param isPlace   是否显示加载前的图片
     * @param isNoCache 是否跳过内存缓存
     */
    private static void loadImage(RequestManager manager, ImageView imageView, String url, int error, int width,
                                  int height, boolean isPlace, boolean isNoCache, Transformation<Bitmap>... bitmapTransformations) {
        if (!TextUtils.isEmpty(url)) {
            DrawableTypeRequest request = manager.load(url);
            loadImage(request, imageView, error, width, height, isPlace, isNoCache, bitmapTransformations);
        } else {
            if (error != 0) {
                imageView.setImageResource(error);
            }
            Log.e(TAG, "load img failure, url is empty");
        }
    }

    /**
     * @param manager
     * @param imageView 图片控件
     * @param bytes     图片byte数组
     * @param error     加载失败显示的图片
     * @param width     图片宽度
     * @param height    图片高度
     * @param isPlace   是否显示加载前的图片
     * @param isNoCache 是否跳过内存缓存
     */
    private static void loadImage(RequestManager manager, ImageView imageView, byte[] bytes, int error, int width,
                                  int height, boolean isPlace, boolean isNoCache, Transformation<Bitmap>... bitmapTransformations) {
        if (bytes != null && bytes.length > 0) {
            DrawableTypeRequest request = manager.load(bytes);
            loadImage(request, imageView, error, width, height, isPlace, isNoCache, bitmapTransformations);
        } else {
            if (error != 0) {
                imageView.setImageResource(error);
            }
            Log.e(TAG, "load img failure, url is empty");
        }
    }

    /**
     * @param context
     * @param imageView 图片控件
     * @param filePath  图片地址
     * @param error     加载失败显示的图片
     * @param width     图片宽度
     * @param height    图片高度
     * @param isPlace   是否显示加载前的图片
     * @param isNoCache 是否跳过内存缓存
     */
    public static void loadImageForFile(Context context, ImageView imageView, String filePath, int error, int width,
                                        int height, boolean isPlace, boolean isNoCache) {
        loadImageForFile(Glide.with(context), imageView, filePath, error, width, height, isPlace, isNoCache);
    }

    /**
     * @param activity
     * @param imageView 图片控件
     * @param filePath  图片地址
     * @param error     加载失败显示的图片
     * @param width     图片宽度
     * @param height    图片高度
     * @param isPlace   是否显示加载前的图片
     * @param isNoCache 是否跳过内存缓存
     */
    public static void loadImageForFile(Activity activity, ImageView imageView, String filePath, int error, int width,
                                        int height, boolean isPlace, boolean isNoCache) {
        loadImageForFile(Glide.with(activity), imageView, filePath, error, width, height, isPlace, isNoCache);
    }

    /**
     * @param fragment
     * @param imageView 图片控件
     * @param filePath  图片地址
     * @param error     加载失败显示的图片
     * @param width     图片宽度
     * @param height    图片高度
     * @param isPlace   是否显示加载前的图片
     * @param isNoCache 是否跳过内存缓存
     */
    public static void loadImageForFile(Fragment fragment, ImageView imageView, String filePath, int error, int width,
                                        int height, boolean isPlace, boolean isNoCache) {
        loadImageForFile(Glide.with(fragment), imageView, filePath, error, width, height, isPlace, isNoCache);
    }

    /**
     * @param fragment
     * @param imageView 图片控件
     * @param filePath  图片地址
     * @param error     加载失败显示的图片
     * @param width     图片宽度
     * @param height    图片高度
     * @param isPlace   是否显示加载前的图片
     * @param isNoCache 是否跳过内存缓存
     */
    public static void loadImageForFile(android.app.Fragment fragment, ImageView imageView, String filePath, int error, int width,
                                        int height, boolean isPlace, boolean isNoCache) {
        loadImageForFile(Glide.with(fragment), imageView, filePath, error, width, height, isPlace, isNoCache);
    }

    /**
     * @param activity
     * @param imageView 图片控件
     * @param filePath  图片地址
     * @param error     加载失败显示的图片
     * @param width     图片宽度
     * @param height    图片高度
     * @param isPlace   是否显示加载前的图片
     * @param isNoCache 是否跳过内存缓存
     */
    public static void loadImageForFile(FragmentActivity activity, ImageView imageView, String filePath, int error, int width,
                                        int height, boolean isPlace, boolean isNoCache) {
        loadImageForFile(Glide.with(activity), imageView, filePath, error, width, height, isPlace, isNoCache);
    }

    /**
     * @param manager
     * @param imageView 图片控件
     * @param filePath  图片地址
     * @param error     加载失败显示的图片
     * @param width     图片宽度
     * @param height    图片高度
     * @param isPlace   是否显示加载前的图片
     * @param isNoCache 是否跳过内存缓存
     */
    private static void loadImageForFile(RequestManager manager, ImageView imageView, String filePath, int error, int width,
                                         int height, boolean isPlace, boolean isNoCache, Transformation<Bitmap>... bitmapTransformations) {
        if (!TextUtils.isEmpty(filePath)) {
            DrawableTypeRequest request = manager.load(new File(filePath));
            loadImage(request, imageView, error, width, height, isPlace, isNoCache, bitmapTransformations);
        } else {
            if (error != 0) {
                imageView.setImageResource(error);
            }
            Log.e(TAG, "load img failure, filePath is empty");
        }
    }

    /**
     * @param activity
     * @param imageView 图片控件
     * @param res       图片资源
     * @param error     加载失败显示的图片
     * @param width     图片宽度
     * @param height    图片高度
     * @param isPlace   是否显示加载前的图片
     * @param isNoCache 是否跳过内存缓存
     */
    public static void loadImageForRes(Activity activity, ImageView imageView,
                                       int res, int error, int width, int height,
                                       boolean isPlace, boolean isNoCache) {
        loadImage(Glide.with(activity), imageView, res, error, width, height, isPlace, isNoCache);
    }

    /**
     * @param context
     * @param imageView 图片控件
     * @param res       图片资源
     * @param error     加载失败显示的图片
     * @param isPlace   是否显示加载前的图片
     * @param isNoCache 是否跳过内存缓存
     */
    public static void loadImageForRes(Context context, ImageView imageView,
                                       int res, int error, int width, int height,
                                       boolean isPlace, boolean isNoCache) {
        loadImage(Glide.with(context), imageView, res, error, width, height, isPlace, isNoCache);
    }

    /**
     * @param manager
     * @param imageView 图片控件
     * @param res       图片资源
     * @param error     加载失败显示的图片
     * @param width     图片宽度
     * @param height    图片高度
     * @param isPlace   是否显示加载前的图片
     * @param isNoCache 是否跳过内存缓存
     */
    private static void loadImage(RequestManager manager, ImageView imageView, int res, int error, int width,
                                  int height, boolean isPlace, boolean isNoCache, Transformation<Bitmap>... bitmapTransformations) {
        if (res > 0) {
            DrawableTypeRequest request = manager.load(res);
            loadImage(request, imageView, error, width, height, isPlace, isNoCache, bitmapTransformations);
        } else {
            if (error != 0) {
                imageView.setImageResource(error);
            }
            Log.e(TAG, "load img failure, url is empty");
        }
    }

    /**
     * @param context
     * @param imageView 图片控件
     * @param url       图片地址
     * @param error     加载失败显示的图片
     * @param width     图片宽度
     * @param height    图片高度
     * @param isPlace   是否显示加载前的图片
     * @param isNoCache 是否跳过内存缓存
     */
    public static void loadCircleImage(Context context, ImageView imageView, String url, int error, int width,
                                       int height, boolean isPlace, boolean isNoCache) {
        if (!TextUtils.isEmpty(url)) {
            loadImage(Glide.with(context), imageView, url, error, width, height, isPlace, isNoCache, new CropCircleTransformation(context));
        } else if (error != 0) {
            Glide.with(context).load(error).bitmapTransform(new CropCircleTransformation(context)).into(imageView);
        }
    }

    /**
     * @param context
     * @param imageView 图片控件
     * @param filePath  图片地址
     * @param error     加载失败显示的图片
     * @param width     图片宽度
     * @param height    图片高度
     * @param isPlace   是否显示加载前的图片
     * @param isNoCache 是否跳过内存缓存
     */
    public static void loadCircleImageForFile(Context context, ImageView imageView, String filePath, int error, int width,
                                              int height, boolean isPlace, boolean isNoCache) {
        if (!TextUtils.isEmpty(filePath)) {
            loadImageForFile(Glide.with(context), imageView, filePath, error, width, height, isPlace, isNoCache,
                    new CropCircleTransformation(context));
        } else if (error != 0) {
            Glide.with(context).load(error).bitmapTransform(new CropCircleTransformation(context)).into(imageView);
        }
    }

    /**
     * @param context
     * @param imageView 图片控件
     * @param url       图片地址
     * @param error     加载失败显示的图片
     * @param width     图片宽度
     * @param height    图片高度
     * @param isPlace   是否显示加载前的图片
     * @param isNoCache 是否跳过内存缓存
     * @param radius    圆角角度
     */
    public static void loadRoundedImage(Context context, ImageView imageView, String url, int error, int width,
                                        int height, boolean isPlace, boolean isNoCache, int radius) {
        if (!TextUtils.isEmpty(url)) {
            loadImage(Glide.with(context), imageView, url, error, width, height, isPlace, isNoCache,
                    new RoundedCornersTransformation(context, radius, 0));
        } else if (error != 0) {
            Glide.with(context).load(error).bitmapTransform(new RoundedCornersTransformation(context, radius, 0)).into(imageView);
        }
    }

    /**
     * @param context
     * @param imageView 图片控件
     * @param bytes     图片byte数组
     * @param error     加载失败显示的图片
     * @param width     图片宽度
     * @param height    图片高度
     * @param isPlace   是否显示加载前的图片
     * @param isNoCache 是否跳过内存缓存
     * @param radius    圆角角度
     */
    public static void loadRoundedImage(Context context, ImageView imageView, byte[] bytes, int error, int width,
                                        int height, boolean isPlace, boolean isNoCache, int radius) {
        if (bytes != null && bytes.length > 0) {
            loadImage(Glide.with(context), imageView, bytes, error, width, height, isPlace, isNoCache,
                    new RoundedCornersTransformation(context, radius, 0));
        } else if (error != 0) {
            Glide.with(context).load(error).bitmapTransform(new RoundedCornersTransformation(context, radius, 0)).into(imageView);
        }
    }

    /**
     * @param context
     * @param imageView 图片控件
     * @param filePath  图片地址
     * @param error     加载失败显示的图片
     * @param width     图片宽度
     * @param height    图片高度
     * @param isPlace   是否显示加载前的图片
     * @param isNoCache 是否跳过内存缓存
     * @param radius    圆角角度
     */
    public static void loadRoundedImageForFile(Context context, ImageView imageView, String filePath, int error, int width,
                                               int height, boolean isPlace, boolean isNoCache, int radius) {
        if (!TextUtils.isEmpty(filePath)) {
            loadImageForFile(Glide.with(context), imageView, filePath, error, width, height, isPlace, isNoCache,
                    new RoundedCornersTransformation(context, radius, 3));
        } else if (error != 0) {
            Glide.with(context).load(error).bitmapTransform(new RoundedCornersTransformation(context, radius, 0)).into(imageView);
        }
    }

    /**
     * @param context
     * @param imageView             图片控件
     * @param url                   图片地址
     * @param error                 加载失败显示的图片
     * @param width                 图片宽度
     * @param height                图片高度
     * @param isPlace               是否显示加载前的图片
     * @param isNoCache             是否跳过内存缓存
     * @param bitmapTransformations
     */
    public static void loadTransformImage(Context context, ImageView imageView, String url, int error, int width,
                                          int height, boolean isPlace, boolean isNoCache, Transformation<Bitmap>... bitmapTransformations) {
        if (!TextUtils.isEmpty(url)) {
            loadImage(Glide.with(context), imageView, url, error, width, height, isPlace, isNoCache,
                    bitmapTransformations);
        } else if (error != 0) {
            Glide.with(context).load(error).bitmapTransform(bitmapTransformations).into(imageView);
        }
    }

    /**
     * @param context
     * @param imageView             图片控件
     * @param filePath              图片地址
     * @param error                 加载失败显示的图片
     * @param width                 图片宽度
     * @param height                图片高度
     * @param isPlace               是否显示加载前的图片
     * @param isNoCache             是否跳过内存缓存
     * @param bitmapTransformations
     */
    public static void loadTransformImageForFile(Context context, ImageView imageView, String filePath, int error, int width,
                                                 int height, boolean isPlace, boolean isNoCache, Transformation<Bitmap>... bitmapTransformations) {
        if (!TextUtils.isEmpty(filePath)) {
            loadImageForFile(Glide.with(context), imageView, filePath, error, width, height, isPlace, isNoCache,
                    bitmapTransformations);
        } else if (error != 0) {
            Glide.with(context).load(error).bitmapTransform(bitmapTransformations).into(imageView);
        }
    }

    /**
     * @param context
     * @param imageView 图片控件
     * @param url       图片地址
     * @param error     加载失败显示的图片
     * @param isPlace   是否显示加载前的图片
     * @param isNoCache 是否跳过内存缓存
     */
    public static void loadTransformImage(Context context, ImageView imageView, String url, int error,
                                          boolean isPlace, boolean isNoCache, BitmapTransformation transformation) {
        if (!TextUtils.isEmpty(url)) {
            DrawableTypeRequest request = Glide.with(context).load(url);

            if (error != 0) {
                request.error(error);
                if (isPlace) {
                    request.placeholder(error);
                }
            }

            if (transformation != null) {
                request.bitmapTransform(transformation);
            }

            if (isNoCache) {
                request.skipMemoryCache(true);
            }

            request.into(imageView);
        } else if (error != 0) {
            loadTransformImage(context, imageView, error, error, true, false, transformation);
            //Glide.with(context).load(error).bitmapTransform(transformation).into(imageView);
        }
    }

    /**
     * @param context
     * @param imageView 图片控件
     * @param bytes       图片资源
     * @param error     加载失败显示的图片
     * @param isPlace   是否显示加载前的图片
     * @param isNoCache 是否跳过内存缓存
     */
    public static void loadTransformImage(Context context, ImageView imageView, byte[] bytes, int error,
                                          boolean isPlace, boolean isNoCache, BitmapTransformation transformation) {
        if (bytes != null && bytes.length > 0) {
            DrawableTypeRequest request = Glide.with(context).load(bytes);

            if (error != 0) {
                request.error(error);
                if (isPlace) {
                    request.placeholder(error);
                }
            }

            if (transformation != null) {
                request.bitmapTransform(transformation);
            }

            if (isNoCache) {
                request.skipMemoryCache(true);
            }

            request.into(imageView);
        } else if (error != 0) {
            loadTransformImage(context, imageView, error, error, true, false, transformation);
            //Glide.with(context).load(error).bitmapTransform(transformation).into(imageView);
        }
    }

    /**
     * @param context
     * @param imageView 图片控件
     * @param res       图片地址
     * @param error     加载失败显示的图片
     * @param isPlace   是否显示加载前的图片
     * @param isNoCache 是否跳过内存缓存
     */
    public static void loadTransformImage(Context context, ImageView imageView, int res, int error,
                                          boolean isPlace, boolean isNoCache, BitmapTransformation transformation) {
        if (res != 0) {
            DrawableTypeRequest request = Glide.with(context).load(res);

            if (error != 0) {
                request.error(error);
                if (isPlace) {
                    request.placeholder(error);
                }
            }

            if (transformation != null) {
                request.bitmapTransform(transformation);
            }

            if (isNoCache) {
                request.skipMemoryCache(true);
            }

            request.into(imageView);
        } else if (error != 0) {
            loadTransformImage(context, imageView, error, error, true, false, transformation);
        }
    }

    private static void loadImage(DrawableTypeRequest request, ImageView imageView, int error, int width,
                                  int height, boolean isPlace, boolean isNoCache, Transformation<Bitmap>... bitmapTransformations) {

        if (error != 0) {
            request.error(error);
            if (isPlace) {
                request.placeholder(error);
            }
        }

        if (width > 0 && height > 0) {
            request.override(width, height);
        }

        if (bitmapTransformations.length > 0) {
            request.bitmapTransform(bitmapTransformations);
        }

        if (isNoCache) {
            request.skipMemoryCache(true);
        }
        request.diskCacheStrategy(DiskCacheStrategy.SOURCE);

        request.into(imageView);
    }


    /**
     * @param context
     * @param imageView 图片控件
     * @param url       图片地址
     * @param error     加载失败显示的图片
     * @param isPlace   是否显示加载前的图片
     * @param isNoCache 是否跳过内存缓存
     */
    public static void loadGif(Context context, ImageView imageView, String url, int error,
                               boolean isPlace, boolean isNoCache) {
        if (!TextUtils.isEmpty(url)) {
            DrawableTypeRequest request = Glide.with(context).load(url);

            if (error != 0) {
                request.error(error);
                if (isPlace) {
                    request.placeholder(error);
                }
            }

            if (isNoCache) {
                request.skipMemoryCache(true);
            }

            request.asGif();
            request.diskCacheStrategy(DiskCacheStrategy.SOURCE);

            request.into(imageView);
        } else if (error != 0) {
            Glide.with(context).load(error).into(imageView);
        }
    }

    /**
     * @param context
     * @param imageView 图片控件
     * @param resId      图片ID
     * @param error     加载失败显示的图片
     * @param isPlace   是否显示加载前的图片
     * @param isNoCache 是否跳过内存缓存
     */
    public static void loadGifForResource(Context context, ImageView imageView, int resId, int error,
                                          boolean isPlace, boolean isNoCache) {
        DrawableTypeRequest request = Glide.with(context).load(resId);

        if (error != 0) {
            request.error(error);
            if (isPlace) {
                request.placeholder(error);
            }
        }

        if (isNoCache) {
            request.skipMemoryCache(true);
        }

        request.asGif();
        request.diskCacheStrategy(DiskCacheStrategy.SOURCE);

        request.into(imageView);
    }

    /**
     * 清空Glide磁盘缓存
     *
     * @param context
     */
    public static void clearDiskCache(final Context context) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(context.getApplicationContext()).clearDiskCache();
            }
        }).start();
    }

    /**
     * 清空Glide内存缓存
     *
     * @param context
     */
    public static void clearMemory(Context context) {
        Glide.get(context).clearMemory();
    }
}
