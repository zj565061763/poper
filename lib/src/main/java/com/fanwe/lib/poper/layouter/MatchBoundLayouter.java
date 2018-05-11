package com.fanwe.lib.poper.layouter;

import android.view.View;
import android.view.ViewGroup;

/**
 * popview布局未超过父布局边界的时候采用{@link ViewGroup.LayoutParams#MATCH_PARENT}
 */
public class MatchBoundLayouter extends FixBoundLayouter
{
    public MatchBoundLayouter(Size size)
    {
        super(size);
    }

    @Override
    protected boolean fixSizeWithinBound(View popView, ViewGroup.LayoutParams params, int layoutParamsSize)
    {
        if (layoutParamsSize != ViewGroup.LayoutParams.MATCH_PARENT)
        {
            getParameter().setLayoutParamsSize(params, ViewGroup.LayoutParams.MATCH_PARENT);
            popView.setLayoutParams(params);
            return true;
        }
        return false;
    }
}
