package com.gbq.library.swiperefresh;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.gbq.library.R;

/**
 * 类说明：SwipeRecyclerView
 *          SwipeRefreshLayout封装RecyclerView
 * Author: Kuzan
 * Date: 2017/8/05 17:12.
 */
public class SwipeRecyclerView extends CustomSwipeRefreshLayout {

    /**内置的RecyclerView*/
    private RecyclerView mRecyclerView;

    /**空数据提示布局容器*/
    private LinearLayout mEmptyLayout;
    /**是否显示空布局*/
    private boolean isShowEmpty = false;

    /**
     * 是否自动上拉刷新
     */
    private boolean isAutomaticUp = false;

    public SwipeRecyclerView(Context context) {
        this(context, null);
    }

    public SwipeRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRecyclerView = (RecyclerView) LayoutInflater.from(mContext).inflate(R.layout.layout_recyler_view, null);
        this.addView(mRecyclerView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        setScrollBarEnabled(false);
        //setNestedScrollingEnabled(false);
        initListener();
    }

    /**
     * 自动上拉更新
     * */
    private void initListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (isAutomaticUp && newState == RecyclerView.SCROLL_STATE_IDLE && pullUp()) {
                    startLoadMore(CustomSwipeRefreshLayout.FOOTER_DEFAULT_HEIGHT);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });
    }

    /**
     * 设置空布局
     *
     * @param emptyView
     * @param layoutGravity 空布局在父布局的方向
     */
    public void setEmptyView(View emptyView, int layoutGravity) {
        if (mEmptyLayout == null) {
            initEmptyLayout();
        }
        mEmptyLayout.setGravity(layoutGravity);
        mEmptyLayout.addView(emptyView);
    }

    /**
     * 显示空布局
     */
    public void showEmptyView() {
        if (mEmptyLayout != null && mEmptyLayout.getParent() == null) {
            int index = getRecyclerViewIndex();
            if (index > 0) {
                resetTarget();
                isShowEmpty = true;
                this.addView(mEmptyLayout, index);

                if (mRecyclerView.getParent() != null) {
                    removeView(mRecyclerView);
                }
            }
        }
    }

    /**
     * 隐藏空布局
     */
    public void hideEmptyView() {
        if (mRecyclerView.getParent() == null) {
            int index = getEmptyViewIndex();
            if (index > 0) {
                resetTarget();
                isShowEmpty = false;
                addView(mRecyclerView, index);

                if (mEmptyLayout != null && mEmptyLayout.getParent() != null) {
                    removeView(mEmptyLayout);
                }
            }
        }
    }

    /**
     * 初始化空布局
     * */
    private void initEmptyLayout() {
        mEmptyLayout = new LinearLayout(mContext);
        LayoutParams lp = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mEmptyLayout.setLayoutParams(lp);
        mEmptyLayout.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    /**
     * 获取是否显示空布局
     * */
    public boolean isShowEmpty() {
        return isShowEmpty;
    }

    /**
     * 获取RecyclerView所在的位置
     * */
    private int getRecyclerViewIndex() {
        int index = -1;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.equals(mRecyclerView)) {
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     * 获取EmptyView所在的位置
     * */
    private int getEmptyViewIndex() {
        int index = -1;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.equals(mEmptyLayout)) {
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     * 滑动到底部时，是否自动触发上拉加载更多。
     *
     * @param isAutomaticUp
     */
    public void isAutomaticUp(boolean isAutomaticUp) {
        this.isAutomaticUp = isAutomaticUp;
    }

    /**
     * 提供获取内置RecyclerView的方法
     * */
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    /********* 提供一系列对内置RecyclerView的操作方法 *********/
    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        mRecyclerView.setLayoutManager(layoutManager);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    public void setItemAnimator(RecyclerView.ItemAnimator animator) {
        mRecyclerView.setItemAnimator(animator);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration decor) {
        mRecyclerView.addItemDecoration(decor);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration decor, int index) {
        mRecyclerView.addItemDecoration(decor, index);
    }

    public void setViewPadding(int left, int top, int right, int bottom) {
        mRecyclerView.setPadding(left, top, right, bottom);
    }

    public void setScrollBarEnabled(boolean isEnabled) {
        mRecyclerView.setVerticalScrollBarEnabled(isEnabled);
    }
}
