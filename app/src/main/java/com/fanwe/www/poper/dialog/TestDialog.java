package com.fanwe.www.poper.dialog;

import android.app.Activity;
import android.view.View;
import android.widget.Button;

import com.fanwe.lib.dialog.impl.FDialogConfirm;
import com.fanwe.lib.poper.FPoper;
import com.fanwe.lib.poper.layouter.FixWrapLayouter;
import com.fanwe.lib.poper.layouter.BoundLayouter;
import com.fanwe.lib.utils.FViewUtil;
import com.fanwe.www.poper.R;
import com.fanwe.www.poper.TestPopView;

/**
 * Created by Administrator on 2018/1/10.
 */
public class TestDialog extends FDialogConfirm
{
    private Button btn_big;
    private Button btn_small;

    private Button btn_pop;

    private TestPopView mPopView;

    public TestDialog(Activity activity)
    {
        super(activity);
        setCustomView(R.layout.dialog_test);
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
            mPopView = new TestPopView(getOwnerActivity());
            mPopView.getPoper()
                    .addPopLayouter(new FixWrapLayouter(BoundLayouter.Size.Width).setDebug(true))
                    .addPopLayouter(new FixWrapLayouter(BoundLayouter.Size.Height).setDebug(true))
                    .setContainer(fl_content)
                    .setTarget(btn_pop)
                    .setPosition(FPoper.Position.LeftOutsideTop);
        }
        return mPopView;
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        getPopView().getPoper().attach(false);
    }
}
