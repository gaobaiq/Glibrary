package com.gbq.library.image;

import android.content.Context;
import android.os.Environment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;
import com.gbq.library.FrameConfig;
import com.gbq.library.utils.FileUtils;

import java.io.InputStream;


public class OkHttpGlideModule implements GlideModule {

    public static boolean isARGB_8888 = true;

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {

        if (FileUtils.isSdCardExist() && context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) != null) {
            builder.setDiskCache(new DiskLruCacheFactory(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath(), DiskCache.Factory.DEFAULT_DISK_CACHE_DIR, FrameConfig.imageCacheSize * 1024 * 1024));
        } else {
            builder.setDiskCache(new InternalCacheDiskCacheFactory(context, FrameConfig.imageCacheSize * 1024 * 1024));
        }

        //把图片编码转为ARGB_8888（默认是RGB_565）
        if (isARGB_8888) {
            builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        }
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
    }

}