package com.gbq.library.widget.customview.wheelview;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gbq.library.R;
import com.gbq.library.base.BasePopupWindow;
import com.gbq.library.utils.DateUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

/**
 * 类说明：日期时间选择器，可同时选中日期及时间
 * Author: Kuzan
 * Date: 2017/9/5 13:45.
 */
public class DateTimePicker extends BasePopupWindow implements View.OnClickListener {
    private final String TAG = "DateTimePicker";
    private TextView mBtnCancel;
    private TextView mBtnFinish;
    private TextView mTvTitle;
    private RelativeLayout mLayoutTitle;
    private View mLine;
    private WheelView mWheelYear;
    private TextView mTvYear;
    private LinearLayout mLayoutYear;
    private WheelView mWheelMonth;
    private TextView mTvMonth;
    private LinearLayout mLayoutMonth;
    private WheelView mWheelDay;
    private TextView mTvDay;
    private LinearLayout mLayoutDay;
    private WheelView mWheelHour;
    private TextView mTvHour;
    private LinearLayout mLayoutHour;
    private WheelView mWheelMinute;
    private TextView mTvMinute;
    private LinearLayout mLayoutMinute;

    /** 不显示 */
    public static final int NONE = -1;
    /** 年月日 */
    public static final int YEAR_MONTH_DAY = 0;
    /** 年月 */
    public static final int YEAR_MONTH = 1;
    /** 月日 */
    public static final int MONTH_DAY = 2;
    /** 24小时 */
    public static final int HOUR_24 = 3;
    /**
     * @deprecated use {@link #HOUR_24} instead
     */
    @Deprecated
    public static final int HOUR_OF_DAY = 3;
    /** 12小时 */
    public static final int HOUR_12 = 4;
    /**
     * @deprecated use {@link #HOUR_12} instead
     */
    @Deprecated
    public static final int HOUR = 4;
    /**年*/
    private ArrayList<String> years = new ArrayList<>();
    /**月*/
    private ArrayList<String> months = new ArrayList<>();
    /**日*/
    private ArrayList<String> days = new ArrayList<>();
    /**时*/
    private ArrayList<String> hours = new ArrayList<>();
    /**分*/
    private ArrayList<String> minutes = new ArrayList<>();

    private int selectedYearIndex = 0, selectedMonthIndex = 0, selectedDayIndex = 0;
    private String selectedHour = "", selectedMinute = "";

    private OnWheelListener onWheelListener;
    private OnDateTimePickListener onDateTimePickListener;

    private int dateMode = YEAR_MONTH_DAY, timeMode = HOUR_24;
    private int startYear = 2010, startMonth = 1, startDay = 1;
    private int endYear = 2050, endMonth = 12, endDay = 31;
    private int startHour, startMinute = 0;
    private int endHour, endMinute = 59;
    private int textSize = WheelView.TEXT_SIZE;
    private boolean useWeight = false;
    private boolean resetWhileWheel = true;

    @IntDef(value = {NONE, YEAR_MONTH_DAY, YEAR_MONTH, MONTH_DAY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DateMode {

    }

    @IntDef(value = {NONE, HOUR_24, HOUR_12})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TimeMode {

    }

    public DateTimePicker(Context context) {
        this(context, YEAR_MONTH_DAY, HOUR_24);
    }

    public DateTimePicker(Context context, @TimeMode int timeMode) {
        this(context, YEAR_MONTH_DAY, timeMode);
    }

    public DateTimePicker(Context context, @DateMode int dateMode, @TimeMode int timeMode) {
        super(context);

        if (dateMode == NONE && timeMode == NONE) {
            throw new IllegalArgumentException("The modes are NONE at the same time");
        }
        if (dateMode == YEAR_MONTH_DAY && timeMode != NONE) {
            textSize = 12;
        }
        this.dateMode = dateMode;
        //根据时间模式初始化小时范围
        if (timeMode == HOUR_12) {
            startHour = 1;
            endHour = 12;
        } else {
            startHour = 0;
            endHour = 23;
        }
        this.timeMode = timeMode;

        initWheelView();
    }

    @Override
    protected int getViewId() {
        return R.layout.pop_date_picker;
    }

    @Override
    protected int getAnimId() {
        return R.style.AnimationPopupWindow_bottom_to_up;
    }

    @Override
    protected void init() {
        super.init();

        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void initAfterViews() {
        initView();
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        setPopWindowBg(1.0f);
    }

    /**
     * 初始化控件
     * */
    private void initView() {
        mBtnCancel = (TextView) mView.findViewById(R.id.btn_cancel);
        mBtnFinish = (TextView) mView.findViewById(R.id.btn_finish);
        mTvTitle = (TextView) mView.findViewById(R.id.tv_title);
        mLayoutTitle = (RelativeLayout) mView.findViewById(R.id.layout_title);
        mLine = mView.findViewById(R.id.line);
        mWheelYear = (WheelView) mView.findViewById(R.id.wheel_year);
        mTvYear = (TextView) mView.findViewById(R.id.tv_year);
        mLayoutYear = (LinearLayout) mView.findViewById(R.id.layout_year);
        mWheelMonth = (WheelView) mView.findViewById(R.id.wheel_month);
        mTvMonth = (TextView) mView.findViewById(R.id.tv_month);
        mLayoutMonth = (LinearLayout) mView.findViewById(R.id.layout_month);
        mWheelDay = (WheelView) mView.findViewById(R.id.wheel_day);
        mTvDay = (TextView) mView.findViewById(R.id.tv_day);
        mLayoutDay = (LinearLayout) mView.findViewById(R.id.layout_day);
        mWheelHour = (WheelView) mView.findViewById(R.id.wheel_hour);
        mTvHour = (TextView) mView.findViewById(R.id.tv_hour);
        mLayoutHour = (LinearLayout) mView.findViewById(R.id.layout_hour);
        mWheelMinute = (WheelView) mView.findViewById(R.id.wheel_minute);
        mTvMinute = (TextView) mView.findViewById(R.id.tv_minute);
        mLayoutMinute = (LinearLayout) mView.findViewById(R.id.layout_minute);

        mLayoutYear.setVisibility(View.GONE);
        mLayoutMonth.setVisibility(View.GONE);
        mLayoutDay.setVisibility(View.GONE);
        mLayoutHour.setVisibility(View.GONE);
        mLayoutMinute.setVisibility(View.GONE);

        mBtnCancel.setOnClickListener(this);
        mBtnFinish.setOnClickListener(this);
    }

    /**
     * 初始化滚动图
     * */
    public void initWheelView() {
        // 如果未设置默认项，则需要在此初始化数据
        if ((dateMode == YEAR_MONTH_DAY || dateMode == YEAR_MONTH) && years.size() == 0) {
            Log.v(TAG, "init years before make view");
            initYearData();
        }
        if (dateMode != NONE && months.size() == 0) {
            Log.v(TAG, "init months before make view");
            int selectedYear = DateUtils.trimZero(getSelectedYear());
            changeMonthData(selectedYear);
        }
        if ((dateMode == YEAR_MONTH_DAY || dateMode == MONTH_DAY) && days.size() == 0) {
            Log.v(TAG, "init days before make view");
            int selectedYear;
            if (dateMode == YEAR_MONTH_DAY) {
                selectedYear = DateUtils.trimZero(getSelectedYear());
            } else {
                selectedYear = Calendar.getInstance(Locale.CHINA).get(Calendar.YEAR);
            }
            int selectedMonth = DateUtils.trimZero(getSelectedMonth());
            changeDayData(selectedYear, selectedMonth);
        }
        if (timeMode != NONE && hours.size() == 0) {
            Log.v(TAG, "init hours before make view");
            initHourData();
        }
        if (timeMode != NONE && minutes.size() == 0) {
            Log.v(TAG, "init minutes before make view");
            changeMinuteData(DateUtils.trimZero(selectedHour));
        }

        mWheelYear.setTextSize(textSize);
        mWheelMonth.setTextSize(textSize);
        mWheelDay.setTextSize(textSize);
        mWheelHour.setTextSize(textSize);
        mWheelMinute.setTextSize(textSize);

        mWheelYear.setUseWeight(useWeight);
        mWheelMonth.setUseWeight(useWeight);
        mWheelDay.setUseWeight(useWeight);
        mWheelHour.setUseWeight(useWeight);
        mWheelMinute.setUseWeight(useWeight);

        // 初始化年
        if (dateMode == YEAR_MONTH_DAY || dateMode == YEAR_MONTH) {
            mLayoutYear.setVisibility(View.VISIBLE);
            mWheelYear.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
            mWheelYear.setItems(years, selectedYearIndex);
            mWheelYear.setOnItemSelectListener(new WheelView.OnItemSelectListener() {
                @Override
                public void onSelected(int index) {
                    selectedYearIndex = index;
                    String selectedYearStr = years.get(selectedYearIndex);
                    if (onWheelListener != null) {
                        onWheelListener.onYearWheeled(selectedYearIndex, selectedYearStr);
                    }
                    Log.v(TAG, "change months after year wheeled");
                    if (resetWhileWheel) {
                        selectedMonthIndex = 0;//重置月份索引
                        selectedDayIndex = 0;//重置日子索引
                    }
                    //需要根据年份及月份动态计算天数
                    int selectedYear = DateUtils.trimZero(selectedYearStr);
                    changeMonthData(selectedYear);
                    mWheelMonth.setItems(months, selectedMonthIndex);
                    if (onWheelListener != null) {
                        onWheelListener.onMonthWheeled(selectedMonthIndex, months.get(selectedMonthIndex));
                    }
                    changeDayData(selectedYear, DateUtils.trimZero(months.get(selectedMonthIndex)));
                    mWheelDay.setItems(days, selectedDayIndex);
                    if (onWheelListener != null) {
                        onWheelListener.onDayWheeled(selectedDayIndex, days.get(selectedDayIndex));
                    }
                }
            });
        }

        // 初始化月
        if (dateMode != NONE) {
            mLayoutMonth.setVisibility(View.VISIBLE);
            mWheelMonth.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
            mWheelMonth.setItems(months, selectedMonthIndex);
            mWheelMonth.setOnItemSelectListener(new WheelView.OnItemSelectListener() {
                @Override
                public void onSelected(int index) {
                    selectedMonthIndex = index;
                    String selectedMonthStr = months.get(selectedMonthIndex);
                    if (onWheelListener != null) {
                        onWheelListener.onMonthWheeled(selectedMonthIndex, selectedMonthStr);
                    }
                    if (dateMode == YEAR_MONTH_DAY || dateMode == MONTH_DAY) {
                        Log.v(TAG, "change days after month wheeled");
                        if (resetWhileWheel) {
                            selectedDayIndex = 0;//重置日子索引
                        }
                        int selectedYear;
                        if (dateMode == YEAR_MONTH_DAY) {
                            selectedYear = DateUtils.trimZero(getSelectedYear());
                        } else {
                            selectedYear = Calendar.getInstance(Locale.CHINA).get(Calendar.YEAR);
                        }
                        changeDayData(selectedYear, DateUtils.trimZero(selectedMonthStr));
                        mWheelDay.setItems(days, selectedDayIndex);
                        if (onWheelListener != null) {
                            onWheelListener.onDayWheeled(selectedDayIndex, days.get(selectedDayIndex));
                        }
                    }
                }
            });
        }

        // 初始化日
        if (dateMode == YEAR_MONTH_DAY || dateMode == MONTH_DAY) {
            mLayoutDay.setVisibility(View.VISIBLE);
            mWheelDay.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
            mWheelDay.setItems(days, selectedDayIndex);
            mWheelDay.setOnItemSelectListener(new WheelView.OnItemSelectListener() {
                @Override
                public void onSelected(int index) {
                    selectedDayIndex = index;
                    if (onWheelListener != null) {
                        onWheelListener.onDayWheeled(selectedDayIndex, days.get(selectedDayIndex));
                    }
                }
            });
        }

        // 初始化小时
        if (timeMode != NONE) {
            mLayoutHour.setVisibility(View.VISIBLE);
            mWheelHour.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
            mWheelHour.setItems(hours, selectedHour);
            mWheelHour.setOnItemSelectListener(new WheelView.OnItemSelectListener() {
                @Override
                public void onSelected(int index) {
                    selectedHour = hours.get(index);
                    if (onWheelListener != null) {
                        onWheelListener.onHourWheeled(index, selectedHour);
                    }
                    Log.v(TAG, "change minutes after hour wheeled");
                    changeMinuteData(DateUtils.trimZero(selectedHour));
                    mWheelMinute.setItems(minutes, selectedMinute);
                }
            });

            // 初始化分钟
            mLayoutMinute.setVisibility(View.VISIBLE);
            mWheelMinute.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
            mWheelMinute.setItems(minutes, selectedMinute);
            mWheelMinute.setOnItemSelectListener(new WheelView.OnItemSelectListener() {
                @Override
                public void onSelected(int index) {
                    selectedMinute = minutes.get(index);
                    if (onWheelListener != null) {
                        onWheelListener.onMinuteWheeled(index, selectedMinute);
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_finish) {
            onFinishClick();
            dismiss();
        } else {
            dismiss();
        }
    }

    private void onFinishClick() {
        if (onDateTimePickListener == null) {
            return;
        }
        String year = getSelectedYear();
        String month = getSelectedMonth();
        String day = getSelectedDay();
        String hour = getSelectedHour();
        String minute = getSelectedMinute();
        switch (dateMode) {
            case YEAR_MONTH_DAY:
                ((OnYearMonthDayTimePickListener) onDateTimePickListener).onDateTimePicked(year, month, day, hour, minute);
                break;
            case YEAR_MONTH:
                ((OnYearMonthTimePickListener) onDateTimePickListener).onDateTimePicked(year, month, hour, minute);
                break;
            case MONTH_DAY:
                ((OnMonthDayTimePickListener) onDateTimePickListener).onDateTimePicked(month, day, hour, minute);
                break;
            case NONE:
                ((OnTimePickListener) onDateTimePickListener).onDateTimePicked(hour, minute);
                break;
        }
    }

    /** 可用于设置每项的高度，范围为2-4 */
    public final void setLineSpaceMultiplier(@FloatRange(from = 2, to = 4) float multiplier) {
        if (mWheelYear != null) {
            mWheelYear.setLineSpaceMultiplier(multiplier);
        }
        if (mWheelMonth != null) {
            mWheelMonth.setLineSpaceMultiplier(multiplier);
        }
        if (mWheelDay != null) {
            mWheelDay.setLineSpaceMultiplier(multiplier);
        }
        if (mWheelHour != null) {
            mWheelHour.setLineSpaceMultiplier(multiplier);
        }
        if (mWheelMinute != null) {
            mWheelMinute.setLineSpaceMultiplier(multiplier);
        }
    }

    /** 可用于设置每项的宽度，单位为dp */
    public void setPadding(int padding) {
        if (mWheelYear != null) {
            mWheelYear.setPadding(padding);
        }
        if (mWheelMonth != null) {
            mWheelMonth.setPadding(padding);
        }
        if (mWheelDay != null) {
            mWheelDay.setPadding(padding);
        }
        if (mWheelHour != null) {
            mWheelHour.setPadding(padding);
        }
        if (mWheelMinute != null) {
            mWheelMinute.setPadding(padding);
        }
    }

    /** 设置文字大小 */
    public void setTextSize(int textSize) {
        if (mWheelYear != null) {
            mWheelYear.setTextSize(textSize);
        }
        if (mWheelMonth != null) {
            mWheelMonth.setTextSize(textSize);
        }
        if (mWheelDay != null) {
            mWheelDay.setTextSize(textSize);
        }
        if (mWheelHour != null) {
            mWheelHour.setTextSize(textSize);
        }
        if (mWheelMinute != null) {
            mWheelMinute.setTextSize(textSize);
        }
    }

    /** 设置文字颜色 */
    public void setTextColor(@ColorInt int textColorFocus, @ColorInt int textColorNormal) {
        if (mWheelYear != null) {
            mWheelYear.setTextColor(textColorNormal, textColorFocus);
        }
        if (mWheelMonth != null) {
            mWheelMonth.setTextColor(textColorNormal, textColorFocus);
        }
        if (mWheelDay != null) {
            mWheelDay.setTextColor(textColorNormal, textColorFocus);
        }
        if (mWheelHour != null) {
            mWheelHour.setTextColor(textColorNormal, textColorFocus);
        }
        if (mWheelMinute != null) {
            mWheelMinute.setTextColor(textColorNormal, textColorFocus);
        }
    }

    /** 设置文字颜色 */
    public void setTextColor(@ColorInt int textColor) {
        if (mWheelYear != null) {
            mWheelYear.setTextColor(textColor);
        }
        if (mWheelMonth != null) {
            mWheelMonth.setTextColor(textColor);
        }
        if (mWheelDay != null) {
            mWheelDay.setTextColor(textColor);
        }
        if (mWheelHour != null) {
            mWheelHour.setTextColor(textColor);
        }
        if (mWheelMinute != null) {
            mWheelMinute.setTextColor(textColor);
        }
    }

    /** 设置分隔线配置项，设置null将隐藏分割线及阴影 */
    public void setDividerConfig(@Nullable WheelView.DividerConfig config) {
        if (config == null) {
            config = new WheelView.DividerConfig();
            config.setVisible(false);
            config.setShadowVisible(false);
        }
        if (mWheelYear != null) {
            mWheelYear.setDividerConfig(config);
        }
        if (mWheelMonth != null) {
            mWheelMonth.setDividerConfig(config);
        }
        if (mWheelDay != null) {
            mWheelDay.setDividerConfig(config);
        }
        if (mWheelHour != null) {
            mWheelHour.setDividerConfig(config);
        }
        if (mWheelMinute != null) {
            mWheelMinute.setDividerConfig(config);
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
        if (mWheelYear != null) {
            mWheelYear.setOffset(offset);
        }
        if (mWheelMonth != null) {
            mWheelMonth.setOffset(offset);
        }
        if (mWheelDay != null) {
            mWheelDay.setOffset(offset);
        }
        if (mWheelHour != null) {
            mWheelHour.setOffset(offset);
        }
        if (mWheelMinute != null) {
            mWheelMinute.setOffset(offset);
        }
    }

    /** 设置是否禁用循环 */
    public void setCycleDisable(boolean cycleDisable) {
        if (mWheelYear != null) {
            mWheelYear.setCycleDisable(cycleDisable);
        }
        if (mWheelMonth != null) {
            mWheelMonth.setCycleDisable(cycleDisable);
        }
        if (mWheelDay != null) {
            mWheelDay.setCycleDisable(cycleDisable);
        }
        if (mWheelHour != null) {
            mWheelHour.setCycleDisable(cycleDisable);
        }
        if (mWheelMinute != null) {
            mWheelMinute.setCycleDisable(cycleDisable);
        }
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

    /**
     * 是否使用比重来平分布局
     */
    public void setUseWeight(boolean useWeight) {
        this.useWeight = useWeight;
    }

    /**
     * 滚动时是否重置下一级的索引
     */
    public void setResetWhileWheel(boolean resetWhileWheel) {
        this.resetWhileWheel = resetWhileWheel;
    }

    /**
     * 设置年份范围
     *
     * @deprecated use {@link #setDateRangeStart(int, int, int)}
     * and {@link #setDateRangeEnd(int, int, int)} or  instead
     */
    @Deprecated
    public void setRange(int startYear, int endYear) {
        if (dateMode == NONE) {
            throw new IllegalArgumentException("Date mode invalid");
        }
        this.startYear = startYear;
        this.endYear = endYear;
        initYearData();
    }

    /**
     * 设置范围：开始的年月日
     */
    public void setDateRangeStart(int startYear, int startMonth, int startDay) {
        if (dateMode == NONE) {
            throw new IllegalArgumentException("Date mode invalid");
        }
        this.startYear = startYear;
        this.startMonth = startMonth;
        this.startDay = startDay;
        initYearData();
    }

    /**
     * 设置范围：结束的年月日
     */
    public void setDateRangeEnd(int endYear, int endMonth, int endDay) {
        if (dateMode == NONE) {
            throw new IllegalArgumentException("Date mode invalid");
        }
        this.endYear = endYear;
        this.endMonth = endMonth;
        this.endDay = endDay;
        initYearData();
    }

    /**
     * 设置范围：开始的年月日
     */
    public void setDateRangeStart(int startYearOrMonth, int startMonthOrDay) {
        if (dateMode == NONE) {
            throw new IllegalArgumentException("Date mode invalid");
        }
        if (dateMode == YEAR_MONTH_DAY) {
            throw new IllegalArgumentException("Not support year/month/day mode");
        }
        if (dateMode == YEAR_MONTH) {
            this.startYear = startYearOrMonth;
            this.startMonth = startMonthOrDay;
        } else if (dateMode == MONTH_DAY) {
            int year = Calendar.getInstance(Locale.CHINA).get(Calendar.YEAR);
            startYear = endYear = year;
            this.startMonth = startYearOrMonth;
            this.startDay = startMonthOrDay;
        }
        initYearData();
    }

    /**
     * 设置范围：结束的年月日
     */
    public void setDateRangeEnd(int endYearOrMonth, int endMonthOrDay) {
        if (dateMode == NONE) {
            throw new IllegalArgumentException("Date mode invalid");
        }
        if (dateMode == YEAR_MONTH_DAY) {
            throw new IllegalArgumentException("Not support year/month/day mode");
        }
        if (dateMode == YEAR_MONTH) {
            this.endYear = endYearOrMonth;
            this.endMonth = endMonthOrDay;
        } else if (dateMode == MONTH_DAY) {
            this.endMonth = endYearOrMonth;
            this.endDay = endMonthOrDay;
        }
        initYearData();
    }

    /**
     * 设置范围：开始的时分
     */
    public void setTimeRangeStart(int startHour, int startMinute) {
        if (timeMode == NONE) {
            throw new IllegalArgumentException("Time mode invalid");
        }
        boolean illegal = false;
        if (startHour < 0 || startMinute < 0 || startMinute > 59) {
            illegal = true;
        }
        if (timeMode == HOUR_12 && (startHour == 0 || startHour > 12)) {
            illegal = true;
        }
        if (timeMode == HOUR_24 && startHour >= 24) {
            illegal = true;
        }
        if (illegal) {
            throw new IllegalArgumentException("Time out of range");
        }
        this.startHour = startHour;
        this.startMinute = startMinute;
        initHourData();
    }

    /**
     * 设置范围：结束的时分
     */
    public void setTimeRangeEnd(int endHour, int endMinute) {
        if (timeMode == NONE) {
            throw new IllegalArgumentException("Time mode invalid");
        }
        boolean illegal = false;
        if (endHour < 0 || endMinute < 0 || endMinute > 59) {
            illegal = true;
        }
        if (timeMode == HOUR_12 && (endHour == 0 || endHour > 12)) {
            illegal = true;
        }
        if (timeMode == HOUR_24 && endHour >= 24) {
            illegal = true;
        }
        if (illegal) {
            throw new IllegalArgumentException("Time out of range");
        }
        this.endHour = endHour;
        this.endMinute = endMinute;
        initHourData();
    }

    /**
     * 设置年月日时分的显示单位
     */
    public void setLabel(String yearLabel, String monthLabel, String dayLabel, String hourLabel, String minuteLabel) {
        if (mTvYear != null) {
            mTvYear.setText(yearLabel);
        }
        if (mTvMonth != null) {
            mTvMonth.setText(monthLabel);
        }
        if (mTvDay != null) {
            mTvDay.setText(dayLabel);
        }
        if (mTvHour != null) {
            mTvHour.setText(hourLabel);
        }
        if (mTvMinute != null) {
            mTvMinute.setText(minuteLabel);
        }
    }

    /**
     * 设置默认选中的年月日时分
     */
    public void setSelectedItem(int year, int month, int day, int hour, int minute) {
        if (dateMode != YEAR_MONTH_DAY) {
            throw new IllegalArgumentException("Date mode invalid");
        }
        Log.v(TAG, "change months and days while set selected");
        changeMonthData(year);
        changeDayData(year, month);
        selectedYearIndex = findItemIndex(years, year);
        selectedMonthIndex = findItemIndex(months, month);
        selectedDayIndex = findItemIndex(days, day);
        if (timeMode != NONE) {
            selectedHour = DateUtils.fillZero(hour);
            selectedMinute = DateUtils.fillZero(minute);
        }
    }

    /**
     * 设置默认选中的年月时分或者月日时分
     */
    public void setSelectedItem(int yearOrMonth, int monthOrDay, int hour, int minute) {
        if (dateMode == YEAR_MONTH_DAY) {
            throw new IllegalArgumentException("Date mode invalid");
        }
        if (dateMode == MONTH_DAY) {
            Log.v(TAG, "change months and days while set selected");
            int year = Calendar.getInstance(Locale.CHINA).get(Calendar.YEAR);
            startYear = endYear = year;
            changeMonthData(year);
            changeDayData(year, yearOrMonth);
            selectedMonthIndex = findItemIndex(months, yearOrMonth);
            selectedDayIndex = findItemIndex(days, monthOrDay);
        } else if (dateMode == YEAR_MONTH) {
            Log.v(TAG, "change months while set selected");
            changeMonthData(yearOrMonth);
            selectedYearIndex = findItemIndex(years, yearOrMonth);
            selectedMonthIndex = findItemIndex(months, monthOrDay);
        }
        if (timeMode != NONE) {
            selectedHour = DateUtils.fillZero(hour);
            selectedMinute = DateUtils.fillZero(minute);
        }
    }

    private int findItemIndex(ArrayList<String> items, int item) {
        //折半查找有序元素的索引
        int index = Collections.binarySearch(items, item, new Comparator<Object>() {
            @Override
            public int compare(Object lhs, Object rhs) {
                String lhsStr = lhs.toString();
                String rhsStr = rhs.toString();
                lhsStr = lhsStr.startsWith("0") ? lhsStr.substring(1) : lhsStr;
                rhsStr = rhsStr.startsWith("0") ? rhsStr.substring(1) : rhsStr;
                try {
                    return Integer.parseInt(lhsStr) - Integer.parseInt(rhsStr);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });
        if (index < 0) {
            throw new IllegalArgumentException("Item[" + item + "] out of range");
        }
        return index;
    }

    public String getSelectedYear() {
        if (dateMode == YEAR_MONTH_DAY || dateMode == YEAR_MONTH) {
            if (years.size() <= selectedYearIndex) {
                selectedYearIndex = years.size() - 1;
            }
            return years.get(selectedYearIndex);
        }
        return "";
    }

    public String getSelectedMonth() {
        if (dateMode != NONE) {
            if (months.size() <= selectedMonthIndex) {
                selectedMonthIndex = months.size() - 1;
            }
            return months.get(selectedMonthIndex);
        }
        return "";
    }

    public String getSelectedDay() {
        if (dateMode == YEAR_MONTH_DAY || dateMode == MONTH_DAY) {
            if (days.size() <= selectedDayIndex) {
                selectedDayIndex = days.size() - 1;
            }
            return days.get(selectedDayIndex);
        }
        return "";
    }

    public String getSelectedHour() {
        if (timeMode != NONE) {
            return selectedHour;
        }
        return "";
    }

    public String getSelectedMinute() {
        if (timeMode != NONE) {
            return selectedMinute;
        }
        return "";
    }

    /**
     * 初始化年数据
     * */
    private void initYearData() {
        years.clear();
        if (startYear == endYear) {
            years.add(String.valueOf(startYear));
        } else if (startYear < endYear) {
            //年份正序
            for (int i = startYear; i <= endYear; i++) {
                years.add(String.valueOf(i));
            }
        } else {
            //年份逆序
            for (int i = startYear; i >= endYear; i--) {
                years.add(String.valueOf(i));
            }
        }
        if (!resetWhileWheel) {
            if (dateMode == YEAR_MONTH_DAY || dateMode == YEAR_MONTH) {
                int index = years.indexOf(DateUtils.fillZero(Calendar.getInstance().get(Calendar.YEAR)));
                if (index == -1) {
                    //当前设置的年份不在指定范围，则默认选中范围开始的年
                    selectedYearIndex = 0;
                } else {
                    selectedYearIndex = index;
                }
            }
        }
    }

    /**
     * 改变月份数据
     * */
    private void changeMonthData(int selectedYear) {
        String preSelectMonth = "";
        if (!resetWhileWheel) {
            if (months.size() > selectedMonthIndex) {
                preSelectMonth = months.get(selectedMonthIndex);
            } else {
                preSelectMonth = DateUtils.fillZero(Calendar.getInstance().get(Calendar.MONTH) + 1);
            }
            Log.v(TAG, "preSelectMonth=" + preSelectMonth);
        }
        months.clear();
        if (startMonth < 1 || endMonth < 1 || startMonth > 12 || endMonth > 12) {
            throw new IllegalArgumentException("Month out of range [1-12]");
        }
        if (startYear == endYear) {
            if (startMonth > endMonth) {
                for (int i = endMonth; i >= startMonth; i--) {
                    months.add(DateUtils.fillZero(i));
                }
            } else {
                for (int i = startMonth; i <= endMonth; i++) {
                    months.add(DateUtils.fillZero(i));
                }
            }
        } else if (selectedYear == startYear) {
            for (int i = startMonth; i <= 12; i++) {
                months.add(DateUtils.fillZero(i));
            }
        } else if (selectedYear == endYear) {
            for (int i = 1; i <= endMonth; i++) {
                months.add(DateUtils.fillZero(i));
            }
        } else {
            for (int i = 1; i <= 12; i++) {
                months.add(DateUtils.fillZero(i));
            }
        }
        if (!resetWhileWheel) {
            //当前设置的月份不在指定范围，则默认选中范围开始的月份
            int preSelectMonthIndex = months.indexOf(preSelectMonth);
            selectedMonthIndex = preSelectMonthIndex == -1 ? 0 : preSelectMonthIndex;
        }
    }

    /**
     * 改变天数据
     * */
    private void changeDayData(int selectedYear, int selectedMonth) {
        int maxDays = DateUtils.calculateDaysInMonth(selectedYear, selectedMonth);
        String preSelectDay = "";
        if (!resetWhileWheel) {
            if (selectedDayIndex >= maxDays) {
                //如果之前选择的日是之前年月的最大日，则日自动为该年月的最大日
                selectedDayIndex = maxDays - 1;
            }
            if (days.size() > selectedDayIndex) {
                //年或月变动时，保持之前选择的日不动
                preSelectDay = days.get(selectedDayIndex);
            } else {
                preSelectDay = DateUtils.fillZero(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            }
            Log.v(TAG, "maxDays=" + maxDays + ", preSelectDay=" + preSelectDay);
        }
        days.clear();
        if (selectedYear == startYear && selectedMonth == startMonth
                && selectedYear == endYear && selectedMonth == endMonth) {
            //开始年月及结束年月相同情况
            for (int i = startDay; i <= endDay; i++) {
                days.add(DateUtils.fillZero(i));
            }
        } else if (selectedYear == startYear && selectedMonth == startMonth) {
            //开始年月相同情况
            for (int i = startDay; i <= maxDays; i++) {
                days.add(DateUtils.fillZero(i));
            }
        } else if (selectedYear == endYear && selectedMonth == endMonth) {
            //结束年月相同情况
            for (int i = 1; i <= endDay; i++) {
                days.add(DateUtils.fillZero(i));
            }
        } else {
            for (int i = 1; i <= maxDays; i++) {
                days.add(DateUtils.fillZero(i));
            }
        }
        if (!resetWhileWheel) {
            //当前设置的日子不在指定范围，则默认选中范围开始的日子
            int preSelectDayIndex = days.indexOf(preSelectDay);
            selectedDayIndex = preSelectDayIndex == -1 ? 0 : preSelectDayIndex;
        }
    }

    /**
     * 初始化小时数据
     * */
    private void initHourData() {
        hours.clear();
        int currentHour = 0;
        if (!resetWhileWheel) {
            if (timeMode == HOUR_24) {
                currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            } else {
                currentHour = Calendar.getInstance().get(Calendar.HOUR);
            }
        }
        for (int i = startHour; i <= endHour; i++) {
            String hour = DateUtils.fillZero(i);
            if (!resetWhileWheel) {
                if (i == currentHour) {
                    selectedHour = hour;
                }
            }
            hours.add(hour);
        }
        if (hours.indexOf(selectedHour) == -1) {
            //当前设置的小时不在指定范围，则默认选中范围开始的小时
            selectedHour = hours.get(0);
        }
        if (!resetWhileWheel) {
            selectedMinute = DateUtils.fillZero(Calendar.getInstance().get(Calendar.MINUTE));
        }
    }

    /**
     * 改变分钟数据
     * */
    private void changeMinuteData(int selectedHour) {
        minutes.clear();
        if (startHour == endHour) {
            if (startMinute > endMinute) {
                int temp = startMinute;
                startMinute = endMinute;
                endMinute = temp;
            }
            for (int i = startMinute; i <= endMinute; i++) {
                minutes.add(DateUtils.fillZero(i));
            }
        } else if (selectedHour == startHour) {
            for (int i = startMinute; i <= 59; i++) {
                minutes.add(DateUtils.fillZero(i));
            }
        } else if (selectedHour == endHour) {
            for (int i = 0; i <= endMinute; i++) {
                minutes.add(DateUtils.fillZero(i));
            }
        } else {
            for (int i = 0; i <= 59; i++) {
                minutes.add(DateUtils.fillZero(i));
            }
        }
        if (minutes.indexOf(selectedMinute) == -1) {
            //当前设置的分钟不在指定范围，则默认选中范围开始的分钟
            selectedMinute = minutes.get(0);
        }
    }

    /**
     * 滑动监听
     * */
    public interface OnWheelListener {

        void onYearWheeled(int index, String year);

        void onMonthWheeled(int index, String month);

        void onDayWheeled(int index, String day);

        void onHourWheeled(int index, String hour);

        void onMinuteWheeled(int index, String minute);

    }

    public void setOnWheelListener(OnWheelListener onWheelListener) {
        this.onWheelListener = onWheelListener;
    }

    /**
     * 日期时间监听
     * */
    public void setOnDateTimePickListener(OnDateTimePickListener listener) {
        this.onDateTimePickListener = listener;
    }

    protected interface OnDateTimePickListener {

    }

    /**
     * 年月日时间监听
     * */
    public interface OnYearMonthDayTimePickListener extends OnDateTimePickListener {
        void onDateTimePicked(String year, String month, String day, String hour, String minute);
    }

    /**
     * 年月监听
     * */
    public interface OnYearMonthTimePickListener extends OnDateTimePickListener {
        void onDateTimePicked(String year, String month, String hour, String minute);
    }

    /**
     * @deprecated use {@link OnYearMonthTimePickListener} instead
     */
    @Deprecated
    public interface OnYearMonthPickListener extends OnYearMonthTimePickListener {

    }

    /**
     * 月日监听
     * */
    public interface OnMonthDayTimePickListener extends OnDateTimePickListener {

        void onDateTimePicked(String month, String day, String hour, String minute);
    }

    /**
     * @deprecated use {@link OnMonthDayTimePickListener} instead
     */
    @Deprecated
    public interface OnMonthDayPickListener extends OnMonthDayTimePickListener {

    }

    /**
     * 时间监听
     * */
    public interface OnTimePickListener extends OnDateTimePickListener {
        void onDateTimePicked(String hour, String minute);
    }
}
