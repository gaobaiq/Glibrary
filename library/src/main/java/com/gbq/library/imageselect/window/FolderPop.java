package com.gbq.library.imageselect.window;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.PopupWindow;

import com.gbq.library.R;
import com.gbq.library.imageselect.adapter.FolderAdapter;
import com.gbq.library.imageselect.beans.FolderBean;
import com.gbq.library.utils.ScreenUtils;

import java.util.List;

/**
 * 类说明：图片文件夹弹窗
 * Author: Kuzan
 * Date: 2017/5/25 14:58.
 */
public class FolderPop extends PopupWindow {

    private View mView;
    private Context mContext;
    private RecyclerView mFolderList;
    private FolderAdapter mAdapter;
    private OnSelectListener mListener;

    public FolderPop(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    protected void init() {
        mView = LayoutInflater.from(mContext).inflate(R.layout.pop_folder, null);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ScreenUtils.getScreenHeight(mContext) * 2 / 3);
        setContentView(mView);
        mFolderList = (RecyclerView)mView.findViewById(R.id.list_folder);
        mFolderList.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new FolderAdapter(mContext,0);
        mAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < mAdapter.getList().size()) {
                    if (mListener != null) {
                        mListener.onSelect(mAdapter.getList().get(position));
                        mAdapter.setSelectItem(position);
                    }
                    dismiss();
                }
            }
        });
        mFolderList.setAdapter(mAdapter);
        setBackgroundDrawable(mContext.getResources().getDrawable(android.R.color.transparent));
        setOutsideTouchable(true);
        setTouchable(true);
        setFocusable(true);
        setAnimationStyle(R.style.AnimationPopupWindow_folder);
    }

    public void setFolders(List<FolderBean> folders){
        mAdapter.replaceList(folders);
    }

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);
        setPopWindowBg(0.5f);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        setPopWindowBg(1f);
    }

    protected void setPopWindowBg(float alpha) {
        // 设置背景颜色变暗
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        lp.alpha = alpha;
        ((Activity) mContext).getWindow().setAttributes(lp);
    }

    public void setOnSelectListener(OnSelectListener l){
        mListener = l;
    }

    public interface OnSelectListener{
        void onSelect(FolderBean folder);
    }

}
