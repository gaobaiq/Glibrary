package com.gbq.library.swiperefresh;

import android.view.View;

/**
 * 类说明：底部状态监听器
 * Author: Kuzan
 * Date: 2017/8/3 16:37.
 */
public interface OnFooterStateListener {
    /**
     * 底部滑动变化
     *
     * @param footer         底部View
     * @param scrollOffset  滑动距离
     * @param scrollRatio   从开始到触发阀值的滑动比率（0到100）如果滑动到达了阀值，就算在滑动，这个值也是100
     */
    void onScrollChange(View footer, int scrollOffset, int scrollRatio);

    /**
     * 底部处于加载状态 （触发上拉加载的时候调用）
     *
     * @param footer 底部View
     */
    void onLoading(View footer);

    /**
     * 底部收起
     *
     * @param footer 底部View
     */
    void onRetract(View footer);

    /**
     * 没有更多
     *
     * @param footer
     */
    void onNoMore(View footer);

    /**
     * 有更多
     *
     * @param footer
     */
    void onHasMore(View footer);
}
