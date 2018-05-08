package com.fanwe.www.poper.dialog;

import android.app.Activity;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.fanwe.lib.dialog.impl.FDialogConfirm;
import com.fanwe.lib.utils.FViewUtil;
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
        paddings(0);
        setCanceledOnTouchOutside(true);

        mTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Rect rect = FViewUtil.getGlobalVisibleRect(mTextView);

                int[] screen = new int[2];
                mTextView.getLocationOnScreen(screen);

                int[] window = new int[2];
                mTextView.getLocationInWindow(window);

                getPopView().getPoper().attach(true);
            }
        });
    }

    public TestPopView getPopView()
    {
        if (mPopView == null)
        {
            mPopView = new TestPopView(getOwnerActivity());
            mPopView.getPoper().setContainer(fl_content).setTarget(mTextView);
        }
        return mPopView;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event)
    {
        if (getPopView().dispatchTouchEvent(getOwnerActivity(), event))
        {
            return true;
        }
        return super.onTouchEvent(event);
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
