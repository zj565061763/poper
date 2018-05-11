package com.fanwe.lib.poper.layouter;

import android.view.View;
import android.view.ViewGroup;

public class TargetWidthLayouter extends AbstractTargetSizeLayouter
{
    public TargetWidthLayouter()
    {
    }

    public TargetWidthLayouter(boolean isDebug)
    {
        super(isDebug);
    }

    @Override
    protected int getTargetSize(View targetView)
    {
        return targetView.getWidth();
    }

    @Override
    protected int getParentSize(View popViewParent)
    {
        return popViewParent.getWidth();
    }

    @Override
    protected int getPopSize(View popView)
    {
        return popView.getWidth();
    }

    @Override
    protected int getLayoutParamsSize(ViewGroup.LayoutParams params)
    {
        return params.width;
    }

    @Override
    protected void setLayoutParamsSize(ViewGroup.LayoutParams params, int size)
    {
        params.width = size;
    }
}
