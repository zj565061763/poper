package com.fanwe.www.poper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.fanwe.library.SDLibrary;

public class TestActivity extends AppCompatActivity
{
    public static final String TAG = TestActivity.class.getSimpleName();

    private FrameLayout fl_container;
    private TextView tv_target;
    private View popView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        SDLibrary.getInstance().init(getApplication());
        setContentView(R.layout.act_test);
        fl_container = (FrameLayout) findViewById(R.id.fl_container);
        tv_target = (TextView) findViewById(R.id.tv_target);

        popView = getLayoutInflater().inflate(R.layout.view_pop, null);
        fl_container.addView(popView);

        fl_container.addOnAttachStateChangeListener(mOnAttachStateChangeListener);
        popView.addOnAttachStateChangeListener(mOnAttachStateChangeListener);
        tv_target.addOnAttachStateChangeListener(mOnAttachStateChangeListener);
    }

    private View.OnAttachStateChangeListener mOnAttachStateChangeListener = new View.OnAttachStateChangeListener()
    {
        @Override
        public void onViewAttachedToWindow(View v)
        {

        }

        @Override
        public void onViewDetachedFromWindow(View v)
        {
            if (v == fl_container)
            {
                Log.i(TAG, "onViewDetachedFromWindow container");
            } else if (v == popView)
            {
                Log.i(TAG, "onViewDetachedFromWindow popView");
            } else if (v == tv_target)
            {
                Log.i(TAG, "onViewDetachedFromWindow target");
            }
        }
    };

}
