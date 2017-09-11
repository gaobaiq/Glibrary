package com.gbq.library.widget.customview.banner;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * 类说明：自定义轮播图
 * Author: Kuzan
 * Date: 2017/6/17 13:59.
 */
public class ViewPagerScroller extends Scroller {
    private int mScrollDuration = 550;
    private boolean zero;

    public ViewPagerScroller(Context context) {
        super(context);
    }

    public ViewPagerScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public ViewPagerScroller(Context context, Interpolator interpolator,
                             boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, zero ? 0 : mScrollDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, zero ? 0 : mScrollDuration);
    }

    public int getScrollDuration() {
        return mScrollDuration;
    }

    public void setScrollDuration(int scrollDuration) {
        this.mScrollDuration = scrollDuration;
    }

    public boolean isZero() {
        return zero;
    }

    public void setZero(boolean zero) {
        this.zero = zero;
    }
}
