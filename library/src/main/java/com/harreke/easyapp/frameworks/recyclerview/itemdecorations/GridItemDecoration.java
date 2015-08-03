package com.harreke.easyapp.frameworks.recyclerview.itemdecorations;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.harreke.easyapp.frameworks.base.ApplicationFramework;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/02
 */
public class GridItemDecoration extends RecyclerView.ItemDecoration {
    private int mSpacing = 0;
    private int mSpanCount = 0;

    public GridItemDecoration(int spanCount) {
        this(spanCount, (int) (ApplicationFramework.Density * 8));
    }

    public GridItemDecoration(int spanCount, int spacing) {
        mSpanCount = spanCount;
        mSpacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewPosition();
        int mod;

        mod = position % mSpanCount;
        outRect.left = mod * mSpacing / mSpanCount;
        outRect.right = (mSpanCount - mod - 1) * mSpacing / mSpanCount;
        if (position >= mSpanCount) {
            outRect.top = mSpacing;
        }
    }
}