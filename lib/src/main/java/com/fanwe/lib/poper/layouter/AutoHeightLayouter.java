package com.fanwe.lib.poper.layouter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class AutoHeightLayouter implements PopLayouter
{
    private boolean mIsDebug;

    @Override
    public void layout(int x, int y, View popView, View popViewParent)
    {
        final int parentSize = popViewParent.getHeight();
        if (parentSize <= 0)
        {
            return;
        }

        int consume = 0;

        final int bottom = popView.getBottom();
        if (bottom > parentSize)
        {
            consume += (bottom - parentSize);
        }

        final int top = popView.getTop();
        if (top < 0)
        {
            consume += (-top);
        }

        final ViewGroup.LayoutParams params = popView.getLayoutParams();
        if (consume > 0)
        {
            int fixSize = popView.getHeight() - consume;
            if (fixSize < 0)
            {
                fixSize = 0;
            }

            if (params.height == fixSize && fixSize == 0)
            {
            } else
            {
                params.height = fixSize;
                popView.setLayoutParams(params);
                if (mIsDebug)
                {
                    Log.i(AutoHeightLayouter.class.getSimpleName(), "fixSize:" + fixSize);
                }
            }
        } else
        {
            if (top > 0 && bottom < parentSize)
            {
                if (params.height != ViewGroup.LayoutParams.WRAP_CONTENT)
                {
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    popView.setLayoutParams(params);

                    if (mIsDebug)
                    {
                        Log.e(AutoHeightLayouter.class.getSimpleName(), "WRAP_CONTENT");
                    }
                }
            }
        }
    }
}
