package com.fanwe.lib.poper;

import android.app.Activity;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

/**
 * Created by zhengjun on 2018/1/12.
 */
class ActivityDrawListener
{
    private ViewTreeObserver mViewTreeObserver;
    private Callback mCallback;

    private boolean mIsRegister;

    public ActivityDrawListener(Activity activity)
    {
        final ViewGroup viewGroup = activity.findViewById(android.R.id.content);
        if (viewGroup == null)
        {
            throw new NullPointerException("android.R.id.content container not found in activity");
        }
        final ViewTreeObserver viewTreeObserver = viewGroup.getViewTreeObserver();
        if (viewTreeObserver == null)
        {
            throw new NullPointerException("android.R.id.content ViewTreeObserver is null");
        }

        mViewTreeObserver = viewTreeObserver;
    }

    public void setCallback(Callback callback)
    {
        mCallback = callback;
    }

    public boolean register()
    {
        if (!mIsRegister)
        {
            if (mViewTreeObserver.isAlive())
            {
                mViewTreeObserver.removeOnPreDrawListener(mOnPreDrawListener);
                mViewTreeObserver.addOnPreDrawListener(mOnPreDrawListener);
                mIsRegister = true;
                return true;
            }
        }
        return false;
    }

    public boolean unregister()
    {
        if (mIsRegister)
        {
            if (mViewTreeObserver.isAlive())
            {
                mViewTreeObserver.removeOnPreDrawListener(mOnPreDrawListener);
            }
            mIsRegister = false;
            return true;
        }
        return false;
    }

    private ViewTreeObserver.OnPreDrawListener mOnPreDrawListener = new ViewTreeObserver.OnPreDrawListener()
    {
        @Override
        public boolean onPreDraw()
        {
            if (mCallback != null)
            {
                mCallback.onActivityDraw();
            }
            return true;
        }
    };

    public interface Callback
    {
        void onActivityDraw();
    }
}
