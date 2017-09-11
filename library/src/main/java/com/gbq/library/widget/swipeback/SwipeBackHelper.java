package com.gbq.library.widget.swipeback;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import com.gbq.library.R;
import com.gbq.library.utils.SwipeBackUtils;

/**
 * 类说明：侧滑返回工具
 * Author: Kuzan
 * Date: 2017/8/17 15:02.
 */
public class SwipeBackHelper {
    /**
     * 注意在使用的时候加上
     * <activity android:name=".TestActivity"
     *      android:theme="@style/AppSwipeBackTheme"
     *      android:launchMode="singleTask"/>
     *
     * <style name="AppSwipeBackTheme" parent="@style/AppTheme" >
     *      <item name="android:windowBackground">@color/transparent</item>
     *      <item name="android:windowIsTranslucent">true</item>
     * </style>
     *
     * 要不然退出的时候会黑屏
     * */
    private Activity mActivity;

    private SwipeBackLayout mSwipeBackLayout;

    public SwipeBackHelper(Activity activity) {
        mActivity = activity;
    }

    @SuppressWarnings("deprecation")
    public void onActivityCreate() {
        mActivity.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mActivity.getWindow().getDecorView().setBackgroundDrawable(null);
        mSwipeBackLayout = (SwipeBackLayout) LayoutInflater.from(mActivity).inflate(R.layout.layout_swipe_back, null);
        mSwipeBackLayout.addSwipeListener(new SwipeBackLayout.SwipeListener() {
            @Override
            public void onScrollStateChange(int state, float scrollPercent) {

            }

            @Override
            public void onEdgeTouch(int edgeFlag) {
                SwipeBackUtils.convertActivityToTranslucent(mActivity);
            }

            @Override
            public void onScrollOverThreshold() {

            }
        });
    }

    public void onPostCreate() {
        mSwipeBackLayout.attachToActivity(mActivity);
    }

    public View findViewById(int id) {
        if (mSwipeBackLayout != null) {
            return mSwipeBackLayout.findViewById(id);
        }
        return null;
    }

    public SwipeBackLayout getSwipeBackLayout() {
        return mSwipeBackLayout;
    }
}
