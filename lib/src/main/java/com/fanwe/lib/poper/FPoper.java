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

import android.app.Activity;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.lib.poper.layouter.DefaultLayouter;
import com.fanwe.lib.updater.Updater;
import com.fanwe.lib.updater.ViewUpdater;
import com.fanwe.lib.updater.impl.OnLayoutChangeUpdater;
import com.fanwe.lib.updater.impl.OnPreDrawUpdater;
import com.fanwe.lib.viewtracker.FViewTracker;
import com.fanwe.lib.viewtracker.ViewTracker;

/**
 * 可以让PopView显示在Target的某个位置
 */
public class FPoper implements Poper
{
    private final Activity mActivity;

    private ViewGroup mContainer;
    private final ViewGroup mPoperParent;
    private View mPopView;

    private ViewTracker mTracker;
    private ViewUpdater mTargetUpdater;
    private ViewUpdater mPopUpdater;

    private Layouter mLayouter;

    private boolean mIsDebug;

    public FPoper(Activity activity)
    {
        if (activity == null)
            throw new NullPointerException("activity is null");

        mActivity = activity;
        mPoperParent = new SimplePoperParent(activity);
    }

    private ViewGroup getContainer()
    {
        if (mContainer == null)
            mContainer = mActivity.findViewById(android.R.id.content);
        return mContainer;
    }

    private ViewTracker getTracker()
    {
        if (mTracker == null)
        {
            mTracker = new FViewTracker();
            mTracker.setCallback(new ViewTracker.Callback()
            {
                @Override
                public void onSourceChanged(View newSource, View oldSource)
                {
                    super.onSourceChanged(newSource, oldSource);
                    mPopView = newSource;
                    getPopUpdater().setView(newSource);

                    if (newSource == null)
                        removeUpdateListener();
                }

                @Override
                public void onTargetChanged(View newTarget, View oldTarget)
                {
                    super.onTargetChanged(newTarget, oldTarget);
                    getTargetUpdater().setView(newTarget);

                    if (newTarget == null)
                        removeUpdateListener();
                }

                @Override
                public boolean canUpdate(View source, View target)
                {
                    if (mPopView == null)
                        throw new NullPointerException("popview is null");

                    if (target == null)
                        return false;

                    final boolean isShown = target.isShown();
                    final PoperParent parent = (PoperParent) mPoperParent;
                    parent.synchronizeVisibilityWithTarget(isShown);

                    if (!isShown)
                        return false;

                    parent.addToContainer(getContainer());
                    parent.addPopView(mPopView);

                    return true;
                }

                @Override
                public void onUpdate(int x, int y, View source, View sourceParent, View target)
                {
                    if (mLayouter == null)
                        mLayouter = new DefaultLayouter();

                    mLayouter.layout(x, y, source, sourceParent, target);
                }
            });
        }
        return mTracker;
    }

    private ViewUpdater getTargetUpdater()
    {
        if (mTargetUpdater == null)
        {
            mTargetUpdater = new OnPreDrawUpdater();
            mTargetUpdater.setUpdatable(new Updater.Updatable()
            {
                @Override
                public void update()
                {
                    getTracker().update();
                }
            });
            mTargetUpdater.setOnStateChangeCallback(new Updater.OnStateChangeCallback()
            {
                @Override
                public void onStateChanged(boolean started, Updater updater)
                {
                    if (mIsDebug)
                        Log.i(Poper.class.getSimpleName(), "TargetUpdater started:" + started);
                }
            });
        }
        return mTargetUpdater;
    }

    private ViewUpdater getPopUpdater()
    {
        if (mPopUpdater == null)
        {
            mPopUpdater = new OnLayoutChangeUpdater();
            mPopUpdater.setUpdatable(new Updater.Updatable()
            {
                @Override
                public void update()
                {
                    getTracker().update();
                }
            });
            mPopUpdater.setOnStateChangeCallback(new Updater.OnStateChangeCallback()
            {
                @Override
                public void onStateChanged(boolean started, Updater updater)
                {
                    if (mIsDebug)
                        Log.i(Poper.class.getSimpleName(), "PopUpdater started:" + started);
                }
            });
        }
        return mPopUpdater;
    }

    @Override
    public Poper setDebug(boolean debug)
    {
        mIsDebug = debug;
        return this;
    }

    @Override
    public Poper setPopView(int layoutId)
    {
        final View view = (layoutId == 0) ? null : LayoutInflater.from(mActivity).inflate(layoutId, mPoperParent, false);
        return setPopView(view);
    }

    @Override
    public Poper setPopView(final View popView)
    {
        getTracker().setSource(popView);
        return this;
    }

    @Override
    public Poper setTarget(final View target)
    {
        getTracker().setTarget(target);
        return this;
    }

    @Override
    public Poper setPosition(Position position)
    {
        if (position == null)
            throw new NullPointerException("position is null");

        switch (position)
        {
            case TopLeft:
                getTracker().setPosition(ViewTracker.Position.TopLeft);
                break;
            case TopCenter:
                getTracker().setPosition(ViewTracker.Position.TopCenter);
                break;
            case TopRight:
                getTracker().setPosition(ViewTracker.Position.TopRight);
                break;

            case LeftCenter:
                getTracker().setPosition(ViewTracker.Position.LeftCenter);
                break;
            case Center:
                getTracker().setPosition(ViewTracker.Position.Center);
                break;
            case RightCenter:
                getTracker().setPosition(ViewTracker.Position.RightCenter);
                break;

            case BottomLeft:
                getTracker().setPosition(ViewTracker.Position.BottomLeft);
                break;
            case BottomCenter:
                getTracker().setPosition(ViewTracker.Position.BottomCenter);
                break;
            case BottomRight:
                getTracker().setPosition(ViewTracker.Position.BottomRight);
                break;

            case Left:
                getTracker().setPosition(ViewTracker.Position.Left);
                break;
            case Top:
                getTracker().setPosition(ViewTracker.Position.Top);
                break;
            case Right:
                getTracker().setPosition(ViewTracker.Position.Right);
                break;
            case Bottom:
                getTracker().setPosition(ViewTracker.Position.Bottom);
                break;
        }
        return this;
    }

    @Override
    public Poper setMarginX(int marginX)
    {
        getTracker().setMarginX(marginX);
        return this;
    }

    @Override
    public Poper setMarginY(int marginY)
    {
        getTracker().setMarginY(marginY);
        return this;
    }

    @Override
    public Poper setMarginX(View view, boolean add)
    {
        getTracker().setMarginX(view, add);
        return this;
    }

    @Override
    public Poper setMarginY(View view, boolean add)
    {
        getTracker().setMarginY(view, add);
        return this;
    }

    @Override
    public Poper setContainer(ViewGroup container)
    {
        mContainer = container;
        return this;
    }

    @Override
    public Poper setLayouter(Layouter layouter)
    {
        mLayouter = layouter;
        return this;
    }

    @Override
    public View getPopView()
    {
        return mPopView;
    }

    @Override
    public View getTarget()
    {
        return getTracker().getTarget();
    }

    @Override
    public boolean isAttached()
    {
        if (mPopView == null || mContainer == null)
            return false;

        return mPopView.getParent() == mPoperParent &&
                mPoperParent.getParent() == mContainer &&
                isViewAttached(mContainer);
    }

    @Override
    public Poper attach(boolean attach)
    {
        if (attach)
        {
            if (getTarget() != null)
            {
                addUpdateListener();
                getTracker().update();
            }
        } else
        {
            removeUpdateListener();
            ((PoperParent) mPoperParent).remove();
        }
        return this;
    }

    @Override
    public void release()
    {
        removeUpdateListener();
    }

    private void addUpdateListener()
    {
        getTargetUpdater().start();
        getPopUpdater().start();
    }

    private void removeUpdateListener()
    {
        getTargetUpdater().stop();
        getPopUpdater().stop();
    }

    private static boolean isViewAttached(View view)
    {
        if (view == null)
            return false;

        if (Build.VERSION.SDK_INT >= 19)
            return view.isAttachedToWindow();
        else
            return view.getWindowToken() != null;
    }
}
