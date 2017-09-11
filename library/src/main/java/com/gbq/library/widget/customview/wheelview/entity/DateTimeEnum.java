package com.gbq.library.widget.customview.wheelview.entity;

/**
 * 类说明：日期时间选择器枚举
 * Author: Kuzan
 * Date: 2017/9/5 13:54.
 */
public enum DateTimeEnum {
    /**不显示*/
    NONE(-1, "不显示"),
    /**年月日*/
    YEAR_MONTH_DAY(0, "年月日"),
    /**年月*/
    YEAR_MONTH(1, "年月"),
    /**月日*/
    MONTH_DAY(2, "月日"),
    /**24小时*/
    HOUR_24(3, "24小时"),
    /**12小时*/
    HOUR_12(4, "12小时");

    private int key;
    private String value;

    DateTimeEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public static DateTimeEnum valueOf(int key) {
        switch (key) {
            case -1:
                return NONE;
            case 0:
                return YEAR_MONTH_DAY;
            case 1:
                return YEAR_MONTH;
            case 2:
                return MONTH_DAY;
            case 3:
                return HOUR_24;
            case 4:
                return HOUR_12;
            default:
                return NONE;
        }
    }

    public int getKey() {
        return this.key;
    }

    public String getValue() {
        return this.value;
    }
}
