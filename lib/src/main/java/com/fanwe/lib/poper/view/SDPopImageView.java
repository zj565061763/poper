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
package com.fanwe.lib.poper.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.fanwe.lib.poper.SDPoper;

/**
 * 可以对某个view镜像截图，并把截图的bitmap设置给当前ImageView
 */
public class SDPopImageView extends ImageView
{
    public SDPopImageView(Context context)
    {
        super(context);
        init();
    }

    public SDPopImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    private SDPoper mPoper;

    private void init()
    {
        if (!(getContext() instanceof Activity))
        {
            throw new IllegalArgumentException("context must be instance of Activity");
        }
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
        return this;
    }

    public SDPoper getPoper()
    {
        if (mPoper == null)
        {
            mPoper = new SDPoper((Activity) getContext());
            mPoper.setPopView(this).setPosition(SDPoper.Position.TopLeft);
        }
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
