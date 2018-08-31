package com.sd.lib.poper.layouter;

import android.view.View;

import com.sd.lib.poper.Poper;

public class DefaultLayouter implements Poper.Layouter
{
    @Override
    public void layout(int x, int y, View popView, View target)
    {
        popView.offsetLeftAndRight(x - popView.getLeft());
        popView.offsetTopAndBottom(y - popView.getTop());
    }
}
