package com.zh.android.compatandroid12.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.zh.android.compatandroid12.R;
import com.zh.android.compatandroid12.model.NumberModel;

import me.drakeet.multitype.ItemViewBinder;
import me.drakeet.multitype.ItemViewHolder;

public final class NumberViewBinder extends ItemViewBinder<NumberModel, NumberViewBinder.ViewHolder> {
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ViewHolder(inflater.inflate(R.layout.item_view_number, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull NumberModel item) {
        holder.vItem.setText(String.valueOf(item.getValue()));
    }

    static class ViewHolder extends ItemViewHolder {
        private final TextView vItem;

        public ViewHolder(View itemView) {
            super(itemView);
            vItem = itemView.findViewById(R.id.item);
        }
    }
}