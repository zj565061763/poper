package com.fanwe.www.poper;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.fanwe.lib.dialog.impl.FDialogConfirm;
import com.fanwe.lib.poper.FPoper;
import com.fanwe.library.utils.SDToast;

/**
 * Created by Administrator on 2018/1/10.
 */

public class DialogActivity extends AppCompatActivity
{
    private FPoper mPoper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        FDialogConfirm dialog = new FDialogConfirm(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setTextContent("content");
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener()
        {
            @Override
            public void onDismiss(DialogInterface dialog)
            {
                SDToast.showToast("onDismiss");
            }
        });
        dialog.showBottom();

        getPoper().setContainer(dialog.fl_content)
                .setTarget(dialog.tv_content)
                .attach(true);
    }


    public FPoper getPoper()
    {
        if (mPoper == null)
        {
            mPoper = new FPoper(this);
            mPoper.setPopView(R.layout.view_pop);
        }
        return mPoper;
    }
}
