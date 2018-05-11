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
package com.fanwe.lib.poper.parameter;

import android.view.View;
import android.view.ViewGroup;

public class WidthParameter implements Parameter
{
    @Override
    public int getSize(View view)
    {
        return view.getWidth();
    }

    @Override
    public int getStartBound(View view)
    {
        return view.getLeft();
    }

    @Override
    public int getEndBound(View view)
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
