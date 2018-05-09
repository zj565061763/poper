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
package com.fanwe.lib.poper.layouter;

import android.view.View;

/**
 * popview坐标绘制接口
 */
public interface PopLayouter
{
    PopLayouter DEFAULT = new PopLayouter()
    {
        @Override
        public void layout(int x, int y, View popView, View popViewParent)
        {
            final int differHorizontal = x - popView.getLeft();
            popView.offsetLeftAndRight(differHorizontal);

            final int differVertical = y - popView.getTop();
            popView.offsetTopAndBottom(differVertical);
        }
    };

    /**
     * 刷新popview的位置
     *
     * @param x             popview在x方向相对父布局的位置
     * @param y             popview在y方向相对父布局的位置
     * @param popView
     * @param popViewParent popview父布局
     */
    void layout(int x, int y, View popView, View popViewParent);
}
