package com.gbq.library.widget.customview;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.gbq.library.R;
import com.gbq.library.utils.ScreenUtils;

/**
 * 类说明：一个能保持比例的 ImageView
 *      TODO 暂时只支持维持宽度适应高度
 *      需在application节点添加
 *      ========================================
 *      <meta-data
 *           android:name="targer_screen_width"
 *           android:value="720"/>
 *      ========================================
 * Author: Kuzan
 * Date: 2017/5/25 15:53.
 */
public class RatioImageView extends ImageView {
    private float mRatio = 0f;
    private int mWidth;

    private static final String TARGET_SCREEN_WIDTH = "targer_screen_width";

    private Context context;

    public RatioImageView(Context context) {
        super(context);
        this.context = context;
    }

    public RatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        getAttrs(context, attrs);
    }

    public RatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        getAttrs(context, attrs);
    }

    private void getAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.RatioImageView);
            mRatio = mTypedArray.getFloat(R.styleable.RatioImageView_ratio, 0f);
            int width = mTypedArray.getInteger(R.styleable.RatioImageView_width, 0);
            calculateWidth(width);
            mTypedArray.recycle();
        }
    }

    public void setRatio(float ratio) {
        mRatio = ratio;
        invalidate();
    }

    public float getRatio() {
        return mRatio;
    }

    private void calculateWidth(int width) {
        int targetScreenWidth = 0;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            targetScreenWidth = info.metaData.getInt(TARGET_SCREEN_WIDTH);
        } catch (Exception e) {
        }

        if (width > 0) {
            if (targetScreenWidth > 0) {
                mWidth = width * ScreenUtils.getScreenWidth(context) / targetScreenWidth;
            } else {
                mWidth = width;
            }
        }
    }

    public void setWidth(int width) {
        calculateWidth(width);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mRatio > 0 || mWidth > 0) {

            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = MeasureSpec.getSize(heightMeasureSpec);

            if (mWidth > 0) {
                width = mWidth;
            }

            // TODO: 现在只支持固定宽度
            if (width > 0 && mRatio > 0) {
                height = (int) ((float) width * mRatio);
            }
            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
