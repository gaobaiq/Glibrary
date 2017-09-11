package com.gbq.library.imageselect.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.gbq.library.R;
import com.gbq.library.adapter.recyclerview.BaseViewHolder;
import com.gbq.library.adapter.recyclerview.CommonRecyclerAdapter;
import com.gbq.library.image.GlideUtil;
import com.gbq.library.imageselect.beans.FolderBean;

import java.util.ArrayList;

/**
 * 类说明：文件选择器
 * Author: Kuzan
 * Date: 2017/5/25 14:58.
 */
public class FolderAdapter extends CommonRecyclerAdapter<FolderBean> {

    private int mImageSize;
    private int selectItem;

    public FolderAdapter(Context context, int imageSize) {
        super(context);
        mImageSize = imageSize;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_of_folder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        FolderBean folder = mList.get(position);

        holder.setText(R.id.tv_folder_name, folder.getName())
        .setVisible(R.id.iv_select_icon,selectItem == position);

        ArrayList<String> images = folder.getImages();

        if (images != null && !images.isEmpty()) {
            holder.setText(R.id.tv_image_size, images.size() + "张");
            GlideUtil.loadImageForFile(mContext, (ImageView) holder.get(R.id.iv_image), images.get(0),
                    R.drawable.img_default_portrait,mImageSize, mImageSize, true, true);
        } else {
            holder.setText(R.id.tv_image_size, "0张");
            holder.setImageResource(R.id.iv_image,R.drawable.img_default_portrait);
        }
    }

    public void setSelectItem(int position){
        selectItem = position;
        notifyDataSetChanged();
    }

}
