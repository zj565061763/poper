package com.sd.lib.poper.layouter;

import android.view.View;

import com.sd.lib.poper.Poper;

public class CombineLayouter implements Poper.Layouter
{
    private final Poper.Layouter[] mLayouters;

    public CombineLayouter(Poper.Layouter... layouters)
    {
        if (layouters == null || layouters.length <= 0)
            throw new IllegalArgumentException("layouters is null or empty");

        mLayouters = layouters;
    }

    @Override
    public void layout(int x, int y, View popView, View target)
    {
        for (Poper.Layouter item : mLayouters)
        {
            item.layout(x, y, popView, target);
        }
    }
}
