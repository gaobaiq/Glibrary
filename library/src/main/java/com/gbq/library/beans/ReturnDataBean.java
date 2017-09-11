package com.gbq.library.beans;

import java.io.Serializable;
import java.util.List;

/**
 * 类说明：返回数据data
 * Author: Kuzan
 * Date: 2016/5/5 20:50
 */
public class ReturnDataBean<T> implements Serializable {
    int total;
    boolean next;
    int pageSize;
    int pageNum;
    List<T> list;

    @Override
    public String toString() {
        return "ReturnDataBean{" +
                "total=" + total +
                ", next=" + next +
                ", pageSize=" + pageSize +
                ", pageNum=" + pageNum +
                ", list=" + list +
                '}';
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public boolean isNext() {
        return next;
    }

    public void setNext(boolean next) {
        this.next = next;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
