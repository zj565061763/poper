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
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zhengjun on 2017/9/5.
 */
class SDPoperParent extends ViewGroup
{
    public SDPoperParent(Context context)
    {
        super(context);
    }

    public SDPoperParent(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public SDPoperParent(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        final int count = getChildCount();
        for (int i = 0; i < count; i++)
        {
            View child = getChildAt(i);

            final int left = child.getLeft();
            final int top = child.getTop();
            final int right = left + child.getMeasuredWidth();
            final int bottom = top + child.getMeasuredHeight();
            child.layout(left, top, right, bottom);
        }
    }
}
