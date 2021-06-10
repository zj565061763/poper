package com.sd.lib.poper.ext;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sd.lib.viewtracker.FViewTracker;
import com.sd.lib.viewtracker.ViewTracker;
import com.sd.lib.viewupdater.ViewUpdater;
import com.sd.lib.viewupdater.impl.OnLayoutChangeUpdater;

/**
 * 位置跟踪
 */
public class FPositionTracker {
    private final ViewTracker mTracker = new FViewTracker();

    private ViewUpdater mSourceUpdater;
    private ViewUpdater mTargetUpdater;

    private ViewUpdater getSourceUpdater() {
        if (mSourceUpdater == null) {
            mSourceUpdater = createSourceUpdater();
            mSourceUpdater.setUpdatable(mUpdatable);
        }
        return mSourceUpdater;
    }

    private ViewUpdater getTargetUpdater() {
        if (mTargetUpdater == null) {
            mTargetUpdater = createTargetUpdater();
            mTargetUpdater.setUpdatable(mUpdatable);
        }
        return mTargetUpdater;
    }

    /**
     * 设置回调对象
     */
    public void setCallback(@Nullable ViewTracker.Callback callback) {
        mTracker.setCallback(callback);
    }

    /**
     * 设置源View
     */
    public void setSource(@Nullable View view) {
        mTracker.setSource(view);
        getSourceUpdater().setView(view);
    }

    /**
     * 设置目标View
     */
    public void setTarget(@Nullable View view) {
        mTracker.setTarget(view);
        getTargetUpdater().setView(view);
    }

    /**
     * 设置追踪位置
     */
    public void setPosition(@NonNull ViewTracker.Position position) {
        mTracker.setPosition(position);
    }

    /**
     * 开始追踪
     */
    public void start() {
        getSourceUpdater().start();
        getTargetUpdater().start();
        mTracker.update();
    }

    /**
     * 停止追踪
     */
    public void stop() {
        getSourceUpdater().stop();
        getTargetUpdater().stop();
    }

    /**
     * 创建源View更新对象
     */
    @NonNull
    protected ViewUpdater createSourceUpdater() {
        return new OnLayoutChangeUpdater();
    }

    /**
     * 创建目标View更新对象
     */
    @NonNull
    protected ViewUpdater createTargetUpdater() {
        return new OnLayoutChangeUpdater();
    }

    private final ViewUpdater.Updatable mUpdatable = new ViewUpdater.Updatable() {
        @Override
        public void update() {
            mTracker.update();
        }
    };
}
