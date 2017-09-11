package com.gbq.library.widget.customview.clearscreen;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.gbq.library.widget.customview.clearscreen.view.ScreenSideView;

import java.util.LinkedList;

/**
 * 类说明：清除屏幕功能
 * Author: Kuzan
 * Date: 2017/8/23 10:50.
 */
public class ClearScreenHelper {
    private IClearRootView mScreenSideView;

    private LinkedList<View> mClearList;

    private IClearEvent mIClearEvent;

    @Deprecated
    public ClearScreenHelper(Context context) {
        this(context, null);
    }

    /**
     * Recomment
     * @param context
     * @param rootView
     */
    public ClearScreenHelper(Context context, IClearRootView rootView) {
        initView(context, rootView);
        initPara();
        initCallback();
    }

    private void initView(Context context, IClearRootView root) {
        if (root == null) {
            ViewGroup decorView = (ViewGroup) ((Activity) context).getWindow().getDecorView();
            final ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mScreenSideView = new ScreenSideView(context);
            decorView.addView((View) mScreenSideView, params);
        } else {
            mScreenSideView = root;
            View imgV = new View(context);
            imgV.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imgV.setClickable(true);
            root.addView(imgV, 0);
        }
    }

    private void initPara() {
        mClearList = new LinkedList<>();
        setOrientation(ClearConstants.Orientation.RIGHT);
    }

    private void initCallback() {
        mScreenSideView.setIPositionCallBack(new IPositionCallBack() {
            @Override
            public void onPositionChange(int offsetX, int offsetY) {
                for (int i = 0; i < mClearList.size(); i++) {
                    mClearList.get(i).setTranslationX(offsetX);
                    mClearList.get(i).setTranslationY(offsetY);
                }
            }
        });

        mScreenSideView.setIClearEvent(new IClearEvent() {
            @Override
            public void onClearEnd() {
                if (mIClearEvent != null){
                    mIClearEvent.onClearEnd();
                }
            }

            @Override
            public void onRecovery() {
                if (mIClearEvent != null){
                    mIClearEvent.onRecovery();
                }
            }
        });
    }

    public void setIClearEvent(IClearEvent l){
        mIClearEvent = l;
    }

    public void setOrientation(ClearConstants.Orientation orientation) {
        mScreenSideView.setClearSide(orientation);
    }

    /**绑定随滑动布局而隐藏的控件*/
    public void bind(@NonNull View... cellList) {
        for (View cell : cellList) {
            if (!mClearList.contains(cell)) {
                mClearList.add(cell);
            }
        }
    }

    public void unbind(@NonNull View... cellList) {
        for (View cell : cellList) {
            if (mClearList.contains(cell)) {
                mClearList.remove(cell);
            }
        }
    }

    public void unbindAllCell() {
        mClearList.clear();
    }
}
