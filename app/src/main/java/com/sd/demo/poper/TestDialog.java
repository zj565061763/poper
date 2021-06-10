package com.sd.demo.poper;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.sd.lib.poper.FPoper;
import com.sd.lib.poper.Poper;

public class TestDialog extends Dialog {
    private FrameLayout mFrameLayout;
    private TextView mTextView;
    private Poper mPoper;

    public TestDialog(Activity context) {
        super(context);
        setCanceledOnTouchOutside(false);
        setOwnerActivity(context);
        setContentView(R.layout.dialog_test);
        mFrameLayout = findViewById(R.id.fl_container);
        mTextView = findViewById(R.id.textview);

        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ViewGroup.LayoutParams params = v.getLayoutParams();
                params.width = v.getWidth() + 10;
                params.height = v.getHeight() + 10;
                v.setLayoutParams(params);
            }
        });
    }

    public Poper getPoper() {
        if (mPoper == null) {
            mPoper = new FPoper(getOwnerActivity());
            mPoper.setPopView(R.layout.view_pop);
            mPoper.setContainer(mFrameLayout);
        }
        return mPoper;
    }

    @Override
    protected void onStart() {
        super.onStart();
        getPoper().setTarget(mTextView).attach(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        getPoper().attach(false);
    }
}
