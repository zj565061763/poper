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

import java.lang.ref.WeakReference;

/**
 * 让popview的大小跟随某个view的大小
 */
public class ViewBoundLayouter extends BoundLayouter
{
    private int mDeltaSize = 0;
    private WeakReference<View> mView;

    public ViewBoundLayouter(Size size, View view)
    {
        super(size);
        if (view == null)
        {
            throw new NullPointerException("view is null");
        }
        mView = new WeakReference<>(view);
    }

    /**
     * 设置大小的增量
     *
     * @param deltaSize
     * @return
     */
    public ViewBoundLayouter setDeltaSize(int deltaSize)
    {
        mDeltaSize = deltaSize;
        return this;
    }

    private View getView()
    {
        return mView == null ? null : mView.get();
    }

    @Override
    public final void layout(View popView, View popViewParent, FPoper poper)
    {
        final View view = getView();
        if (view == null)
        {
            poper.removePopLayouter(this);
            return;
        }

        final int parentSize = getParameter().getSize(popViewParent);
        if (parentSize <= 0)
        {
            return;
        }

        int viewSize = getParameter().getSize(view) + mDeltaSize;
        if (viewSize < 0)
        {
            viewSize = 0;
        }

        final int popSize = getParameter().getSize(popView);
        if (popSize != viewSize)
        {
            final ViewGroup.LayoutParams params = popView.getLayoutParams();
            getParameter().setLayoutParamsSize(params, viewSize);
            popView.setLayoutParams(params);

            if (isDebug())
            {
                Log.i(getDebugTag(), "viewSize:" + viewSize);
            }
        }
    }
}
