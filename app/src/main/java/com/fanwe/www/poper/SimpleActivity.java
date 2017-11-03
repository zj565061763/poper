package com.fanwe.www.poper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.lib.poper.SDPoper;
import com.fanwe.library.SDLibrary;

public class SimpleActivity extends AppCompatActivity
{
    private SDPoper mPoper;
    private ViewGroup fl_container;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        SDLibrary.getInstance().init(getApplication());
        setContentView(R.layout.act_simple);
        fl_container = (ViewGroup) findViewById(R.id.fl_container);

        findViewById(R.id.tv_target).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TextView textView = (TextView) v;
                textView.setText(String.valueOf(textView.getText()) + "_0000000000");
            }
        });

        mPoper = new SDPoper(this);
        mPoper.setDebug(true);
        mPoper.setContainer(fl_container); // 可以修改默认的容器
        mPoper.setPopView(R.layout.view_pop) //设置要Pop的View，可以是布局id或者View对象
                .setTarget(findViewById(R.id.tv_target)); //设置要跟踪的目标View
    }

    public void onClickTopLeft(View v)
    {
        mPoper.setPosition(SDPoper.Position.TopLeft) //左上角对齐
                .attach(true); //true-依附目标view，false-移除依附
    }

    public void onClickTopCenter(View v)
    {
        mPoper.setPosition(SDPoper.Position.TopCenter)
                .attach(true);
    }

    public void onClickTopRight(View v)
    {
        mPoper.setPosition(SDPoper.Position.TopRight)
                .attach(true);
    }

    public void onClickLeftCenter(View v)
    {
        mPoper.setPosition(SDPoper.Position.LeftCenter)
                .attach(true);
    }

    public void onClickCenter(View v)
    {
        mPoper.setPosition(SDPoper.Position.Center)
                .attach(true);
    }

    public void onClickRightCenter(View v)
    {
        mPoper.setPosition(SDPoper.Position.RightCenter)
                .attach(true);
    }

    public void onClickBottomLeft(View v)
    {
        mPoper.setPosition(SDPoper.Position.BottomLeft)
                .attach(true);
    }

    public void onClickBottomCenter(View v)
    {
        mPoper.setPosition(SDPoper.Position.BottomCenter)
                .attach(true);
    }

    public void onClickBottomRight(View v)
    {
        mPoper.setPosition(SDPoper.Position.BottomRight)
                .attach(true);
    }

    public void onClickTopLeftOutside(View v)
    {
        mPoper.setPosition(SDPoper.Position.TopLeftOutside)
                .attach(true);
    }

    public void onClickTopCenterOutside(View v)
    {
        mPoper.setPosition(SDPoper.Position.TopCenterOutside)
                .attach(true);
    }

    public void onClickTopRightOutside(View v)
    {
        mPoper.setPosition(SDPoper.Position.TopRightOutside)
                .attach(true);
    }

    public void onClickBottomLeftOutside(View v)
    {
        mPoper.setPosition(SDPoper.Position.BottomLeftOutside)
                .attach(true);
    }

    public void onClickBottomCenterOutside(View v)
    {
        mPoper.setPosition(SDPoper.Position.BottomCenterOutside)
                .attach(true);
    }

    public void onClickBottomRightOutside(View v)
    {
        mPoper.setPosition(SDPoper.Position.BottomRightOutside)
                .attach(true);
    }

    public void onClickLeftTopOutside(View v)
    {
        mPoper.setPosition(SDPoper.Position.LeftTopOutside)
                .attach(true);
    }

    public void onClickLeftCenterOutside(View v)
    {
        mPoper.setPosition(SDPoper.Position.LeftCenterOutside)
                .attach(true);
    }

    public void onClickLeftBottomOutside(View v)
    {
        mPoper.setPosition(SDPoper.Position.LeftBottomOutside)
                .attach(true);
    }


    public void onClickRightTopOutside(View v)
    {
        mPoper.setPosition(SDPoper.Position.RightTopOutside)
                .attach(true);
    }

    public void onClickRightCenterOutside(View v)
    {
        mPoper.setPosition(SDPoper.Position.RightCenterOutside)
                .attach(true);
    }

    public void onClickRightBottomOutside(View v)
    {
        mPoper.setPosition(SDPoper.Position.RightBottomOutside)
                .attach(true);
    }

    public void onClickToggleVisibility(View v)
    {
        v = findViewById(R.id.tv_target);
        if (v.getVisibility() != View.VISIBLE)
        {
            v.setVisibility(View.VISIBLE);
        } else
        {
            v.setVisibility(View.INVISIBLE);
        }
    }

}
