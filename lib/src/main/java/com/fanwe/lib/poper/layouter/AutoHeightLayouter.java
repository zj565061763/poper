package com.fanwe.lib.poper.layouter;

import android.view.View;
import android.view.ViewGroup;

public class AutoHeightLayouter implements PopLayouter
{
    @Override
    public void layout(int x, int y, View popView, View popViewParent)
    {
        final int parentSize = popViewParent.getHeight();
        if (parentSize <= 0)
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
        int size = params.height;
        if (consume > 0)
        {
            final int oldSize = popView.getHeight();
            size = oldSize - consume;
        } else
        {
            if (top > 0 && bottom < parentSize)
            {
                size = ViewGroup.LayoutParams.WRAP_CONTENT;
            }
        }

        if (params.height != size)
        {
            params.height = size;
            popView.setLayoutParams(params);
        }
    }
}
