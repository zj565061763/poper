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
        setCanceledOnTouchOutside(true);
        setTextContent("fdsfufhajfisjfjsdfoijaeoijfoijsfdojaisofjaiejasofijaosdjfoajsoidfiasdfashfiosdjfisajf" +
                "fojeijfoasejfosajfopasfjosijfosjfosajfjsaeofjsofjioasjfisaofjosajfosjfojasfijsojfseojfjsf" +
                "ifasoejfosaijfosajfosijafsa");
    }

    private FPoper mPoper;

    private FPoper getPoper()
    {
        if (mPoper == null)
        {
            mPoper = new FPoper(getOwnerActivity());
            mPoper.setDebug(true);
            mPoper.setContainer(fl_content)
                    .setPopView(R.layout.view_pop)
                    .setPosition(FPoper.Position.TopCenter);
        }
        return mPoper;
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        getPoper().setTarget(tv_content).attach(true);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        getPoper().attach(false);
    }
}
