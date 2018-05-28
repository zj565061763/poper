package com.fanwe.lib.poper;

import android.view.View;
import android.view.ViewGroup;

import com.fanwe.lib.poper.layouter.PopLayouter;

public interface Poper
{
    /**
     * 设置是否调试模式
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
    Poper setPosition(FPoper.Position position);

    /**
     * 设置x轴方向的偏移量，大于0往右，小于0往左
     *
     * @param marginX
     */
    Poper setMarginX(int marginX);

    /**
     * 设置y轴方向的偏移量，大于0往下，小于0往上
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
     * 添加{@link PopLayouter}
     *
     * @param layouter
     * @return
     */
    Poper addPopLayouter(PopLayouter layouter);

    /**
     * 移除{@link PopLayouter}
     *
     * @param layouter
     * @return
     */
    Poper removePopLayouter(PopLayouter layouter);

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
     * 2.popview对象<br>
     * <p>
     * 调用此方法会断开所有引用，并清空popview和target
     */
    void release();
}
