package com.fanwe.www.poper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.lib.poper.FPoper;
import com.fanwe.library.SDLibrary;

import java.util.HashMap;
import java.util.Map;

public class SimpleActivity extends AppCompatActivity
{
    private Map<FPoper, Integer> mMapPoper = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        SDLibrary.getInstance().init(getApplication());
        setContentView(R.layout.act_simple);

        findViewById(R.id.view_target).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ViewGroup.LayoutParams params = v.getLayoutParams();
                params.width = v.getWidth() + 100;
                v.setLayoutParams(params);
            }
        });

        FPoper poper = new FPoper(this)
                .setDebug(true)
//                .setContainer((ViewGroup) findViewById(R.id.fl_container)) // 设置popview可以显示的容器范围，默认是Activity中id为android.R.id.content的容器
//                .setMarginX(10) // 设置x轴需要偏移的值，大于0往右，小于0往左
//                .setMarginY(10) // 设置y轴方向的偏移量，大于0往下，小于0往上
                .setPopView(R.layout.view_pop) // 设置要popview，可以是布局id或者View对象
                .setPosition(FPoper.Position.TopLeft) //左上角对齐
                .setTarget(findViewById(R.id.view_target)) // 设置要跟踪的目标View
                .attach(true); // //true-依附目标view，false-移除依附

        mMapPoper.put(poper, 0);
    }

    public FPoper getPoper()
    {
        return mMapPoper.keySet().iterator().next();
    }

    public void onClickTopLeft(View v)
    {
        getPoper().setPosition(FPoper.Position.TopLeft) //左上角对齐
                .attach(true); //true-依附目标view，false-移除依附
    }

    public void onClickTopCenter(View v)
    {
        getPoper().setPosition(FPoper.Position.TopCenter)
                .attach(true);
    }

    public void onClickTopRight(View v)
    {
        getPoper().setPosition(FPoper.Position.TopRight)
                .attach(true);
    }

    public void onClickLeftCenter(View v)
    {
        getPoper().setPosition(FPoper.Position.LeftCenter)
                .attach(true);
    }

    public void onClickCenter(View v)
    {
        getPoper().setPosition(FPoper.Position.Center)
                .attach(true);
    }

    public void onClickRightCenter(View v)
    {
        getPoper().setPosition(FPoper.Position.RightCenter)
                .attach(true);
    }

    public void onClickBottomLeft(View v)
    {
        getPoper().setPosition(FPoper.Position.BottomLeft)
                .attach(true);
    }

    public void onClickBottomCenter(View v)
    {
        getPoper().setPosition(FPoper.Position.BottomCenter)
                .attach(true);
    }

    public void onClickBottomRight(View v)
    {
        getPoper().setPosition(FPoper.Position.BottomRight)
                .attach(true);
    }

    public void onClickTopLeftOutside(View v)
    {
        getPoper().setPosition(FPoper.Position.TopLeftOutside)
                .attach(true);
    }

    public void onClickTopCenterOutside(View v)
    {
        getPoper().setPosition(FPoper.Position.TopCenterOutside)
                .attach(true);
    }

    public void onClickTopRightOutside(View v)
    {
        getPoper().setPosition(FPoper.Position.TopRightOutside)
                .attach(true);
    }

    public void onClickBottomLeftOutside(View v)
    {
        getPoper().setPosition(FPoper.Position.BottomLeftOutside)
                .attach(true);
    }

    public void onClickBottomCenterOutside(View v)
    {
        getPoper().setPosition(FPoper.Position.BottomCenterOutside)
                .attach(true);
    }

    public void onClickBottomRightOutside(View v)
    {
        getPoper().setPosition(FPoper.Position.BottomRightOutside)
                .attach(true);
    }

    public void onClickLeftTopOutside(View v)
    {
        getPoper().setPosition(FPoper.Position.LeftTopOutside)
                .attach(true);
    }

    public void onClickLeftCenterOutside(View v)
    {
        getPoper().setPosition(FPoper.Position.LeftCenterOutside)
                .attach(true);
    }

    public void onClickLeftBottomOutside(View v)
    {
        getPoper().setPosition(FPoper.Position.LeftBottomOutside)
                .attach(true);
    }


    public void onClickRightTopOutside(View v)
    {
        getPoper().setPosition(FPoper.Position.RightTopOutside)
                .attach(true);
    }

    public void onClickRightCenterOutside(View v)
    {
        getPoper().setPosition(FPoper.Position.RightCenterOutside)
                .attach(true);
    }

    public void onClickRightBottomOutside(View v)
    {
        getPoper().setPosition(FPoper.Position.RightBottomOutside)
                .attach(true);
    }

    public void onClickToggleVisibility(View v)
    {
        View view_target = findViewById(R.id.view_target);

        if (view_target != null)
        {
            if (view_target.getVisibility() != View.VISIBLE)
            {
                view_target.setVisibility(View.VISIBLE);
            } else
            {
                view_target.setVisibility(View.GONE);
            }
        }
    }
}
