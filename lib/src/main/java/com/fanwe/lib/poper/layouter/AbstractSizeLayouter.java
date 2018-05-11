package com.fanwe.lib.poper.layouter;

import android.view.View;
import android.view.ViewGroup;

public abstract class AbstractSizeLayouter extends AbstractPopLayouter
{
    public AbstractSizeLayouter()
    {
    }

    public AbstractSizeLayouter(boolean isDebug)
    {
        super(isDebug);
    }

    protected abstract int getParentSize(View popViewParent);

    protected abstract int getPopSize(View popView);

    protected abstract int getLayoutParamsSize(ViewGroup.LayoutParams params);

    protected abstract void setLayoutParamsSize(ViewGroup.LayoutParams params, int size);
}
