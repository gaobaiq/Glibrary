package com.gbq.library.beans;

import java.io.Serializable;

/**
 * 类说明：事件总线对象
 * Author: Kuzan
 * Date: 2017/6/17 12:02.
 */
public class RxBusEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private int type;
    private Object obj;
    private int clickId;

    private RxBusEvent(RxBusEventBuilder builder) {
        this.type = builder.type;
        this.obj = builder.obj;
        this.clickId = builder.clickId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public int getClickId() {
        return clickId;
    }

    public void setClickId(int clickId) {
        this.clickId = clickId;
    }

    public static RxBusEventBuilder newBuilder(int type) {
        return new RxBusEventBuilder(type);
    }

    public static class RxBusEventBuilder {
        int type;
        Object obj;
        int clickId;

        public RxBusEventBuilder(int type) {
            this.type = type;
        }

        public RxBusEventBuilder clickId(int clickId) {
            this.clickId = clickId;
            return this;
        }

        public RxBusEventBuilder setObj(Object obj) {
            this.obj = obj;
            return this;
        }

        public RxBusEvent build() {
            return new RxBusEvent(this);
        }
    }

    @Override
    public String toString() {
        return "RxBusEvent{" +
                "type=" + type +
                ", obj=" + obj +
                ", clickId=" + clickId +
                '}';
    }
}
