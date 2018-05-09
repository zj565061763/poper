package com.fanwe.lib.poper.layouter;

import android.view.View;
import android.view.ViewGroup;

public class AutoSizeLayouter extends SimpleLayouter
{
    @Override
    public void layout(int x, int y, View popView, View popViewParent)
    {
        super.layout(x, y, popView, popViewParent);

        final int parentHeight = popViewParent.getHeight();
        if (parentHeight > 0)
        {
            final int bottom = popView.getBottom();
            if (bottom == parentHeight)
            {
                return;
            }

            int targetHeight = 0;
            if (bottom > parentHeight)
            {
                targetHeight = popView.getHeight() - (bottom - parentHeight);
            } else
            {
                targetHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
            }

            final ViewGroup.LayoutParams params = popView.getLayoutParams();
            if (targetHeight != params.height)
            {
                params.height = targetHeight;
                popView.setLayoutParams(params);
            }
        }
    }
}
