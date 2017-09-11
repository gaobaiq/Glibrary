package com.gbq.library.adapter.abslistview;

/**
 * 类说明：多种Item类型支持接口
 * Author: Kuzan
 * Date: 2016/7/31 19:21.
 */
public interface MultiItemTypeSupport<T> {

    int getLayoutId(int position, T t);

    int getViewTypeCount();

    int getItemViewType(int position, T t);

}
