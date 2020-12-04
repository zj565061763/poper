package com.sd.lib.poper.ext;

import android.view.View;

import com.sd.lib.viewtracker.FViewTracker;
import com.sd.lib.viewtracker.ViewTracker;
import com.sd.lib.viewupdater.ViewUpdater;
import com.sd.lib.viewupdater.impl.OnLayoutChangeUpdater;
import com.sd.lib.viewupdater.impl.OnPreDrawUpdater;

/**
 * 位置跟踪
 */
public class FPositionTracker
{
    private final ViewTracker mTracker = new FViewTracker();

    private ViewUpdater mSourceUpdater;
    private ViewUpdater mTargetUpdater;

    private ViewUpdater getSourceUpdater()
    {
        if (mSourceUpdater == null)
        {
            mSourceUpdater = new OnLayoutChangeUpdater();
            mSourceUpdater.setUpdatable(new ViewUpdater.Updatable()
            {
                @Override
                public void update()
                {
                    mTracker.update();
                }
            });
        }
        return mSourceUpdater;
    }

    private ViewUpdater getTargetUpdater()
    {
        if (mTargetUpdater == null)
        {
            mTargetUpdater = new OnPreDrawUpdater();
            mTargetUpdater.setUpdatable(new ViewUpdater.Updatable()
            {
                @Override
                public void update()
                {
                    mTracker.update();
                }
            });
        }
        return mTargetUpdater;
    }

    /**
     * 设置回调对象
     *
     * @param callback
     */
    public void setCallback(ViewTracker.Callback callback)
    {
        mTracker.setCallback(callback);
    }

    /**
     * 设置源View
     *
     * @param view
     */
    public void setSource(View view)
    {
        mTracker.setSource(view);
        getSourceUpdater().setView(view);
    }

    /**
     * 设置目标View
     *
     * @param view
     */
    public void setTarget(View view)
    {
        mTracker.setTarget(view);
        getTargetUpdater().setView(view);
    }

    /**
     * 设置追踪位置
     *
     * @param position
     */
    public void setPosition(ViewTracker.Position position)
    {
        mTracker.setPosition(position);
    }

    /**
     * 开始追踪
     */
    public void start()
    {
        getSourceUpdater().start();
        getTargetUpdater().start();
    }

    /**
     * 停止追踪
     */
    public void stop()
    {
        getSourceUpdater().stop();
        getTargetUpdater().stop();
    }
}