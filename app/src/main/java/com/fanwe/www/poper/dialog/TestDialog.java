package com.fanwe.www.poper.dialog;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.fanwe.lib.dialog.impl.FDialogConfirm;
import com.fanwe.lib.poper.FPoper;
import com.fanwe.lib.poper.layouter.AutoHeightLayouter;
import com.fanwe.lib.utils.FViewUtil;
import com.fanwe.www.poper.R;

/**
 * Created by Administrator on 2018/1/10.
 */
public class TestDialog extends FDialogConfirm
{
    private View ll_root;
    private TestPopView mPopView;
    private TextView mTextView;

    public TestDialog(Activity activity)
    {
        super(activity);
        setCustomView(R.layout.dialog_test);
        ll_root = findViewById(R.id.ll_root);
        mTextView = findViewById(R.id.textview);

        ll_root.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FViewUtil.setHeight(ll_root, ll_root.getHeight() + 10);
            }
        });
        mTextView.setOnClickListener(new View.OnClickListener()
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
                    .setPopLayouter(new AutoHeightLayouter())
                    .setContainer(fl_content)
                    .setTarget(mTextView)
                    .setPosition(FPoper.Position.BottomOutsideCenter);
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
