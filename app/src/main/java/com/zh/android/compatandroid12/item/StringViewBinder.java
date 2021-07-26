package com.zh.android.compatandroid12.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.zh.android.compatandroid12.R;
import com.zh.android.compatandroid12.model.StringModel;

import me.drakeet.multitype.ItemViewBinder;
import me.drakeet.multitype.ItemViewHolder;

public class StringViewBinder extends ItemViewBinder<StringModel, StringViewBinder.ViewHolder> {
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ViewHolder(inflater.inflate(R.layout.item_view_string, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull StringModel item) {
        holder.vItem.setText(item.getValue());
    }

    static class ViewHolder extends ItemViewHolder {
        private final TextView vItem;

        public ViewHolder(View itemView) {
            super(itemView);
            vItem = itemView.findViewById(R.id.item);
        }
    }
}