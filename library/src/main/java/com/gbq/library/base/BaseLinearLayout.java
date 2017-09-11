package com.gbq.library.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.ButterKnife;

/**
 * 类说明：linearLayout基类
 * Author: Kuzan
 * Date: 2017/9/20 09:20.
 */
public abstract class BaseLinearLayout extends LinearLayout {
    protected Context mContext;
    protected View mView;
    protected OnViewClickListener mViewClick;

    public BaseLinearLayout(Context context) {
        this(context, null);
    }

    public BaseLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;

        initLayout();
        initViewAndData();
    }

    private void initLayout() {
        mView = LayoutInflater.from(mContext).inflate(getLayoutId(), null);
        addView(mView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        ButterKnife.bind(this, mView);
    }

    /**
     * 获取layoutId
     * */
    protected abstract int getLayoutId();

    /**
     * 初始化界面和数据
     * */
    protected abstract void initViewAndData();

    /**
     * 点击事件抽象方法
     */
    public void setOnViewClickListener(OnViewClickListener listener) {
        this.mViewClick = listener;
    }

    public interface OnViewClickListener {
        void onViewClick(View view);
    }
}
