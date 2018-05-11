package com.fanwe.www.poper;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.fanwe.lib.poper.FPoper;
import com.fanwe.lib.poper.layouter.SizeLayouter;
import com.fanwe.lib.poper.layouter.TargetSizeLayouter;
import com.fanwe.lib.utils.FViewUtil;
import com.fanwe.lib.utils.context.FResUtil;
import com.fanwe.library.activity.SDBaseActivity;

/**
 * Created by Administrator on 2018/1/10.
 */

public class AutoActivity extends SDBaseActivity
{
    private Button btn_big;
    private Button btn_small;

    private Button btn_pop;

    private TestPopView mPopView;

    @Override
    protected void init(Bundle savedInstanceState)
    {
        setContentView(R.layout.act_auto);
        btn_big = findViewById(R.id.btn_big);
        btn_small = findViewById(R.id.btn_small);
        btn_pop = findViewById(R.id.btn_pop);

        btn_big.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FViewUtil.setSize(btn_pop, btn_pop.getWidth() + 100, btn_pop.getHeight() + 100);
            }
        });
        btn_small.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FViewUtil.setSize(btn_pop, btn_pop.getWidth() - 100, btn_pop.getHeight() - 100);
            }
        });

        btn_pop.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getPopView().getPoper().attach(!getPopView().getPoper().isAttached());
            }
        });
    }

    public TestPopView getPopView()
    {
        if (mPopView == null)
        {
            mPopView = new TestPopView(this);
            mPopView.getPoper()
                    .addPopLayouter(new TargetSizeLayouter(SizeLayouter.Size.Width)
                            .setDeltaSize(FResUtil.dp2px(100))
                            .setDebug(true))
                    .setTarget(btn_pop)
                    .setPosition(FPoper.Position.BottomOutsideCenter);
        }
        return mPopView;
    }
}
