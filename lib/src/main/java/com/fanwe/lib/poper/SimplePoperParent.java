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
import android.view.ViewParent;
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
    public void addToContainer(ViewGroup container)
    {
        final ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        container.addView(this, params);
    }

    @Override
    public void addPopView(View popView)
    {
        final ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        final ViewGroup.LayoutParams params = popView.getLayoutParams();
        if (params != null)
        {
            p.width = params.width;
            p.height = params.height;
        }

        removeAllViews();
        addView(popView, p);
    }

    @Override
    public void synchronizeVisibilityWithTarget(boolean isShown)
    {
        final int visibility = isShown ? View.VISIBLE : View.GONE;

        if (getVisibility() != visibility)
            setVisibility(visibility);
    }

    @Override
    public void remove()
    {
        final ViewParent parent = getParent();
        if (parent == null)
            return;
        try
        {
            ((ViewGroup) parent).removeView(this);
        } catch (Exception e)
        {
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);

        if (mOnLayoutCallback != null)
            mOnLayoutCallback.onLayout();
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom)
    {
        if (left < 0)
            left = getPaddingLeft();
        if (top < 0)
            top = getPaddingTop();
        if (right < 0)
            right = getPaddingRight();
        if (bottom < 0)
            bottom = getPaddingBottom();

        if (left != getPaddingLeft() || top != getPaddingTop()
                || right != getPaddingRight() || bottom != getPaddingBottom())
        {
            super.setPadding(left, top, right, bottom);
        }
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
            remove();
    }
}
