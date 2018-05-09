package com.fanwe.lib.poper.layouter;

import android.view.View;
import android.view.ViewGroup;

public class AutoSizeLayouter implements PopLayouter
{
    @Override
    public void layout(int x, int y, View popView, View popViewParent, View container)
    {
        DEFAULT.layout(x, y, popView, popViewParent, container);

        final ViewGroup.LayoutParams params = popView.getLayoutParams();
        final int parentHeight = popViewParent.getHeight();
        final int bottom = popView.getBottom();

        int targetHeight = 0;
        if (parentHeight > 0 && bottom >= parentHeight)
        {
            targetHeight = popView.getHeight() - (bottom - parentHeight);
        } else
        {
            targetHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
        }

        if (targetHeight != params.height)
        {
            params.height = targetHeight;
            popView.setLayoutParams(params);
        }
    }
}
