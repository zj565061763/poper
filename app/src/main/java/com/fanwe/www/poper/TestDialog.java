package com.fanwe.www.poper;

import android.app.Activity;

import com.fanwe.lib.dialog.impl.FDialogConfirm;
import com.fanwe.lib.poper.FPoper;

/**
 * Created by Administrator on 2018/1/10.
 */

public class TestDialog extends FDialogConfirm
{
    public TestDialog(Activity activity)
    {
        super(activity);
    }

    private FPoper mPoper;

    public FPoper getPoper()
    {
        if (mPoper == null)
        {
            mPoper = new FPoper(getOwnerActivity());
            mPoper.setDebug(true);
            mPoper.setContainer(fl_content)
                    .setPopView(R.layout.view_pop)
                    .setTarget(tv_content)
                    .setPosition(FPoper.Position.TopRight);
        }
        return mPoper;
    }
}
