package com.fanwe.lib.poper;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
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
    public static final String TAG = ActivityDrawListener.class.getSimpleName();

    private static final Handler HANDLER = new Handler(Looper.getMainLooper());
    private static final int NOTIFY_DURATION = 10;
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


    private long mLastTime;

    @Override
    public boolean onPreDraw()
    {
        HANDLER.removeCallbacks(mNotifyCallbackRunnable);

        final long differ = System.currentTimeMillis() - mLastTime;
        if (differ >= NOTIFY_DURATION)
        {
            notifyCallback();
        } else
        {
            final long delay = NOTIFY_DURATION - differ;
            Log.e(TAG, "too fast:" + differ + " " + delay);
            HANDLER.postDelayed(mNotifyCallbackRunnable, delay);
        }
        return true;
    }

    private Runnable mNotifyCallbackRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            notifyCallback();
        }
    };

    private void notifyCallback()
    {
        mLastTime = System.currentTimeMillis();

        if (mListCallback.isEmpty())
        {
            return;
        }
        Iterator<Callback> it = mListCallback.iterator();
        while (it.hasNext())
        {
            it.next().onActivityDraw();
        }
    }

    /**
     * 添加回调对象
     *
     * @param callback
     * @return true-本次操作执行了添加，false-已经添加或者添加失败
     */
    public boolean addCallback(Callback callback)
    {
        if (callback == null || mListCallback.contains(callback))
        {
            return false;
        }
        mListCallback.add(callback);
        return true;
    }

    /**
     * 移除回调对象
     *
     * @param callback
     * @return true-对象被移除
     */
    public boolean removeCallback(Callback callback)
    {
        return mListCallback.remove(callback);
    }

    public interface Callback
    {
        void onActivityDraw();
    }
}
