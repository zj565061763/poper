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
import android.view.ViewParent;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 可以让PopView显示在Target的某个位置
 */
public class FPoper implements Poper
{
    private final Activity mActivity;
    private DrawListener mDrawListener;

    private ViewGroup mContainer;
    private final PoperParent mPoperParent;
    private View mPopView;

    private WeakReference<View> mTarget;

    private Position mPosition = Position.TopRight;
    private int mMarginX;
    private int mMarginY;

    private final int[] mLocationTarget = {0, 0};
    private final int[] mLocationParent = {0, 0};
    private int mLayoutX;
    private int mLayoutY;

    private List<Layouter> mListLayouter;

    private boolean mIsDebug;

    public FPoper(Activity activity)
    {
        if (activity == null)
            throw new NullPointerException("activity is null");

        mActivity = activity;
        mPoperParent = new PoperParent(activity);

        final ViewGroup viewGroup = activity.findViewById(android.R.id.content);
        setContainer(viewGroup);
        getDrawListener().setView(viewGroup);
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
            view = LayoutInflater.from(mActivity).inflate(layoutId, mPoperParent, false);

        return setPopView(view);
    }

    @Override
    public Poper setPopView(View popView)
    {
        final View old = mPopView;
        if (old != popView)
        {
            mPopView = popView;

            if (popView == null)
                removeUpdateListener();
        }
        return this;
    }

    @Override
    public Poper setTarget(View target)
    {
        final View old = getTarget();
        if (old != target)
        {
            if (target != null)
            {
                mTarget = new WeakReference<>(target);
            } else
            {
                mTarget = null;
                removeUpdateListener();
            }
        }
        return this;
    }

    @Override
    public Poper setPosition(Position position)
    {
        if (position == null)
            throw new NullPointerException("position is null");

        mPosition = position;
        return this;
    }

    @Override
    public Poper setMarginX(int marginX)
    {
        mMarginX = marginX;
        return this;
    }

    @Override
    public Poper setMarginY(int marginY)
    {
        mMarginY = marginY;
        return this;
    }

    @Override
    public Poper setContainer(ViewGroup container)
    {
        if (container == null)
            throw new NullPointerException("container is null");

        mContainer = container;
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
        return mTarget == null ? null : mTarget.get();
    }

    @Override
    public boolean isAttached()
    {
        return mPopView != null &&
                mPopView.getParent() == mPoperParent &&
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
                updatePosition();
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
        setPopView(null);
        setTarget(null);
    }

    private void removePopView()
    {
        mPoperParent.removeView(mPopView);
    }

    private DrawListener getDrawListener()
    {
        if (mDrawListener == null)
        {
            mDrawListener = new DrawListener()
            {
                @Override
                protected void onRegisterChanged(boolean isRegister)
                {
                    super.onRegisterChanged(isRegister);
                    if (mIsDebug)
                        Log.i(Poper.class.getSimpleName(), FPoper.this + " DrawListener isRegister:" + isRegister);

                    if (isRegister)
                        mPoperParent.setOnLayoutCallback(mOnLayoutCallback);
                    else
                        mPoperParent.setOnLayoutCallback(null);
                }

                @Override
                protected void onDraw()
                {
                    updatePosition();
                }
            };
        }
        return mDrawListener;
    }

    private void addUpdateListener()
    {
        getDrawListener().register();
    }

    private void removeUpdateListener()
    {
        getDrawListener().unregister();
    }

    private final PoperParent.OnLayoutCallback mOnLayoutCallback = new PoperParent.OnLayoutCallback()
    {
        @Override
        public void onLayout()
        {
            updatePosition();
        }
    };

    /**
     * 刷新popview的位置
     */
    private void updatePosition()
    {
        if (mPopView == null)
            throw new NullPointerException("popview is null");

        final View target = getTarget();
        if (target == null)
        {
            removeUpdateListener();
            return;
        }

        final boolean isShown = target.isShown();
        synchronizeVisibilityIfNeed(isShown);
        if (!isShown)
            return;

        addToParentIfNeed();


        mPoperParent.getLocationOnScreen(mLocationParent);
        getTarget().getLocationOnScreen(mLocationTarget);

        mLayoutX = mLocationTarget[0] - mLocationParent[0] + mMarginX;
        mLayoutY = mLocationTarget[1] - mLocationParent[1] + mMarginY;

        switch (mPosition)
        {
            case TopLeft:
                layoutTopLeft(target);
                break;
            case TopCenter:
                layoutTopCenter(target);
                break;
            case TopRight:
                layoutTopRight(target);
                break;

            case LeftCenter:
                layoutLeftCenter(target);
                break;
            case Center:
                layoutCenter(target);
                break;
            case RightCenter:
                layoutRightCenter(target);
                break;

            case BottomLeft:
                layoutBottomLeft(target);
                break;
            case BottomCenter:
                layoutBottomCenter(target);
                break;
            case BottomRight:
                layoutBottomRight(target);
                break;

            case TopOutsideLeft:
                layoutTopOutsideLeft(target);
                break;
            case TopOutsideCenter:
                layoutTopOutsideCenter(target);
                break;
            case TopOutsideRight:
                layoutTopOutsideRight(target);
                break;

            case BottomOutsideLeft:
                layoutBottomOutsideLeft(target);
                break;
            case BottomOutsideCenter:
                layoutBottomOutsideCenter(target);
                break;
            case BottomOutsideRight:
                layoutBottomOutsideRight(target);
                break;

            case LeftOutsideTop:
                layoutLeftOutsideTop(target);
                break;
            case LeftOutsideCenter:
                layoutLeftOutsideCenter(target);
                break;
            case LeftOutsideBottom:
                layoutLeftOutsideBottom(target);
                break;

            case RightOutsideTop:
                layoutRightOutsideTop(target);
                break;
            case RightOutsideCenter:
                layoutRightOutsideCenter(target);
                break;
            case RightOutsideBottom:
                layoutRightOutsideBottom(target);
                break;
            default:
                break;
        }

        layoutIfNeed();
    }

    private void synchronizeVisibilityIfNeed(boolean isShown)
    {
        final int visibility = isShown ? View.VISIBLE : View.GONE;

        if (mPoperParent.getVisibility() != visibility)
            mPoperParent.setVisibility(visibility);
    }

    //---------- position start----------

    private void layoutTopLeft(View target)
    {
    }

    private void layoutTopCenter(View target)
    {
        mLayoutX += (target.getWidth() / 2 - mPopView.getWidth() / 2);
    }

    private void layoutTopRight(View target)
    {
        mLayoutX += (target.getWidth() - mPopView.getWidth());
    }

    private void layoutLeftCenter(View target)
    {
        mLayoutY += (target.getHeight() / 2 - mPopView.getHeight() / 2);
    }

    private void layoutCenter(View target)
    {
        layoutTopCenter(target);
        layoutLeftCenter(target);
    }

    private void layoutRightCenter(View target)
    {
        layoutTopRight(target);
        layoutLeftCenter(target);
    }

    private void layoutBottomLeft(View target)
    {
        mLayoutY += target.getHeight() - mPopView.getHeight();
    }

    private void layoutBottomCenter(View target)
    {
        layoutTopCenter(target);
        layoutBottomLeft(target);
    }

    private void layoutBottomRight(View target)
    {
        layoutTopRight(target);
        layoutBottomLeft(target);
    }

    private void layoutTopOutsideLeft(View target)
    {
        layoutTopLeft(target);
        mLayoutY -= mPopView.getHeight();
    }

    private void layoutTopOutsideCenter(View target)
    {
        layoutTopCenter(target);
        mLayoutY -= mPopView.getHeight();
    }

    private void layoutTopOutsideRight(View target)
    {
        layoutTopRight(target);
        mLayoutY -= mPopView.getHeight();
    }

    private void layoutBottomOutsideLeft(View target)
    {
        layoutBottomLeft(target);
        mLayoutY += mPopView.getHeight();
    }

    private void layoutBottomOutsideCenter(View target)
    {
        layoutBottomCenter(target);
        mLayoutY += mPopView.getHeight();
    }

    private void layoutBottomOutsideRight(View target)
    {
        layoutBottomRight(target);
        mLayoutY += mPopView.getHeight();
    }

    private void layoutLeftOutsideTop(View target)
    {
        layoutTopLeft(target);
        mLayoutX -= mPopView.getWidth();
    }

    private void layoutLeftOutsideCenter(View target)
    {
        layoutLeftCenter(target);
        mLayoutX -= mPopView.getWidth();
    }

    private void layoutLeftOutsideBottom(View target)
    {
        layoutBottomLeft(target);
        mLayoutX -= mPopView.getWidth();
    }

    private void layoutRightOutsideTop(View target)
    {
        layoutTopRight(target);
        mLayoutX += mPopView.getWidth();
    }

    private void layoutRightOutsideCenter(View target)
    {
        layoutRightCenter(target);
        mLayoutX += mPopView.getWidth();
    }

    private void layoutRightOutsideBottom(View target)
    {
        layoutBottomRight(target);
        mLayoutX += mPopView.getWidth();
    }

    //---------- position end----------

    private void addToParentIfNeed()
    {
        if (mPoperParent.getParent() != mContainer)
        {
            mPoperParent.removeSelf();

            final ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);

            mContainer.addView(mPoperParent, params);
        }

        final ViewParent parent = mPopView.getParent();
        if (parent != mPoperParent)
        {
            if (parent != null)
                throw new RuntimeException("PopView already has a parent");

            final ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            final ViewGroup.LayoutParams params = mPopView.getLayoutParams();
            if (params != null)
            {
                p.width = params.width;
                p.height = params.height;
            }

            mPoperParent.removeAllViews();
            mPoperParent.addView(mPopView, p);
        }
    }

    private void layoutIfNeed()
    {
        final int differHorizontal = mLayoutX - mPopView.getLeft();
        mPopView.offsetLeftAndRight(differHorizontal);

        final int differVertical = mLayoutY - mPopView.getTop();
        mPopView.offsetTopAndBottom(differVertical);

        if (mListLayouter != null)
        {
            for (Layouter item : mListLayouter)
            {
                item.layout(mPopView, mPoperParent, this);
            }
        }
    }

    private static boolean isViewAttached(View view)
    {
        if (Build.VERSION.SDK_INT >= 19)
            return view.isAttachedToWindow();
        else
            return view.getWindowToken() != null;
    }
}
