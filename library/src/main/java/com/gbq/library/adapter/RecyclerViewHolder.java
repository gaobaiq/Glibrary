package com.gbq.library.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 类说明：
 * Author: Kuzan
 * Date: 2017/5/25 15:47.
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
