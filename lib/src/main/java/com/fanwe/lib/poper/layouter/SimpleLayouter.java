package com.fanwe.lib.poper.layouter;

import android.view.View;

public class SimpleLayouter implements PopLayouter
{
    @Override
    public void layout(int x, int y, View popView, View popViewParent)
    {
        final int differHorizontal = x - popView.getLeft();
        popView.offsetLeftAndRight(differHorizontal);

        final int differVertical = y - popView.getTop();
        popView.offsetTopAndBottom(differVertical);
    }
}
