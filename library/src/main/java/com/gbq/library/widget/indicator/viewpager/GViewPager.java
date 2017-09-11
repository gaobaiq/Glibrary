package com.gbq.library.widget.indicator.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 类说明：封装自己的ViewPager
 * Author: Kuzan
 * Date: 2017/8/11 10:34.
 */
public class GViewPager extends ViewPager {
    /**是否可滚动*/
    private boolean canScroll;

    public GViewPager(Context context) {
        super(context);
        canScroll = false;
    }

    public GViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        canScroll = false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (canScroll) {
            try {
                return super.onInterceptTouchEvent(ev);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (canScroll) {
            return super.onTouchEvent(event);
        }
        return false;
    }

    public void toggleLock() {
        canScroll = !canScroll;
    }

    public void setCanScroll(boolean canScroll) {
        this.canScroll = canScroll;
    }

    public boolean isCanScroll() {
        return canScroll;
    }
}
