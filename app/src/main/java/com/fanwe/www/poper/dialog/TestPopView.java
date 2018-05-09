package com.fanwe.www.poper.dialog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.fanwe.lib.poper.FPoper;
import com.fanwe.lib.poper.layouter.AutoSizeLayouter;
import com.fanwe.library.view.SDAppView;
import com.fanwe.www.poper.R;

public class TestPopView extends SDAppView
{
    public TestPopView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public TestPopView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public TestPopView(Context context)
    {
        super(context);
        init();
    }

    public static final String TAG = TestPopView.class.getSimpleName();

    private FPoper mPoper;

    private void init()
    {
        setContentView(R.layout.view_test_pop);
    }

    public FPoper getPoper()
    {
        if (mPoper == null)
        {
            mPoper = new FPoper(getActivity());
            mPoper.setDebug(true)
                    .setPopLayouter(new AutoSizeLayouter())
                    .setPopView(this)
                    .setPosition(FPoper.Position.BottomOutsideCenter);
        }
        return mPoper;
    }

    @Override
    protected boolean onTouchDownOutside(MotionEvent ev)
    {
        getPoper().attach(false);
        return true;
    }
}
