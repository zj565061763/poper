package com.sd.www.poper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.sd.lib.adapter.FSimpleAdapter;
import com.sd.lib.looper.Looper;
import com.sd.lib.looper.impl.FSimpleLooper;
import com.sd.lib.poper.FPoper;
import com.sd.lib.poper.Poper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class ListViewActivity extends AppCompatActivity
{
    public static final String TAG = ListViewActivity.class.getSimpleName();

    private ListView lv_content;
    private ViewGroup fl_container;

    private Looper mLooper;
    private Map<View, Poper> mMapViewPoper = new WeakHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_listview);
        lv_content = findViewById(R.id.lv_content);
        fl_container = findViewById(R.id.fl_container);

        List<String> listModel = new ArrayList<>();
        for (int i = 0; i < 100; i++)
        {
            listModel.add(String.valueOf(i));
        }
        mAdapter.getDataHolder().setData(listModel);
        lv_content.setAdapter(mAdapter);

        getLooper().start();
    }


    private Poper getPoper(View view)
    {
        Poper poper = mMapViewPoper.get(view);
        if (poper == null)
        {
            poper = new FPoper(ListViewActivity.this)
                    .setContainer(fl_container)
                    .setPopView(R.layout.view_pop)
                    .setTarget(view);
            mMapViewPoper.put(view, poper);
        }
        return poper;
    }

    private final FSimpleAdapter<String> mAdapter = new FSimpleAdapter<String>(this)
    {
        @Override
        public int getLayoutId(int position, View convertView, ViewGroup parent)
        {
            return R.layout.item_list_view;
        }

        @Override
        public void onBindData(int position, View convertView, ViewGroup parent, String model)
        {
            Button btn1 = get(R.id.btn1, convertView);
            Button btn2 = get(R.id.btn2, convertView);
            Button btn3 = get(R.id.btn3, convertView);
            Button btn4 = get(R.id.btn4, convertView);
            Button btn5 = get(R.id.btn5, convertView);

            getPoper(btn1).attach(true);
            getPoper(btn2).attach(true);
            getPoper(btn3).attach(true);
            getPoper(btn4).attach(true);
            getPoper(btn5).attach(true);
        }
    };

    private Looper getLooper()
    {
        if (mLooper == null)
        {
            mLooper = new FSimpleLooper();
            mLooper.setInterval(1000);
            mLooper.setLoopRunnable(new Runnable()
            {
                @Override
                public void run()
                {
                    Log.i(TAG, "View:" + mMapViewPoper.size() + " " +
                            "Child:" + fl_container.getChildCount());
                }
            });
        }
        return mLooper;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        getLooper().stop();
    }
}
