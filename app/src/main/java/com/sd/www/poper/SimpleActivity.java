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

    public void onClickTopLeft(View v)
    {
        getPoper().setPosition(Poper.Position.TopLeft) //左上角对齐
                .attach(true); //true-依附目标view，false-移除依附
    }

    public void onClickTopCenter(View v)
    {
        getPoper().setPosition(Poper.Position.TopCenter)
                .attach(true);
    }

    public void onClickTopRight(View v)
    {
        getPoper().setPosition(Poper.Position.TopRight)
                .attach(true);
    }

    public void onClickLeftCenter(View v)
    {
        getPoper().setPosition(Poper.Position.LeftCenter)
                .attach(true);
    }

    public void onClickCenter(View v)
    {
        getPoper().setPosition(Poper.Position.Center)
                .attach(true);
    }

    public void onClickRightCenter(View v)
    {
        getPoper().setPosition(Poper.Position.RightCenter)
                .attach(true);
    }

    public void onClickBottomLeft(View v)
    {
        getPoper().setPosition(Poper.Position.BottomLeft)
                .attach(true);
    }

    public void onClickBottomCenter(View v)
    {
        getPoper().setPosition(Poper.Position.BottomCenter)
                .attach(true);
    }

    public void onClickBottomRight(View v)
    {
        getPoper().setPosition(Poper.Position.BottomRight)
                .attach(true);
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
        }
    }
}
