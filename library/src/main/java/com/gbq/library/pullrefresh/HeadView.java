package com.gbq.library.pullrefresh;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gbq.library.R;

/**
 * 类说明：下拉刷新头部View
 * Author: Kuzan
 * Date: 2017/5/25 15:35.
 */
public class HeadView extends LinearLayout implements PullRefreshView.OnHeadStateListener {
    ImageView ivHeaderDownArrow;
    ImageView ivHeaderLoading;
    TextView textView;

    AnimationDrawable animationDrawable;

    private boolean isReach = false;

    public HeadView(Context context) {
        super(context);
        animationDrawable = (AnimationDrawable) getResources().getDrawable(R.drawable.progress_round);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.layout_head_view, this, false);
        this.addView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        initView(layout);
        restore();
        this.setPadding(0, 30, 0, 20);
    }

    private void initView(View view){
        ivHeaderDownArrow = (ImageView)view.findViewById(R.id.iv_header_down_arrow);
        ivHeaderLoading = (ImageView)view.findViewById(R.id.iv_header_loading);
        textView = (TextView)view.findViewById(R.id.tv_header_state);
    }

    @Override
    public void onScrollChange(View head, int scrollOffset, int scrollRatio) {

        if (scrollRatio == 100 && !isReach) {
            textView.setText("松开刷新");
            ivHeaderDownArrow.setRotation(180);
            isReach = true;
        } else if (scrollRatio != 100 && isReach) {
            textView.setText("下拉刷新");
            ivHeaderDownArrow.setRotation(0);
            isReach = false;
        }
    }

    @Override
    public void onRefreshHead(View head) {
        ivHeaderLoading.setVisibility(VISIBLE);
        ivHeaderDownArrow.setVisibility(GONE);
        ivHeaderLoading.setImageDrawable(animationDrawable);
        animationDrawable.start();
        textView.setText("正在刷新");
    }

    @Override
    public void onRetractHead(View head) {
        restore();
        animationDrawable.stop();
        isReach = false;
    }

    private void restore() {
        ivHeaderLoading.setVisibility(GONE);
        ivHeaderDownArrow.setVisibility(VISIBLE);
        ivHeaderLoading.setImageResource(R.drawable.loading1);
        ivHeaderDownArrow.setImageResource(R.drawable.icon_down_arrow);
        ivHeaderDownArrow.setRotation(0);
        textView.setText("下拉刷新");
    }
}
