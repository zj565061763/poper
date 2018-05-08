package com.fanwe.www.poper.dialog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.fanwe.lib.poper.FPoper;
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

    private FPoper mPoper;

    private void init()
    {
        setContentView(R.layout.view_test_pop);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public FPoper getPoper()
    {
        if (mPoper == null)
        {
            mPoper = new FPoper(getActivity());
            mPoper.setDebug(true).setPopView(this).setPosition(FPoper.Position.BottomOutsideCenter);
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
