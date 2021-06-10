package com.sd.demo.poper;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.sd.lib.adapter.FSimpleAdapter;
import com.sd.lib.poper.FPoper;
import com.sd.lib.poper.Poper;
import com.sd.demo.poper.databinding.ActListviewBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class ListViewActivity extends AppCompatActivity {
    public static final String TAG = ListViewActivity.class.getSimpleName();

    private ActListviewBinding mBinding;
    private final Map<View, Poper> mMapViewPoper = new WeakHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActListviewBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        initListView();
    }

    private void initListView() {
        final List<String> listModel = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            listModel.add(String.valueOf(i));
        }
        mAdapter.getDataHolder().setData(listModel);
        mBinding.lvContent.setAdapter(mAdapter);
        mBinding.lvContent.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    view.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, "View:" + mMapViewPoper.size() + " " +
                                    "Child:" + mBinding.flContainer.getChildCount());
                        }
                    });
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    private Poper getPoper(View view) {
        Poper poper = mMapViewPoper.get(view);
        if (poper == null) {
            poper = new FPoper(ListViewActivity.this)
                    .setContainer(mBinding.flContainer)
                    .setPopView(R.layout.view_pop)
                    .setTarget(view);
            mMapViewPoper.put(view, poper);
        }
        return poper;
    }

    private final FSimpleAdapter<String> mAdapter = new FSimpleAdapter<String>(this) {
        @Override
        public int getLayoutId(int position, View convertView, ViewGroup parent) {
            return R.layout.item_list_view;
        }

        @Override
        public void onBindData(int position, View convertView, ViewGroup parent, String model) {
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
}
