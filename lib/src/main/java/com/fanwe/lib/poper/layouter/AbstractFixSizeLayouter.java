package com.fanwe.lib.poper.layouter;

import android.view.View;

public abstract class AbstractFixSizeLayouter extends AbstractSizeLayouter
{
    @Override
    public final void layout(int x, int y, View popView, View popViewParent, View targetView)
    {
        final int parentSize = getParentSize(popViewParent);
        if (parentSize <= 0)
        {
            return;
        }

    }
}
