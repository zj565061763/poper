package com.fanwe.lib.poper.layouter;

import android.view.View;

public class AutoSizeLayouter implements PopLayouter
{
    private final AutoHeightLayouter mHeightLayouter = new AutoHeightLayouter();
    private final AutoWidthLayouter mWidthLayouter = new AutoWidthLayouter();

    @Override
    public void layout(int x, int y, View popView, View popViewParent)
    {
        mHeightLayouter.layout(x, y, popView, popViewParent);
        mWidthLayouter.layout(x, y, popView, popViewParent);
    }
}
