package com.sd.lib.poper;

import android.view.View;
import android.view.ViewGroup;

/**
 * popview父布局
 */
interface PoperParent
{
    /**
     * 把当前布局添加到容器
     *
     * @param container
     */
    void attachToContainer(ViewGroup container);

    /**
     * 把popView添加到当前布局
     *
     * @param popView
     */
    void addPopView(View popView);

    /**
     * 同步target的可见状态到当前布局
     *
     * @param isShown true-target可见，false-target不可见
     */
    void synchronizeVisibilityWithTarget(boolean isShown);

    /**
     * 把当前布局从父布局移除
     */
    void removeSelf();
}
