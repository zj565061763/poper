package com.fanwe.lib.poper.layouter;

import android.view.View;

/**
 * popview坐标绘制接口
 */
public interface PopLayouter
{
    PopLayouter DEFAULT = new SimpleLayouter();

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
