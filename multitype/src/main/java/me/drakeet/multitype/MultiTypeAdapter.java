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

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.Collections;
import java.util.List;

import static me.drakeet.multitype.Preconditions.checkNotNull;

/**
 * 多类型通用Adapter
 *
 * @author drakeet
 */
public class MultiTypeAdapter extends BaseAdapter {
    private static final String TAG = "MultiTypeAdapter";

    private @NonNull
    List<?> items;
    private @NonNull
    TypePool typePool;

    public MultiTypeAdapter() {
        this(Collections.emptyList());
    }

    public MultiTypeAdapter(@NonNull List<?> items) {
        this(items, new MultiTypePool());
    }

    public MultiTypeAdapter(@NonNull List<?> items, int initialCapacity) {
        this(items, new MultiTypePool(initialCapacity));
    }

    public MultiTypeAdapter(@NonNull List<?> items, @NonNull TypePool pool) {
        checkNotNull(items);
        checkNotNull(pool);
        this.items = items;
        this.typePool = pool;
    }

    /**
     * 注册ItemViewBinder，一种模型对应一个ItemViewBinder
     */
    public <T> void register(@NonNull Class<? extends T> clazz, @NonNull ItemViewBinder<T, ?> binder) {
        checkNotNull(clazz);
        checkNotNull(binder);
        checkAndRemoveAllTypesIfNeeded(clazz);
        register(clazz, binder, new DefaultLinker<>());
    }

    /**
     * 注册模型类和对应的ItemViewBinder，以及链接器
     */
    <T> void register(
            @NonNull Class<? extends T> clazz,
            @NonNull ItemViewBinder<T, ?> binder,
            @NonNull Linker<T> linker) {
        typePool.register(clazz, binder, linker);
        binder.adapter = this;
    }

    /**
     * 注册，返回一对多
     */
    @CheckResult
    public @NonNull
    <T> OneToManyFlow<T> register(@NonNull Class<? extends T> clazz) {
        checkNotNull(clazz);
        checkAndRemoveAllTypesIfNeeded(clazz);
        return new OneToManyBuilder<>(this, clazz);
    }

    /**
     * 注册指定TypePool中的信息到Adapter
     */
    public void registerAll(@NonNull final TypePool pool) {
        checkNotNull(pool);
        final int size = pool.size();
        for (int i = 0; i < size; i++) {
            registerWithoutChecking(
                    pool.getClass(i),
                    pool.getItemViewBinder(i),
                    pool.getLinker(i)
            );
        }
    }

    /**
     * 设置Adapter的数据
     */
    public void setItems(@NonNull List<?> items) {
        checkNotNull(items);
        this.items = items;
    }

    /**
     * 获取Adapter的数据
     */
    public @NonNull
    List<?> getItems() {
        return items;
    }

    /**
     * 绑定一个TypePool
     */
    public void setTypePool(@NonNull TypePool typePool) {
        checkNotNull(typePool);
        this.typePool = typePool;
    }

    /**
     * 获取绑定的TypePool
     */
    public @NonNull
    TypePool getTypePool() {
        return typePool;
    }

    /**
     * 获取条目对应的类型
     */
    @Override
    public final int getItemViewType(int position) {
        Object item = items.get(position);
        return indexInTypesOf(position, item);
    }

    /**
     * 返回有多少种条目类型
     */
    @Override
    public int getViewTypeCount() {
        return typePool.size();
    }

    /**
     * 获取条目View
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemViewHolder viewHolder;
        int itemViewType = getItemViewType(position);
        //没有View可以复用，创建ViewHolder，内部会创建布局
        if (convertView == null) {
            viewHolder = onCreateViewHolder(parent, itemViewType);
            convertView = viewHolder.itemView;
            //缓存ViewHolder到View中
            convertView.setTag(viewHolder);
        } else {
            //有布局复用，从View中取出绑定的ViewHolder
            viewHolder = (ItemViewHolder) convertView.getTag();
        }
        //设置相关属性
        viewHolder.setAdapter(this);
        viewHolder.setItemViewType(itemViewType);
        viewHolder.setAdapterPosition(position);
        //渲染ViewHolder，内部会渲染布局
        onBindViewHolder(viewHolder, position, getItems());
        return convertView;
    }

    /**
     * 创建条目的ViewHolder
     */
    public final ItemViewHolder onCreateViewHolder(ViewGroup parent, int indexViewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        //从类型池中，通过type类型，查找对应的ItemViewBinder实例
        ItemViewBinder<?, ?> binder = typePool.getItemViewBinder(indexViewType);
        //通知ItemViewBinder创建ViewHolder
        return binder.onCreateViewHolder(inflater, parent);
    }

    /**
     * 渲染条目时调用
     */
    @SuppressWarnings("unchecked")
    public final void onBindViewHolder(ItemViewHolder holder, int position, @NonNull List<?> payloads) {
        //获取条目数据
        Object item = items.get(position);
        //从类型池中，通过type类型，查找对应的ItemViewBinder实例
        ItemViewBinder binder = typePool.getItemViewBinder(holder.getItemViewType());
        //通知ItemViewBinder渲染条目
        binder.onBindViewHolder(holder, item, payloads);
    }

    /**
     * 获取条目数量
     */
    @Override
    public int getCount() {
        return items.size();
    }

    /**
     * 获取指定position的数据
     */
    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    /**
     * 获取条目Id
     */
    @Override
    @SuppressWarnings("unchecked")
    public final long getItemId(int position) {
        Object item = items.get(position);
        int itemViewType = getItemViewType(position);
        ItemViewBinder binder = typePool.getItemViewBinder(itemViewType);
        return binder.getItemId(item);
    }

    /**
     * 通过ViewHolder，获取对应的ItemViewBinder
     */
    private @NonNull
    ItemViewBinder getRawBinderByViewHolder(@NonNull ItemViewHolder holder) {
        return typePool.getItemViewBinder(holder.getItemViewType());
    }

    /**
     * 通过position查找条目对应的类型
     */
    int indexInTypesOf(int position, @NonNull Object item) throws BinderNotFoundException {
        int index = typePool.firstIndexOf(item.getClass());
        if (index != -1) {
            @SuppressWarnings("unchecked")
            Linker<Object> linker = (Linker<Object>) typePool.getLinker(index);
            return index + linker.index(position, item);
        }
        throw new BinderNotFoundException(item.getClass());
    }

    /**
     * 移除某个模型的ItemViewBinder
     */
    private void checkAndRemoveAllTypesIfNeeded(@NonNull Class<?> clazz) {
        if (typePool.unregister(clazz)) {
            Log.w(TAG, "You have registered the " + clazz.getSimpleName() + " type. " +
                    "It will override the original binder(s).");
        }
    }

    /**
     * 安全的注册方法，在注册前，先移除再注册，确保只注册一次
     */
    @SuppressWarnings("unchecked")
    private void registerWithoutChecking(@NonNull Class clazz, @NonNull ItemViewBinder binder, @NonNull Linker linker) {
        checkAndRemoveAllTypesIfNeeded(clazz);
        register(clazz, binder, linker);
    }
}