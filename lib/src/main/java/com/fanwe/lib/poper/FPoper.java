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

import com.fanwe.lib.updater.Updater;
import com.fanwe.lib.updater.ViewUpdater;
import com.fanwe.lib.updater.impl.OnPreDrawUpdater;
import com.fanwe.lib.viewtracker.FViewTracker;
import com.fanwe.lib.viewtracker.ViewTracker;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 可以让PopView显示在Target的某个位置
 */
public class FPoper implements Poper
{
    private final Activity mActivity;

    private ViewGroup mContainer;
    private ViewGroup mPoperParent;
    private View mPopView;

    private ViewTracker mTracker;
    private ViewUpdater mUpdater;

    private List<Layouter> mListLayouter;

    private boolean mIsDebug;

    public FPoper(Activity activity)
    {
        if (activity == null)
            throw new NullPointerException("activity is null");

        mActivity = activity;
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

                    if (newSource == null)
                        removeUpdateListener();
                }

                @Override
                public void onTargetChanged(View newTarget, View oldTarget)
                {
                    super.onTargetChanged(newTarget, oldTarget);

                    if (newTarget == null)
                        removeUpdateListener();
                }

                @Override
                public boolean canUpdate(View source, View target)
                {
                    if (source == null)
                        throw new NullPointerException("popview is null");

                    if (target == null)
                        return false;

                    final boolean isShown = target.isShown();

                    final View poperParent = getPoperParent();
                    ((PoperParent) poperParent).synchronizeVisibilityWithTarget(isShown);

                    if (!isShown)
                        return false;

                    addToParentIfNeed();
                    return true;
                }

                @Override
                public void onUpdate(int x, int y, View source, View sourceParent, View target)
                {
                    final View poperParent = getPoperParent();

                    ((PoperParent) poperParent).layoutPopView(x, y, mPopView);
                    if (mListLayouter != null)
                    {
                        for (Layouter item : mListLayouter)
                        {
                            item.layout(mPopView, poperParent, FPoper.this);
                        }
                    }
                }
            });
        }
        return mTracker;
    }

    private ViewUpdater getUpdater()
    {
        if (mUpdater == null)
        {
            mUpdater = new OnPreDrawUpdater();
            mUpdater.setUpdatable(new Updater.Updatable()
            {
                @Override
                public void update()
                {
                    getTracker().update();
                }
            });
            mUpdater.setOnStateChangeCallback(new Updater.OnStateChangeCallback()
            {
                @Override
                public void onStateChanged(boolean started, Updater updater)
                {
                    if (mIsDebug)
                        Log.i(Poper.class.getSimpleName(), FPoper.this + " Updater isStarted:" + started);

                    final PoperParent parent = (PoperParent) getPoperParent();
                    if (started)
                        parent.setOnLayoutCallback(mOnLayoutCallback);
                    else
                        parent.setOnLayoutCallback(null);
                }
            });
            mUpdater.setView(mActivity.findViewById(android.R.id.content));
        }
        return mUpdater;
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
        View view = null;

        if (layoutId != 0)
            view = LayoutInflater.from(mActivity).inflate(layoutId, getPoperParent(), false);

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

            case TopOutsideLeft:
                getTracker().setPosition(ViewTracker.Position.TopOutsideLeft);
                break;
            case TopOutsideCenter:
                getTracker().setPosition(ViewTracker.Position.TopOutsideCenter);
                break;
            case TopOutsideRight:
                getTracker().setPosition(ViewTracker.Position.TopOutsideRight);
                break;

            case BottomOutsideLeft:
                getTracker().setPosition(ViewTracker.Position.BottomOutsideLeft);
                break;
            case BottomOutsideCenter:
                getTracker().setPosition(ViewTracker.Position.BottomOutsideCenter);
                break;
            case BottomOutsideRight:
                getTracker().setPosition(ViewTracker.Position.BottomOutsideRight);
                break;

            case LeftOutsideTop:
                getTracker().setPosition(ViewTracker.Position.LeftOutsideTop);
                break;
            case LeftOutsideCenter:
                getTracker().setPosition(ViewTracker.Position.LeftOutsideCenter);
                break;
            case LeftOutsideBottom:
                getTracker().setPosition(ViewTracker.Position.LeftOutsideBottom);
                break;

            case RightOutsideTop:
                getTracker().setPosition(ViewTracker.Position.RightOutsideTop);
                break;
            case RightOutsideCenter:
                getTracker().setPosition(ViewTracker.Position.RightOutsideCenter);
                break;
            case RightOutsideBottom:
                getTracker().setPosition(ViewTracker.Position.RightOutsideBottom);
                break;
            default:
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
    public Poper setContainer(ViewGroup container)
    {
        mContainer = container;
        return this;
    }

    @Override
    public Poper setPoperParent(ViewGroup parent)
    {
        if (!(parent instanceof PoperParent))
            throw new IllegalArgumentException("parent must be instance of " + PoperParent.class);

        mPoperParent = parent;
        return this;
    }

    @Override
    public Poper addLayouter(Layouter layouter)
    {
        if (layouter != null)
        {
            if (mListLayouter == null)
                mListLayouter = new CopyOnWriteArrayList<>();

            if (!mListLayouter.contains(layouter))
                mListLayouter.add(layouter);
        }
        return this;
    }

    @Override
    public Poper removeLayouter(Layouter layouter)
    {
        if (layouter != null && mListLayouter != null)
        {
            mListLayouter.remove(layouter);

            if (mListLayouter.isEmpty())
                mListLayouter = null;
        }
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
        if (mPopView == null || mPoperParent == null || mContainer == null)
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
            removePopView();
        }
        return this;
    }

    @Override
    public void release()
    {
        removeUpdateListener();
        mContainer = null;
        mPoperParent = null;
        mPopView = null;
        getTracker().setSource(null);
        getTracker().setTarget(null);
    }

    private void removePopView()
    {
        if (mPoperParent != null && mPoperParent.getParent() != null)
            ((PoperParent) mPoperParent).remove();
    }

    private ViewGroup getContainer()
    {
        if (mContainer == null)
            mContainer = mActivity.findViewById(android.R.id.content);
        return mContainer;
    }

    private ViewGroup getPoperParent()
    {
        if (mPoperParent == null)
            mPoperParent = new SimplePoperParent(mActivity);
        return mPoperParent;
    }

    private void addUpdateListener()
    {
        getUpdater().start();
    }

    private void removeUpdateListener()
    {
        getUpdater().stop();
    }

    private final PoperParent.OnLayoutCallback mOnLayoutCallback = new PoperParent.OnLayoutCallback()
    {
        @Override
        public void onLayout()
        {
            getTracker().update();
        }
    };

    private void addToParentIfNeed()
    {
        final ViewGroup container = getContainer();
        final ViewGroup poperParent = getPoperParent();

        if (poperParent.getParent() != container)
        {
            if (poperParent.getParent() != null)
                throw new RuntimeException("PopParent already has a parent");

            ((PoperParent) poperParent).addToContainer(container);
        }

        if (mPopView.getParent() != poperParent)
        {
            if (mPopView.getParent() != null)
                throw new RuntimeException("PopView already has a parent");

            ((PoperParent) poperParent).addPopView(mPopView);
        }
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
