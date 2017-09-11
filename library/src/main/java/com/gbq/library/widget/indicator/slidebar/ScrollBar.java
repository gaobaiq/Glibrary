package com.gbq.library.widget.indicator.slidebar;

import android.view.View;

/**
 * 类说明：指示器滑动块
 * Author: Kuzan
 * Date: 2017/8/11 9:44.
 */
public interface ScrollBar {
    enum Gravity {
        TOP,
        TOP_FLOAT,
        BOTTOM,
        BOTTOM_FLOAT,
        CENTENT,
        CENTENT_BACKGROUND
    }

    int getHeight(int tabHeight);

    int getWidth(int tabWidth);

    View getSlideView();

    Gravity getGravity();

    void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);
}
