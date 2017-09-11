package com.gbq.library.widget.swipeback;

/**
 * 类说明：侧滑返回
 *          在使用的Activity直接继承这个就可以
 * Author: Kuzan
 * Date: 2017/8/17 15:02.
 */
public interface ISwipeBack {

    /**
     * 返回与对应activity相关的swipeBackLayout
     */
    SwipeBackLayout getSwipeBackLayout();

    /**
     * 是否启用SwipeBack功能
     * */
    void setSwipeBackEnable(boolean enable);

    /**
     * 右滑退出activity
     */
    void scrollToFinishActivity();
}
