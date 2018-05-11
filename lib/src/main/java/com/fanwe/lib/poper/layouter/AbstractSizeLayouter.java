package com.fanwe.lib.poper.layouter;

import android.view.View;

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
}
