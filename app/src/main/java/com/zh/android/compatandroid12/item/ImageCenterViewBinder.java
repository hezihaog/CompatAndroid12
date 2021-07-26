package com.zh.android.compatandroid12.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.zh.android.compatandroid12.model.ImageModel;
import com.zh.android.compatandroid12.R;

import me.drakeet.multitype.ItemViewBinder;

/**
 * 图片居中的条目
 */
public class ImageCenterViewBinder extends ItemViewBinder<ImageModel, ImageCenterViewBinder.ViewHolder> {
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ViewHolder(inflater.inflate(R.layout.item_image_center, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ImageCenterViewBinder.ViewHolder holder, @NonNull ImageModel item) {
        holder.vImage.setImageResource(item.getImgResId());
    }

    static class ViewHolder extends me.drakeet.multitype.ItemViewHolder {
        private final ImageView vImage;

        public ViewHolder(View itemView) {
            super(itemView);
            vImage = itemView.findViewById(R.id.image);
        }
    }
}