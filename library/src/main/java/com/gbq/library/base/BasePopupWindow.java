package com.gbq.library.base;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.gbq.library.R;

import butterknife.ButterKnife;

/**
 * 类说明：PopupWindow基类
 * Author: Kuzan
 * Date: 2016/9/20 09:21.
 */
public abstract class BasePopupWindow extends PopupWindow {
    protected View mView;
    protected Context mContext;
    protected Activity mActivity;

    protected onPopWindowViewClick mViewClick;

    public BasePopupWindow(Context context){
        super(context);
        this.mContext = context;
        this.mActivity = (Activity) context;
        init();
        initAfterViews();
    }

    protected void init(){
        mView= LayoutInflater.from(mContext).inflate(getViewId(), null);
        ButterKnife.bind(this, mView);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(mView);
        setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.color.transparent));
        setFocusable(true);
        setAnimationStyle(getAnimId());
        setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响背景
        setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.bg_shape_transparent));
    }

    /**
     * 获取viewId
     * */
    protected abstract int getViewId();

    /**
     * 获取弹窗动画
     * */
    protected abstract int getAnimId();

    /**
     * 初始化数据
     * */
    protected abstract void initAfterViews();

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        setPopWindowBg(0.3f);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        setPopWindowBg(1f);
    }

    protected void setPopWindowBg(float alpha) {
        // 设置背景颜色变暗
        WindowManager.LayoutParams lp = ((Activity)mContext).getWindow().getAttributes();
        lp.alpha = alpha;
        ((Activity)mContext).getWindow().setAttributes(lp);
    }

    /**
     * 点击事件抽象方法
     */
    public void setOnHolderClick(onPopWindowViewClick viewClick) {
        this.mViewClick = viewClick;
    }

    public interface onPopWindowViewClick {
        void onViewClick(View view);
    }
}
