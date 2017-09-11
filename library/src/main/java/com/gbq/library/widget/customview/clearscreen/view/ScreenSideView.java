package com.gbq.library.widget.customview.clearscreen.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.gbq.library.widget.customview.clearscreen.ClearConstants;
import com.gbq.library.widget.customview.clearscreen.IClearEvent;
import com.gbq.library.widget.customview.clearscreen.IClearRootView;
import com.gbq.library.widget.customview.clearscreen.IPositionCallBack;

/**
 * 类说明：滑动布局
 * Author: Kuzan
 * Date: 2017/8/23 10:58.
 */
public class ScreenSideView extends LinearLayout implements IClearRootView {
    private final int MIN_SCROLL_SIZE = 30;
    private final int LEFT_SIDE_X = 0;
    private final int RIGHT_SIDE_X = getResources().getDisplayMetrics().widthPixels;

    private int mDownX;
    private int mEndX;
    private ValueAnimator mEndAnimator;

    private boolean isCanScroll;

    private ClearConstants.Orientation mOrientation;

    private IPositionCallBack mIPositionCallBack;
    private IClearEvent mIClearEvent;

    @Override
    public void setIPositionCallBack(IPositionCallBack l) {
        mIPositionCallBack = l;
    }

    @Override
    public void setIClearEvent(IClearEvent l) {
        mIClearEvent = l;
    }

    public ScreenSideView(Context context) {
        this(context, null);
    }

    public ScreenSideView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScreenSideView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mEndAnimator = ValueAnimator.ofFloat(0, 1.0f).setDuration(200);
        mEndAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float factor = (float) valueAnimator.getAnimatedValue();
                int diffX = mEndX - mDownX;
                mIPositionCallBack.onPositionChange((int) (mDownX + diffX * factor), 0);
            }
        });
        mEndAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mOrientation.equals(ClearConstants.Orientation.RIGHT) && mEndX == RIGHT_SIDE_X) {
                    mIClearEvent.onClearEnd();
                    mOrientation = ClearConstants.Orientation.LEFT;
                } else if (mOrientation.equals(ClearConstants.Orientation.LEFT) && mEndX == LEFT_SIDE_X) {
                    mIClearEvent.onRecovery();
                    mOrientation = ClearConstants.Orientation.RIGHT;
                }
                mDownX = mEndX;
                isCanScroll = false;
            }
        });
    }

    @Override
    public void setClearSide(ClearConstants.Orientation orientation) {
        mOrientation = orientation;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int x = (int) event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isScrollFromSide(x)) {
                    isCanScroll = true;
                    return true;
                }
            case MotionEvent.ACTION_MOVE:
                if (isGreaterThanMinSize(x) && isCanScroll) {
                    mIPositionCallBack.onPositionChange(getRealTimeX(x), 0);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isGreaterThanMinSize(x) && isCanScroll) {
                    mDownX = getRealTimeX(x);
                    fixPosition();
                    mEndAnimator.start();
                }
        }
        return super.onTouchEvent(event);
    }

    private int getRealTimeX(int x) {
        if (mOrientation.equals(ClearConstants.Orientation.RIGHT) && mDownX > RIGHT_SIDE_X / 3
                || mOrientation.equals(ClearConstants.Orientation.LEFT) && (mDownX > RIGHT_SIDE_X * 2 / 3)) {
            return x + MIN_SCROLL_SIZE;
        } else {
            return x - MIN_SCROLL_SIZE;
        }
    }

    private void fixPosition() {
        if (mOrientation.equals(ClearConstants.Orientation.RIGHT) && mDownX > RIGHT_SIDE_X / 3) {
            mEndX = RIGHT_SIDE_X;
        } else if (mOrientation.equals(ClearConstants.Orientation.LEFT) && (mDownX < RIGHT_SIDE_X * 2 / 3)) {
            mEndX = LEFT_SIDE_X;
        }
    }

    private boolean isGreaterThanMinSize(int x) {
        int absX = Math.abs(mDownX - x);
        return absX > MIN_SCROLL_SIZE;
    }

    private boolean isScrollFromSide(int x) {
        if (x <= LEFT_SIDE_X + MIN_SCROLL_SIZE && mOrientation.equals(ClearConstants.Orientation.RIGHT)
                || (x > RIGHT_SIDE_X - MIN_SCROLL_SIZE && mOrientation.equals(ClearConstants.Orientation.LEFT))) {
            return true;
        } else {
            return false;
        }
    }
}
