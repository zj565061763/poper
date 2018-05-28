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
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

/**
 * Created by zhengjun on 2018/1/12.
 */
final class ActivityDrawListener
{
    private ViewGroup mViewGroup;
    private Callback mCallback;

    private boolean mIsRegister;

    public ActivityDrawListener(Activity activity)
    {
        final ViewGroup viewGroup = activity.findViewById(android.R.id.content);
        if (viewGroup == null)
            throw new NullPointerException("android.R.id.content container not found in activity");

        mViewGroup = viewGroup;
    }

    public void setCallback(Callback callback)
    {
        mCallback = callback;
    }

    public boolean register()
    {
        if (!mIsRegister)
        {
            final ViewTreeObserver viewTreeObserver = mViewGroup.getViewTreeObserver();
            if (viewTreeObserver.isAlive())
            {
                viewTreeObserver.removeOnPreDrawListener(mOnPreDrawListener);
                viewTreeObserver.addOnPreDrawListener(mOnPreDrawListener);
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
            final ViewTreeObserver viewTreeObserver = mViewGroup.getViewTreeObserver();
            if (viewTreeObserver.isAlive())
                viewTreeObserver.removeOnPreDrawListener(mOnPreDrawListener);

            mIsRegister = false;
            return true;
        }
        return false;
    }

    private final ViewTreeObserver.OnPreDrawListener mOnPreDrawListener = new ViewTreeObserver.OnPreDrawListener()
    {
        @Override
        public boolean onPreDraw()
        {
            if (mCallback != null)
                mCallback.onActivityDraw();
            return true;
        }
    };

    public interface Callback
    {
        void onActivityDraw();
    }
}
