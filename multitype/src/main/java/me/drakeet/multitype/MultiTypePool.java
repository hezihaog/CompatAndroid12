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

import java.util.ArrayList;
import java.util.List;

import static me.drakeet.multitype.Preconditions.checkNotNull;

/**
 * An List implementation of TypePool.
 *
 * @author drakeet
 */
public class MultiTypePool implements TypePool {
    /**
     * 模型列表
     */
    private final @NonNull
    List<Class<?>> classes;
    /**
     * ItemViewBinder列表
     */
    private final @NonNull
    List<ItemViewBinder<?, ?>> binders;
    /**
     * 一对多链接器列表
     */
    private final @NonNull
    List<Linker<?>> linkers;

    /**
     * 创建类型池，并初始化3个列表
     */
    public MultiTypePool() {
        this.classes = new ArrayList<>();
        this.binders = new ArrayList<>();
        this.linkers = new ArrayList<>();
    }

    /**
     * 创建类型池，并指定初始化大小
     */
    public MultiTypePool(int initialCapacity) {
        this.classes = new ArrayList<>(initialCapacity);
        this.binders = new ArrayList<>(initialCapacity);
        this.linkers = new ArrayList<>(initialCapacity);
    }


    /**
     * 创建类型池，可传入3个列表实例
     */
    public MultiTypePool(
            @NonNull List<Class<?>> classes,
            @NonNull List<ItemViewBinder<?, ?>> binders,
            @NonNull List<Linker<?>> linkers) {
        checkNotNull(classes);
        checkNotNull(binders);
        checkNotNull(linkers);
        this.classes = classes;
        this.binders = binders;
        this.linkers = linkers;
    }

    /**
     * 注册，模型类和对应的ItemViewBinder，以及链接器
     */
    @Override
    public <T> void register(
            @NonNull Class<? extends T> clazz,
            @NonNull ItemViewBinder<T, ?> binder,
            @NonNull Linker<T> linker) {
        checkNotNull(clazz);
        checkNotNull(binder);
        checkNotNull(linker);
        classes.add(clazz);
        binders.add(binder);
        linkers.add(linker);
    }

    /**
     * 解注册
     */
    @Override
    public boolean unregister(@NonNull Class<?> clazz) {
        checkNotNull(clazz);
        boolean removed = false;
        while (true) {
            int index = classes.indexOf(clazz);
            if (index != -1) {
                classes.remove(index);
                binders.remove(index);
                linkers.remove(index);
                removed = true;
            } else {
                break;
            }
        }
        return removed;
    }

    /**
     * 返回注册的类型个数
     */
    @Override
    public int size() {
        return classes.size();
    }


    @Override
    public int firstIndexOf(@NonNull final Class<?> clazz) {
        checkNotNull(clazz);
        //从注册的模型列表中找，找到了直接返回
        int index = classes.indexOf(clazz);
        if (index != -1) {
            return index;
        }
        //没找到，再遍历一遍，看看是不是传了父类Class进来，是的话，返回
        for (int i = 0; i < classes.size(); i++) {
            if (classes.get(i).isAssignableFrom(clazz)) {
                return i;
            }
        }
        //没找到
        return -1;
    }

    /**
     * 根据位置，查找注册的模型类
     */
    @Override
    public @NonNull
    Class<?> getClass(int index) {
        return classes.get(index);
    }

    /**
     * 根据位置，查找注册的ItemViewBinder
     */
    @Override
    public @NonNull
    ItemViewBinder<?, ?> getItemViewBinder(int index) {
        return binders.get(index);
    }

    /**
     * 根据位置，查找链接器
     */
    @Override
    public @NonNull
    Linker<?> getLinker(int index) {
        return linkers.get(index);
    }
}
