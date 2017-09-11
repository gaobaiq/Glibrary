package com.gbq.library.widget.customview.clearscreen;

import android.view.View;

/**
 * 类说明：
 * Author: Kuzan
 * Date: 2017/8/23 10:51.
 */
public interface IClearRootView {
    /**设置竖屏或横屏*/
    void setClearSide(ClearConstants.Orientation orientation);

    /**滑动回调*/
    void setIPositionCallBack(IPositionCallBack callBack);

    /**滑动事件*/
    void setIClearEvent(IClearEvent event);

    /**添加布局*/
    void addView(View child, int index);
}
