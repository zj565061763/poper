package com.sd.lib.poper.layouter;

import android.view.View;

import androidx.annotation.NonNull;

import com.sd.lib.poper.Poper;

public class CombineLayouter implements Poper.Layouter {
    private final Poper.Layouter[] mLayouters;

    public CombineLayouter(@NonNull Poper.Layouter... layouters) {
        mLayouters = layouters;
    }

    @Override
    public void layout(int x, int y, @NonNull View popView, @NonNull View target) {
        for (Poper.Layouter item : mLayouters) {
            item.layout(x, y, popView, target);
        }
    }
}
