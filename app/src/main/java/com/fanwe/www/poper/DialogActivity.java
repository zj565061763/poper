package com.fanwe.www.poper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.fanwe.lib.dialog.impl.FDialogConfirm;
import com.fanwe.lib.poper.FPoper;

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

        FDialogConfirm dialogConfirm = new FDialogConfirm(this);
        dialogConfirm.setCanceledOnTouchOutside(true);
        dialogConfirm.setTextContent("content");

        dialogConfirm.showBottom();

        getPoper().setContainer(dialogConfirm.fl_content)
                .setTarget(dialogConfirm.tv_content)
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
