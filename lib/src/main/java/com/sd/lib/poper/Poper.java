package com.sd.lib.poper;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface Poper {
    /**
     * 设置PopView
     *
     * @param layoutId 布局id
     */
    @NonNull
    Poper setPopView(int layoutId);

    /**
     * 设置PopView
     */
    @NonNull
    Poper setPopView(@Nullable View popView);

    /**
     * 设置目标view
     */
    @NonNull
    Poper setTarget(@Nullable View target);

    /**
     * 设置显示的位置{@link Position}
     */
    @NonNull
    Poper setPosition(@NonNull Position position);

    /**
     * {@link #setMarginX(Margin)}
     */
    @NonNull
    Poper setMarginX(int margin);

    /**
     * {@link #setMarginY(Margin)}
     */
    @NonNull
    Poper setMarginY(int margin);

    /**
     * 设置追踪到指定位置后，x值的偏移量，大于0往右，小于0往左
     */
    @NonNull
    Poper setMarginX(@Nullable Margin margin);

    /**
     * 设置追踪到指定位置后，y值的偏移量，大于0往下，小于0往上
     */
    @NonNull
    Poper setMarginY(@Nullable Margin margin);

    /**
     * 设置PopView显示的容器范围，默认是Activity中id为android.R.id.content的容器
     */
    @NonNull
    Poper setContainer(@Nullable ViewGroup container);

    /**
     * 设置位置绘制对象
     */
    @NonNull
    Poper setLayouter(@Nullable Layouter layouter);

    /**
     * 返回PopView
     */
    @Nullable
    View getPopView();

    /**
     * 返回Target
     */
    @Nullable
    View getTarget();

    /**
     * 当前PopView是否已经被添加到UI上面
     */
    boolean isAttached();

    /**
     * 添加/移除
     */
    @NonNull
    Poper attach(boolean attach);

    enum Position {
        /** 与target左上角对齐 */
        TopLeft,
        /** 与target顶部中间对齐 */
        TopCenter,
        /** 与target右上角对齐 */
        TopRight,

        /** 与target左边中间对齐 */
        LeftCenter,
        /** 中间对齐 */
        Center,
        /** 与target右边中间对齐 */
        RightCenter,

        /** 与target左下角对齐 */
        BottomLeft,
        /** 与target底部中间对齐 */
        BottomCenter,
        /** 与target右下角对齐 */
        BottomRight,

        /** 与target左边对齐 */
        Left,
        /** 与target顶部对齐 */
        Top,
        /** 与target右边对齐 */
        Right,
        /** 与target底部对齐 */
        Bottom,
    }

    /**
     * 绘制接口
     */
    interface Layouter {
        /**
         * 绘制回调
         *
         * @param x       按照指定位置让popview和目标view对齐后，popview相对于父布局在x方向需要是什么值
         * @param y       按照指定位置让popview和目标view对齐后，popview相对于父布局在y方向需要是什么值
         * @param popView popview
         * @param target  目标view
         */
        void layout(int x, int y, View popView, View target);
    }

    interface Margin {
        int getMargin();
    }
}
