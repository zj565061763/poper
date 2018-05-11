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

abstract class AbstractTargetSizeLayouter extends AbstractSizeLayouter
{
    public AbstractTargetSizeLayouter()
    {
    }

    public AbstractTargetSizeLayouter(boolean isDebug)
    {
        super(isDebug);
    }

    @Override
    public final void layout(View popView, View popViewParent, View targetView)
    {
        final int parentSize = getParameter().getSize(popViewParent);
        if (parentSize <= 0)
        {
            return;
        }

        final int targetSize = getParameter().getSize(targetView);
        final int popSize = getParameter().getSize(popView);
        if (popSize != targetSize)
        {
            final ViewGroup.LayoutParams params = popView.getLayoutParams();
            getParameter().setLayoutParamsSize(params, targetSize);
            popView.setLayoutParams(params);

            if (isDebug())
            {
                Log.i(getDebugTag(), "targetSize:" + targetSize);
            }
        }
    }
}
