/*
 * Copyright 2016 drakeet. https://github.com/drakeet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.drakeet.multitype;

import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

/***
 * 条目绑定类，一个类型对应一个Binder类
 * @author drakeet
 */
public abstract class ItemViewBinder<T, VH extends ItemViewHolder> {
    /* internal */ MultiTypeAdapter adapter;

    /**
     * 创建ViewHolder时回调
     */
    protected abstract @NonNull
    VH onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent);

    /**
     * 渲染条目
     */
    protected void onBindViewHolder(@NonNull VH holder, @NonNull T item, @NonNull List<Object> payloads) {
        onBindViewHolder(holder, item);
    }

    /**
     * 渲染条目
     */
    protected abstract void onBindViewHolder(@NonNull VH holder, @NonNull T item);

    /**
     * 获取当前条目在列表中的位置
     */
    protected final int getPosition(@NonNull final ItemViewHolder holder) {
        return holder.getAdapterPosition();
    }

    /**
     * 获取条目所在的列表的Adapter适配器
     */
    protected final @NonNull
    MultiTypeAdapter getAdapter() {
        if (adapter == null) {
            throw new IllegalStateException("ItemViewBinder " + this + " not attached to MultiTypeAdapter. " +
                    "You should not call the method before registering the binder.");
        }
        return adapter;
    }

    /**
     * 返回条目Id，默认为-1
     */
    protected long getItemId(@NonNull T item) {
        return ListView.NO_ID;
    }
}