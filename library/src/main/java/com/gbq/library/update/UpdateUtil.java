package com.gbq.library.update;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Environment;

import com.gbq.library.toast.ToastUtils;
import com.gbq.library.utils.FileUtils;
import com.gbq.library.utils.StringUtils;

import java.io.File;
import java.util.List;

/**
 * 类说明：版本更新工具类
 * Author: Kuzan
 * Date: 2017/6/17 10:32.
 */
public class UpdateUtil {
    /**
     * 启动下载
     *
     * @param context
     * @param downLoadUrl apk下载链接
     */
    public static void startUpgrade(Context context, String downLoadUrl) {
        if (StringUtils.isNotEmptyString(downLoadUrl)) {
            Intent intent = new Intent(context, UpdateService.class);
            intent.putExtra("downLoadUrl", downLoadUrl);
            intent.setAction(UpdateService.DOWN_ACTION_START);
            context.startService(intent);
        } else {
            ToastUtils.ToastMessage(context, "下载链接为空");
        }
    }

    /**
     * 停止下载
     *
     * @param context
     */
    public static void stopUpgrade(Context context) {
        Intent intent = new Intent(context, UpdateService.class);
        intent.setAction(UpdateService.DOWN_ACTION_STOP);
        context.startService(intent);
    }

    /**
     * 获取应用版本名
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(),
                    0);
            String version = packInfo.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取应用版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取app名称
     *
     * @param context
     * @return
     */
    public static String getAppName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int res = packInfo.applicationInfo.labelRes;
            return context.getResources().getString(res);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取应用启动activity名字
     */
    public static String getLauncherActivityName(Context context) {
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(context.getPackageName());
        PackageManager pManager = context.getPackageManager();
        List<ResolveInfo> apps = pManager.queryIntentActivities(resolveIntent, 0);
        ResolveInfo ri = apps.iterator().next();
        if (ri != null) {
            return ri.activityInfo.name;
        }
        return "";
    }

    /**
     * 获取保存更新apk的文件路径
     *
     * @param context
     * @return
     */
    public static String getLocalFilePath(Context context) {
        if (FileUtils.isSdCardExist()) {
            return context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + context.getPackageName() + ".apk";
        }
        return "";
    }
}
