package com.gbq.library.widget.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

/**
 * 类说明：适配5.0以上ScrollView嵌套RecyclerView 滑动不顺畅的问题。
 * Author: Kuzan
 * Date: 2017/5/25 15:59.
 */
public class SupportScrollview extends ScrollView {
    private int downX;
    private int downY;
    private int mTouchSlop;

    public SupportScrollview(Context context) {
        super(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public SupportScrollview(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public SupportScrollview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int action = e.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) e.getRawX();
                downY = (int) e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) e.getRawY();
                if (Math.abs(moveY - downY) > mTouchSlop) {
                    return true;
                }
        }
        return super.onInterceptTouchEvent(e);
    }
}
