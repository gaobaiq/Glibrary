package com.gbq.library.swiperefresh;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gbq.library.R;

/**
 * 类说明：上拉加载更多底部View
 * Author: Kuzan
 * Date: 2017/8/03 16:36.
 */
public class FooterView extends LinearLayout implements OnFooterStateListener {

    ImageView ivFooterDownArrow;
    ImageView ivFooterLoading;
    TextView textView;

    AnimationDrawable animationDrawable;

    private boolean isReach = false;
    private boolean isMore = true;

    public FooterView(Context context) {
        super(context);
        animationDrawable = (AnimationDrawable)ContextCompat.getDrawable(context, R.drawable.progress_round);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.layout_refresh_view, this, false);
        this.addView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        initView(layout);
        restore();
        this.setPadding(0, 20, 0, 30);
    }

    private void initView(View view){
        ivFooterDownArrow = (ImageView)view.findViewById(R.id.iv_header_down_arrow);
        ivFooterLoading = (ImageView)view.findViewById(R.id.iv_header_loading);
        textView = (TextView)view.findViewById(R.id.tv_header_state);
    }

    @Override
    public void onScrollChange(View footer, int scrollOffset, int scrollRatio) {
        if (isMore) {
            if (scrollRatio == CustomSwipeRefreshLayout.FOOTER_DEFAULT_HEIGHT && !isReach) {
                textView.setText(R.string.load_by_loosen);
                ivFooterDownArrow.setRotation(0);
                isReach = true;
            } else if (scrollRatio != CustomSwipeRefreshLayout.FOOTER_DEFAULT_HEIGHT && isReach) {
                textView.setText(R.string.load_by_pull_up);
                isReach = false;
                ivFooterDownArrow.setRotation(180);
            }
        }
    }

    @Override
    public void onLoading(View footer) {
        if (isMore) {
            ivFooterLoading.setVisibility(VISIBLE);
            ivFooterDownArrow.setVisibility(GONE);
            ivFooterLoading.setImageDrawable(animationDrawable);
            animationDrawable.start();
            textView.setText(R.string.loading);
        }
    }

    @Override
    public void onRetract(View footer) {
        if (isMore) {
            restore();
            animationDrawable.stop();
            isReach = false;
        }
    }

    @Override
    public void onNoMore(View footer) {
        ivFooterLoading.setVisibility(GONE);
        ivFooterDownArrow.setVisibility(GONE);
        textView.setText(R.string.has_loaded_finish);
        isMore = false;
    }

    @Override
    public void onHasMore(View tail) {
        ivFooterLoading.setVisibility(GONE);
        ivFooterDownArrow.setVisibility(VISIBLE);
        textView.setText(R.string.load_by_pull_up);
        isMore = true;
    }

    private void restore() {
        ivFooterLoading.setVisibility(GONE);
        ivFooterDownArrow.setVisibility(VISIBLE);
        ivFooterLoading.setImageResource(R.drawable.loading1);
        ivFooterDownArrow.setImageResource(R.drawable.icon_down_arrow);
        ivFooterDownArrow.setRotation(180);
        textView.setText(R.string.load_by_pull_up);
    }
}
