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
        mPoper.setPopView(this)
                .setDynamicUpdate(false)
                .setPosition(SDPoper.Position.TopLeft);

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
