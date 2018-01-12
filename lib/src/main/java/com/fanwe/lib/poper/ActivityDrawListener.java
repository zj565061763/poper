package com.fanwe.lib.poper;

import android.app.Activity;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by zhengjun on 2018/1/12.
 */
public class ActivityDrawListener implements ViewTreeObserver.OnPreDrawListener
{
    private static final Map<Activity, ActivityDrawListener> MAP_ACTIVITY_LISTENER = new WeakHashMap<>();

    private List<Callback> mListCallback = new ArrayList<>();

    private ActivityDrawListener(Activity activity)
    {
        FrameLayout frameLayout = activity.findViewById(android.R.id.content);
        if (frameLayout == null)
        {
            throw new NullPointerException("android.R.id.content container not found in activity");
        }
        frameLayout.getViewTreeObserver().addOnPreDrawListener(this);
    }

    public static ActivityDrawListener get(Activity activity)
    {
        if (activity == null)
        {
            return null;
        }
        ActivityDrawListener instance = MAP_ACTIVITY_LISTENER.get(activity);
        if (instance == null)
        {
            instance = new ActivityDrawListener(activity);
            MAP_ACTIVITY_LISTENER.put(activity, instance);
        }
        return instance;
    }

    @Override
    public boolean onPreDraw()
    {
        if (mListCallback.isEmpty())
        {
            return true;
        }

        Iterator<Callback> it = mListCallback.iterator();
        while (it.hasNext())
        {
            it.next().onActivityDraw();
        }

        return true;
    }

    public void addCallback(Callback callback)
    {
        if (callback == null || mListCallback.contains(callback))
        {
            return;
        }
        mListCallback.add(callback);
    }

    public void removeCallback(Callback callback)
    {
        mListCallback.remove(callback);
    }

    public interface Callback
    {
        void onActivityDraw();
    }
}
