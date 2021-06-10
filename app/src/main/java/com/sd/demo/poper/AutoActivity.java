package com.sd.demo.poper;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.sd.lib.poper.Poper;
import com.sd.lib.poper.layouter.CombineLayouter;
import com.sd.lib.poper.layouter.DefaultLayouter;
import com.sd.lib.poper.layouter.FixBoundaryLayouter;
import com.sd.lib.utils.FViewUtil;
import com.sd.demo.poper.databinding.ActAutoBinding;

/**
 * Created by Administrator on 2018/1/10.
 */
public class AutoActivity extends AppCompatActivity implements View.OnClickListener {
    private ActAutoBinding mBinding;
    private TestPopView mPopView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActAutoBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
    }

    public TestPopView getPopView() {
        if (mPopView == null) {
            mPopView = new TestPopView(this);
            mPopView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            mPopView.getPoper()
                    .setTarget(mBinding.btnPop)
                    .setMarginY(new Poper.Margin() {
                        @Override
                        public int getMargin() {
                            return mPopView.getHeight();
                        }
                    })
                    .setLayouter(new CombineLayouter(new DefaultLayouter(), new FixBoundaryLayouter(FixBoundaryLayouter.Boundary.Height).setDebug(true)))
                    .setPosition(Poper.Position.Bottom);
        }
        return mPopView;
    }

    @Override
    public void onClick(View v) {
        if (v == mBinding.btnBig) {
            FViewUtil.setSize(mBinding.btnPop, mBinding.btnPop.getWidth() + 50, mBinding.btnPop.getHeight() + 50);
        } else if (v == mBinding.btnSmall) {
            FViewUtil.setSize(mBinding.btnPop, mBinding.btnPop.getWidth() - 50, mBinding.btnPop.getHeight() - 50);
        } else if (v == mBinding.btnPop) {
            final boolean attach = !getPopView().getPoper().isAttached();
            getPopView().getPoper().attach(attach);
        }
    }
}
