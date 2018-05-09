package com.fanwe.lib.poper.layouter;

import android.view.View;
import android.view.ViewGroup;

public class AutoWidthLayouter implements PopLayouter
{
    @Override
    public void layout(int x, int y, View popView, View popViewParent)
    {
        final int parentSize = popViewParent.getWidth();
        if (parentSize <= 0)
        {
            return;
        }

        int consume = 0;

        final int right = popView.getWidth();
        if (right > parentSize)
        {
            consume += (right - parentSize);
        }

        final int left = popView.getLeft();
        if (left < 0)
        {
            consume += (-left);
        }

        final ViewGroup.LayoutParams params = popView.getLayoutParams();
        int size = params.width;
        if (consume > 0)
        {
            final int oldSize = popView.getWidth();
            size = oldSize - consume;
        } else
        {
            if (left > 0 && right < parentSize)
            {
                size = ViewGroup.LayoutParams.WRAP_CONTENT;
            }
        }

        if (params.width != size)
        {
            params.width = size;
            popView.setLayoutParams(params);
        }
    }
}
