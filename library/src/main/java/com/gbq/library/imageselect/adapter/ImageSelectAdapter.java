package com.gbq.library.imageselect.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.gbq.library.R;
import com.gbq.library.image.GlideUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 类说明：图片选择器
 * Author: Kuzan
 * Date: 2017/5/25 14:58.
 */
public class ImageSelectAdapter extends RecyclerView.Adapter<ImageSelectAdapter.ViewHolder> {

    private Context mContext;
    private List<String> mData;
    private LayoutInflater inflater;

    private OnImageSelectListener listener;

    private ArrayList<String> selectImages = new ArrayList<>();

    private int mImageSize;
    private int mMaxCount;

    public ImageSelectAdapter(Context context,int imageSize, int maxCount) {
        mContext = context;
        mImageSize = imageSize;
        this.inflater = LayoutInflater.from(mContext);
        mMaxCount = maxCount;
    }

    public List<String> getData() {
        return mData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_of_select_images, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) holder.ivImage.getLayoutParams();
        lp.width = mImageSize;
        lp.height = mImageSize;
        holder.ivImage.setLayoutParams(lp);

        final String image = mData.get(position);
        GlideUtil.loadImageForFile(mContext, holder.ivImage, image,
                R.drawable.img_default_portrait, mImageSize, mImageSize, true, true);

        if (mMaxCount != 0) {
            holder.ivSelectIcon.setVisibility(View.VISIBLE);
        } else {
            holder.ivSelectIcon.setVisibility(View.GONE);
        }

        if (selectImages.contains(image)) {
            holder.ivSelectIcon.setImageResource(R.drawable.icon_select);
        } else {
            holder.ivSelectIcon.setImageResource(R.drawable.icon_un_select);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectImages.contains(image)) {
                    selectImages.remove(image);
                    holder.ivSelectIcon.setImageResource(R.drawable.icon_un_select);
                    if (listener != null) {
                        listener.OnImageSelect(image, false, selectImages.size());
                    }
                } else if (mMaxCount <= 0 || selectImages.size() < mMaxCount) {
                    selectImages.add(image);
                    holder.ivSelectIcon.setImageResource(R.drawable.icon_select);
                    if (listener != null) {
                        listener.OnImageSelect(image, true, selectImages.size());
                    }
                }
            }
        });
    }

    public void refresh(List<String> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void add(String data) {
        if (mData == null) {
            mData = new ArrayList<>();
        }

        mData.add(data);
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<String> data) {

        if (data == null || data.isEmpty()) {
            return;
        }

        if (mData == null) {
            mData = new ArrayList<>();
        }

        mData.addAll(data);
        notifyDataSetChanged();
    }

    public ArrayList<String> getSelectImages() {
        return selectImages;
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImage;
        ImageView ivSelectIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            ivImage = (ImageView) itemView.findViewById(R.id.iv_image);
            ivSelectIcon = (ImageView) itemView.findViewById(R.id.iv_select_icon);
        }
    }

    public interface OnImageSelectListener {
        void OnImageSelect(String image, boolean isSelect, int selectCount);
    }

    public void setOnImageSelectListener(OnImageSelectListener listener) {
        this.listener = listener;
    }
}
