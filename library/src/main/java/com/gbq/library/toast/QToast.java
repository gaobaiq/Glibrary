package com.gbq.library.toast;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.gbq.library.R;
import com.gbq.library.utils.ScreenUtils;

/**
 * 类说明：自定义toast
 * Author: Kuzan
 * Date: 2017/5/25 15:09.
 */
public class QToast {
    public static final long LENGTH_SHORT = 1500;

    public static final long LENGTH_LONG = 2500;

    private WindowManager wdm;
    private TextView mMessageView;
    private WindowManager.LayoutParams params;
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            cancel();
        }
    };

    private boolean isShow = false;

    private long mDuration;

    public QToast(Context context) {
        wdm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Toast toast = Toast.makeText(context, "", Toast.LENGTH_LONG);
        mMessageView = new TextView(context);
        mMessageView.setPadding(40, 20, 40, 20);
        mMessageView.setBackgroundResource(R.drawable.toast_bg);
        mMessageView.setTextColor(Color.WHITE);

        params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        params.windowAnimations = toast.getView().getAnimation().INFINITE;
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        params.y = ScreenUtils.getScreenHeight(context) / 8;
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
    }

    public static QToast makeText(Context context, String msg, long duration) {
        QToast toast = new QToast(context);
        toast.setText(msg);
        toast.setDuration(duration);
        return toast;
    }

    public void setText(CharSequence s) {
        if (mMessageView != null) {
            mMessageView.setText(s);
        }
    }

    public void setDuration(long duration) {
        mDuration = duration;
    }

    public void show() {
        mHandler.removeCallbacks(mRunnable);
        if (!isShow) {
            isShow = true;
            wdm.addView(mMessageView, params);
        }
        mHandler.postDelayed(mRunnable, mDuration);
    }

    private void cancel() {
        if (isShow) {
            isShow = false;
            wdm.removeView(mMessageView);
        }
    }
}
