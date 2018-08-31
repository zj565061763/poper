package com.sd.www.poper;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sd.lib.adapter.FSimpleAdapter;
import com.sd.lib.poper.FPoper;

import java.util.ArrayList;
import java.util.List;

public class TestPopView extends FrameLayout
{
    private ListView mListView;
    private FSimpleAdapter<String> mAdapter;

    private FPoper mPoper;

    public TestPopView(Context context)
    {
        super(context);
        init();
    }

    private void init()
    {
        LayoutInflater.from(getContext()).inflate(R.layout.view_test_pop, this, true);
        mListView = findViewById(R.id.lv_content);
        mListView.setAdapter(getAdapter());

        final List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++)
        {
            list.add(String.valueOf(i));
        }
        getAdapter().getDataHolder().setData(list);
    }

    private FSimpleAdapter<String> getAdapter()
    {
        if (mAdapter == null)
        {
            mAdapter = new FSimpleAdapter<String>()
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
                    convertView.setOnLongClickListener(new OnLongClickListener()
                    {
                        @Override
                        public boolean onLongClick(View v)
                        {
                            getDataHolder().addData(String.valueOf(getDataHolder().size()));
                            return false;
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
            mPoper = new FPoper((Activity) getContext());
            mPoper.setPopView(this);
        }
        return mPoper;
    }
}
