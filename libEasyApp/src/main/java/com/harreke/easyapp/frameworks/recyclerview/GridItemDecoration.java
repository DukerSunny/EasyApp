package com.harreke.easyapp.frameworks.recyclerview;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.harreke.easyapp.frameworks.bases.application.ApplicationFramework;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/02
 */
public class GridItemDecoration extends RecyclerView.ItemDecoration {
    private int mHorizontalSpacing = 0;
    private int mSpanCount = 0;
    private int mVerticalSpacing = 0;

    public GridItemDecoration(int spanCount) {
        this(spanCount, (int) (ApplicationFramework.Density * 8));
    }

    public GridItemDecoration(int spanCount, int spacing) {
        mSpanCount = spanCount;
        mHorizontalSpacing = spacing / spanCount;
        mVerticalSpacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildPosition(view);
        int mod;

        mod = position % mSpanCount;
        if (mod == 0) {
            outRect.right = mHorizontalSpacing;
        }
        if (mod > 0 && mod < mSpanCount - 1) {
            outRect.left = mHorizontalSpacing;
            outRect.right = mHorizontalSpacing;
        }
        if (mod == mSpanCount - 1) {
            outRect.left = mHorizontalSpacing;
        }
        if (position >= mSpanCount) {
            outRect.top = mVerticalSpacing;
        }
    }
}