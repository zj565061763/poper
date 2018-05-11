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

import android.view.View;
import android.view.ViewGroup;

/**
 * 宽度自适应
 */
public class AutoWidthLayouter extends AbstractAutoSizeLayouter
{
    public AutoWidthLayouter()
    {
    }

    public AutoWidthLayouter(boolean isDebug)
    {
        super(isDebug);
    }

    @Override
    protected int getParentSize(View popViewParent)
    {
        return popViewParent.getWidth();
    }

    @Override
    protected int getStartBound(View popView)
    {
        return popView.getLeft();
    }

    @Override
    protected int getEndBound(View popView)
    {
        return popView.getRight();
    }

    @Override
    protected int getSize(View popView)
    {
        return popView.getWidth();
    }

    @Override
    protected int getLayoutParamsSize(ViewGroup.LayoutParams params)
    {
        return params.width;
    }

    @Override
    protected void setLayoutParamsSize(ViewGroup.LayoutParams params, int fixSize)
    {
        params.width = fixSize;
    }
}
