package com.fanwe.lib.poper.layouter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.lib.poper.Poper;

/**
 * 当view边界超出父布局边界的时候，修正view超出边界的部分，让view边界刚好和父布局边界重合
 * <p>
 * 当view边界小于父布局边界的时候，用设置的值({@link #setSizeWithinBound(int)})来更新view的大小
 */
public class FixBoundsLayouter implements Poper.Layouter
{
    private final Parameter mParameter;
    private int mSizeWithinBound = ViewGroup.LayoutParams.WRAP_CONTENT;

    private boolean mIsDebug;

    public FixBoundsLayouter(Bound bound)
    {
        if (bound == null)
            throw new NullPointerException("bound is null");

        mParameter = (bound == Bound.Width) ? new WidthParameter() : new HeightParameter();
    }

    public final FixBoundsLayouter setDebug(boolean debug)
    {
        mIsDebug = debug;
        return this;
    }

    /**
     * 设置view边界小于父布局边界时候的大小，默认{{@link ViewGroup.LayoutParams#WRAP_CONTENT}}
     *
     * @param size
     * @return
     */
    public final FixBoundsLayouter setSizeWithinBound(int size)
    {
        mSizeWithinBound = size;
        return this;
    }

    @Override
    public void layout(int x, int y, View popView, View popViewParent, View target)
    {
        final int parentSize = getParameter().getSize(popViewParent);
        if (parentSize <= 0)
            return;

        final int paddingStart = getParameter().getPaddingStart(popViewParent);
        final int paddingEnd = getParameter().getPaddingEnd(popViewParent);
        if (parentSize - (paddingStart + paddingEnd) <= 0)
            return;

        final int parentStart = paddingStart;
        final int parentEnd = parentSize - paddingEnd;

        int consume = 0;

        final int start = getParameter().getBoundsStart(popView);
        if (start < parentStart)
            consume += (parentStart - start);

        final int end = getParameter().getBoundsEnd(popView);
        if (end > parentEnd)
            consume += (end - parentEnd);

        final ViewGroup.LayoutParams params = popView.getLayoutParams();
        final int layoutParamsSize = getParameter().getLayoutParamsSize(params);
        if (consume > 0)
        {
            final int size = getParameter().getSize(popView);
            int fixSize = size - consume;
            if (fixSize < 0)
                fixSize = 0;

            if (layoutParamsSize == fixSize && fixSize == 0)
            {
                if (mIsDebug)
                    Log.e(FixBoundsLayouter.class.getSimpleName(), "ignored layoutParamsSize == fixSize && fixSize == 0");
            } else
            {
                final boolean fixed = fixSizeOverBound(popView, params, layoutParamsSize, fixSize);
                if (fixed)
                {
                    if (mIsDebug)
                        Log.i(FixBoundsLayouter.class.getSimpleName(), "fixSize over bound:" + getParameter().getLayoutParamsSize(params));
                }
            }
        } else
        {
            if (start > parentStart && end < parentEnd)
            {
                final boolean fixed = fixSizeWithinBound(popView, params, layoutParamsSize, mSizeWithinBound);
                if (fixed)
                {
                    if (mIsDebug)
                        Log.e(FixBoundsLayouter.class.getSimpleName(), "fixSize within bound:" + getParameter().getLayoutParamsSize(params));
                }
            }
        }
    }

    protected boolean fixSizeOverBound(View view, ViewGroup.LayoutParams params, int layoutParamsSize, int fixSize)
    {
        // 直接赋值，不检查 layoutParamsSize != fixSize，因为有时候setLayoutParams(params)执行一次无效
        getParameter().setLayoutParamsSize(params, fixSize);
        view.setLayoutParams(params);
        return true;
    }

    protected boolean fixSizeWithinBound(View view, ViewGroup.LayoutParams params, int layoutParamsSize, int fixSize)
    {
        if (layoutParamsSize != fixSize)
        {
            getParameter().setLayoutParamsSize(params, fixSize);
            view.setLayoutParams(params);
            return true;
        }
        return false;
    }

    protected final Parameter getParameter()
    {
        return mParameter;
    }

    public enum Bound
    {
        Width,
        Height
    }

    public interface Parameter
    {
        int getSize(View view);

        int getPaddingStart(View view);

        int getPaddingEnd(View view);

        int getBoundsStart(View view);

        int getBoundsEnd(View view);

        int getLayoutParamsSize(ViewGroup.LayoutParams params);

        void setLayoutParamsSize(ViewGroup.LayoutParams params, int size);
    }

    public class WidthParameter implements Parameter
    {
        @Override
        public int getSize(View view)
        {
            return view.getWidth();
        }

        @Override
        public int getPaddingStart(View view)
        {
            return view.getPaddingLeft();
        }

        @Override
        public int getPaddingEnd(View view)
        {
            return view.getPaddingRight();
        }

        @Override
        public int getBoundsStart(View view)
        {
            return view.getLeft();
        }

        @Override
        public int getBoundsEnd(View view)
        {
            return view.getRight();
        }

        @Override
        public int getLayoutParamsSize(ViewGroup.LayoutParams params)
        {
            return params.width;
        }

        @Override
        public void setLayoutParamsSize(ViewGroup.LayoutParams params, int size)
        {
            params.width = size;
        }
    }

    public class HeightParameter implements Parameter
    {
        @Override
        public int getSize(View view)
        {
            return view.getHeight();
        }

        @Override
        public int getPaddingStart(View view)
        {
            return view.getPaddingTop();
        }

        @Override
        public int getPaddingEnd(View view)
        {
            return view.getPaddingBottom();
        }

        @Override
        public int getBoundsStart(View view)
        {
            return view.getTop();
        }

        @Override
        public int getBoundsEnd(View view)
        {
            return view.getBottom();
        }

        @Override
        public int getLayoutParamsSize(ViewGroup.LayoutParams params)
        {
            return params.height;
        }

        @Override
        public void setLayoutParamsSize(ViewGroup.LayoutParams params, int size)
        {
            params.height = size;
        }
    }
}
