package com.fanwe.www.poper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.fanwe.lib.poper.SDPoper;
import com.fanwe.library.adapter.SDSimpleAdapter;
import com.fanwe.library.listener.SDSimpleIterateCallback;
import com.fanwe.library.looper.ISDLooper;
import com.fanwe.library.looper.impl.SDSimpleLooper;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDCollectionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

public class ListViewActivity extends AppCompatActivity
{
    private ListView lv_content;
    private ViewGroup fl_container;

    private List<String> mListModel = new ArrayList<>();

    private ISDLooper mLooper = new SDSimpleLooper();
    private WeakHashMap<SDPoper, Integer> mMapPoper = new WeakHashMap<>();
    private WeakHashMap<View, SDPoper> mMapViewPoper = new WeakHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_listview);
        lv_content = (ListView) findViewById(R.id.lv_content);
        fl_container = (ViewGroup) findViewById(R.id.fl_container);
        createData();

        SDSimpleAdapter<String> adapter = new SDSimpleAdapter<String>(mListModel, this)
        {
            @Override
            public int getLayoutId(int position, View convertView, ViewGroup parent)
            {
                return R.layout.item_list_view;
            }

            @Override
            public void bindData(int position, View convertView, ViewGroup parent, String model)
            {
                Button button = get(R.id.btn, convertView);

                button.removeOnAttachStateChangeListener(mOnAttachStateChangeListener);
                button.addOnAttachStateChangeListener(mOnAttachStateChangeListener);
            }

            private View.OnAttachStateChangeListener mOnAttachStateChangeListener = new View.OnAttachStateChangeListener()
            {
                @Override
                public void onViewAttachedToWindow(View v)
                {
                    SDPoper poper = new SDPoper(ListViewActivity.this)
                            .setDebug(true)
                            .setContainer(fl_container)
                            .setPopView(R.layout.view_pop)
                            .setTarget(v)
                            .setPosition(SDPoper.Position.TopRight)
                            .attach(true);

                    mMapPoper.put(poper, 1);
                    mMapViewPoper.put(v, poper);
                }

                @Override
                public void onViewDetachedFromWindow(View v)
                {
                    SDPoper poper = mMapViewPoper.remove(v);
                    if (poper != null)
                    {
                        poper.attach(false);
                    }
                }
            };

        };
        lv_content.setAdapter(adapter);

        mLooper.start(1000, new Runnable()
        {
            @Override
            public void run()
            {
                LogUtil.i("Poper:" + mMapPoper.size() + " " +
                        "View:" + mMapViewPoper.size() + " " +
                        "Child:" + fl_container.getChildCount());
            }
        });
    }

    private void createData()
    {
        SDCollectionUtil.foreach(100, new SDSimpleIterateCallback()
        {
            @Override
            public boolean next(int i)
            {
                mListModel.add(String.valueOf(i));
                return false;
            }
        });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mLooper.stop();
    }
}
