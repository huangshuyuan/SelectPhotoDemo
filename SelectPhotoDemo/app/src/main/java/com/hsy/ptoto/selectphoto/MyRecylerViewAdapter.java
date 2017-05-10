package com.hsy.ptoto.selectphoto;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.impl.AlbumImageLoader;

import java.io.File;
import java.util.List;

/**
 * Created by huangshuyuan on 2017/5/10.
 */

public class MyRecylerViewAdapter extends RecyclerView.Adapter<MyRecylerViewAdapter.ImageViewHolder> {
    private LayoutInflater mInflater;
    private int itemSize;
    private List<String> mImagePathList;
    private OnItemClickListener mItemClickListener;

    public MyRecylerViewAdapter(Context context, OnItemClickListener mItemClickListener, int itemSize) {
        this.mInflater = LayoutInflater.from(context);
        this.itemSize = itemSize;
        this.mItemClickListener = mItemClickListener;
    }


    public void notifyDataSetChanged(List<String> imagePathList) {
        this.mImagePathList = imagePathList;
        super.notifyDataSetChanged();
    }


    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageViewHolder viewHolder = new ImageViewHolder(itemSize, mInflater.inflate(R.layout.item_image, parent, false));
        viewHolder.mItemClickListener = mItemClickListener;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        holder.loadImage(mImagePathList.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return mImagePathList == null ? 0 : mImagePathList.size();
    }


    /*严大神的*/
    static class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private OnItemClickListener mItemClickListener;
        private int itemSize;
        ImageView mIvIcon;

        public ImageViewHolder(int itemSize, View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.itemSize = itemSize;
            itemView.getLayoutParams().height = itemSize;
            itemView.requestLayout();
            mIvIcon = (ImageView) itemView.findViewById(R.id.iv_img);
        }

        public void loadImage(String imagePath) {
            //加载本地图片
//            GlideImageLoader
            Album.getAlbumConfig().getImageLoader().loadImage(mIvIcon, imagePath, itemSize, itemSize);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
