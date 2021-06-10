package com.sd.lib.poper;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sd.lib.poper.layouter.DefaultLayouter;
import com.sd.lib.viewtracker.FViewTracker;
import com.sd.lib.viewtracker.ViewTracker;
import com.sd.lib.viewupdater.ViewUpdater;
import com.sd.lib.viewupdater.impl.OnLayoutChangeUpdater;
import com.sd.lib.viewupdater.impl.OnPreDrawUpdater;

/**
 * 可以让PopView显示在Target的某个位置
 */
public class FPoper implements Poper {
    private final Context mContext;

    private ViewGroup mContainer;
    private final ViewGroup mPoperParent;
    private View mPopView;

    private ViewTracker mTracker;
    private ViewUpdater mTargetUpdater;
    private ViewUpdater mPopUpdater;

    private Margin mMarginX;
    private Margin mMarginY;

    private Layouter mLayouter;

    public FPoper(@NonNull Context context) {
        mContext = context;
        mPoperParent = new SimplePoperParent(context);
    }

    @NonNull
    private ViewGroup getContainer() {
        if (mContainer == null) {
            if (mContext instanceof Activity) {
                final Activity activity = (Activity) mContext;
                mContainer = activity.findViewById(Window.ID_ANDROID_CONTENT);
            }
        }

        if (mContainer == null) {
            throw new RuntimeException("container is null");
        }
        return mContainer;
    }

    @NonNull
    private Layouter getLayouter() {
        if (mLayouter == null) {
            mLayouter = new DefaultLayouter();
        }
        return mLayouter;
    }

    @NonNull
    private ViewTracker getTracker() {
        if (mTracker == null) {
            mTracker = new FViewTracker();
            mTracker.setCallback(new ViewTracker.Callback() {
                @Override
                public void onSourceChanged(@Nullable View oldSource, @Nullable View newSource) {
                    super.onSourceChanged(oldSource, newSource);
                    mPopView = newSource;
                    if (newSource == null) {
                        removeUpdateListener();
                    }
                }

                @Override
                public void onTargetChanged(@Nullable View oldTarget, @Nullable View newTarget) {
                    super.onTargetChanged(oldTarget, newTarget);
                    if (newTarget == null) {
                        removeUpdateListener();
                    }
                }

                @Override
                public boolean canUpdate(@NonNull View source, @NonNull View target) {
                    final boolean isTargetShown = isViewAttached(target) && target.isShown();

                    // 同步可见状态到PoperParent
                    final PoperParent parent = (PoperParent) mPoperParent;
                    parent.synchronizeVisibilityWithTarget(isTargetShown);

                    if (!isTargetShown) {
                        return false;
                    }

                    parent.attachToContainer(getContainer());
                    parent.addPopView(mPopView);
                    return true;
                }

                @Override
                public void onUpdate(int x, int y, @NonNull View source, @NonNull View target) {
                    final int marginX = mMarginX == null ? 0 : mMarginX.getMargin();
                    final int marginY = mMarginY == null ? 0 : mMarginY.getMargin();

                    x += marginX;
                    y += marginY;

                    getLayouter().layout(x, y, source, target);
                }
            });
        }
        return mTracker;
    }

    @NonNull
    private ViewUpdater getTargetUpdater() {
        if (mTargetUpdater == null) {
            mTargetUpdater = new OnPreDrawUpdater();
            mTargetUpdater.setUpdatable(new ViewUpdater.Updatable() {
                @Override
                public void update() {
                    getTracker().update();
                }
            });
        }
        return mTargetUpdater;
    }

    @NonNull
    private ViewUpdater getPopUpdater() {
        if (mPopUpdater == null) {
            mPopUpdater = new OnLayoutChangeUpdater();
            mPopUpdater.setUpdatable(new ViewUpdater.Updatable() {
                @Override
                public void update() {
                    getTracker().update();
                }
            });
        }
        return mPopUpdater;
    }

    @NonNull
    @Override
    public Poper setPopView(int layoutId) {
        final View view = (layoutId == 0) ?
                null :
                LayoutInflater.from(mContext).inflate(layoutId, mPoperParent, false);
        setPopView(view);
        return this;
    }

    @NonNull
    @Override
    public Poper setPopView(@Nullable View popView) {
        getTracker().setSource(popView);
        getPopUpdater().setView(popView);
        return this;
    }

    @NonNull
    @Override
    public Poper setTarget(@Nullable View target) {
        getTracker().setTarget(target);
        getTargetUpdater().setView(target);
        return this;
    }

    @NonNull
    @Override
    public Poper setPosition(@NonNull Position position) {
        switch (position) {
            case TopLeft:
                getTracker().setPosition(ViewTracker.Position.TopLeft);
                break;
            case TopCenter:
                getTracker().setPosition(ViewTracker.Position.TopCenter);
                break;
            case TopRight:
                getTracker().setPosition(ViewTracker.Position.TopRight);
                break;

            case LeftCenter:
                getTracker().setPosition(ViewTracker.Position.LeftCenter);
                break;
            case Center:
                getTracker().setPosition(ViewTracker.Position.Center);
                break;
            case RightCenter:
                getTracker().setPosition(ViewTracker.Position.RightCenter);
                break;

            case BottomLeft:
                getTracker().setPosition(ViewTracker.Position.BottomLeft);
                break;
            case BottomCenter:
                getTracker().setPosition(ViewTracker.Position.BottomCenter);
                break;
            case BottomRight:
                getTracker().setPosition(ViewTracker.Position.BottomRight);
                break;

            case Left:
                getTracker().setPosition(ViewTracker.Position.Left);
                break;
            case Top:
                getTracker().setPosition(ViewTracker.Position.Top);
                break;
            case Right:
                getTracker().setPosition(ViewTracker.Position.Right);
                break;
            case Bottom:
                getTracker().setPosition(ViewTracker.Position.Bottom);
                break;
        }
        return this;
    }

    @NonNull
    @Override
    public Poper setMarginX(final int margin) {
        setMarginX(new Margin() {
            @Override
            public int getMargin() {
                return margin;
            }
        });
        return this;
    }

    @NonNull
    @Override
    public Poper setMarginY(final int margin) {
        setMarginY(new Margin() {
            @Override
            public int getMargin() {
                return margin;
            }
        });
        return this;
    }

    @NonNull
    @Override
    public Poper setMarginX(@Nullable Margin margin) {
        mMarginX = margin;
        return this;
    }

    @NonNull
    @Override
    public Poper setMarginY(@Nullable Margin margin) {
        mMarginY = margin;
        return this;
    }

    @NonNull
    @Override
    public Poper setContainer(@Nullable ViewGroup container) {
        mContainer = container;
        return this;
    }

    @NonNull
    @Override
    public Poper setLayouter(@Nullable Layouter layouter) {
        mLayouter = layouter;
        return this;
    }

    @Nullable
    @Override
    public View getPopView() {
        return mPopView;
    }

    @Nullable
    @Override
    public View getTarget() {
        return getTracker().getTarget();
    }

    @Override
    public boolean isAttached() {
        if (mPopView == null || mContainer == null) {
            return false;
        }

        return mPopView.getParent() == mPoperParent &&
                mPoperParent.getParent() == mContainer &&
                isViewAttached(mContainer);
    }

    @NonNull
    @Override
    public Poper attach(boolean attach) {
        if (attach) {
            if (getTarget() != null) {
                addUpdateListener();
                getTracker().update();
            }
        } else {
            removeUpdateListener();
            ((PoperParent) mPoperParent).removeSelf();
        }
        return this;
    }

    private void addUpdateListener() {
        getTargetUpdater().start();
        getPopUpdater().start();
    }

    private void removeUpdateListener() {
        getTargetUpdater().stop();
        getPopUpdater().stop();
    }

    private static boolean isViewAttached(@Nullable View view) {
        if (view == null) {
            return false;
        }

        if (Build.VERSION.SDK_INT >= 19) {
            return view.isAttachedToWindow();
        } else {
            return view.getWindowToken() != null;
        }
    }
}