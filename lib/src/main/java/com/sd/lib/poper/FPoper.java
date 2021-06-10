package com.sd.lib.poper;

import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private final Activity mActivity;

    private ViewGroup mContainer;
    private final ViewGroup mPoperParent;
    private View mPopView;

    private ViewTracker mTracker;
    private ViewUpdater mTargetUpdater;
    private ViewUpdater mPopUpdater;

    private Margin mMarginX;
    private Margin mMarginY;

    private Layouter mLayouter;

    public FPoper(Activity activity) {
        if (activity == null) {
            throw new NullPointerException("activity is null");
        }

        mActivity = activity;
        mPoperParent = new SimplePoperParent(activity);
    }

    private ViewGroup getContainer() {
        if (mContainer == null) {
            mContainer = mActivity.findViewById(android.R.id.content);
        }
        return mContainer;
    }

    private ViewTracker getTracker() {
        if (mTracker == null) {
            mTracker = new FViewTracker();
            mTracker.setCallback(new ViewTracker.Callback() {
                @Override
                public void onSourceChanged(View oldSource, View newSource) {
                    super.onSourceChanged(oldSource, newSource);
                    mPopView = newSource;
                    if (newSource == null) {
                        removeUpdateListener();
                    }
                }

                @Override
                public void onTargetChanged(View oldTarget, View newTarget) {
                    super.onTargetChanged(oldTarget, newTarget);
                    if (newTarget == null) {
                        removeUpdateListener();
                    }
                }

                @Override
                public boolean canUpdate(View source, View target) {
                    if (mPopView == null) {
                        throw new NullPointerException("popview is null");
                    }

                    if (target == null) {
                        return false;
                    }

                    final boolean isShown = isViewAttached(target) && target.isShown();
                    final PoperParent parent = (PoperParent) mPoperParent;
                    parent.synchronizeVisibilityWithTarget(isShown);

                    if (!isShown) {
                        return false;
                    }

                    parent.attachToContainer(getContainer());
                    parent.addPopView(mPopView);

                    return true;
                }

                @Override
                public void onUpdate(int x, int y, View source, View target) {
                    if (mLayouter == null) {
                        mLayouter = new DefaultLayouter();
                    }

                    final int marginX = mMarginX == null ? 0 : mMarginX.getMargin();
                    final int marginY = mMarginY == null ? 0 : mMarginY.getMargin();

                    x += marginX;
                    y += marginY;

                    mLayouter.layout(x, y, source, target);
                }
            });
        }
        return mTracker;
    }

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

    @Override
    public Poper setPopView(int layoutId) {
        final View view = (layoutId == 0) ? null : LayoutInflater.from(mActivity).inflate(layoutId, mPoperParent, false);
        return setPopView(view);
    }

    @Override
    public Poper setPopView(final View popView) {
        getTracker().setSource(popView);
        getPopUpdater().setView(popView);
        return this;
    }

    @Override
    public Poper setTarget(final View target) {
        getTracker().setTarget(target);
        getTargetUpdater().setView(target);
        return this;
    }

    @Override
    public Poper setPosition(Position position) {
        if (position == null) {
            throw new NullPointerException("position is null");
        }

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

    @Override
    public Poper setMarginX(Margin margin) {
        mMarginX = margin;
        return this;
    }

    @Override
    public Poper setMarginY(Margin margin) {
        mMarginY = margin;
        return this;
    }

    @Override
    public Poper setContainer(ViewGroup container) {
        mContainer = container;
        return this;
    }

    @Override
    public Poper setLayouter(Layouter layouter) {
        mLayouter = layouter;
        return this;
    }

    @Override
    public View getPopView() {
        return mPopView;
    }

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

    @Override
    public void release() {
        removeUpdateListener();
    }

    private void addUpdateListener() {
        getTargetUpdater().start();
        getPopUpdater().start();
    }

    private void removeUpdateListener() {
        getTargetUpdater().stop();
        getPopUpdater().stop();
    }

    private static boolean isViewAttached(View view) {
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
