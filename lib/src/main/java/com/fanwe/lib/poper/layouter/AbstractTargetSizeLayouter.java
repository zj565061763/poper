package com.fanwe.lib.poper.layouter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public abstract class AbstractTargetSizeLayouter extends AbstractSizeLayouter
{
    public AbstractTargetSizeLayouter()
    {
    }

    public AbstractTargetSizeLayouter(boolean isDebug)
    {
        super(isDebug);
    }

    @Override
    public final void layout(View popView, View popViewParent, View targetView)
    {
        final int parentSize = getParentSize(popViewParent);
        if (parentSize <= 0)
        {
            return;
        }

        final int targetSize = getTargetSize(targetView);
        final int popSize = getPopSize(popView);
        if (popSize != targetSize)
        {
            final ViewGroup.LayoutParams params = popView.getLayoutParams();
            setLayoutParamsSize(params, targetSize);
            popView.setLayoutParams(params);

            if (isDebug())
            {
                Log.i(getDebugTag(), "targetSize:" + targetSize);
            }
        }
    }

    protected abstract int getTargetSize(View targetView);
}
