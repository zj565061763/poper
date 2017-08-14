package com.fanwe.www.poper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

        mPoper = new SDPoper(this);
        mPoper.setPopView(new CustomPopView(this))
                .setDynamicUpdate(true) //设置是否需要动态更新PopView的位置，如果true
                .setTarget(findViewById(R.id.tv_target));
    }

    public void onClickTopLeft(View v)
    {
        mPoper.setPosition(SDPoper.Position.TopLeft)
                .attach(true);
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

}
