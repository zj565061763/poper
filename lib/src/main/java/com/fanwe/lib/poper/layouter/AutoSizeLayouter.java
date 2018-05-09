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

public class AutoSizeLayouter implements PopLayouter
{
    private final AutoHeightLayouter mHeightLayouter;
    private final AutoWidthLayouter mWidthLayouter;

    public AutoSizeLayouter()
    {
        this(false);
    }

    public AutoSizeLayouter(boolean isDebug)
    {
        mHeightLayouter = new AutoHeightLayouter(isDebug);
        mWidthLayouter = new AutoWidthLayouter(isDebug);
    }

    @Override
    public void layout(int x, int y, View popView, View popViewParent)
    {
        mHeightLayouter.layout(x, y, popView, popViewParent);
        mWidthLayouter.layout(x, y, popView, popViewParent);
    }
}
