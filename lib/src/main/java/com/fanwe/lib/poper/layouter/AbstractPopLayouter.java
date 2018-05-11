package com.fanwe.lib.poper.layouter;

public abstract class AbstractPopLayouter implements PopLayouter
{
    private final boolean mIsDebug;

    public AbstractPopLayouter()
    {
        this(false);
    }

    public AbstractPopLayouter(boolean isDebug)
    {
        mIsDebug = isDebug;
    }

    public final boolean isDebug()
    {
        return mIsDebug;
    }

    protected String getDebugTag()
    {
        return getClass().getSimpleName();
    }
}
