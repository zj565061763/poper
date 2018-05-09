package com.fanwe.www.poper;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.fanwe.library.activity.SDBaseActivity;
import com.fanwe.www.poper.dialog.TestDialog;

/**
 * Created by Administrator on 2018/1/10.
 */

public class DialogActivity extends SDBaseActivity
{
    private Button btn_dialog;

    @Override
    protected void init(Bundle savedInstanceState)
    {
        setContentView(R.layout.act_dialog);
        btn_dialog = findViewById(R.id.btn_dialog);
        btn_dialog.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TestDialog dialog = new TestDialog(DialogActivity.this);
                dialog.show();
            }
        });
    }
}
