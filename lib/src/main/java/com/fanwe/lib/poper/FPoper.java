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
public class FPoper
{
    private static final String TAG = "FPoper";

    private final ViewGroup mActivityContent;

    private ViewGroup mContainer;
    private final FPoperParent mPoperParent;
    private View mPopView;
    private Position mPosition = Position.TopLeft;

    private int mMarginLeft;
    private int mMarginTop;

    private int mMarginX;
    private int mMarginY;

    private WeakReference<View> mTarget;

    private int[] mLocationTarget = {0, 0};
    private int[] mLocationParent = {0, 0};

    private boolean mHasAddUpdateListener;

    private boolean mIsDebug;

    public FPoper(Activity activity)
    {
        if (activity == null)
        {
            throw new NullPointerException("activity is null");
        }

        mPoperParent = new FPoperParent(activity);
        mActivityContent = activity.findViewById(android.R.id.content);
    }

    public FPoper setDebug(boolean debug)
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
    public FPoper setContainer(ViewGroup container)
    {
        ViewGroup old = getContainer();
        if (old != container)
        {
            mContainer = container;
        }
        return this;
    }

    /**
     * 返回popview显示的容器
     *
     * @return
     */
    private ViewGroup getContainer()
    {
        if (mContainer == null)
        {
            mContainer = mActivityContent;
        }
        return mContainer;
    }

    private Context getContext()
    {
        return mActivityContent.getContext();
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
    public FPoper setPopView(int layoutId)
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
    public FPoper setPopView(View popView)
    {
        final View old = getPopView();
        if (old != popView)
        {
            if (old != null)
            {
                old.removeOnLayoutChangeListener(mOnLayoutChangeListenerPopView);
                attach(false);
            }

            mPopView = popView;

            if (popView != null)
            {
                popView.removeOnLayoutChangeListener(mOnLayoutChangeListenerPopView);
                popView.addOnLayoutChangeListener(mOnLayoutChangeListenerPopView);
            }
        }
        return this;
    }

    private View.OnLayoutChangeListener mOnLayoutChangeListenerPopView = new View.OnLayoutChangeListener()
    {
        @Override
        public void onLayoutChange(View v,
                                   int left, int top, int right, int bottom,
                                   int oldLeft, int oldTop, int oldRight, int oldBottom)
        {
            dynamicUpdatePosition();
        }
    };

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
    public FPoper setTarget(View target)
    {
        final View old = getTarget();
        if (old != target)
        {
            if (old != null)
            {
                old.removeOnAttachStateChangeListener(mOnAttachStateChangeListenerTarget);
            }

            if (target != null)
            {
                mTarget = new WeakReference<>(target);
                target.removeOnAttachStateChangeListener(mOnAttachStateChangeListenerTarget);
                target.addOnAttachStateChangeListener(mOnAttachStateChangeListenerTarget);
            } else
            {
                mTarget = null;
                removeUpdateListener();
            }
        }
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
            removeUpdateListener();
        }
    };

    private void addUpdateListener()
    {
        if (mHasAddUpdateListener)
        {
            return;
        }

        mActivityContent.getViewTreeObserver().removeOnPreDrawListener(mOnPreDrawListener);
        mActivityContent.getViewTreeObserver().addOnPreDrawListener(mOnPreDrawListener);
        mHasAddUpdateListener = true;
        if (mIsDebug)
        {
            Log.i(TAG, "addUpdateListener:" + getTarget());
        }
    }

    private void removeUpdateListener()
    {
        if (!mHasAddUpdateListener)
        {
            return;
        }

        mActivityContent.getViewTreeObserver().removeOnPreDrawListener(mOnPreDrawListener);
        mHasAddUpdateListener = false;
        if (mIsDebug)
        {
            Log.e(TAG, "removeUpdateListener:" + getTarget());
        }
    }

    private ViewTreeObserver.OnPreDrawListener mOnPreDrawListener = new ViewTreeObserver.OnPreDrawListener()
    {
        @Override
        public boolean onPreDraw()
        {
            dynamicUpdatePosition();
            return true;
        }
    };

    /**
     * 设置显示的位置
     *
     * @param position
     */
    public FPoper setPosition(Position position)
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
    public FPoper setMarginX(int marginX)
    {
        mMarginX = marginX;
        return this;
    }

    /**
     * 设置y轴方向的偏移量，大于0往下，小于0往上
     *
     * @param marginY
     */
    public FPoper setMarginY(int marginY)
    {
        mMarginY = marginY;
        return this;
    }

    /**
     * 把PopView添加到Parent
     *
     * @param attach
     * @return
     */
    public FPoper attach(boolean attach)
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

    /**
     * 当前PopView是否已经被添加到Parent
     *
     * @return
     */
    public boolean isAttached()
    {
        return getPopView() != null
                && getPopView().getParent() == mPoperParent
                && mPoperParent.getParent() == getContainer()
                && isViewAttached(getContainer());
    }

    private void removePopView()
    {
        mPoperParent.removeView(getPopView());
    }

    private void dynamicUpdatePosition()
    {
        final View target = getTarget();
        if (target == null)
        {
            removeUpdateListener();
            return;
        }
        if (!isViewAttached(target))
        {
            return;
        }
        if (!isAttached())
        {
            return;
        }
        updatePosition();
    }

    /**
     * 刷新popview的位置
     */
    private void updatePosition()
    {
        if (getPopView() == null)
        {
            return;
        }

        addToParentIfNeed();

        final View target = getTarget();
        final boolean isShown = target.isShown();

        synchronizeVisibilityIfNeed(isShown);
        if (!isShown)
        {
            return;
        }

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

            case TopLeftOutside:
                layoutTopLeftOutside(target);
                break;
            case TopCenterOutside:
                layoutTopCenterOutside(target);
                break;
            case TopRightOutside:
                layoutTopRightOutside(target);
                break;

            case BottomLeftOutside:
                layoutBottomLeftOutside(target);
                break;
            case BottomCenterOutside:
                layoutBottomCenterOutside(target);
                break;
            case BottomRightOutside:
                layoutBottomRightOutside(target);
                break;

            case LeftTopOutside:
                layoutLeftTopOutside(target);
                break;
            case LeftCenterOutside:
                layoutLeftCenterOutside(target);
                break;
            case LeftBottomOutside:
                layoutLeftBottomOutside(target);
                break;

            case RightTopOutside:
                layoutRightTopOutside(target);
                break;
            case RightCenterOutside:
                layoutRightCenterOutside(target);
                break;
            case RightBottomOutside:
                layoutRightBottomOutside(target);
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
        if (isShown)
        {
            mPoperParent.setVisibility(View.VISIBLE);
        } else
        {
            mPoperParent.setVisibility(View.GONE);
        }
    }

    //---------- position start----------

    private void layoutTopLeft(View target)
    {
    }

    private void layoutTopCenter(View target)
    {
        mMarginLeft += (getViewWidth(target) / 2 - getViewWidth(getPopView()) / 2);
    }

    private void layoutTopRight(View target)
    {
        mMarginLeft += (getViewWidth(target) - getViewWidth(getPopView()));
    }

    private void layoutLeftCenter(View target)
    {
        mMarginTop += (getViewHeight(target) / 2 - getViewHeight(getPopView()) / 2);
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
        mMarginTop += getViewHeight(target) - getViewHeight(getPopView());
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

    private void layoutTopLeftOutside(View target)
    {
        layoutTopLeft(target);
        mMarginTop -= getViewHeight(getPopView());
    }

    private void layoutTopCenterOutside(View target)
    {
        layoutTopCenter(target);
        mMarginTop -= getViewHeight(getPopView());
    }

    private void layoutTopRightOutside(View target)
    {
        layoutTopRight(target);
        mMarginTop -= getViewHeight(getPopView());
    }

    private void layoutBottomLeftOutside(View target)
    {
        layoutBottomLeft(target);
        mMarginTop += getViewHeight(getPopView());
    }

    private void layoutBottomCenterOutside(View target)
    {
        layoutBottomCenter(target);
        mMarginTop += getViewHeight(getPopView());
    }

    private void layoutBottomRightOutside(View target)
    {
        layoutBottomRight(target);
        mMarginTop += getViewHeight(getPopView());
    }

    private void layoutLeftTopOutside(View target)
    {
        layoutTopLeft(target);
        mMarginLeft -= getViewWidth(getPopView());
    }

    private void layoutLeftCenterOutside(View target)
    {
        layoutLeftCenter(target);
        mMarginLeft -= getViewWidth(getPopView());
    }

    private void layoutLeftBottomOutside(View target)
    {
        layoutBottomLeft(target);
        mMarginLeft -= getViewWidth(getPopView());
    }

    private void layoutRightTopOutside(View target)
    {
        layoutTopRight(target);
        mMarginLeft += getViewWidth(getPopView());
    }

    private void layoutRightCenterOutside(View target)
    {
        layoutRightCenter(target);
        mMarginLeft += getViewWidth(getPopView());
    }

    private void layoutRightBottomOutside(View target)
    {
        layoutBottomRight(target);
        mMarginLeft += getViewWidth(getPopView());
    }

    //---------- position end----------

    private void addToParentIfNeed()
    {
        if (mPoperParent.getParent() != getContainer())
        {
            mPoperParent.removeSelf();

            final ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);

            getContainer().addView(mPoperParent, params);
        }

        final ViewParent parent = getPopView().getParent();
        if (parent != mPoperParent)
        {
            if (parent instanceof ViewGroup)
            {
                ((ViewGroup) parent).removeView(getPopView());
            }

            final ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            final ViewGroup.LayoutParams params = getPopView().getLayoutParams();
            if (params != null)
            {
                p.width = params.width;
                p.height = params.height;
            }
            mPoperParent.addView(getPopView(), p);
        }
    }

    private void layoutIfNeed()
    {
        boolean needUpdate = false;

        if (getPopView().getLeft() != mMarginLeft)
        {
            needUpdate = true;
        }
        if (getPopView().getTop() != mMarginTop)
        {
            needUpdate = true;
        }

        if (needUpdate)
        {
            final int left = mMarginLeft;
            final int top = mMarginTop;
            final int right = left + getViewWidth(getPopView());
            final int bottom = top + getViewHeight(getPopView());

            getPopView().layout(left, top, right, bottom);

            if (mIsDebug)
            {
                Log.i(TAG, "left:" + left + " top:" + top);
            }
        }
    }

    private static boolean isViewAttached(View view)
    {
        if (view == null)
        {
            return false;
        }
        ViewParent parent = view.getRootView().getParent();
        if (parent == null)
        {
            return false;
        }
        if (!(parent instanceof View))
        {
            return true;
        }
        return isViewAttached((View) parent);
    }

    private static int getViewWidth(View view)
    {
        int value = view.getWidth();
        return value;
    }

    private static int getViewHeight(View view)
    {
        int value = view.getHeight();
        return value;
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
        TopLeftOutside,
        /**
         * 在target的顶部外侧左右居中
         */
        TopCenterOutside,
        /**
         * 在target的顶部外侧靠右对齐
         */
        TopRightOutside,

        /**
         * 在target的底部外侧靠左对齐
         */
        BottomLeftOutside,
        /**
         * 在target的底部外侧左右居中
         */
        BottomCenterOutside,
        /**
         * 在target的底部外侧靠右对齐
         */
        BottomRightOutside,

        /**
         * 在target的左边外侧靠顶部对齐
         */
        LeftTopOutside,
        /**
         * 在target的左边外侧上下居中
         */
        LeftCenterOutside,
        /**
         * 在target的左边外侧靠底部对齐
         */
        LeftBottomOutside,

        /**
         * 在target的右边外侧靠顶部对齐
         */
        RightTopOutside,
        /**
         * 在target的右边外侧上下居中
         */
        RightCenterOutside,
        /**
         * 在target的右边外侧靠底部对齐
         */
        RightBottomOutside,
    }
}
