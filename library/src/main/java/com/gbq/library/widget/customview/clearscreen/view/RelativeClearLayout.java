package com.gbq.library.widget.customview.clearscreen.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.gbq.library.widget.customview.clearscreen.ClearConstants;
import com.gbq.library.widget.customview.clearscreen.IClearEvent;
import com.gbq.library.widget.customview.clearscreen.IClearRootView;
import com.gbq.library.widget.customview.clearscreen.IPositionCallBack;


/**
 * 类说明：封装相对布局
 * Author: Kuzan
 * Date: 2017/8/23 11:09.
 */
public class RelativeClearLayout extends RelativeLayout implements IClearRootView {
    private final int MIN_SCROLL_SIZE = 50;
    private final int LEFT_SIDE_X = 0;
    private final int RIGHT_SIDE_X = getResources().getDisplayMetrics().widthPixels;

    private int mDownX;
    private int mEndX;
    private ValueAnimator mEndAnimator;

    private boolean isCanScroll;
    private boolean isTouchWithAnimRunning;

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

    public RelativeClearLayout(Context context) {
        this(context, null);
    }

    public RelativeClearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RelativeClearLayout(Context context, AttributeSet attrs, int defStyle) {
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
        int offsetX = x - mDownX;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_MOVE:
                if (isGreaterThanMinSize(mDownX, x) && isCanScroll) {
                    mIPositionCallBack.onPositionChange(getPositionChangeX(offsetX), 0);
                    return true;
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_UP:
                if (isGreaterThanMinSize(mDownX, x) && isCanScroll) {
                    mDownX = getPositionChangeX(offsetX);
                    fixPosition(offsetX);
                    mEndAnimator.start();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        final int x = (int) event.getX();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                isTouchWithAnimRunning = mEndAnimator.isRunning();
                mDownX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                if (isGreaterThanMinSize(mDownX, x) && !isTouchWithAnimRunning) {
                    isCanScroll = true;
                    return true;
                }
        }
        return super.onInterceptTouchEvent(event);
    }

    private int getPositionChangeX(int offsetX) {
        int absOffsetX = Math.abs(offsetX);
        if (mOrientation.equals(ClearConstants.Orientation.RIGHT)) {
            return absOffsetX - MIN_SCROLL_SIZE;
        } else {
            return RIGHT_SIDE_X - (absOffsetX - MIN_SCROLL_SIZE);
        }
    }

    private void fixPosition(int offsetX) {
        int absOffsetX = Math.abs(offsetX);
        if (mOrientation.equals(ClearConstants.Orientation.RIGHT) && absOffsetX > RIGHT_SIDE_X / 3) {
            mEndX = RIGHT_SIDE_X;
        } else if (mOrientation.equals(ClearConstants.Orientation.LEFT) && (absOffsetX > RIGHT_SIDE_X / 3)) {
            mEndX = LEFT_SIDE_X;
        }
    }

    public boolean isGreaterThanMinSize(int x1, int x2) {
        if(mOrientation.equals(ClearConstants.Orientation.RIGHT)){
            return x2 - x1 > MIN_SCROLL_SIZE;
        }else {
            return x1 - x2 > MIN_SCROLL_SIZE;
        }
    }
}
