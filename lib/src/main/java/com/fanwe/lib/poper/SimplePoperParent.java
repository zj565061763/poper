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
package com.fanwe.lib.poper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by zhengjun on 2017/9/5.
 */
final class SimplePoperParent extends FrameLayout implements PoperParent
{
    private OnLayoutCallback mOnLayoutCallback;

    public SimplePoperParent(Context context)
    {
        super(context);
    }

    @Override
    public void setOnLayoutCallback(OnLayoutCallback onLayoutCallback)
    {
        mOnLayoutCallback = onLayoutCallback;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);

        if (mOnLayoutCallback != null)
            mOnLayoutCallback.onLayout();
    }

    @Override
    public void onViewAdded(View child)
    {
        super.onViewAdded(child);

        if (getChildCount() > 1)
            throw new RuntimeException("PoperParent can only add one child");
    }

    @Override
    public void onViewRemoved(View child)
    {
        super.onViewRemoved(child);

        if (getChildCount() <= 0)
        {
            try
            {
                ((ViewGroup) getParent()).removeView(this);
            } catch (Exception e)
            {
            }
        }
    }
}
