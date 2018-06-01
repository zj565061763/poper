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

public interface Poper
{
    /**
     * 设置是否调试模式，过滤日志tag:Poper
     *
     * @param debug
     * @return
     */
    Poper setDebug(boolean debug);

    /**
     * 设置要Pop的view
     *
     * @param layoutId 布局id
     * @return
     */
    Poper setPopView(int layoutId);

    /**
     * 设置要Pop的view
     *
     * @param popView
     * @return
     */
    Poper setPopView(View popView);

    /**
     * 设置目标view
     *
     * @param target
     */
    Poper setTarget(View target);

    /**
     * 设置显示的位置
     *
     * @param position
     */
    Poper setPosition(Position position);

    /**
     * 设置对齐后x轴方向的偏移量，大于0往右，小于0往左
     *
     * @param marginX
     */
    Poper setMarginX(int marginX);

    /**
     * 设置对齐后y轴方向的偏移量，大于0往下，小于0往上
     *
     * @param marginY
     */
    Poper setMarginY(int marginY);

    /**
     * 设置popview可以显示的容器范围<br>
     * 默认是Activity中id为android.R.id.content的容器
     *
     * @param container
     * @return
     */
    Poper setContainer(ViewGroup container);

    /**
     * 设置popview的父布局，必须实现{@link PoperParent}接口
     *
     * @param parent
     * @return
     */
    Poper setPoperParent(ViewGroup parent);

    /**
     * 添加{@link Layouter}
     *
     * @param layouter
     * @return
     */
    Poper addLayouter(Layouter layouter);

    /**
     * 移除{@link Layouter}
     *
     * @param layouter
     * @return
     */
    Poper removeLayouter(Layouter layouter);

    /**
     * 返回popview
     *
     * @return
     */
    View getPopView();

    /**
     * 返回Target
     *
     * @return
     */
    View getTarget();

    /**
     * 当前PopView是否已经被添加到Parent
     *
     * @return
     */
    boolean isAttached();

    /**
     * 把PopView添加到Parent
     *
     * @param attach
     * @return
     */
    Poper attach(boolean attach);

    /**
     * poper会被以下对象强引用：<br>
     * 1.Activity中id为android.R.id.content容器的ViewTreeObserver对象<br>
     * 2.popview父布局对象<br>
     * <p>
     * 调用此方法会断开所有引用，并清空popview和target
     */
    void release();

    enum Position
    {
        /**
         * 与target左上角对齐
         */
        TopLeft,
        /**
         * 与target顶部中间对齐
         */
        TopCenter,
        /**
         * 与target右上角对齐
         */
        TopRight,

        /**
         * 与target左边中间对齐
         */
        LeftCenter,
        /**
         * 中间对齐
         */
        Center,
        /**
         * 与target右边中间对齐
         */
        RightCenter,

        /**
         * 与target左下角对齐
         */
        BottomLeft,
        /**
         * 与target底部中间对齐
         */
        BottomCenter,
        /**
         * 与target右下角对齐
         */
        BottomRight,

        /**
         * 在target的顶部外侧靠左对齐
         */
        TopOutsideLeft,
        /**
         * 在target的顶部外侧左右居中
         */
        TopOutsideCenter,
        /**
         * 在target的顶部外侧靠右对齐
         */
        TopOutsideRight,

        /**
         * 在target的底部外侧靠左对齐
         */
        BottomOutsideLeft,
        /**
         * 在target的底部外侧左右居中
         */
        BottomOutsideCenter,
        /**
         * 在target的底部外侧靠右对齐
         */
        BottomOutsideRight,

        /**
         * 在target的左边外侧靠顶部对齐
         */
        LeftOutsideTop,
        /**
         * 在target的左边外侧上下居中
         */
        LeftOutsideCenter,
        /**
         * 在target的左边外侧靠底部对齐
         */
        LeftOutsideBottom,

        /**
         * 在target的右边外侧靠顶部对齐
         */
        RightOutsideTop,
        /**
         * 在target的右边外侧上下居中
         */
        RightOutsideCenter,
        /**
         * 在target的右边外侧靠底部对齐
         */
        RightOutsideBottom,
    }

    /**
     * Poper绘制接口
     * <p>
     * 可用于修正popview的宽高
     */
    interface Layouter
    {
        /**
         * 绘制回调
         *
         * @param popView
         * @param popViewParent popview父布局
         * @param poper
         */
        void layout(View popView, View popViewParent, FPoper poper);
    }
}
