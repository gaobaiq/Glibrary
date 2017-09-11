package com.gbq.library.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 类说明：文件处理工具
 * Author: Kuzan
 * Date: 2016/4/25 11:02
 */
public class FileUtils {

    /**
     * 判断SDCard是否存在 [当没有外挂SD卡时，内置ROM也被识别为存在sd卡]
     *
     * @return
     */
    public static boolean isSdCardExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取SD卡根目录路径 "/mnt/sdcard"
     *
     * @return
     */
    public static String getSdCardPath() {
        if (isSdCardExist()) {
            return Environment.getExternalStorageDirectory().getPath();
        }
        return "no sdcard path found";
    }

    /**
     * 删除文件
     *
     * @param fileName
     */
    public static void deleteFile(String fileName) {
        if (!TextUtils.isEmpty(fileName)) {
            File file = new File(fileName);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * 创建文件
     *
     * @param path
     * @return
     */
    public static File createFile(String path) {
        File file = new File(path);
        File dirs = new File(file.getParent());
        if (dirs != null && !dirs.exists()) {
            dirs.mkdirs(); // 先创建目录
        }
        if (!file.exists()) {
            try {
                file.createNewFile(); // 目录下创建文件
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 从sdcard读取一个文件
     *
     * @param filePath 如/Camera/P5035-205230.jpg，sdcard下的完整路径
     * @return
     * @author wangguanwen 2015年3月11日 上午10:38:05 <br>
     */
    public static String readFile(final String filePath) {
        File file = new File(filePath);
        if (file != null && file.exists()) {
            try {
                FileInputStream is = new FileInputStream(file);
                byte[] b = new byte[(is.available())];
                is.read(b);
                String result = new String(b);
                return result;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * 向sdcard写入一个文件
     *
     * @param fileName 如"debug"
     * @param content  文字内容
     * @throws IOException
     * @author wangguanwen 2015年3月11日 上午10:55:03 <br>
     */
    public static void writeFile(final String dirPath, final String fileName, final String content)
            throws IOException {
        if (!checkDirectoryExists(dirPath)) {
            new File(dirPath).mkdirs();
        }

        File file = new File(dirPath, fileName);
        file.createNewFile();

        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file), "utf-8");
        osw.write(content);
        osw.close();
    }

    /**
     * 判断指定目录是否存在 .
     *
     * @param dirPath
     * @return
     * @author wangguanwen 2015年3月11日 上午11:30:18 <br>
     */
    public static Boolean checkDirectoryExists(final String dirPath) {
        File f = new File(dirPath);
        try {
            if (f != null && f.exists() && f.isDirectory()) {
                return true;
            } else {
                return false;
            }
        } finally {
            f = null;
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件路径
     * @return {@code true}: 存在<br>{@code false}: 不存在
     */
    public static boolean isFileExists(String filePath) {
        return isFileExists(getFileByPath(filePath));
    }

    /**
     * 根据文件路径获取文件
     *
     * @param filePath 文件路径
     * @return 文件
     */
    public static File getFileByPath(String filePath) {
        return StringUtils.isSpace(filePath) ? null : new File(filePath);
    }

    /**
     * 判断文件是否存在
     *
     * @param file 文件
     * @return {@code true}: 存在<br>{@code false}: 不存在
     */
    public static boolean isFileExists(File file) {
        return file != null && file.exists();
    }

    /**
     * 图片压缩
     *
     * @param fileUri 本地图片uri
     * @return 压缩后的文件
     */
    public static File scal(Uri fileUri) {
        String path = fileUri.getPath();
        if (TextUtils.isEmpty(path)) {
            path = fileUri.toString();
        }
        File outputFile = new File(path);
        long fileSize = outputFile.length();
        final long fileMaxSize = 200 * 1024;
        if (fileSize >= fileMaxSize) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            int height = options.outHeight;
            int width = options.outWidth;

            double scale = Math.sqrt((float) fileSize / fileMaxSize);
            options.outHeight = (int) (height / scale);
            options.outWidth = (int) (width / scale);
            options.inSampleSize = (int) (scale + 0.5);
            options.inJustDecodeBounds = false;

            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            if (bitmap == null) {
                return null;
            }
            int degree = readPictureDegree(path);
            bitmap = rotaingImageView(degree, bitmap);
            outputFile = new File(createImageFile().getPath());
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(outputFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            } else {
                File tempFile = outputFile;
                outputFile = new File(createImageFile().getPath());
                copyFileUsingFileChannels(tempFile, outputFile);
            }
            System.gc();
        }
        return outputFile;
    }

    public static Uri createImageFile() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "jpeg_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName, /* prefix */
                    ".jpg", /* suffix */
                    storageDir /* directory */
            );
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Save a file: path for use with ACTION_VIEW intents
        return Uri.fromFile(image);
    }

    public static void copyFileUsingFileChannels(File source, File dest) {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            try {
                inputChannel = new FileInputStream(source).getChannel();
                outputChannel = new FileOutputStream(dest).getChannel();
                outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } finally {
            try {
                inputChannel.close();
                outputChannel.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片
     *
     * @param angle 角度
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        //旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        System.out.println("angle2=" + angle);
        // 创建新的图片
        if (angle > 0) {
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } else {
            return bitmap;
        }
    }

    /**
     * 获取全路径中的文件拓展名
     *
     * @param file 文件
     * @return 文件拓展名
     */
    public static String getFileExtension(File file) {
        if (file == null) return null;
        return getFileExtension(file.getPath());
    }

    /**
     * 获取全路径中的文件拓展名
     *
     * @param filePath 文件路径
     * @return 文件拓展名
     */
    public static String getFileExtension(String filePath) {
        if (StringUtils.isSpace(filePath)) return filePath;
        int lastPoi = filePath.lastIndexOf('.');
        int lastSep = filePath.lastIndexOf(File.separator);
        if (lastPoi == -1 || lastSep >= lastPoi) return "";
        return filePath.substring(lastPoi + 1);
    }

    /**
     * 获取应用专属缓存目录
     *      android 4.4及以上系统不需要申请SD卡读写权限
     *      因此也不用考虑6.0系统动态申请SD卡读写权限问题，切随应用被卸载后自动清空 不会污染用户存储空间
     *
     * @param context 上下文
     * @param type 文件夹类型 可以为空，为空则返回API得到的一级目录
     *
     * @return 缓存文件夹 如果没有SD卡或SD卡有问题则返回内存缓存目录，否则优先返回SD卡缓存目录
     */
    public static File getCacheDirectory(Context context, String type) {
        File appCacheDir = getExternalCacheDirectory(context, type);
        if (appCacheDir == null){
            appCacheDir = getInternalCacheDirectory(context, type);
        }

        if (appCacheDir == null) {
            Log.e("getCacheDirectory","getCacheDirectory fail ,the reason is mobile phone unknown exception !");
        } else {
            if (!appCacheDir.exists()&&!appCacheDir.mkdirs()) {
                Log.e("getCacheDirectory","getCacheDirectory fail ,the reason is make directory fail !");
            }
        }
        return appCacheDir;
    }

    /**
     * 获取SD卡缓存目录
     * @param context 上下文
     * @param type 文件夹类型 如果为空则返回 /storage/emulated/0/Android/data/app_package_name/cache
     *             否则返回对应类型的文件夹如Environment.DIRECTORY_PICTURES 对应的文件夹为 .../data/app_package_name/files/Pictures
     * {@link Environment#DIRECTORY_MUSIC},
     * {@link Environment#DIRECTORY_PODCASTS},
     * {@link Environment#DIRECTORY_RINGTONES},
     * {@link Environment#DIRECTORY_ALARMS},
     * {@link Environment#DIRECTORY_NOTIFICATIONS},
     * {@link Environment#DIRECTORY_PICTURES}, or
     * {@link Environment#DIRECTORY_MOVIES}.or 自定义文件夹名称
     * @return 缓存目录文件夹 或 null（无SD卡或SD卡挂载失败）
     */
    public static File getExternalCacheDirectory(Context context, String type) {
        File appCacheDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            if (TextUtils.isEmpty(type)) {
                appCacheDir = context.getExternalCacheDir();
            }else {
                appCacheDir = context.getExternalFilesDir(type);
            }

            if (appCacheDir == null){// 有些手机需要通过自定义目录
                appCacheDir = new File(Environment.getExternalStorageDirectory(),"Android/data/"+context.getPackageName()+"/cache/"+type);
            }

            if (appCacheDir == null) {
                Log.e("getExternalDirectory","getExternalDirectory fail ,the reason is sdCard unknown exception !");
            } else {
                if (!appCacheDir.exists()&&!appCacheDir.mkdirs()) {
                    Log.e("getExternalDirectory","getExternalDirectory fail ,the reason is make directory fail !");
                }
            }
        } else {
            Log.e("getExternalDirectory","getExternalDirectory fail ,the reason is sdCard nonexistence or sdCard mount fail !");
        }
        return appCacheDir;
    }

    /**
     * 获取内存缓存目录
     *
     * @param type 子目录，可以为空，为空直接返回一级目录
     * @return 缓存目录文件夹 或 null（创建目录文件失败）
     * 注：该方法获取的目录是能供当前应用自己使用，外部应用没有读写权限，如 系统相机应用
     */
    public static File getInternalCacheDirectory(Context context, String type) {
        File appCacheDir;
        if (TextUtils.isEmpty(type)) {
            // /data/data/app_package_name/cache
            appCacheDir = context.getCacheDir();
        } else {
            // /data/data/app_package_name/files/type
            appCacheDir = new File(context.getFilesDir(), type);
        }

        if (!appCacheDir.exists()&&!appCacheDir.mkdirs()) {
            Log.e("getInternalDirectory","getInternalDirectory fail ,the reason is make directory fail !");
        }
        return appCacheDir;
    }
}
