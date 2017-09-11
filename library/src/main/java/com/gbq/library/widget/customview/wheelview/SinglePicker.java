package com.gbq.library.widget.customview.wheelview;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gbq.library.R;
import com.gbq.library.base.BasePopupWindow;
import com.gbq.library.utils.ConvertUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 类说明：滑轮单项选择器
 * Author: Kuzan
 * Date: 2017/9/1 10:31.
 */
public class SinglePicker<T> extends BasePopupWindow implements View.OnClickListener {
    private TextView mBtnCancel;
    private TextView mBtnFinish;
    private TextView mTvTitle;
    private RelativeLayout mLayoutTitle;
    private View mLine;
    private WheelView mWheelView;

    /**不能识别对象，只能是int,float,double,long类型*/
    private List<T> items = new ArrayList<>();
    private List<String> itemStrings = new ArrayList<>();

    private OnWheelListener<T> onWheelListener;
    private OnItemPickListener<T> onItemPickListener;
    private int selectedItemIndex = 0;

    private int itemWidth;

    public SinglePicker(Context context) {
        super(context);
    }

    @Override
    protected int getViewId() {
        return R.layout.pop_wheel_picker;
    }

    @Override
    protected int getAnimId() {
        return R.style.AnimationPopupWindow_bottom_to_up;
    }

    @Override
    protected void initAfterViews() {
        initView();
    }

    @Override
    protected void init() {
        super.init();
        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        manager.getDefaultDisplay().getSize(size);
        itemWidth = size.x;

        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        setPopWindowBg(1.0f);
    }

    private void initView() {
        mBtnCancel = (TextView) mView.findViewById(R.id.btn_cancel);
        mBtnFinish = (TextView) mView.findViewById(R.id.btn_finish);
        mTvTitle = (TextView) mView.findViewById(R.id.tv_title);
        mLayoutTitle = (RelativeLayout) mView.findViewById(R.id.layout_title);
        mLine = mView.findViewById(R.id.line);
        mWheelView = (WheelView) mView.findViewById(R.id.wheel_view);
        ViewGroup.LayoutParams params = mWheelView.getLayoutParams();
        params.width = itemWidth;
        mWheelView.setLayoutParams(params);
        mWheelView.setOnItemSelectListener(new WheelView.OnItemSelectListener() {
            @Override
            public void onSelected(int index) {
                selectedItemIndex = index;
                if (onWheelListener != null) {
                    onWheelListener.onWheeled(selectedItemIndex, items.get(index));
                }
            }
        });

        mBtnCancel.setOnClickListener(this);
        mBtnFinish.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_finish) {
            if (onItemPickListener != null) {
                onItemPickListener.onItemPicked(selectedItemIndex, getSelectedItem());
            }
            dismiss();
        } else {
            dismiss();
        }
    }

    /** 设置数据项 */
    public void replaceItems(T[] items) {
        setItems(Arrays.asList(items));
    }

    /** 设置数据项 */
    public void replaceItems(List<T> items) {
        setItems(items);
    }

    /** 添加数据项 */
    public void addItem(T item) {
        items.add(item);
        itemStrings.add(formatToString(item));
    }

    /** 移除数据项 */
    public void removeItem(T item) {
        items.remove(item);
        itemStrings.remove(formatToString(item));
    }

    /** 设置数据项 */
    public void setItems(List<T> items) {
        if (items == null || items.size() == 0) {
            return;
        }
        this.items = items;
        for (T item : items) {
            itemStrings.add(formatToString(item));
        }
        if (mWheelView != null) {
            if (selectedItemIndex < 0 || selectedItemIndex >= items.size()) {
                selectedItemIndex = 0;
            }
            mWheelView.setItems(itemStrings, selectedItemIndex);
        }
    }

    /** 设置选项的宽(dp) */
    public void setItemWidth(int itemWidth) {
        if (mWheelView != null) {
            ViewGroup.LayoutParams params = mWheelView.getLayoutParams();
            params.width = ConvertUtil.toPx(mContext, itemWidth);
            mWheelView.setLayoutParams(params);
        } else {
            this.itemWidth = itemWidth;
        }
    }

    /** 设置默认选中的项的索引 */
    public void setSelectedIndex(int index) {
        if (index >= 0) {
            selectedItemIndex = index;
        }
    }

    /** 将数据转为String类型 */
    private String formatToString(T item) {
        if (item instanceof Float || item instanceof Double) {
            return new DecimalFormat("0.00").format(item);
        }
        return item.toString();
    }

    /** 设置顶部标题栏背景颜色 */
    public void setTopBackgroundColor(@ColorInt int topBackgroundColor) {
        if (mLayoutTitle != null) {
            mLayoutTitle.setBackgroundColor(topBackgroundColor);
        }
    }

    /** 设置顶部标题栏背景颜色 */
    public void setTopBackgroundResource(int topBackgroundRes) {
        if (mLayoutTitle != null) {
            mLayoutTitle.setBackgroundResource(topBackgroundRes);
        }
    }

    /** 设置顶部标题栏下划线是否显示 */
    public void setTopLineVisible(boolean topLineVisible) {
        if (mLine != null) {
            mLine.setVisibility(topLineVisible ? View.VISIBLE : View.GONE);
        }
    }

    /** 设置顶部标题栏下划线颜色 */
    public void setTopLineColor(@ColorInt int topLineColor) {
        if (mLine != null) {
            mLine.setBackgroundColor(topLineColor);
        }
    }

    /** 设置顶部标题栏取消按钮是否显示 */
    public void setCancelVisible(boolean cancelVisible) {
        if (mBtnCancel != null) {
            mBtnCancel.setVisibility(cancelVisible ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 设置顶部标题栏取消按钮文字
     */
    public void setCancelText(CharSequence cancelText) {
        if (mBtnCancel != null) {
            mBtnCancel.setText(cancelText);
        }
    }

    /** 设置顶部标题栏取消按钮文字 */
    public void setCancelText(@StringRes int textRes) {
        setCancelText(mContext.getResources().getString(textRes));
    }

    /** 设置顶部标题栏取消按钮文字颜色 */
    public void setCancelTextColor(@ColorInt int cancelTextColor) {
        if (mBtnCancel != null) {
            mBtnCancel.setTextColor(cancelTextColor);
        }
    }

    /** 设置顶部标题栏取消按钮文字大小（单位为sp） */
    public void setCancelTextSize(@IntRange(from = 10, to = 40) int cancelTextSize) {
        if (mBtnCancel != null) {
            mBtnCancel.setTextSize(cancelTextSize);
        }
    }

    /** 设置顶部标题栏确定按钮文字 */
    public void setFinishText(CharSequence submitText) {
        if (mBtnFinish != null) {
            mBtnFinish.setText(submitText);
        }
    }

    /** 设置顶部标题栏确定按钮文字 */
    public void setFinishText(@StringRes int textRes) {
        setFinishText(mContext.getResources().getString(textRes));
    }

    /** 设置顶部标题栏确定按钮文字颜色 */
    public void setFinishTextColor(@ColorInt int submitTextColor) {
        if (mBtnFinish != null) {
            mBtnFinish.setTextColor(submitTextColor);
        }
    }

    /** 设置顶部标题栏确定按钮文字大小（单位为sp） */
    public void setFinishTextSize(@IntRange(from = 10, to = 40) int submitTextSize) {
        if (mBtnFinish != null) {
            mBtnFinish.setTextSize(submitTextSize);
        }
    }

    /** 设置顶部标题栏标题文字 */
    public void setTitleText(CharSequence titleText) {
        if (mTvTitle != null) {
            mTvTitle.setText(titleText);
        }
    }

    /** 设置顶部标题栏标题文字 */
    public void setTitleText(@StringRes int textRes) {
        setTitleText(mContext.getResources().getString(textRes));
    }

    /** 设置顶部标题栏标题文字颜色 */
    public void setTitleTextColor(@ColorInt int titleTextColor) {
        if (mTvTitle != null ) {
            mTvTitle.setTextColor(titleTextColor);
        }
    }

    /** 设置顶部标题栏标题文字大小（单位为sp） */
    public void setTitleTextSize(@IntRange(from = 10, to = 40) int titleTextSize) {
        if (mTvTitle != null ) {
            mTvTitle.setTextSize(titleTextSize);
        }
    }

    /** 可用于设置每项的高度，范围为2-4 */
    public final void setLineSpaceMultiplier(@FloatRange(from = 2, to = 4) float multiplier) {
        if (mWheelView != null) {
            mWheelView.setLineSpaceMultiplier(multiplier);
        }
    }

    /** 可用于设置每项的宽度，单位为dp */
    public void setPadding(int padding) {
        if (mWheelView != null) {
            mWheelView.setPadding(padding);
        }
    }

    /** 设置文字大小 */
    public void setTextSize(int textSize) {
        if (mWheelView != null) {
            mWheelView.setTextSize(textSize);
        }
    }

    /** 设置文字颜色 */
    public void setTextColor(@ColorInt int textColorFocus, @ColorInt int textColorNormal) {
        if (mWheelView != null) {
            mWheelView.setTextColor(textColorNormal, textColorFocus);
        }
    }

    /** 设置文字颜色 */
    public void setTextColor(@ColorInt int textColor) {
        if (mWheelView != null) {
            mWheelView.setTextColor(textColor);
        }
    }

    /** 设置分隔线配置项，设置null将隐藏分割线及阴影 */
    public void setDividerConfig(@Nullable WheelView.DividerConfig config) {
        if (config == null) {
            config = new WheelView.DividerConfig();
            config.setVisible(false);
            config.setShadowVisible(false);
        }
        if (mWheelView != null) {
            mWheelView.setDividerConfig(config);
        }
    }

    /**
     * @deprecated use {@link #setDividerConfig(WheelView.DividerConfig)} instead
     */
    @Deprecated
    public void setLineConfig(WheelView.DividerConfig config) {
        setDividerConfig(config);
    }

    /**
     * 设置选项偏移量，可用来要设置显示的条目数，范围为1-5。
     * 1显示3条、2显示5条、3显示7条……
     */
    public void setOffset(@IntRange(from = 1, to = 5) int offset) {
        if (mWheelView != null) {
            mWheelView.setOffset(offset);
        }
    }

    /**
     * 设置是否禁用循环
     */
    public void setCycleDisable(boolean cycleDisable) {
        if (mWheelView != null) {
            mWheelView.setCycleDisable(cycleDisable);
        }
    }

    /**
     * 得到选择器视图，可内嵌到其他视图容器
     */
    @Override
    public View getContentView() {
        return mView;
    }

    /**
     * 获取选中的item
     * */
    public T getSelectedItem() {
        return items.get(selectedItemIndex);
    }

    /**
     * 设置滑动过程监听器
     */
    public void setOnWheelListener(OnWheelListener<T> onWheelListener) {
        this.onWheelListener = onWheelListener;
    }

    /**
     * 设置确认选择监听器
     */
    public void setOnItemPickListener(OnItemPickListener<T> listener) {
        this.onItemPickListener = listener;
    }

    public interface OnItemPickListener<T> {
        void onItemPicked(int index, T item);
    }

    public interface OnWheelListener<T> {
        void onWheeled(int index, T item);
    }
}
