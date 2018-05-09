package com.fanwe.lib.poper.layouter;

import android.view.View;
import android.view.ViewGroup;

public class AutoHeightLayouter extends SimpleLayouter
{
    @Override
    public void layout(int x, int y, View popView, View popViewParent)
    {
        super.layout(x, y, popView, popViewParent);

        final int parentHeight = popViewParent.getHeight();
        if (parentHeight > 0)
        {
            int consume = 0;

            // bottom
            final int bottom = popView.getBottom();
            if (bottom > parentHeight)
            {
                consume += (bottom - parentHeight);
            }

            // top
            final int top = popView.getTop();
            if (top < 0)
            {
                consume += (-top);
            }

            final ViewGroup.LayoutParams params = popView.getLayoutParams();

            int height = params.height;
            if (consume > 0)
            {
                height = popView.getHeight() - consume;
            } else
            {
                if (top > 0 && bottom < parentHeight)
                {
                    height = ViewGroup.LayoutParams.WRAP_CONTENT;
                }
            }

            if (params.height != height)
            {
                params.height = height;
                popView.setLayoutParams(params);
            }
        }
    }
}