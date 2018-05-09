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
        public void layout(int x, int y, View popView, View popViewParent, View container)
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
     * @param container     popview父布局所在的容器
     */
    void layout(int x, int y, View popView, View popViewParent, View container);
}
