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
package com.fanwe.library.poper.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.fanwe.library.poper.SDPoper;

/**
 * 可以对某个view镜像截图，并把截图的bitmap设置给当前ImageView
 */
public class SDPopImageView extends ImageView
{
    private SDPoper mPoper;

    public SDPopImageView(View drawingCacheView)
    {
        super(drawingCacheView.getContext());

        mPoper = new SDPoper((Activity) drawingCacheView.getContext());
        mPoper.setPopView(this).setPosition(SDPoper.Position.TopLeft);

        setDrawingCacheView(drawingCacheView);
    }

    /**
     * 设置要截图的view
     *
     * @param drawingCacheView
     * @return
     */
    public SDPopImageView setDrawingCacheView(View drawingCacheView)
    {
        Bitmap bitmap = createViewBitmap(drawingCacheView);
        setImageBitmap(bitmap);
        mPoper.setTarget(drawingCacheView);
        return this;
    }

    public SDPoper getPoper()
    {
        return mPoper;
    }

    private static Bitmap createViewBitmap(View view)
    {
        if (view == null)
        {
            return null;
        }
        view.setDrawingCacheEnabled(true);
        Bitmap drawingCache = view.getDrawingCache();
        if (drawingCache == null)
        {
            return null;
        }

        Bitmap bmp = Bitmap.createBitmap(drawingCache);
        view.destroyDrawingCache();
        return bmp;
    }
}
