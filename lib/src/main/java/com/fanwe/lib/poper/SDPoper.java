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
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;

import java.lang.ref.WeakReference;

/**
 * 可以让PopView显示在Target的某个位置
 */
public class SDPoper
{
    private static final String TAG = "SDPoper";

    private ViewGroup mContainer;
    private final SDPoperParent mPoperParent;
    private View mPopView;
    private Position mPosition = Position.TopLeft;

    private int mMarginLeft;
    private int mMarginTop;

    private int mMarginX;
    private int mMarginY;

    private WeakReference<View> mTarget;

    private int[] mLocationTarget = {0, 0};
    private int[] mLocationParent = {0, 0};

    private boolean mIsDebug;

    public SDPoper(Activity activity)
    {
        if (activity == null)
        {
            throw new NullPointerException("activity is null");
        }

        mPoperParent = new SDPoperParent(activity);
        setContainer((ViewGroup) activity.findViewById(android.R.id.content));
    }

    public SDPoper setDebug(boolean debug)
    {
        mIsDebug = debug;
        return this;
    }

    /**
     * 设置popview可以显示的容器范围<br>
     * 默认是Activity中id为android.R.id.content的容器
     *
     * @param container
     * @return
     */
    public SDPoper setContainer(ViewGroup container)
    {
        if (container != null && mContainer != container)
        {
            mPoperParent.removeSelf();
            mContainer = container;
        }
        return this;
    }

    private Context getContext()
    {
        return mPoperParent.getContext();
    }

    /**
     * 返回popview
     *
     * @return
     */
    public View getPopView()
    {
        return mPopView;
    }

    /**
     * 设置要Pop的view
     *
     * @param layoutId 布局id
     * @return
     */
    public SDPoper setPopView(int layoutId)
    {
        View view = null;
        if (layoutId != 0)
        {
            view = LayoutInflater.from(getContext()).inflate(layoutId, mPoperParent, false);
        }
        return setPopView(view);
    }

    /**
     * 设置要Pop的view
     *
     * @param popView
     * @return
     */
    public SDPoper setPopView(View popView)
    {
        if (mPopView != popView)
        {
            mPopView = popView;
            if (popView == null)
            {
                attach(false);
            }
        }
        return this;
    }

    /**
     * 返回Target
     *
     * @return
     */
    public View getTarget()
    {
        if (mTarget != null)
        {
            return mTarget.get();
        } else
        {
            return null;
        }
    }

    /**
     * 设置目标view
     *
     * @param target
     */
    public SDPoper setTarget(View target)
    {
        final View oldTarget = getTarget();
        if (oldTarget != target)
        {
            removeTargetListener();

            if (target != null)
            {
                mTarget = new WeakReference<>(target);
            } else
            {
                mTarget = null;
            }
        }
        return this;
    }

    private void addTargetListener()
    {
        final View target = getTarget();
        if (target != null)
        {
            target.getViewTreeObserver().removeOnPreDrawListener(mOnPreDrawListenerTarget);
            target.getViewTreeObserver().addOnPreDrawListener(mOnPreDrawListenerTarget);

            target.removeOnAttachStateChangeListener(mOnAttachStateChangeListenerTarget);
            target.addOnAttachStateChangeListener(mOnAttachStateChangeListenerTarget);

            if (mIsDebug)
            {
                Log.i(TAG, "addTargetListener:" + target);
            }
        }
    }

    private void removeTargetListener()
    {
        final View target = getTarget();
        if (target != null)
        {
            target.getViewTreeObserver().removeOnPreDrawListener(mOnPreDrawListenerTarget);
            target.removeOnAttachStateChangeListener(mOnAttachStateChangeListenerTarget);

            if (mIsDebug)
            {
                Log.e(TAG, "removeTargetListener:" + target);
            }
        }
    }

    /**
     * 设置显示的位置
     *
     * @param position
     */
    public SDPoper setPosition(Position position)
    {
        if (position != null)
        {
            mPosition = position;
        }
        return this;
    }

    /**
     * 设置x轴方向的偏移量，大于0往右，小于0往左
     *
     * @param marginX
     */
    public SDPoper setMarginX(int marginX)
    {
        mMarginX = marginX;
        return this;
    }

    /**
     * 设置y轴方向的偏移量，大于0往下，小于0往上
     *
     * @param marginY
     */
    public SDPoper setMarginY(int marginY)
    {
        mMarginY = marginY;
        return this;
    }

    private View.OnAttachStateChangeListener mOnAttachStateChangeListenerTarget = new View.OnAttachStateChangeListener()
    {
        @Override
        public void onViewAttachedToWindow(View v)
        {

        }

        @Override
        public void onViewDetachedFromWindow(View v)
        {
            attach(false);
        }
    };

    private ViewTreeObserver.OnPreDrawListener mOnPreDrawListenerTarget = new ViewTreeObserver.OnPreDrawListener()
    {
        @Override
        public boolean onPreDraw()
        {
            if (isAttached())
            {
                updatePosition();
            }
            return true;
        }
    };

    /**
     * 保存target的信息
     */
    private void saveLocationInfo()
    {
        mPoperParent.getLocationInWindow(mLocationParent);
        getTarget().getLocationInWindow(mLocationTarget);
    }

    /**
     * 把PopView添加到Parent
     *
     * @param attach
     * @return
     */
    public SDPoper attach(boolean attach)
    {
        if (attach)
        {
            addTargetListener();
            updatePosition();
        } else
        {
            removeTargetListener();
            removePopViewFromParent();
        }
        return this;
    }

    /**
     * 当前PopView是否已经被添加到Parent
     *
     * @return
     */
    public boolean isAttached()
    {
        return mPopView != null
                && mPopView.getParent() == mPoperParent
                && mPoperParent.getParent() == mContainer;
    }

    private void removePopViewFromParent()
    {
        if (isAttached())
        {
            mPoperParent.removeView(mPopView);
        }
    }

    /**
     * 刷新popview的位置
     */
    private void updatePosition()
    {
        if (mPopView == null)
        {
            return;
        }
        final View target = getTarget();
        if (target == null || target.getParent() == null)
        {
            return;
        }
        final int targetVisibility = target.getVisibility();
        if (mPopView.getVisibility() != targetVisibility)
        {
            mPopView.setVisibility(targetVisibility);
        }
        if (View.VISIBLE != targetVisibility)
        {
            return;
        }

        addToParentIfNeed();

        saveLocationInfo();
        mMarginLeft = mLocationTarget[0] - mLocationParent[0] + mMarginX;
        mMarginTop = mLocationTarget[1] - mLocationParent[1] + mMarginY;

        switch (mPosition)
        {
            case TopLeft:
                alignTopLeft(target);
                break;
            case TopCenter:
                alignTopCenter(target);
                break;
            case TopRight:
                alignTopRight(target);
                break;

            case LeftCenter:
                alignLeftCenter(target);
                break;
            case Center:
                alignCenter(target);
                break;
            case RightCenter:
                alignRightCenter(target);
                break;

            case BottomLeft:
                alignBottomLeft(target);
                break;
            case BottomCenter:
                alignBottomCenter(target);
                break;
            case BottomRight:
                alignBottomRight(target);
                break;
            default:
                break;
        }
        updateParamsIfNeed();
    }

    //---------- position start----------

    private void alignTopLeft(View target)
    {
    }

    private void alignTopCenter(View target)
    {
        mMarginLeft += (target.getWidth() / 2 - getPopView().getWidth() / 2);
    }

    private void alignTopRight(View target)
    {
        mMarginLeft += (target.getWidth() - getPopView().getWidth());
    }

    private void alignLeftCenter(View target)
    {
        mMarginTop += (target.getHeight() / 2 - getPopView().getHeight() / 2);
    }

    private void alignCenter(View target)
    {
        alignTopCenter(target);
        alignLeftCenter(target);
    }

    private void alignRightCenter(View target)
    {
        alignTopRight(target);
        alignLeftCenter(target);
    }

    private void alignBottomLeft(View target)
    {
        mMarginTop += target.getHeight() - getPopView().getHeight();
    }

    private void alignBottomCenter(View target)
    {
        alignTopCenter(target);
        alignBottomLeft(target);
    }

    private void alignBottomRight(View target)
    {
        alignTopRight(target);
        alignBottomLeft(target);
    }

    //---------- position end----------

    private void addToParentIfNeed()
    {
        final ViewParent parent = mPopView.getParent();
        if (parent != mPoperParent)
        {
            if (parent != null)
            {
                ((ViewGroup) parent).removeView(mPopView);
            }

            final ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            final ViewGroup.LayoutParams params = mPopView.getLayoutParams();
            if (params != null)
            {
                p.width = params.width;
                p.height = params.height;
            }
            mPoperParent.addView(mPopView, p);
        }

        if (mPoperParent.getParent() != mContainer)
        {
            final ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);

            mContainer.addView(mPoperParent, params);
        }
    }

    private void updateParamsIfNeed()
    {
        boolean needUpdate = false;

        if (mPopView.getLeft() != mMarginLeft)
        {
            needUpdate = true;
        }
        if (mPopView.getTop() != mMarginTop)
        {
            needUpdate = true;
        }

        if (needUpdate)
        {
            int left = mMarginLeft;
            int top = mMarginTop;
            int right = left + mPopView.getMeasuredWidth();
            int bottom = top + mPopView.getMeasuredHeight();

            mPopView.layout(left, top, right, bottom);
        }
    }

    public enum Position
    {
        /**
         * 左上角对齐
         */
        TopLeft,
        /**
         * 顶部中间对齐
         */
        TopCenter,
        /**
         * 右上角对齐
         */
        TopRight,

        /**
         * 左边中间对齐
         */
        LeftCenter,
        /**
         * 中间对齐
         */
        Center,
        /**
         * 右边中间对齐
         */
        RightCenter,

        /**
         * 左下角对齐
         */
        BottomLeft,
        /**
         * 底部中间对齐
         */
        BottomCenter,
        /**
         * 右下角对齐
         */
        BottomRight,
    }
}
