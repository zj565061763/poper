/*
 * Copyright (C) 2017 zhengjun, fanwe (http://www.fanwe.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fanwe.lib.poper.layouter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.lib.poper.FPoper;

/**
 * 修正popview的显示边界
 */
public abstract class FixBoundLayouter extends BoundLayouter
{
    public FixBoundLayouter(Size size)
    {
        super(size);
    }

    @Override
    public final void layout(View popView, View popViewParent, FPoper poper)
    {
        final int parentSize = getParameter().getSize(popViewParent);
        if (parentSize <= 0)
        {
            return;
        }

        int consume = 0;

        final int start = getParameter().getStartBound(popView);
        if (start < 0)
        {
            consume += (-start);
        }

        final int end = getParameter().getEndBound(popView);
        if (end > parentSize)
        {
            consume += (end - parentSize);
        }

        final ViewGroup.LayoutParams params = popView.getLayoutParams();
        final int layoutParamsSize = getParameter().getLayoutParamsSize(params);
        if (consume > 0)
        {
            final int size = getParameter().getSize(popView);
            int fixSize = size - consume;
            if (fixSize < 0)
            {
                fixSize = 0;
            }

            if (layoutParamsSize == fixSize && fixSize == 0)
            {
                if (isDebug())
                {
                    Log.e(getDebugTag(), "ignored layoutParamsSize == fixSize && fixSize == 0");
                }
            } else
            {
                final boolean fixed = fixSizeOverBound(popView, params, layoutParamsSize, fixSize);
                if (fixed)
                {
                    if (isDebug())
                    {
                        Log.i(getDebugTag(), "fixSize over bound:" + getParameter().getLayoutParamsSize(params));
                    }
                }
            }
        } else
        {
            if (start > 0 && end < parentSize)
            {
                final boolean fixed = fixSizeWithinBound(popView, params, layoutParamsSize);
                if (fixed)
                {
                    if (isDebug())
                    {
                        Log.e(getDebugTag(), "fixSize within bound:" + getParameter().getLayoutParamsSize(params));
                    }
                }
            }
        }
    }

    protected boolean fixSizeOverBound(View popView, ViewGroup.LayoutParams params, int layoutParamsSize, int fixSize)
    {
        // 直接赋值，不检查layoutParamsSize != fixSize，因为有时候setLayoutParams(params)执行一次无效
        getParameter().setLayoutParamsSize(params, fixSize);
        popView.setLayoutParams(params);
        return true;
    }

    protected abstract boolean fixSizeWithinBound(View popView, ViewGroup.LayoutParams params, int layoutParamsSize);
}
