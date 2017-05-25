package com.gbq.library.pullrefresh;

import android.view.View;

/**
 * 类说明：上接加载下拉刷新工具类
 * Author: Kuzan
 * Date: 2017/5/25 15:40.
 */
public class PullRefreshUtil {
    /**
     * 刷新控件的基本配件 (头部、尾部都用默认的)
     *
     * @param view          刷新控件
     * @param isDownRefresh 是否开启下拉刷新
     * @param isUpRefresh   是否开启上拉加载
     */
    public static void setRefresh(PullRefreshView view, boolean isDownRefresh, boolean isUpRefresh) {

        HeadView headView = null;
        TailView tailView = null;
        if (isDownRefresh) {
            headView = new HeadView(view.getContext());
        }

        if (isUpRefresh) {
            tailView = new TailView(view.getContext());
        }

        setRefresh(view, isDownRefresh, isUpRefresh, headView, tailView, headView, tailView);
    }

    /**
     * 刷新控件的基本配件 (自定义头部、尾部用默认的)
     *
     * @param view              刷新控件
     * @param isDownRefresh     是否开启下拉刷新
     * @param isUpRefresh       是否开启上拉加载
     * @param headView          头部View
     * @param headStateListener 头部监听器
     */
    public static void setRefresh(PullRefreshView view, boolean isDownRefresh, boolean isUpRefresh, View headView, PullRefreshView.OnHeadStateListener headStateListener) {

        TailView tailView = null;
        if (isUpRefresh) {
            tailView = new TailView(view.getContext());
        }
        setRefresh(view, isDownRefresh, isUpRefresh, headView, tailView, headStateListener, tailView);
    }

    /**
     * 刷新控件的基本配件 (自定义尾部 、头部用默认的)
     *
     * @param view              刷新控件
     * @param isDownRefresh     是否开启下拉刷新
     * @param isUpRefresh       是否开启上拉加载
     * @param tailView          尾部View
     * @param tailStateListener 尾部监听器
     */
    public static void setRefresh(PullRefreshView view, boolean isDownRefresh, boolean isUpRefresh, View tailView, PullRefreshView.OnTailStateListener tailStateListener) {

        HeadView headView = null;
        if (isDownRefresh) {
            headView = new HeadView(view.getContext());
        }
        setRefresh(view, isDownRefresh, isUpRefresh, headView, tailView, headView, tailStateListener);
    }

    /**
     * 刷新控件的基本配件 （自定义头部、尾部）
     *
     * @param view              刷新控件
     * @param isDownRefresh     是否开启下拉刷新
     * @param isUpRefresh       是否开启上拉加载
     * @param headView          头部View
     * @param tailView          尾部View
     * @param headStateListener 头部监听器
     * @param tailStateListener 尾部监听器
     */
    public static void setRefresh(PullRefreshView view, boolean isDownRefresh, boolean isUpRefresh, View headView, View tailView, PullRefreshView.OnHeadStateListener headStateListener, PullRefreshView.OnTailStateListener tailStateListener) {

        view.setRefresh(isDownRefresh, isUpRefresh);

        if (isDownRefresh) {
            view.setHead(headView);
            view.setOnHeadStateListener(headStateListener);
        }

        if (isUpRefresh) {
            view.setTail(tailView);
            view.setOnTailStateListener(tailStateListener);
        }
    }
}
