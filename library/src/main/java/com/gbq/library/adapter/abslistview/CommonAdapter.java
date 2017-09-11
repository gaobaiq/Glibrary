package com.gbq.library.adapter.abslistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 类说明：通用的适配器 ListView / GridView
 * Author: Kuzan
 * Date: 2016/7/31 19:21.
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
    protected Context mContext;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    private int layoutId;

    public CommonAdapter(Context context, List<T> datas, int layoutId) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mDatas = datas;
        this.layoutId = layoutId;
    }

    @Override
    public int getCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.get(mContext, convertView, parent, layoutId, position);
        convert(holder, getItem(position));
        return holder.getConvertView();
    }

    public abstract void convert(ViewHolder holder, T t);
}
