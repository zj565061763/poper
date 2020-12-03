package com.sd.www.poper;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.sd.lib.poper.FPoper;
import com.sd.lib.poper.Poper;

import java.util.HashMap;
import java.util.Map;

public class SimpleActivity extends AppCompatActivity
{
    public static final String TAG = SimpleActivity.class.getSimpleName();

    private Map<Poper, Integer> mMapPoper = new HashMap<>();
    private View view_target;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_simple);
        view_target = findViewById(R.id.view_target);
        view_target.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ViewGroup.LayoutParams params = v.getLayoutParams();
                params.width = v.getWidth() + 100;
                v.setLayoutParams(params);
            }
        });

        Poper poper = new FPoper(this)
                // 设置要popview，可以是布局id或者View对象
                .setPopView(R.layout.view_pop)
                // 设置左上角对齐
                .setPosition(Poper.Position.TopLeft)
                // 设置要跟踪的目标View
                .setTarget(view_target)
                // true-依附目标view，false-移除依附
                .attach(true);

        mMapPoper.put(poper, 0);
    }

    public Poper getPoper()
    {
        return mMapPoper.keySet().iterator().next();
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

    public void onClickToggleVisibility(View v)
    {
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
