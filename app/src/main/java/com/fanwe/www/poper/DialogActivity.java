package com.fanwe.www.poper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.fanwe.www.poper.dialog.TestDialog;

/**
 * Created by Administrator on 2018/1/10.
 */

public class DialogActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
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
