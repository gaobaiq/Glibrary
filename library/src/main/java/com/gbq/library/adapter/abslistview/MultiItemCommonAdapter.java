package com.gbq.library.adapter.abslistview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 类说明：支持多种Item类型适配器
 * Author: Kuzan
 * Date: 2016/7/31 19:21.
 */
public abstract class MultiItemCommonAdapter<T> extends CommonAdapter<T> {

    protected MultiItemTypeSupport<T> mMultiItemTypeSupport;

    public MultiItemCommonAdapter(Context context, List<T> datas, MultiItemTypeSupport<T> multiItemTypeSupport) {
        super(context, datas, -1);
        this.mMultiItemTypeSupport = multiItemTypeSupport;
    }

    @Override
    public int getViewTypeCount() {
        if (mMultiItemTypeSupport != null) {
            return mMultiItemTypeSupport.getViewTypeCount();
        }
        return super.getViewTypeCount();
    }

    public int getItemViewType(int position) {
        if (mMultiItemTypeSupport != null) {
            return mMultiItemTypeSupport.getItemViewType(position, mDatas.get(position));
        }
        return super.getItemViewType(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (mMultiItemTypeSupport == null) {
            return super.getView(position, convertView, parent);
        }

        int layoutId = mMultiItemTypeSupport.getLayoutId(position, getItem(position));
        ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, layoutId, position);
        convert(viewHolder, getItem(position));
        return viewHolder.getConvertView();
    }
}
