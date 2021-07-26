package me.drakeet.multitype;

import android.view.View;
import android.widget.BaseAdapter;

/**
 * 列表ViewHolder
 */
public class ItemViewHolder {
    /**
     * 适配器
     */
    private BaseAdapter adapter;
    /**
     * 条目View
     */
    public final View itemView;
    /**
     * 条目类型
     */
    private int mItemViewType;
    /**
     * 位置
     */
    private int adapterPosition;

    public ItemViewHolder(View itemView) {
        this.itemView = itemView;
    }

    public void setItemViewType(int itemViewType) {
        mItemViewType = itemViewType;
    }

    public int getItemViewType() {
        return mItemViewType;
    }

    public void setAdapter(BaseAdapter adapter) {
        this.adapter = adapter;
    }

    public BaseAdapter getAdapter() {
        return adapter;
    }

    public void setAdapterPosition(int adapterPosition) {
        this.adapterPosition = adapterPosition;
    }

    public int getAdapterPosition() {
        return adapterPosition;
    }
}