/*
 * Copyright (C) 2017 zhengjun, fanwe (http://www.fanwe.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fanwe.lib.poper;

import android.view.View;
import android.view.ViewGroup;

/**
 * popview父布局
 */
public interface PoperParent
{
    /**
     * 给当前布局设置一个layout回调，当前布局应该在{@link View#onLayout(boolean, int, int, int, int)}里面通知回调对象
     *
     * @param onLayoutCallback
     */
    void setOnLayoutCallback(OnLayoutCallback onLayoutCallback);

    /**
     * 把当前布局添加到容器
     *
     * @param container
     */
    void addToContainer(ViewGroup container);

    /**
     * 把popView添加到当前布局
     *
     * @param popView
     */
    void addPopView(View popView);

    /**
     * 更新popView的位置
     *
     * @param x       popview相对父布局的x
     * @param y       popview相对父布局的y
     * @param popView
     */
    void layoutPopView(int x, int y, View popView);

    /**
     * 把当前布局从父布局移除
     */
    void remove();

    interface OnLayoutCallback
    {
        void onLayout();
    }
}
