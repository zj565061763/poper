package com.fanwe.lib.poper.layouter;

import android.view.View;

public class AutoSizeLayouter implements PopLayouter
{
    private final AutoHeightLayouter mHeightLayouter;
    private final AutoWidthLayouter mWidthLayouter;

    public AutoSizeLayouter()
    {
        this(false);
    }

    public AutoSizeLayouter(boolean isDebug)
    {
        mHeightLayouter = new AutoHeightLayouter(isDebug);
        mWidthLayouter = new AutoWidthLayouter(isDebug);
    }

    @Override
    public void layout(int x, int y, View popView, View popViewParent)
    {
        mHeightLayouter.layout(x, y, popView, popViewParent);
        mWidthLayouter.layout(x, y, popView, popViewParent);
    }
}
