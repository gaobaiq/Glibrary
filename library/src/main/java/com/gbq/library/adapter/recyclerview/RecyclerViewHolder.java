package com.gbq.library.adapter.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 类说明：
 * Author: Kuzan
 * Date: 2016/3/29 16:02
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    private BaseViewHolder baseViewHolder;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        baseViewHolder = BaseViewHolder.getViewHolder(itemView);
    }

    public BaseViewHolder getViewHolder() {
        return baseViewHolder;
    }
}
