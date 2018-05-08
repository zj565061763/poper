package com.fanwe.www.poper;

import android.os.Bundle;
import android.view.View;

import com.fanwe.library.activity.SDBaseActivity;
import com.fanwe.www.poper.dialog.TestDialog;

/**
 * Created by Administrator on 2018/1/10.
 */

public class DialogActivity extends SDBaseActivity
{

    @Override
    protected void init(Bundle savedInstanceState)
    {
        setContentView(R.layout.act_dialog);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TestDialog testDialog = new TestDialog(DialogActivity.this);
                testDialog.showTop();
            }
        });
    }
}
