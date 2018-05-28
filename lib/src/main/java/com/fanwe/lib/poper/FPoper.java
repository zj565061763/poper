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

import com.fanwe.lib.poper.layouter.PopLayouter;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 可以让PopView显示在Target的某个位置
 */
public class FPoper implements Poper
{
    private final Activity mActivity;
    private ActivityDrawListener mActivityDrawListener;

    private ViewGroup mContainer;
    private final PoperParent mPoperParent;
    private View mPopView;

    private WeakReference<View> mTarget;

    private Position mPosition = Position.TopRight;
    private int mMarginX;
    private int mMarginY;

    private final int[] mLocationTarget = {0, 0};
    private final int[] mLocationParent = {0, 0};
    private int mMarginLeft;
    private int mMarginTop;

    private List<PopLayouter> mListLayouter;

    private boolean mIsDebug;

    public FPoper(Activity activity)
    {
        if (activity == null)
            throw new NullPointerException("activity is null");

        mActivity = activity;
        mPoperParent = new PoperParent(activity);
        mContainer = activity.findViewById(android.R.id.content);
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
            if (old != null)
                old.removeOnLayoutChangeListener(mOnLayoutChangeListenerPopView);

            mPopView = popView;

            if (popView != null)
                popView.addOnLayoutChangeListener(mOnLayoutChangeListenerPopView);
            else
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
    public Poper addPopLayouter(PopLayouter layouter)
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
    public Poper removePopLayouter(PopLayouter layouter)
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

    private View.OnLayoutChangeListener mOnLayoutChangeListenerPopView = new View.OnLayoutChangeListener()
    {
        @Override
        public void onLayoutChange(View v,
                                   int left, int top, int right, int bottom,
                                   int oldLeft, int oldTop, int oldRight, int oldBottom)
        {
            updatePosition();
        }
    };

    private ActivityDrawListener getActivityDrawListener()
    {
        if (mActivityDrawListener == null)
        {
            mActivityDrawListener = new ActivityDrawListener(mActivity);
            mActivityDrawListener.setCallback(new ActivityDrawListener.Callback()
            {
                @Override
                public void onActivityDraw()
                {
                    updatePosition();
                }
            });
        }
        return mActivityDrawListener;
    }

    private void addUpdateListener()
    {
        if (getActivityDrawListener().register())
        {
            if (mIsDebug)
                Log.i(Poper.class.getSimpleName(), "addUpdateListener:" + this);
        }
    }

    private void removeUpdateListener()
    {
        if (getActivityDrawListener().unregister())
        {
            if (mIsDebug)
                Log.e(Poper.class.getSimpleName(), "removeUpdateListener:" + this);
        }
    }

    /**
     * 刷新popview的位置
     */
    private void updatePosition()
    {
        if (mPopView == null)
            throw new NullPointerException("PopView is null");

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

        saveLocationInfo();
        mMarginLeft = mLocationTarget[0] - mLocationParent[0] + mMarginX;
        mMarginTop = mLocationTarget[1] - mLocationParent[1] + mMarginY;

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

    /**
     * 保存位置信息
     */
    private void saveLocationInfo()
    {
        mPoperParent.getLocationOnScreen(mLocationParent);
        getTarget().getLocationOnScreen(mLocationTarget);
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
        mMarginLeft += (target.getWidth() / 2 - mPopView.getWidth() / 2);
    }

    private void layoutTopRight(View target)
    {
        mMarginLeft += (target.getWidth() - mPopView.getWidth());
    }

    private void layoutLeftCenter(View target)
    {
        mMarginTop += (target.getHeight() / 2 - mPopView.getHeight() / 2);
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
        mMarginTop += target.getHeight() - mPopView.getHeight();
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
        mMarginTop -= mPopView.getHeight();
    }

    private void layoutTopOutsideCenter(View target)
    {
        layoutTopCenter(target);
        mMarginTop -= mPopView.getHeight();
    }

    private void layoutTopOutsideRight(View target)
    {
        layoutTopRight(target);
        mMarginTop -= mPopView.getHeight();
    }

    private void layoutBottomOutsideLeft(View target)
    {
        layoutBottomLeft(target);
        mMarginTop += mPopView.getHeight();
    }

    private void layoutBottomOutsideCenter(View target)
    {
        layoutBottomCenter(target);
        mMarginTop += mPopView.getHeight();
    }

    private void layoutBottomOutsideRight(View target)
    {
        layoutBottomRight(target);
        mMarginTop += mPopView.getHeight();
    }

    private void layoutLeftOutsideTop(View target)
    {
        layoutTopLeft(target);
        mMarginLeft -= mPopView.getWidth();
    }

    private void layoutLeftOutsideCenter(View target)
    {
        layoutLeftCenter(target);
        mMarginLeft -= mPopView.getWidth();
    }

    private void layoutLeftOutsideBottom(View target)
    {
        layoutBottomLeft(target);
        mMarginLeft -= mPopView.getWidth();
    }

    private void layoutRightOutsideTop(View target)
    {
        layoutTopRight(target);
        mMarginLeft += mPopView.getWidth();
    }

    private void layoutRightOutsideCenter(View target)
    {
        layoutRightCenter(target);
        mMarginLeft += mPopView.getWidth();
    }

    private void layoutRightOutsideBottom(View target)
    {
        layoutBottomRight(target);
        mMarginLeft += mPopView.getWidth();
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
        final int differHorizontal = mMarginLeft - mPopView.getLeft();
        mPopView.offsetLeftAndRight(differHorizontal);

        final int differVertical = mMarginTop - mPopView.getTop();
        mPopView.offsetTopAndBottom(differVertical);

        if (mListLayouter != null)
        {
            for (PopLayouter item : mListLayouter)
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

    public enum Position
    {
        /**
         * 与target左上角对齐
         */
        TopLeft,
        /**
         * 与target顶部中间对齐
         */
        TopCenter,
        /**
         * 与target右上角对齐
         */
        TopRight,

        /**
         * 与target左边中间对齐
         */
        LeftCenter,
        /**
         * 中间对齐
         */
        Center,
        /**
         * 与target右边中间对齐
         */
        RightCenter,

        /**
         * 与target左下角对齐
         */
        BottomLeft,
        /**
         * 与target底部中间对齐
         */
        BottomCenter,
        /**
         * 与target右下角对齐
         */
        BottomRight,

        /**
         * 在target的顶部外侧靠左对齐
         */
        TopOutsideLeft,
        /**
         * 在target的顶部外侧左右居中
         */
        TopOutsideCenter,
        /**
         * 在target的顶部外侧靠右对齐
         */
        TopOutsideRight,

        /**
         * 在target的底部外侧靠左对齐
         */
        BottomOutsideLeft,
        /**
         * 在target的底部外侧左右居中
         */
        BottomOutsideCenter,
        /**
         * 在target的底部外侧靠右对齐
         */
        BottomOutsideRight,

        /**
         * 在target的左边外侧靠顶部对齐
         */
        LeftOutsideTop,
        /**
         * 在target的左边外侧上下居中
         */
        LeftOutsideCenter,
        /**
         * 在target的左边外侧靠底部对齐
         */
        LeftOutsideBottom,

        /**
         * 在target的右边外侧靠顶部对齐
         */
        RightOutsideTop,
        /**
         * 在target的右边外侧上下居中
         */
        RightOutsideCenter,
        /**
         * 在target的右边外侧靠底部对齐
         */
        RightOutsideBottom,
    }
}
