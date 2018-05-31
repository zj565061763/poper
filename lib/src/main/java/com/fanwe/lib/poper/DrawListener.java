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

import android.view.View;
import android.view.ViewTreeObserver;

import java.lang.ref.WeakReference;

/**
 * Created by zhengjun on 2018/1/12.
 */
abstract class DrawListener
{
    private WeakReference<View> mView;
    private boolean mIsRegister;

    public final void setView(View view)
    {
        final View old = getView();
        if (old != view)
        {
            final boolean hasRegister = mIsRegister;

            if (old != null)
                unregister();

            mView = view == null ? null : new WeakReference<>(view);

            if (view != null && hasRegister)
            {
                register();
                if (!mIsRegister)
                    throw new RuntimeException("register failure when view changed");
            }
        }
    }

    private View getView()
    {
        final View view = mView == null ? null : mView.get();

        if (view == null)
            setRegister(false);

        return view;
    }

    private void setRegister(boolean register)
    {
        if (mIsRegister != register)
        {
            mIsRegister = register;
            onRegisterChanged(register);
        }
    }

    public final boolean register()
    {
        final View view = getView();
        if (view == null)
            return false;

        if (mIsRegister)
            return false;

        final ViewTreeObserver observer = view.getViewTreeObserver();
        if (!observer.isAlive())
            return false;

        observer.removeOnPreDrawListener(mListener);
        observer.addOnPreDrawListener(mListener);
        setRegister(true);

        return true;
    }

    public final boolean unregister()
    {
        final View view = getView();
        if (view == null)
            return false;

        if (!mIsRegister)
            return false;

        final ViewTreeObserver observer = view.getViewTreeObserver();
        if (observer.isAlive())
            observer.removeOnPreDrawListener(mListener);
        setRegister(false);

        return true;
    }

    private final ViewTreeObserver.OnPreDrawListener mListener = new ViewTreeObserver.OnPreDrawListener()
    {
        @Override
        public boolean onPreDraw()
        {
            onDraw();
            return true;
        }
    };

    protected abstract void onDraw();

    protected void onRegisterChanged(boolean isRegister)
    {
    }
}
