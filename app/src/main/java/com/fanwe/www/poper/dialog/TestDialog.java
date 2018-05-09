package com.fanwe.www.poper.dialog;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.fanwe.lib.dialog.impl.FDialogConfirm;
import com.fanwe.lib.poper.FPoper;
import com.fanwe.lib.poper.layouter.AutoSizeLayouter;
import com.fanwe.www.poper.R;

/**
 * Created by Administrator on 2018/1/10.
 */
public class TestDialog extends FDialogConfirm
{
    private TestPopView mPopView;
    private TextView mTextView;

    public TestDialog(Activity activity)
    {
        super(activity);
        setCustomView(R.layout.dialog_test);
        mTextView = findViewById(R.id.textview);
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
//                    .setPopLayouter(new AutoSizeLayouter())
                    .setContainer(fl_content)
                    .setTarget(mTextView)
                    .setPosition(FPoper.Position.BottomOutsideCenter);
        }
        return mPopView;
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        getPopView().getPoper().attach(true);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        getPopView().getPoper().attach(false);
    }
}
