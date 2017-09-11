package com.gbq.library.swiperefresh;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import com.gbq.library.R;
import com.gbq.library.utils.DensityUtils;

/**
 * 类说明：自定义SwipeRefreshLayout
 *      注：若嵌套ScrollView，建议使用NestedScrollView,若里面还嵌套RecyclerView，添加以下属性
 *      layoutManager.setSmoothScrollbarEnabled(true);
 *      layoutManager.setAutoMeasureEnabled(true);
 *      mRecyclerView.setNestedScrollingEnabled(false);
 * Author: Kuzan
 * Date: 2017/8/3 16:27.
 */
public class CustomSwipeRefreshLayout extends ViewGroup implements NestedScrollingParent, NestedScrollingChild {

    protected Context mContext;
    /**加载动画大图*/
    public static final int LARGE = MaterialProgressDrawable.LARGE;
    /**加载动画默认图*/
    public static final int DEFAULT = MaterialProgressDrawable.DEFAULT;

    /**加载动画在左边*/
    public static final int ANIM_IN_LEFT = 1;
    /**加载动画在右边*/
    public static final int ANIM_IN_CENTER = 2;

    private int mAnimPosition = ANIM_IN_LEFT;

    //底部上拉的最小高度
    public static final int FOOTER_DEFAULT_HEIGHT = 200;

    private static final String LOG_TAG = CustomSwipeRefreshLayout.class.getSimpleName();

    private static final int MAX_ALPHA = 255;
    private static final int STARTING_PROGRESS_ALPHA = (int) (.3f * MAX_ALPHA);

    private static final int CIRCLE_DIAMETER = 40;
    private static final int CIRCLE_DIAMETER_LARGE = 56;

    private static final float DECELERATE_INTERPOLATION_FACTOR = 2f;
    private static final int INVALID_POINTER = -1;
    private static final float DRAG_RATE = .5f;

    /**最大角度*/
    private static final float MAX_PROGRESS_ANGLE = .8f;
    /**缩小时间*/
    private static final int SCALE_DOWN_DURATION = 150;
    /**动画时间*/
    private static final int ALPHA_ANIMATION_DURATION = 300;
    /**激活触发时间*/
    private static final int ANIMATE_TO_TRIGGER_DURATION = 200;
    /**动画开始时间*/
    private static final int ANIMATE_TO_START_DURATION = 200;

    /**默认的旋转动画背景颜色*/
    private static final int CIRCLE_BG_LIGHT = 0xFFFAFAFA;
    /**从视图顶部到进度旋转器应该停止的位置的默认偏移量*/
    private static final int DEFAULT_CIRCLE_TARGET = 64;

    /**动画偏移左边的距离*/
    private int animPaddingLeft = 30;

    /**手势目标布局，整个布局*/
    private View mTarget;

    /**下拉刷新事件监听*/
    private OnRefreshListener mRefreshListener;
    /**上拉加载更多事件监听*/
    private OnLoadMoreListener mLoadMoreListener;
    /**底部状态监听器*/
    private OnFooterStateListener mFooterStateListener;

    /**是否在刷新*/
    private boolean isRefreshing = false;
    /***/
    private int mTouchSlop;
    private float mTotalDragDistance = -1;

    /**嵌套滚动,用于在触摸事件处理程序去确定OverScroll的地方*/
    private float mTotalUnconsumed;
    private final NestedScrollingParentHelper mNestedScrollingParentHelper;
    private final NestedScrollingChildHelper mNestedScrollingChildHelper;
    private final int[] mParentScrollConsumed = new int[2];
    private final int[] mParentOffsetInWindow = new int[2];
    private boolean mNestedScrollInProgress;

    private int mMediumAnimationDuration;
    private int mCurrentTargetOffsetTop;
    /**是否已确定起始偏移量*/
    private boolean mOriginalOffsetCalculated = false;

    private float mInitialMotionY;
    private float mInitialDownY;
    private boolean mIsBeingDragged;
    private int mActivePointerId = INVALID_POINTER;
    private boolean mScale;

    /**目标将返回到起始偏移量，在它被取消或刷新时触发*/
    private boolean mReturningToStart;
    private final DecelerateInterpolator mDecelerateInterpolator;
    private static final int[] LAYOUT_ATTRS = new int[] {android.R.attr.enabled};

    /**加载动画有关*/
    private CircleImageView mCircleView;
    private int mCircleViewIndex = -1;
    protected int mFrom;
    private float mStartingScale;
    protected int mOriginalOffsetTop;
    private MaterialProgressDrawable mProgress;
    private Animation mScaleAnimation;
    private Animation mScaleDownAnimation;
    private Animation mAlphaStartAnimation;
    private Animation mAlphaMaxAnimation;
    private Animation mScaleDownToStartAnimation;
    private float mSpinnerFinalOffset;
    private boolean mNotify;
    private int mCircleWidth;
    private int mCircleHeight;

    /**是否设置了自定义起始位置*/
    private boolean mUsingCustomStart;

    /**尾部容器*/
    private FooterLayout mFooterLayout;

    /**尾部View*/
    private View mFooter;

    /**尾部的高度*/
    private int mFooterHeight = FOOTER_DEFAULT_HEIGHT;

    /**滑动的偏移量*/
    private int mScrollOffset = 0;

    private boolean mPullRefreshEnable = true;
    /**是否可以上拉加载更多*/
    private boolean mPullLoadEnable = false;

    /**标记 无状态（既不是上拉 也 不是下拉）*/
    private final int ACTION_NOT = -1;
    /**标记 下拉状态*/
    private final int ACTION_DOWN = 0;
    /**标记 上拉状态*/
    private final int ACTION_UP = 1;
    private int mCurrentAction = ACTION_NOT;

    /**是否已完成*/
    private boolean isConfirm = false;

    /**是否还有更多数据*/
    private boolean isMore = true;

    public CustomSwipeRefreshLayout(Context context) {
        this(context, null);
    }

    public CustomSwipeRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomSwipeRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        mMediumAnimationDuration = getResources().getInteger(android.R.integer.config_mediumAnimTime);

        setWillNotDraw(false);
        mDecelerateInterpolator = new DecelerateInterpolator(DECELERATE_INTERPOLATION_FACTOR);

        final TypedArray a = context.obtainStyledAttributes(attrs, LAYOUT_ATTRS);
        setEnabled(a.getBoolean(0, true));
        a.recycle();

        // 获取加载动画的宽高
        final DisplayMetrics metrics = getResources().getDisplayMetrics();
        mCircleWidth = (int) (CIRCLE_DIAMETER * metrics.density);
        mCircleHeight = (int) (CIRCLE_DIAMETER * metrics.density);

        setClipToPadding(false);
        createProgressView();
        createFooterLayout();

        ViewCompat.setChildrenDrawingOrderEnabled(this, true);
        // the absolute offset has to take into account that the circle starts at an offset
        mSpinnerFinalOffset = DEFAULT_CIRCLE_TARGET * metrics.density;
        mTotalDragDistance = mSpinnerFinalOffset;
        mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);

        mNestedScrollingChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);

        animPaddingLeft = DensityUtils.dp2px(mContext, 20);
    }

    /**
     * 初始化底部
     */
    private void createFooterLayout() {
        mFooterLayout = new FooterLayout(mContext);
        LayoutParams lp = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
        mFooterLayout.setGravity(Gravity.CENTER | Gravity.BOTTOM);
        mFooterLayout.setLayoutParams(lp);

        mFooterLayout.setText(mContext.getResources().getString(R.string.load_by_pull_up));
        mFooterLayout.setTextColor(ContextCompat.getColor(mContext, R.color.colorGrayDark));
        addView(mFooterLayout);
    }

    /**
     * 设置尾部
     * */
    public void setFooter(View footer) {
        mFooter = footer;

        mFooterLayout.setFooterView(mFooter);

        //获取尾部高度
        mFooterHeight = measureViewHeight(mFooterLayout);
        Log.e(LOG_TAG, "mFooterHeight = " + mFooterHeight);
    }

    /**
     * 计算尾部高度
     * */
    private int measureViewHeight(View view) {
        int width = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        view.measure(width, height);
        return view.getMeasuredHeight();
    }

    /**
     * 重置动画
     * */
    private void reset() {
        mCircleView.clearAnimation();
        mProgress.stop();
        mCircleView.setVisibility(View.GONE);
        setColorViewAlpha(MAX_ALPHA);
        if (mScale) {
            // 动画完成和视图隐藏
            setAnimationProgress(0);
        } else {
            // 需要更新
            setTargetOffsetTopAndBottom(mOriginalOffsetTop - mCurrentTargetOffsetTop, true);
        }
        mCurrentTargetOffsetTop = mCircleView.getTop();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        reset();
    }

    private void setColorViewAlpha(int targetAlpha) {
        mCircleView.getBackground().setAlpha(targetAlpha);
        mProgress.setAlpha(targetAlpha);
    }

    public void setProgressViewOffset(boolean scale, int start, int end) {
        mScale = scale;
        mCircleView.setVisibility(View.GONE);
        mOriginalOffsetTop = mCurrentTargetOffsetTop = start;
        mSpinnerFinalOffset = end;
        mUsingCustomStart = true;
        mCircleView.invalidate();
    }

    public void setProgressViewEndTarget(boolean scale, int end) {
        mSpinnerFinalOffset = end;
        mScale = scale;
        mCircleView.invalidate();
    }

    /**
     * 设置旋转动画大小
     */
    public void setSize(int size) {
        if (size != LARGE && size != DEFAULT) {
            return;
        }
        final DisplayMetrics metrics = getResources().getDisplayMetrics();
        if (size == LARGE) {
            mCircleHeight = mCircleWidth = (int) (CIRCLE_DIAMETER_LARGE * metrics.density);
        } else {
            mCircleHeight = mCircleWidth = (int) (CIRCLE_DIAMETER * metrics.density);
        }

        mCircleView.setImageDrawable(null);
        mProgress.updateSizes(size);
        mCircleView.setImageDrawable(mProgress);
    }

    protected int getChildDrawingOrder(int childCount, int i) {
        if (mCircleViewIndex < 0) {
            return i;
        } else if (i == childCount - 1) {
            // 最后绘制的view
            return mCircleViewIndex;
        } else if (i >= mCircleViewIndex) {
            // 在选定view之前移动view
            return i + 1;
        } else {
            // 保持view与选定的view一样
            return i;
        }
    }

    /**
     * 加载动画
     * */
    public void setProgressView(MaterialProgressDrawable mProgress){
        this.mProgress = mProgress;
        mCircleView.setImageDrawable(mProgress);
    }

    /**
     * 创建加载动画
     * */
    private void createProgressView() {
        mCircleView = new CircleImageView(mContext, CIRCLE_BG_LIGHT, CIRCLE_DIAMETER/2);
//        mProgress = new MaterialProgressDrawable(mContext, this);
//        mProgress.setBackgroundColor(CIRCLE_BG_LIGHT);

        CustomProgressDrawable drawable = new CustomProgressDrawable(mContext, this);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_refresh_icon);
        drawable.setBitmap(bitmap);
        mProgress = drawable;

        mCircleView.setImageDrawable(mProgress);
        mCircleView.setVisibility(View.GONE);
        addView(mCircleView);
    }

    /**
     * Api11之前，alpha用于使进度循环代替规模出现
     */
    private boolean isAlphaUsedForScale() {
        return android.os.Build.VERSION.SDK_INT < 11;
    }

    /**
     * 设置动画位置
     * */
    public void setAnimPosition(int position) {
        mAnimPosition = position;
    }

    /**
     * 通知窗口，刷新状态改变
     *
     * @param refreshing 视图是否应该显示刷新进度。
     */
    public void setRefreshing(final boolean refreshing) {
        if (refreshing && isRefreshing != refreshing) {
            isRefreshing = refreshing;
            int endTarget;
            if (!mUsingCustomStart) {
                endTarget = (int) (mSpinnerFinalOffset + mOriginalOffsetTop);
            } else {
                endTarget = (int) mSpinnerFinalOffset;
            }
            // 更新状态
            setTargetOffsetTopAndBottom(endTarget - mCurrentTargetOffsetTop, true);
            mNotify = false;
            startScaleUpAnimation(mRefreshingListener);
        } else {
            // 刷新
            setRefreshing(refreshing, false);
        }
    }

    /**
     * 设置是否启用上拉功能
     *
     * @param isLoadMore   是否开启上拉功能 默认不开启
     */
    public void setLoadMore(boolean isLoadMore) {
        mPullLoadEnable = isLoadMore;
    }

    private void startScaleUpAnimation(Animation.AnimationListener listener) {
        mCircleView.setVisibility(View.VISIBLE);
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            mProgress.setAlpha(MAX_ALPHA);
        }
        mScaleAnimation = new Animation() {
            @Override
            public void applyTransformation(float interpolatedTime, Transformation t) {
                setAnimationProgress(interpolatedTime);
            }
        };
        mScaleAnimation.setDuration(mMediumAnimationDuration);
        if (listener != null) {
            mCircleView.setAnimationListener(listener);
        }
        mCircleView.clearAnimation();
        mCircleView.startAnimation(mScaleAnimation);
    }

    /**
     * Pre API 11
     * @param progress
     */
    private void setAnimationProgress(float progress) {
        if (isAlphaUsedForScale()) {
            setColorViewAlpha((int) (progress * MAX_ALPHA));
        } else {
            ViewCompat.setScaleX(mCircleView, progress);
            ViewCompat.setScaleY(mCircleView, progress);
        }
    }

    /**
     * 刷新状态
     * */
    private void setRefreshing(boolean refreshing, final boolean notify) {
        if (isRefreshing != refreshing) {
            mNotify = notify;
            ensureTarget();
            isRefreshing = refreshing;
            if (isRefreshing) {
                animateOffsetToCorrectPosition(mCurrentTargetOffsetTop, mRefreshingListener);
            } else {
                startScaleDownAnimation(mRefreshingListener);
            }
        }
    }

    private void startScaleDownAnimation(Animation.AnimationListener listener) {

        //  最终的偏移量就是mCircleView距离顶部的高度
        final int deltaY = -mCircleView.getBottom();
        mScaleDownAnimation = new TranslateAnimation(0,0,0,deltaY);
//            mScaleDownAnimation.setDuration(SCALE_DOWN_DURATION);
        mScaleDownAnimation.setDuration(500);
        mCircleView.setAnimationListener(listener);
        mCircleView.clearAnimation();
        mCircleView.startAnimation(mScaleDownAnimation);
    }

    private void startProgressAlphaStartAnimation() {
        mAlphaStartAnimation = startAlphaAnimation(mProgress.getAlpha(), STARTING_PROGRESS_ALPHA);
    }

    private void startProgressAlphaMaxAnimation() {
        mAlphaMaxAnimation = startAlphaAnimation(mProgress.getAlpha(), MAX_ALPHA);
    }

    private Animation startAlphaAnimation(final int startingAlpha, final int endingAlpha) {
        if (mScale && isAlphaUsedForScale()) {
            return null;
        }
        Animation alpha = new Animation() {
            @Override
            public void applyTransformation(float interpolatedTime, Transformation t) {
                mProgress
                        .setAlpha((int) (startingAlpha+ ((endingAlpha - startingAlpha)
                                * interpolatedTime)));
            }
        };
        alpha.setDuration(ALPHA_ANIMATION_DURATION);

        mCircleView.setAnimationListener(null);
        mCircleView.clearAnimation();
        mCircleView.startAnimation(alpha);

        return alpha;
    }

    @Deprecated
    public void setProgressBackgroundColor(int colorRes) {
        setProgressBackgroundColorSchemeResource(colorRes);
    }

    /**
     * 设置加载动画的背景颜色。
     *
     * @param colorRes Resource id of the color.
     */
    public void setProgressBackgroundColorSchemeResource(@ColorRes int colorRes) {
        setProgressBackgroundColorSchemeColor(ContextCompat.getColor(mContext, colorRes));
    }

    /**
     * 设置加载动画的背景颜色。
     *
     * @param color
     */
    public void setProgressBackgroundColorSchemeColor(@ColorInt int color) {
        mCircleView.setBackgroundColor(color);
        mProgress.setBackgroundColor(color);
    }

    @Deprecated
    public void setColorScheme(@ColorInt int... colors) {
        //setColorSchemeResources(colors);
    }

    /**
     * 设置用于从色彩资源进度动画色彩资源。第一种颜色也将是根据用户刷卡手势而增长的颜色。
     *
     * @param colorResIds
     */
    public void setColorSchemeResources(@ColorRes int... colorResIds) {
        final Resources res = getResources();
        int[] colorRes = new int[colorResIds.length];
        for (int i = 0; i < colorResIds.length; i++) {
            colorRes[i] = res.getColor(colorResIds[i]);
        }
        setColorSchemeColors(colorRes);
    }

    /**
     * 设置用于从色彩资源进度动画色彩资源。第一种颜色也将是根据用户刷卡手势而增长的颜色。
     *
     * @param colors
     */
    @SuppressLint("SupportAnnotationUsage")
    @ColorInt
    public void setColorSchemeColors(int... colors) {
        ensureTarget();
        mProgress.setColorSchemeColors(colors);
    }

    /**
     * 返回是否正在刷新
     */
    public boolean isRefreshing() {
        return isRefreshing;
    }

    /**
     * 获取内容模块
     * */
    private void ensureTarget() {
        if (mTarget == null) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (!child.equals(mCircleView) && !child.equals(mFooterLayout)) {
                    mTarget = child;
                    break;
                }
            }
        }
    }

    public void setDistanceToTriggerSync(int distance) {
        mTotalDragDistance = distance;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();
        if (getChildCount() == 0) {
            return;
        }
        // 添加了空布局，要重新获取target
        if (mTarget == null) {
            ensureTarget();
        }
        if (mTarget == null) {
            return;
        }

        final View child = mTarget;

        final int childLeft = getPaddingLeft();
        final int childTop = getPaddingTop();
        final int childWidth = width - getPaddingLeft() - getPaddingRight();
        final int childHeight = height - getPaddingTop() - getPaddingBottom();

        // 内容
        child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);

        int circleWidth = mCircleView.getMeasuredWidth();
        int circleHeight = mCircleView.getMeasuredHeight();
        if (mAnimPosition == ANIM_IN_CENTER) {
            // 加载动画在中间
            mCircleView.layout((width / 2 - circleWidth / 2), mCurrentTargetOffsetTop,
                    (width / 2 + circleWidth / 2), mCurrentTargetOffsetTop + circleHeight);
        } else {
            // 加载动画在左边
//            mCircleView.layout(childLeft, mCurrentTargetOffsetTop,
//                    childLeft + circleWidth, mCurrentTargetOffsetTop + circleHeight);
            mCircleView.layout(animPaddingLeft, mCurrentTargetOffsetTop,
                    animPaddingLeft + circleWidth, mCurrentTargetOffsetTop + circleHeight);
        }

        // 底部
        mFooterLayout.layout(getPaddingLeft(), getMeasuredHeight(), getPaddingLeft() + width, childHeight + mFooterHeight);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mTarget == null) {
            ensureTarget();
        }
        if (mTarget == null) {
            return;
        }

        mTarget.measure(MeasureSpec.makeMeasureSpec(
                getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
                MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(
                getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY));

        mCircleView.measure(MeasureSpec.makeMeasureSpec(mCircleWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(mCircleHeight, MeasureSpec.EXACTLY));

        // 测量底部高度
        measureChild(mFooterLayout, widthMeasureSpec, heightMeasureSpec);

        if (!mUsingCustomStart && !mOriginalOffsetCalculated) {
            mOriginalOffsetCalculated = true;
            mCurrentTargetOffsetTop = mOriginalOffsetTop = -mCircleView.getMeasuredHeight();
        }
        mCircleViewIndex = -1;
        // 获取加载动画所在的地方
        for (int index = 0; index < getChildCount(); index++) {
            if (getChildAt(index) == mCircleView) {
                mCircleViewIndex = index;
                break;
            }
        }
    }

    /**
     * 获取显示为“刷新”布局的一部分的进度圆的直径。
     *
     * @return Diameter in pixels of the progress circle view.
     */
    public int getProgressCircleDiameter() {
        return mCircleView != null?mCircleView.getMeasuredHeight() : 0;
    }

    /**
     * @return 此布局的子视图是否可以向上滚动。重写此如果孩子的观点是一个自定义视图
     */
    public boolean canChildScrollDown() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mTarget instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mTarget;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return ViewCompat.canScrollVertically(mTarget, -1) || mTarget.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mTarget, -1);
        }
    }

    /**
     * 判断是否可以上拉
     * */
    public boolean canChildScrollUp() {
        if (mTarget == null) {
            return false;
        } else if (android.os.Build.VERSION.SDK_INT >= 14) {
            return ViewCompat.canScrollVertically(mTarget, 1);
        } else if (mTarget instanceof AbsListView) {
            AbsListView absListView = (AbsListView)mTarget;
            if (absListView.getChildCount() <= 0) {
                return false;
            } else {
                int lastChildBottom = absListView.getChildAt(absListView.getChildCount() - 1).getBottom();
                return absListView.getLastVisiblePosition() == (absListView.getAdapter()).getCount() - 1 && lastChildBottom <= absListView.getMeasuredHeight();
            }
        } else {
            return ViewCompat.canScrollVertically(mTarget, 1) || mTarget.getScrollY() > 0;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        ensureTarget();

        final int action = MotionEventCompat.getActionMasked(ev);

        if (mReturningToStart && action == MotionEvent.ACTION_DOWN) {
            mReturningToStart = false;
        }

        if (!isEnabled() || mReturningToStart || canChildScrollDown()
                || isRefreshing || mNestedScrollInProgress) {
            // Fail fast if we're not in a state where a swipe is possible
            return false;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                setTargetOffsetTopAndBottom(mOriginalOffsetTop - mCircleView.getTop(), true);
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                mIsBeingDragged = false;
                final float initialDownY = getMotionEventY(ev, mActivePointerId);
                if (initialDownY == -1) {
                    return false;
                }
                mInitialDownY = initialDownY;

                break;

            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_POINTER) {
                    Log.e(LOG_TAG, "Got ACTION_MOVE event but don't have an active pointer id.");
                    return false;
                }

                final float y = getMotionEventY(ev, mActivePointerId);
                if (y == -1) {
                    return false;
                }
                final float yDiff = y - mInitialDownY;
                if (yDiff > mTouchSlop && !mIsBeingDragged) {
                    mInitialMotionY = mInitialDownY + mTouchSlop;
                    mIsBeingDragged = true;
                    mProgress.setAlpha(STARTING_PROGRESS_ALPHA);
                }
                break;

            case MotionEventCompat.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                break;
        }

        return mIsBeingDragged;
    }

    @SuppressLint("NewApi")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);
        int pointerIndex;

        if (mReturningToStart && action == MotionEvent.ACTION_DOWN) {
            mReturningToStart = false;
        }

        if (!isEnabled() || mReturningToStart || canChildScrollDown() || mNestedScrollInProgress) {
            // Fail fast if we're not in a state where a swipe is possible
            return false;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                mIsBeingDragged = false;
                break;

                case MotionEvent.ACTION_MOVE: {
                    // 下拉
                    pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                    if (pointerIndex < 0) {
                        Log.e(LOG_TAG, "Got ACTION_MOVE event but have an invalid active pointer id.");
                        return false;
                    }

                    final float y = MotionEventCompat.getY(ev, pointerIndex);
                    // 记录手指移动的距离,mInitialMotionY是初始的位置，DRAG_RATE是拖拽因子。
                    final float overScrollTop = (y - mInitialMotionY) * DRAG_RATE;
                    // 赋值给mTarget的top使之产生拖动效果
                    mTarget.setTranslationY(overScrollTop);
                    if (mIsBeingDragged) {
                        if (overScrollTop > 0) {
                            moveSpinner(overScrollTop);
                        } else {
                            return false;
                        }
                    }
                break;
            }

            case MotionEventCompat.ACTION_POINTER_DOWN: {
                pointerIndex = MotionEventCompat.getActionIndex(ev);
                if (pointerIndex < 0) {
                    Log.e(LOG_TAG, "Got ACTION_POINTER_DOWN event but have an invalid action index.");
                    return false;
                }
                mActivePointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
                break;
            }

            case MotionEventCompat.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;

            case MotionEvent.ACTION_UP: {
                // 手指松开时启动动画回到头部
                mTarget.animate().translationY(0).setDuration(200).start();

                pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                if (pointerIndex < 0) {
                    Log.e(LOG_TAG, "Got ACTION_UP event but don't have an active pointer id.");
                    return false;
                }

                final float y = MotionEventCompat.getY(ev, pointerIndex);
                final float overScrollTop = (y - mInitialMotionY) * DRAG_RATE;
                mIsBeingDragged = false;
                finishSpinner(overScrollTop);
                mActivePointerId = INVALID_POINTER;
                return false;
            }
            case MotionEvent.ACTION_CANCEL:
                return false;
        }

        return true;
    }

    private float getMotionEventY(MotionEvent ev, int activePointerId) {
        final int index = MotionEventCompat.findPointerIndex(ev, activePointerId);
        if (index < 0) {
            return -1;
        }
        return MotionEventCompat.getY(ev, index);
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean b) {
        if ((android.os.Build.VERSION.SDK_INT < 21 && mTarget instanceof AbsListView)
                || (mTarget != null && !ViewCompat.isNestedScrollingEnabled(mTarget))) {
        } else {
            super.requestDisallowInterceptTouchEvent(b);
        }
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return isEnabled() && canChildScrollDown() && !mReturningToStart && !isRefreshing
                && (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes);
        startNestedScroll(axes & ViewCompat.SCROLL_AXIS_VERTICAL);
        mTotalUnconsumed = 0;
        mNestedScrollInProgress = true;
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
//        // If we are in the middle of consuming, a scroll, then we want to move the spinner back up
//        // before allowing the list to scroll
//        if (dy > 0 && mTotalUnconsumed > 0) {
//            if (dy > mTotalUnconsumed) {
//                consumed[1] = dy - (int) mTotalUnconsumed;
//                mTotalUnconsumed = 0;
//            } else {
//                mTotalUnconsumed -= dy;
//                consumed[1] = dy;
//
//            }
//            moveSpinner(mTotalUnconsumed);
//        }
//
//        // If a client layout is using a custom start position for the circle
//        // view, they mean to hide it again before scrolling the child view
//        // If we get back to mTotalUnconsumed == 0 and there is more to go, hide
//        // the circle so it isn't exposed if its blocking content is moved
//        if (mUsingCustomStart && dy > 0 && mTotalUnconsumed == 0
//                && Math.abs(dy - consumed[1]) > 0) {
//            mCircleView.setVisibility(View.GONE);
//        }
//
//        // Now let our nested parent consume the leftovers
//        final int[] parentConsumed = mParentScrollConsumed;
//        if (dispatchNestedPreScroll(dx - consumed[0], dy - consumed[1], parentConsumed, null)) {
//            consumed[0] += parentConsumed[0];
//            consumed[1] += parentConsumed[1];
//        }

        if (mPullRefreshEnable || mPullLoadEnable) {
            if (Math.abs(dy) <= FOOTER_DEFAULT_HEIGHT) {
                if (!isConfirm) {
                    if (dy < 0 && !canChildScrollDown()) {
                        mCurrentAction = ACTION_DOWN;
                        isConfirm = true;
                    } else if (dy > 0 && !canChildScrollUp()) {
                        mCurrentAction = ACTION_UP;
                        isConfirm = true;
                    } else {
                        mCurrentAction = ACTION_NOT;
                        isConfirm = false;
                    }
                }

                if (moveGuidanceView((float)(-dy))) {
                    consumed[1] += dy;
                }

            }
        }
    }

    /**
     * 移动布局
     * */
    private boolean moveGuidanceView(float distanceY) {
        if(isRefreshing) {
            return false;
        } else {
            LayoutParams lp;

            if(!canChildScrollDown() && mPullRefreshEnable && mCurrentAction == ACTION_DOWN) {
                // 下拉
                return true;
            } else if (!canChildScrollUp() && mPullLoadEnable && mCurrentAction == ACTION_UP) {
                // 上拉
                lp = mFooterLayout.getLayoutParams();
                lp.height = (int)((float)lp.height - distanceY);
                if (lp.height < 0) {
                    lp.height = 0;
                }

                if ((float)lp.height > 300) {
                    lp.height = 300;
                }

                if (lp.height == 0) {
                    isConfirm = false;
                    mCurrentAction = ACTION_NOT;
                }

                mFooterLayout.setLayoutParams(lp);
                moveTargetView(lp.height);

                return true;
            } else {
                resetLoadMoreState();
                return false;
            }
        }
    }

    /**
     * 移动布局，用来显示底部
     * */
    private void moveTargetView(int offset) {
        //this.mTarget.setTranslationY(h);
        if (offset > 0 && mCurrentAction != ACTION_UP) {
            return;
        }

        mScrollOffset = Math.abs(offset);

        scrollTo(0, offset);

        if(mFooterStateListener != null && isMore) {
            mFooterStateListener.onScrollChange(mFooter, mScrollOffset,
                    mScrollOffset >= mFooterHeight ? FOOTER_DEFAULT_HEIGHT : mScrollOffset * FOOTER_DEFAULT_HEIGHT / mFooterHeight);
        }
    }

    @Override
    public int getNestedScrollAxes() {
        return mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    @Override
    public void onStopNestedScroll(View target) {
        mNestedScrollingParentHelper.onStopNestedScroll(target);
        mNestedScrollInProgress = false;
        // Finish the spinner for nested scrolling if we ever consumed any
        // unconsumed nested scroll
        if (mTotalUnconsumed > 0) {
            finishSpinner(mTotalUnconsumed);
            mTotalUnconsumed = 0;
        }
        // Dispatch up our nested parent
        stopNestedScroll();
        handlerAction();
    }

    /**
     * 处理事件
     * */
    private void handlerAction() {
        if(!isRefreshing()) {
            isConfirm = false;
            LayoutParams lp;
            // 下拉
            if(mPullRefreshEnable && mCurrentAction == ACTION_DOWN) {
                // 不处理
            }
            // 上拉
            if(mPullLoadEnable && mCurrentAction == ACTION_UP) {
                lp = mFooterLayout.getLayoutParams();
                if((float)lp.height >= mFooterHeight) {
                    // 开始执行加载更多
                    if (isMore) {
                        startLoadMore(lp.height);
                    }

                    // 刷新状态
                    if (mFooterStateListener != null && isMore) {
                        mFooterStateListener.onLoading(mFooter);
                    } else {
                        loadMoreFinish();
                    }
                } else if(lp.height > 0) {
                    resetFootView(lp.height);
                } else {
                    resetLoadMoreState();
                }
            }
        }
    }

    /**
     * 开始加载更多
     * */
    protected void startLoadMore(int footerViewHeight) {
        this.isRefreshing = true;
        ValueAnimator animator = ValueAnimator.ofFloat(new float[]{(float)footerViewHeight, FOOTER_DEFAULT_HEIGHT});
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                LayoutParams lp = mFooterLayout.getLayoutParams();
                lp.height = (int)((Float)animation.getAnimatedValue()).floatValue();
                mFooterLayout.setLayoutParams(lp);
                moveTargetView(lp.height);
            }
        });

        animator.addListener(new LoadMoreAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (mLoadMoreListener != null) {
                    mLoadMoreListener.onLoadMore();
                }
            }
        });
        animator.setDuration(300L);
        animator.start();
    }

    private void resetFootView(int footerViewHeight) {
        ValueAnimator animator = ValueAnimator.ofFloat(new float[]{(float)footerViewHeight, 0.0F});
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                LayoutParams lp = mFooterLayout.getLayoutParams();
                lp.height = (int)((Float)animation.getAnimatedValue()).floatValue();
                mFooterLayout.setLayoutParams(lp);
                moveTargetView(lp.height);
            }
        });
        animator.addListener(new LoadMoreAnimatorListener() {
            public void onAnimationEnd(Animator animation) {
                resetLoadMoreState();
            }
        });
        animator.setDuration(300L);
        animator.start();
    }

    /**
     * 重置加载状态
     * */
    private void resetLoadMoreState() {
        isRefreshing = false;
        isConfirm = false;
        mCurrentAction = ACTION_NOT;

        moveTargetView(0);
    }

    @Override
    public void onNestedScroll(final View target, final int dxConsumed, final int dyConsumed, final int dxUnconsumed, final int dyUnconsumed) {
        // 首先嵌套到父级
        dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, mParentOffsetInWindow);

        final int dy = dyUnconsumed + mParentOffsetInWindow[1];
        if (dy < 0) {
            mTotalUnconsumed += Math.abs(dy);
            moveSpinner(mTotalUnconsumed);
        }
    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mNestedScrollingChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return mNestedScrollingChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return mNestedScrollingChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        mNestedScrollingChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mNestedScrollingChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return mNestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return mNestedScrollingChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return dispatchNestedPreFling(velocityX, velocityY);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mNestedScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mNestedScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

    private boolean isAnimationRunning(Animation animation) {
        return animation != null && animation.hasStarted() && !animation.hasEnded();
    }

    private void moveSpinner(float overScrollTop) {
        mProgress.showArrow(true);
        float originalDragPercent = overScrollTop / mTotalDragDistance;

        float dragPercent = Math.min(1f, Math.abs(originalDragPercent));
        float adjustedPercent = (float) Math.max(dragPercent - .4, 0) * 5 / 3;
        float extraOS = Math.abs(overScrollTop) - mTotalDragDistance;
        float slingshotDist = mUsingCustomStart ? mSpinnerFinalOffset - mOriginalOffsetTop : mSpinnerFinalOffset;
        float tensionSlingshotPercent = Math.max(0, Math.min(extraOS, slingshotDist * 2) / slingshotDist);
        float tensionPercent = (float) ((tensionSlingshotPercent / 4) - Math.pow((tensionSlingshotPercent / 4), 2)) * 2f;
        float extraMove = (slingshotDist) * tensionPercent * 2;

        int targetY = mOriginalOffsetTop + (int) ((slingshotDist * dragPercent) + extraMove);
        // where 1.0f is a full circle
        if (mCircleView.getVisibility() != View.VISIBLE) {
            mCircleView.setVisibility(View.VISIBLE);
        }
        if (!mScale) {
            ViewCompat.setScaleX(mCircleView, 1f);
            ViewCompat.setScaleY(mCircleView, 1f);
        }

        if (mScale) {
            setAnimationProgress(Math.min(1f, overScrollTop / mTotalDragDistance));
        }
        if (overScrollTop < mTotalDragDistance) {
            if (mProgress.getAlpha() > STARTING_PROGRESS_ALPHA
                    && !isAnimationRunning(mAlphaStartAnimation)) {
                // Animate the alpha
                startProgressAlphaStartAnimation();
            }
        } else {
            if (mProgress.getAlpha() < MAX_ALPHA && !isAnimationRunning(mAlphaMaxAnimation)) {
                // Animate the alpha
                startProgressAlphaMaxAnimation();
            }
        }
        float strokeStart = adjustedPercent * .8f;
        mProgress.setStartEndTrim(0f, Math.min(MAX_PROGRESS_ANGLE, strokeStart));
        mProgress.setArrowScale(Math.min(1f, adjustedPercent));
        // 经过一堆数学处理后的rotation
        float rotation = (-0.25f + .4f * adjustedPercent + tensionPercent * 2) * .5f;
        mProgress.setProgressRotation(rotation);
//            setTargetOffsetTopAndBottom(targetY - mCurrentTargetOffsetTop, true /* requires update */);
        // 最终刷新的位置
        int endTarget;
        if (!mUsingCustomStart) {
            // 没有修改使用默认的值
            endTarget = (int) (mSpinnerFinalOffset - Math.abs(mOriginalOffsetTop));
        } else {
            // 否则使用定义的值
            endTarget = (int) mSpinnerFinalOffset;
        }
        if(targetY>=endTarget){
            // 下移的位置超过最终位置后就不再下移，第一个参数为偏移量
            setTargetOffsetTopAndBottom(0, true);
        } else {
            // 否则继续继续下移
            setTargetOffsetTopAndBottom(targetY - mCurrentTargetOffsetTop, true /* requires update */);
        }
    }

    /**
     * 完成动画旋转
     * */
    private void finishSpinner(float overScrollTop) {
        if (overScrollTop > mTotalDragDistance) {
            // 刷新
            setRefreshing(true, true);
        } else {
            // cancel refresh
            isRefreshing = false;
            mProgress.setStartEndTrim(0f, 0f);
            Animation.AnimationListener listener = null;
            if (!mScale) {
                listener = new Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (!mScale) {
                            startScaleDownAnimation(null);
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                };
            }
            animateOffsetToStartPosition(mCurrentTargetOffsetTop, listener);
            mProgress.showArrow(false);
        }
    }

    private void animateOffsetToCorrectPosition(int from, Animation.AnimationListener listener) {
        mFrom = from;
        mAnimateToCorrectPosition.reset();
        mAnimateToCorrectPosition.setDuration(ANIMATE_TO_TRIGGER_DURATION);
        mAnimateToCorrectPosition.setInterpolator(mDecelerateInterpolator);
        if (listener != null) {
            mCircleView.setAnimationListener(listener);
        }
        mCircleView.clearAnimation();
        mCircleView.startAnimation(mAnimateToCorrectPosition);
    }

    private void animateOffsetToStartPosition(int from, Animation.AnimationListener listener) {
        if (mScale) {
            // Scale the item back down
            startScaleDownReturnToStartAnimation(from, listener);
        } else {
            mFrom = from;
            mAnimateToStartPosition.reset();
            mAnimateToStartPosition.setDuration(ANIMATE_TO_START_DURATION);
            mAnimateToStartPosition.setInterpolator(mDecelerateInterpolator);
            if (listener != null) {
                mCircleView.setAnimationListener(listener);
            }
            mCircleView.clearAnimation();
            mCircleView.startAnimation(mAnimateToStartPosition);
        }
    }

    private final Animation mAnimateToCorrectPosition = new Animation() {
        @Override
        public void applyTransformation(float interpolatedTime, Transformation t) {
            int targetTop;
            int endTarget;
            if (!mUsingCustomStart) {
                endTarget = (int) (mSpinnerFinalOffset - Math.abs(mOriginalOffsetTop));
            } else {
                endTarget = (int) mSpinnerFinalOffset;
            }
            targetTop = (mFrom + (int) ((endTarget - mFrom) * interpolatedTime));
            int offset = targetTop - mCircleView.getTop();
            setTargetOffsetTopAndBottom(offset, false /* requires update */);
            mProgress.setArrowScale(1 - interpolatedTime);
        }
    };

    private void moveToStart(float interpolatedTime) {
        int targetTop = 0;
        targetTop = (mFrom + (int) ((mOriginalOffsetTop - mFrom) * interpolatedTime));
        int offset = targetTop - mCircleView.getTop();
        setTargetOffsetTopAndBottom(offset, false /* requires update */);
    }

    private final Animation mAnimateToStartPosition = new Animation() {
        @Override
        public void applyTransformation(float interpolatedTime, Transformation t) {
            moveToStart(interpolatedTime);
        }
    };

    private void startScaleDownReturnToStartAnimation(int from, Animation.AnimationListener listener) {
        mFrom = from;
        if (isAlphaUsedForScale()) {
            mStartingScale = mProgress.getAlpha();
        } else {
            mStartingScale = ViewCompat.getScaleX(mCircleView);
        }
        mScaleDownToStartAnimation = new Animation() {
            @Override
            public void applyTransformation(float interpolatedTime, Transformation t) {
                float targetScale = (mStartingScale + (-mStartingScale  * interpolatedTime));
                setAnimationProgress(targetScale);
                moveToStart(interpolatedTime);
            }
        };
        mScaleDownToStartAnimation.setDuration(SCALE_DOWN_DURATION);
        if (listener != null) {
            mCircleView.setAnimationListener(listener);
        }
        mCircleView.clearAnimation();
        mCircleView.startAnimation(mScaleDownToStartAnimation);
    }

    private void setTargetOffsetTopAndBottom(int offset, boolean requiresUpdate) {
        mCircleView.bringToFront();
        mCircleView.offsetTopAndBottom(offset);
        mCurrentTargetOffsetTop = mCircleView.getTop();
        if (requiresUpdate && android.os.Build.VERSION.SDK_INT < 11) {
            invalidate();
        }
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = MotionEventCompat.getActionIndex(ev);
        final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
        if (pointerId == mActivePointerId) {
            // This was our active pointer going up. Choose a new
            // active pointer and adjust accordingly.
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
        }
    }

    /**
     * 刷新监听
     * */
    private Animation.AnimationListener mRefreshingListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (isRefreshing) {
                // Make sure the progress view is fully visible
                mProgress.setAlpha(MAX_ALPHA);
                mProgress.start();
                if (mNotify) {
                    if (mRefreshListener != null) {
                        mRefreshListener.onRefresh();
                    }
                }
                mCurrentTargetOffsetTop = mCircleView.getTop();
            } else {
                reset();
            }
        }
    };

    /**
     * 是否上拉加载更多
     * */
    protected boolean pullUp() {
        return mCurrentAction != ACTION_DOWN && mPullLoadEnable;
    }

    /**
     * 完成刷新
     * */
    public void refreshFinish() {
        setRefreshing(false);
    }

    /**
     * 完成加载更多
     * */
    public void loadMoreFinish() {
        if(mCurrentAction == ACTION_UP) {
            resetFootView(mFooterLayout == null?0:mFooterLayout.getMeasuredHeight());

            if (mFooterStateListener != null && isMore) {
                mFooterStateListener.onRetract(mFooter);
            }
        }
    }

    public void isMore(boolean isMore) {
        this.isMore = isMore;
        if (mFooterStateListener != null) {
            if (isMore) {
                mFooterStateListener.onHasMore(mFooter);
            } else {
                mFooterStateListener.onNoMore(mFooter);
            }
        }
    }

    protected void resetTarget() {
        mTarget = null;
    }

    /**
     * 下拉刷新
     * */
    public interface OnRefreshListener {
        void onRefresh();
    }

    /**
     * 下拉刷新
     * */
    public void setOnRefreshListener(OnRefreshListener listener) {
        mRefreshListener = listener;
    }

    /**
     * 上拉加载更多
     * */
    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.mLoadMoreListener = listener;
    }

    /**
     * 设置底部监听器
     *
     * @param listener
     */
    public void setOnFooterStateListener(OnFooterStateListener listener) {
        mFooterStateListener = listener;
    }
}
