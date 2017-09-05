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
package com.fanwe.library.poper;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import java.lang.ref.WeakReference;

/**
 * 可以让PopView显示在Target的某个位置
 */
public class SDPoper
{
    private SDPoperParent mPoperParent;
    private View mPopView;
    private Position mPosition;

    private int mMarginLeft;
    private int mMarginTop;

    private int mMarginX;
    private int mMarginY;

    private WeakReference<View> mTarget;

    private int[] mLocationTarget = {0, 0};
    private int[] mLocationRoot = {0, 0};

    public SDPoper(Activity activity)
    {
        if (activity == null)
        {
            throw new NullPointerException("activity is null");
        }
        FrameLayout frameLayout = (FrameLayout) activity.findViewById(android.R.id.content);
        mPoperParent = (SDPoperParent) frameLayout.findViewById(R.id.lib_poper_parent);
        if (mPoperParent == null)
        {
            mPoperParent = new SDPoperParent(activity.getApplicationContext());

            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            frameLayout.addView(mPoperParent, params);
        }
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
     * @param popView
     * @return
     */
    public SDPoper setPopView(View popView)
    {
        if (mPopView != popView)
        {
            mPopView = popView;
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
            if (oldTarget != null)
            {
                oldTarget.getViewTreeObserver().removeOnPreDrawListener(mOnPreDrawListenerTarget);
            }

            if (target != null)
            {
                mTarget = new WeakReference<>(target);

                target.getViewTreeObserver().addOnPreDrawListener(mOnPreDrawListenerTarget);
            } else
            {
                mTarget = null;
            }
        }
        return this;
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
        mPoperParent.getLocationOnScreen(mLocationRoot);
        getTarget().getLocationOnScreen(mLocationTarget);
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
            if (getTarget() == null)
            {
                throw new IllegalArgumentException("you must invoke setTarget(view) set a target before this");
            }
            updatePosition();
        } else
        {
            removePopViewFromRoot();
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
                && mPopView.getParent() != null
                && mPopView.getParent() == mPoperParent;
    }

    private void removePopViewFromRoot()
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
        final View target = getTarget();
        if (mPopView == null || mPosition == null || target == null)
        {
            return;
        }

        final int visibility = getTarget().getVisibility();
        if (mPopView.getVisibility() != visibility)
        {
            mPopView.setVisibility(visibility);
        }
        if (visibility != View.VISIBLE)
        {
            return;
        }

        saveLocationInfo();
        addToParent();

        mMarginLeft = mLocationTarget[0] - mLocationRoot[0] + mMarginX;
        mMarginTop = mLocationTarget[1] - mLocationRoot[1] + mMarginY;

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

    private void addToParent()
    {
        final ViewParent parent = mPopView.getParent();
        if (parent != mPoperParent)
        {
            if (parent != null)
            {
                ((ViewGroup) parent).removeView(mPopView);
            }

            ViewGroup.LayoutParams params = mPopView.getLayoutParams();
            ViewGroup.LayoutParams p = null;
            if (params != null)
            {
                p = new ViewGroup.LayoutParams(params.width, params.height);
            } else
            {
                p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            mPoperParent.addView(mPopView, p);
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
            mPopView.layout(mMarginLeft,
                    mMarginTop,
                    mMarginLeft + mPopView.getMeasuredWidth(),
                    mMarginTop + mPopView.getMeasuredHeight());
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
