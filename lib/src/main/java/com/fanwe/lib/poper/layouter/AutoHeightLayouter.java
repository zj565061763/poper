package com.fanwe.lib.poper.layouter;

import android.view.View;
import android.view.ViewGroup;

public class AutoHeightLayouter implements PopLayouter
{
    @Override
    public void layout(int x, int y, View popView, View popViewParent)
    {
        final int parentSize = popViewParent.getHeight();
        final int popSize = popView.getHeight();
        if (parentSize <= 0 || popSize <= 0)
        {
            return;
        }

        int consume = 0;

        final int bottom = popView.getBottom();
        if (bottom > parentSize)
        {
            consume += (bottom - parentSize);
        }

        final int top = popView.getTop();
        if (top < 0)
        {
            consume += (-top);
        }

        final ViewGroup.LayoutParams params = popView.getLayoutParams();
        if (consume > 0)
        {
            final int fixSize = popSize - consume;
            if (fixSize < 0)
            {
                throw new IllegalArgumentException(this + " fixSize < 0");
            }
            params.height = fixSize;
            popView.setLayoutParams(params);
        } else
        {
            if (top > 0 && bottom < parentSize)
            {
                if (params.height != ViewGroup.LayoutParams.WRAP_CONTENT)
                {
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    popView.setLayoutParams(params);
                }
            }
        }
    }
}
