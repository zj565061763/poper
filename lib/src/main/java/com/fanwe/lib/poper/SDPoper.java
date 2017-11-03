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

    private final ViewGroup mActivityContent;
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

    private boolean mTrackTargetVisibility = true;

    private boolean mIsDebug;

    public SDPoper(Activity activity)
    {
        if (activity == null)
        {
            throw new NullPointerException("activity is null");
        }

        mPoperParent = new SDPoperParent(activity);
        mActivityContent = (ViewGroup) activity.findViewById(android.R.id.content);

        setContainer(mActivityContent);
    }

    public SDPoper setDebug(boolean debug)
    {
        mIsDebug = debug;
        return this;
    }

    /**
     * 设置是否跟随target的可见状态，默认true-跟随
     *
     * @param trackTargetVisibility
     * @return
     */
    public SDPoper setTrackTargetVisibility(boolean trackTargetVisibility)
    {
        mTrackTargetVisibility = trackTargetVisibility;
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
        final View oldView = getPopView();
        if (oldView != popView)
        {
            if (oldView != null)
            {
                attach(false);
            }
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
            mActivityContent.getViewTreeObserver().removeOnPreDrawListener(mOnPreDrawListenerTarget);
            mActivityContent.getViewTreeObserver().addOnPreDrawListener(mOnPreDrawListenerTarget);

            if (mIsDebug)
            {
                Log.i(TAG, "addTargetListener:" + getTarget());
            }
        }
    }

    private void removeTargetListener()
    {
        mActivityContent.getViewTreeObserver().removeOnPreDrawListener(mOnPreDrawListenerTarget);
        if (mIsDebug)
        {
            Log.e(TAG, "removeTargetListener:" + getTarget());
        }
    }

    private ViewTreeObserver.OnPreDrawListener mOnPreDrawListenerTarget = new ViewTreeObserver.OnPreDrawListener()
    {
        @Override
        public boolean onPreDraw()
        {
            if (getTarget() != null && isAttached())
            {
                updatePosition();
            } else
            {
                removeTargetListener();
            }
            return true;
        }
    };

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
        return getPopView() != null
                && getPopView().getParent() == mPoperParent
                && mPoperParent.getParent() == mContainer;
    }

    private void removePopViewFromParent()
    {
        mPoperParent.removeView(getPopView());
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
        final View target = getTarget();
        if (target == null || target.getParent() == null)
        {
            return;
        }

        addToParentIfNeed();

        if (mTrackTargetVisibility)
        {
            final int targetVisibility = target.getVisibility();
            if (mPoperParent.getVisibility() != targetVisibility)
            {
                mPoperParent.setVisibility(targetVisibility);
            }
            if (View.VISIBLE != targetVisibility)
            {
                return;
            }
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

    //---------- position start----------

    private void layoutTopLeft(View target)
    {
    }

    private void layoutTopCenter(View target)
    {
        mMarginLeft += (target.getWidth() / 2 - getPopView().getWidth() / 2);
    }

    private void layoutTopRight(View target)
    {
        mMarginLeft += (target.getWidth() - getPopView().getWidth());
    }

    private void layoutLeftCenter(View target)
    {
        mMarginTop += (target.getHeight() / 2 - getPopView().getHeight() / 2);
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
        mMarginTop += target.getHeight() - getPopView().getHeight();
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
        mMarginTop -= getPopView().getHeight();
    }

    private void layoutTopCenterOutside(View target)
    {
        layoutTopCenter(target);
        mMarginTop -= getPopView().getHeight();
    }

    private void layoutTopRightOutside(View target)
    {
        layoutTopRight(target);
        mMarginTop -= getPopView().getHeight();
    }

    private void layoutBottomLeftOutside(View target)
    {
        layoutBottomLeft(target);
        mMarginTop += getPopView().getHeight();
    }

    private void layoutBottomCenterOutside(View target)
    {
        layoutBottomCenter(target);
        mMarginTop += getPopView().getHeight();
    }

    private void layoutBottomRightOutside(View target)
    {
        layoutBottomRight(target);
        mMarginTop += getPopView().getHeight();
    }

    private void layoutLeftTopOutside(View target)
    {
        layoutTopLeft(target);
        mMarginLeft -= getPopView().getWidth();
    }

    private void layoutLeftCenterOutside(View target)
    {
        layoutLeftCenter(target);
        mMarginLeft -= getPopView().getWidth();
    }

    private void layoutLeftBottomOutside(View target)
    {
        layoutBottomLeft(target);
        mMarginLeft -= getPopView().getWidth();
    }

    private void layoutRightTopOutside(View target)
    {
        layoutTopRight(target);
        mMarginLeft += getPopView().getWidth();
    }

    private void layoutRightCenterOutside(View target)
    {
        layoutRightCenter(target);
        mMarginLeft += getPopView().getWidth();
    }

    private void layoutRightBottomOutside(View target)
    {
        layoutBottomRight(target);
        mMarginLeft += getPopView().getWidth();
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
            getPopView().layout(mMarginLeft, mMarginTop,
                    mMarginLeft + getPopView().getWidth(), mMarginTop + getPopView().getHeight());
        }
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
