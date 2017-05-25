package com.gbq.library.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 类说明：图片处理工具
 * Author: Kuzan
 * Date: 2017/5/25 15:27.
 */
public class BitmapUtil {
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int r = 0;
        if (width > height) {
            r = height;
        } else {
            r = width;
        }
        Bitmap backgroundBmp = Bitmap.createBitmap(width,
                height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(backgroundBmp);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        RectF rect = new RectF(0, 0, r, r);
        canvas.drawRoundRect(rect, r / 2, r / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, null, rect, paint);
        return backgroundBmp;
    }

    /**
     * 获取圆角位图的方法
     *
     * @param bitmap 需要转化成圆角的位图
     * @param pixels 圆角的度数，数值越大，圆角越大
     * @return 处理后的圆角位图
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }


    public static Bitmap getBitempForUrl(String ImageUrl) {

        if (StringUtils.isEmptyString(ImageUrl)) {
            return null;
        }

        if (ImageUrl.substring(0, 1).equals("/") && ImageUrl.length() > 2) {
            ImageUrl = ImageUrl.substring(1, ImageUrl.length());
        }
        try {
            URL url = new URL(ImageUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");   //设置请求方法为GET
            conn.setReadTimeout(20 * 1000);    //设置请求过时时间为5秒
            InputStream inputStream = conn.getInputStream();   //通过输入流获得图片数据
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        } catch (IOException e) {
            Log.e("eee", e.getMessage());
            return null;
        }
    }

    public static Bitmap getBitmapForView(View view) {
        view.setDrawingCacheEnabled(true);
        return view.getDrawingCache();
    }

    public static Bitmap zoomBitmap(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
                true);
        return newbm;
    }

    /**
     * 图片压缩方法：（使用compress的方法）
     * <p/>
     * <br>
     * <b>说明</b> 如果bitmap本身的大小小于maxSize，则不作处理
     *
     * @param bitmap  要压缩的图片
     * @param maxSize 压缩后的大小，单位kb
     */
    public static void compress(Bitmap bitmap, double maxSize) {
        // 将bitmap放至数组中，意在获得bitmap的大小（与实际读取的原文件要大）
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 格式、质量、输出流
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, baos);
        byte[] b = baos.toByteArray();
        // 将字节换成KB
        double mid = b.length / 1024;
        // 获取bitmap大小 是允许最大大小的多少倍
        double i = mid / maxSize;
        // 判断bitmap占用空间是否大于允许最大空间 如果大于则压缩 小于则不压缩
        if (i > 1) {
            // 缩放图片 此处用到平方根 将宽带和高度压缩掉对应的平方根倍
            // （保持宽高不变，缩放后也达到了最大占用空间的大小）
            //bitmap = scale(bitmap, bitmap.getWidth() / Math.sqrt(i),
            //        bitmap.getHeight() / Math.sqrt(i));
        }
    }

    /**
     * bitmap to byte[];
     *
     * @param bm
     * @return
     */
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }


    /**
     * 图片质量压缩
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image, int maxSize) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > maxSize) {    // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            options -= 10;// 每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中

        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        //try {
        //    FileOutputStream out = new FileOutputStream(srcPath);
        //    bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
        //} catch (Exception e) {
        //    e.printStackTrace();
        //}
        return bitmap;
    }

    //压缩图片
    public static byte[] compress(String filePath,int reqWidth, int reqHeight, int maxSize) {
        Bitmap bm = ImageUtil.decodeSampledBitmapFromResource(filePath, reqWidth, reqHeight);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int quality = 100; // 质量
        while (baos.toByteArray().length / 1024 > maxSize && quality >= 20) { // 循环判断如果压缩后图片是否大于maxSize kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            quality -= 10;// 每次都减少10
            bm.compress(Bitmap.CompressFormat.JPEG, quality, baos);// 这里压缩options%，把压缩后的数据存放到baos中
        }
        return baos.toByteArray();
    }
}
