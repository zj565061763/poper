package com.sd.www.poper;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.sd.lib.poper.FPoper;
import com.sd.lib.poper.Poper;
import com.sd.www.poper.databinding.ActSimpleBinding;

public class SimpleActivity extends AppCompatActivity implements View.OnClickListener
{
    public static final String TAG = SimpleActivity.class.getSimpleName();

    private ActSimpleBinding mBinding;
    private Poper mPoper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mBinding = ActSimpleBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        final Poper poper = new FPoper(this)
                // 设置要popview，可以是布局id或者View对象
                .setPopView(R.layout.view_pop)
                // 设置左上角对齐
                .setPosition(Poper.Position.TopLeft)
                // 设置要跟踪的目标View
                .setTarget(mBinding.viewTarget)
                // true-依附目标view，false-移除依附
                .attach(true);

        mPoper = poper;
    }

    public Poper getPoper()
    {
        return mPoper;
    }

    @Override
    public void onClick(View v)
    {
        if (v == mBinding.viewTarget)
        {
            final ViewGroup.LayoutParams params = v.getLayoutParams();
            params.width = v.getWidth() + 100;
            v.setLayoutParams(params);
        } else if (v == mBinding.btnToggleVisibility)
        {
            final View target = mBinding.viewTarget;
            if (target.getVisibility() != View.VISIBLE)
            {
                target.setVisibility(View.VISIBLE);
            } else
            {
                target.setVisibility(View.GONE);
            }
        } else
        {
            if (v == mBinding.btnTopLeft)
            {
                getPoper().setPosition(Poper.Position.TopLeft)
                        .attach(true);
            } else if (v == mBinding.btnTopCenter)
            {
                getPoper().setPosition(Poper.Position.TopCenter)
                        .attach(true);
            } else if (v == mBinding.btnTopRight)
            {
                getPoper().setPosition(Poper.Position.TopRight)
                        .attach(true);
            } else if (v == mBinding.btnLeftCenter)
            {
                getPoper().setPosition(Poper.Position.LeftCenter)
                        .attach(true);
            } else if (v == mBinding.btnCenter)
            {
                getPoper().setPosition(Poper.Position.Center)
                        .attach(true);
            } else if (v == mBinding.btnRightCenter)
            {
                getPoper().setPosition(Poper.Position.RightCenter)
                        .attach(true);
            } else if (v == mBinding.btnBottomLeft)
            {
                getPoper().setPosition(Poper.Position.BottomLeft)
                        .attach(true);
            } else if (v == mBinding.btnBottomCenter)
            {
                getPoper().setPosition(Poper.Position.BottomCenter)
                        .attach(true);
            } else if (v == mBinding.btnBottomRight)
            {
                getPoper().setPosition(Poper.Position.BottomRight)
                        .attach(true);
            }
        }
    }
}
