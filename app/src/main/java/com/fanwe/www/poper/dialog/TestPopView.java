package com.fanwe.www.poper.dialog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.fanwe.lib.adapter.FSimpleAdapter;
import com.fanwe.lib.poper.FPoper;
import com.fanwe.library.view.SDAppView;
import com.fanwe.www.poper.R;

import java.util.ArrayList;
import java.util.List;

public class TestPopView extends SDAppView
{
    public TestPopView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public TestPopView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public TestPopView(Context context)
    {
        super(context);
        init();
    }

    private ListView mListView;
    private FSimpleAdapter<String> mAdapter;

    private FPoper mPoper;

    private void init()
    {
        setContentView(R.layout.view_test_pop);
        mListView = findViewById(R.id.lv_content);

        mListView.setAdapter(getAdapter());

        final List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++)
        {
            list.add(String.valueOf(i));
        }
        getAdapter().getDataHolder().setData(list);
    }

    private FSimpleAdapter<String> getAdapter()
    {
        if (mAdapter == null)
        {
            mAdapter = new FSimpleAdapter<String>(getActivity())
            {
                @Override
                public int getLayoutId(int position, View convertView, ViewGroup parent)
                {
                    return R.layout.item_test_pop;
                }

                @Override
                public void onBindData(int position, View convertView, ViewGroup parent, final String model)
                {
                    final TextView textView = get(R.id.tv_content, convertView);
                    textView.setText(model);
                    convertView.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            getDataHolder().removeData(model);
                        }
                    });
                }
            };
        }
        return mAdapter;
    }

    public FPoper getPoper()
    {
        if (mPoper == null)
        {
            mPoper = new FPoper(getActivity());
            mPoper.setPopView(this);
        }
        return mPoper;
    }
}
