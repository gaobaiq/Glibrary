package com.gbq.library.widget.customview;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;

/**
 * 类说明：带有下划线和中间线的TextView
 *          下划线一般用来协议
 *          中划线一般用来原价
 * Author: Kuzan
 * Date: 2017/8/8 16:24.
 */
public class LineTextView extends android.support.v7.widget.AppCompatTextView {

    public static final int MIDDLE = 0; // 中划线
    public static final int BOTTOM = 1; // 下划线


    public LineTextView(Context context) {
        this(context, null);
    }

    public LineTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public void setText(CharSequence text, int linePosition){
        super.setText(text);
        getPaint().setAntiAlias(true);
        switch (linePosition){
            case MIDDLE:
                getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                break;
            case BOTTOM:
                getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                break;
        }
    }
}
