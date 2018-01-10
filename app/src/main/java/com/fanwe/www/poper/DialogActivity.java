package com.fanwe.www.poper;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.fanwe.lib.dialog.impl.FDialogConfirm;
import com.fanwe.lib.poper.FPoper;
import com.fanwe.library.utils.SDToast;

/**
 * Created by Administrator on 2018/1/10.
 */

public class DialogActivity extends AppCompatActivity
{
    private FPoper mPoper;
    private FDialogConfirm mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_dialog);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mDialog.setTextContent("---------------");
                mDialog.showBottom();
            }
        });

        mDialog = new FDialogConfirm(this);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setTextContent("content");
        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener()
        {
            @Override
            public void onDismiss(DialogInterface dialog)
            {
                SDToast.showToast("onDismiss");
            }
        });
        mDialog.showBottom();

        getPoper()
                .setContainer(mDialog.fl_content)
                .setTarget(mDialog.tv_content)
                .attach(true);
    }


    public FPoper getPoper()
    {
        if (mPoper == null)
        {
            mPoper = new FPoper(this);
            mPoper.setDebug(true);
            mPoper.setPopView(R.layout.view_pop)
                    .setPosition(FPoper.Position.TopRight);
        }
        return mPoper;
    }
}
