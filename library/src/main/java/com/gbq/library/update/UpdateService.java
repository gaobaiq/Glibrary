package com.gbq.library.update;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.gbq.library.R;
import com.gbq.library.toast.ToastUtils;
import com.gbq.library.utils.FileUtils;
import com.gbq.library.utils.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 类说明：版本更新service
 * Author: Kuzan
 * Date: 2017/6/17 10:31.
 */
public class UpdateService extends Service {
    public static final String TAG = UpdateService.class.getSimpleName();

    private static final int TIMEOUT = 10 * 60 * 1000; // 超时

    private static final int DOWN_OK = 1; // 下载成功标记
    private static final int DOWN_LOADING = 3; // 下载中。。。标记
    private static final int DOWN_ERROR = 0; // 下载失败标记

    public static final String DOWN_ACTION_START = "ACTION_START";
    public static final String DOWN_ACTION_STOP = "ACTION_STOP";

    private String mAppName; // 应用名称
    private String mDownLoadUrl; // apk 下载路径
    private String mLocalFilePath; // apk 本地保存路径

    private NotificationManager mNotificationManager; // 通知管理器
    private Notification mNotification; // 通知

    private Intent mIntent;

    private int mNotificationId = 1;

    private RemoteViews mContentView;

    private boolean isStop = false;

    private boolean isCancel = false;
    private boolean isLoading = false;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            mIntent = intent;
            if (!isLoading && DOWN_ACTION_START.equals(intent.getAction())) {

                mAppName = UpdateUtil.getAppName(this);
                mDownLoadUrl = intent.getStringExtra("downLoadUrl");
                mLocalFilePath = UpdateUtil.getLocalFilePath(this);

                Log.e(TAG, "mDownLoadUrl = " + mDownLoadUrl);
                // 创建通知栏
                createNotification();
                // 启动下载线程
                startUpdate();
                isStop = false;
                isLoading = true;
            } else if (DOWN_ACTION_STOP.equals(intent.getAction())) {
                isStop = true;
                isCancel = true;
                isLoading = false;
                // 结束service
                stopSelf();
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    /***
     * 创建通知栏
     */
    public void createNotification() {
        /*
         * 自定义Notification视图
		 */
        mContentView = new RemoteViews(getPackageName(), R.layout.item_of_notification);
        mContentView.setTextViewText(R.id.notificationTitle, mAppName + "—"
                + getString(R.string.down_load_str001));
        mContentView.setTextViewText(R.id.notificationPercent, "0%");
        mContentView.setProgressBar(R.id.notificationProgress, 100, 0, false);

        // 初始化通知
        mNotification = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.stat_sys_download)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(
                        getString(R.string.down_load_str002) + " " + mAppName)
                .build();
        mNotification.tickerText = mAppName + " "
                + getString(R.string.down_load_str002);
        mNotification.flags |= Notification.FLAG_NO_CLEAR;
        mNotification.defaults |= Notification.DEFAULT_SOUND; // 设置通知铃声和振动提醒
        mNotification.contentView = mContentView;

        String launcherActivityName = UpdateUtil.getLauncherActivityName(this);
        if (StringUtils.isNotEmptyString(launcherActivityName)) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setComponent(new ComponentName(this.getPackageName(),
                    launcherActivityName));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);//关键的一步，设置启动模式

            PendingIntent mPendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            mNotification.contentIntent = mPendingIntent;
        }

        // 发送通知
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(mNotificationId, mNotification);
        // 清除通知铃声
        mNotification.defaults = 0;
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @SuppressWarnings("deprecation")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_LOADING: // apk 下载中
                    int progress = msg.arg1;
                    // 改变通知栏
                    mContentView.setTextViewText(R.id.notificationPercent, progress
                            + "%");
                    mContentView.setProgressBar(R.id.notificationProgress, 100,
                            progress, false);
                    mNotificationManager.notify(mNotificationId, mNotification);
                    Log.e(TAG, "" + progress);
                    return;
                case DOWN_OK: // apk 下载完成
                    mContentView.setTextViewText(R.id.notificationPercent, 100
                            + "%");
                    mContentView.setProgressBar(R.id.notificationProgress, 100,
                            100, false);
                    // 添加通知声音
                    mNotification.defaults |= Notification.DEFAULT_SOUND;
                    // 下载完成，点击安装
                    Uri uri = Uri.fromFile(new File(mLocalFilePath));
                    // 安装应用意图
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(uri, "application/vnd.android.package-archive");
                    PendingIntent mPendingIntent = PendingIntent.getActivity(UpdateService.this,
                            0, intent, 0);
                    mNotification.tickerText = mAppName + " "
                            + getString(R.string.down_load_str003);

                    Log.e(TAG, "下载完成");
                    mContentView.setTextViewText(R.id.notificationTitle, mAppName + "—"
                            + getString(R.string.down_load_str003));
                    mNotification.contentIntent = mPendingIntent;
                    mNotification.icon = android.R.drawable.stat_sys_download_done;
                    mNotification.flags = Notification.FLAG_AUTO_CANCEL;
                    mNotificationManager.notify(mNotificationId, mNotification);
                    isLoading = false;
                    break;
                case DOWN_ERROR: // apk 下载失败

                    if (!isCancel) {
                        Intent startIntent = new Intent();
                        startIntent.setAction("android.intent.action.VIEW");
//                        startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startIntent.addCategory("android.intent.category.DEFAULT");
                        Uri content_url = Uri.parse(mDownLoadUrl);
                        startIntent.setData(content_url);
                        PendingIntent pendingIntent = PendingIntent.getActivity(UpdateService.this,
                                0, startIntent, 0);
                        mContentView.setTextViewText(R.id.notificationTitle, mAppName + "—"
                                + getString(R.string.down_load_str004));
                        mNotification.tickerText = mAppName + " "
                                + getString(R.string.down_load_str004);
                        mNotification.contentIntent = pendingIntent;
                        // 添加通知声音
                        mNotification.defaults |= Notification.DEFAULT_SOUND;
                        mNotification.icon = android.R.drawable.stat_sys_download_done;
                        mNotification.flags = Notification.FLAG_AUTO_CANCEL;
                        mNotificationManager.notify(mNotificationId, mNotification);
//                        ToastUtils.ToastMessage(UpgradeService.this, "下载失败，启动浏览器下载!");
                    } else {
                        // 添加通知声音
                        mNotification.defaults |= Notification.DEFAULT_SOUND;
                        mNotificationManager.cancel(mNotificationId);
                        ToastUtils.ToastMessage(UpdateService.this, "下载任务已取消!");
                    }
                    Log.e(TAG, "下载失败");
                    isLoading = false;
                    break;
            }

            isCancel = false;

            // 结束service
            stopService(mIntent);
            stopSelf();
        }
    };

    /***
     * 开启线程下载更新
     */
    private void startUpdate() {

        // 启动线程下载更新
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 下载apk
                    downloadUpdateFile(mDownLoadUrl, mLocalFilePath, TIMEOUT,
                            handler);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendMessage(handler.obtainMessage(DOWN_ERROR));
                }
            }
        }).start();
    }

    /***
     * 下载文件
     *
     * @return
     * @throws MalformedURLException
     */
    public void downloadUpdateFile(String down_url, String file, int timeoutMillis, Handler handler)
            throws Exception {
        boolean result = false;
        int totalSize = 0; // 文件总大小
        InputStream in = null;
        FileOutputStream fos = null;

        FileUtils.deleteFile(file);
        FileUtils.createFile(file);

        URL url;
        try {
            url = new URL(down_url);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url
                    .openConnection();

            httpURLConnection.setConnectTimeout(timeoutMillis);

            httpURLConnection.setReadTimeout(timeoutMillis);

            // 获取下载文件的size
            totalSize = httpURLConnection.getContentLength();
            Log.e("DownloadApkUtils", "totalSize = " + totalSize);

            if (httpURLConnection.getResponseCode() == 404) {
                throw new Exception("fail!");
            }

            in = httpURLConnection.getInputStream();

            fos = new FileOutputStream(file, false);

        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        int downloadCount = 0; // 已下载大小
        int updateProgress = 0; // 更新进度

        byte buffer[] = new byte[64];
        int readsize = 0;

        try {
            while ((readsize = in.read(buffer)) != -1) {

                fos.write(buffer, 0, readsize);

                // 计算已下载到的大小
                downloadCount += readsize;

                // 先计算已下载的百分比，然后跟上次比较是否有增加，有则更新通知进度
                int now = downloadCount * 100 / totalSize;
                // Log.e(TAG, "now = " + now) ;
                if (updateProgress < now) {
                    updateProgress = now;

                    Message msg = new Message();

                    msg.arg1 = updateProgress;

                    msg.what = DOWN_LOADING;

                    handler.sendMessage(msg);
                }

                if (isStop) {
                    break;
                }
            }
            if (!isStop) {
                result = true;
            } else {
                result = false;
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            try {
                fos.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (result) {
            handler.sendMessage(handler.obtainMessage(DOWN_OK));
        } else {
            handler.sendMessage(handler.obtainMessage(DOWN_ERROR));
        }
    }
}
