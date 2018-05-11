package com.fanwe.lib.poper.layouter;

import android.view.View;
import android.view.ViewGroup;

/**
 * popview布局未超过父布局边界的时候采用{@link ViewGroup.LayoutParams#WRAP_CONTENT}
 */
public class FixWrapLayouter extends FixBoundLayouter
{
    public FixWrapLayouter(Size size)
    {
        super(size);
    }

    @Override
    protected boolean fixSizeWithinBound(View popView, ViewGroup.LayoutParams params, int layoutParamsSize)
    {
        if (layoutParamsSize != ViewGroup.LayoutParams.WRAP_CONTENT)
        {
            getParameter().setLayoutParamsSize(params, ViewGroup.LayoutParams.WRAP_CONTENT);
            popView.setLayoutParams(params);
            return true;
        }
        return false;
    }
}
