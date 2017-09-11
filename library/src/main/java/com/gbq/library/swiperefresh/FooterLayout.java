package com.gbq.library.swiperefresh;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 类说明：底部加载更多布局
 * Author: Kuzan
 * Date: 2017/8/4 9:26.
 */
public class FooterLayout extends LinearLayout {
    private Context mContext;
    private TextView tvLoad;

    public FooterLayout(Context context) {
        this(context, null);
    }

    public FooterLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FooterLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        setupViews();
    }

    private void setupViews() {
        tvLoad = new TextView(mContext);
        addView(tvLoad);
    }

    public void setFooterView(View view) {
        if(view != null) {
            removeAllViews();
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            addView(view, lp);
        }
    }

    public void setFooterView(@LayoutRes int layoutResID) {
        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        View view = inflater.inflate(layoutResID, null);
        if(view != null) {
            removeAllViews();
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            addView(view, lp);
        }
    }

    public void setText(String loadText) {
        if(tvLoad != null) {
            tvLoad.setText(loadText);
        }
    }

    public void setTextColor(int color) {
        if(tvLoad != null) {
            tvLoad.setTextColor(color);
        }
    }
}
