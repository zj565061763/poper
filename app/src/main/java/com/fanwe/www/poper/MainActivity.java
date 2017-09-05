package com.fanwe.www.poper;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.fanwe.library.poper.SDPoper;

public class MainActivity extends AppCompatActivity
{
    private SDPoper mPoper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv_target).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TextView textView = (TextView) v;
                textView.setText(String.valueOf(textView.getText()) + "_0000000000");
            }
        });

        TextView popView = new TextView(this); //创建一个需要Pop的TextView
        popView.setGravity(Gravity.CENTER);
        popView.setText("PopView");
        popView.setBackgroundColor(Color.RED);

        mPoper = new SDPoper(this);
        mPoper.setPopView(popView) //设置要Pop的View
                //.setMarginX(10) //设置x轴需要偏移的值，大于0往右，小于0往左
                //.setMarginY(10) //设置y轴方向的偏移量，大于0往下，小于0往上
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
