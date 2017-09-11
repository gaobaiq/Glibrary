package com.gbq.library.swiperefresh;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import com.gbq.library.R;

/**
 * 类说明：上拉加载更多工具类，下拉默认开启，若想不开启，使用PullRefreshView
 * Author: Kuzan
 * Date: 2017/8/03 16:42.
 */
public class SwipeRefreshUtil {

    /**
     * 刷新控件的基本配件 (底部用默认的)
     *
     * @param view          刷新控件
     * @param isLoadMore   是否开启上拉加载
     */
    public static void setLoadMore(CustomSwipeRefreshLayout view, boolean isLoadMore) {

        FooterView footerView = null;

        if (isLoadMore) {
            footerView = new FooterView(view.getContext());
        }

        setLoadMore(view, isLoadMore, footerView, footerView);
    }

    /**
     * 刷新控件的基本配件 (自定义底部 )
     *
     * @param view              刷新控件
     * @param isLoadMore        是否开启上拉加载
     * @param footerView        尾部View
     * @param listener          尾部监听器
     */
    public static void setLoadMore(CustomSwipeRefreshLayout view, boolean isLoadMore, View footerView, OnFooterStateListener listener) {
        view.setLoadMore(isLoadMore);
        if (isLoadMore) {
            view.setFooter(footerView);
            view.setOnFooterStateListener(listener);
        }
    }

    /**
     * 设置下拉刷新加载动画
     *
     * @param view          刷新控件
     * @param res           刷新动画图片资源
     * @param animPosition  刷新动画的位置 1左边 2中间
     * */
    public static void setRefreshImage(Context context, CustomSwipeRefreshLayout view, int res, int animPosition) {
        CustomProgressDrawable drawable = new CustomProgressDrawable(context, view);
        Bitmap bitmap;
        if (res > 0) {
            bitmap = BitmapFactory.decodeResource(context.getResources(), res);
        } else {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.default_refresh_icon);
        }
        drawable.setBitmap(bitmap);
        view.setProgressView(drawable);
        view.setAnimPosition(animPosition);
    }

    /**
     * 设置下拉刷新动画的位置
     *
     * @param view          刷新控件
     * @param animPosition  刷新动画的位置 1左边 2中间
     * */
    public static void setAnimPosition(CustomSwipeRefreshLayout view, int animPosition) {
        view.setAnimPosition(animPosition);
    }
}
