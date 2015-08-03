package com.harreke.easyapp.frameworks.recyclerview;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.harreke.easyapp.frameworks.base.ApplicationFramework;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/03/05
 */
public abstract class CategoryLayoutManager extends GridLayoutManager {
    public CategoryLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
        setSpanSizeLookup(new CategorySpanSizeLookup());
    }

    public RecyclerView.ItemDecoration createItemDecoration() {
        return new ItemDecoration(getSpanCount());
    }

    protected abstract boolean shouldFillUp(int position);

    private class CategorySpanSizeLookup extends SpanSizeLookup {
        @Override
        public int getSpanSize(int position) {
            return shouldFillUp(position) ? getSpanCount() : 1;
        }
    }

    private class ItemDecoration extends RecyclerView.ItemDecoration {
        private int mHorizontalSpacing = 0;
        private int mSpanCount = 0;
        private int mVerticalSpacing = 0;

        public ItemDecoration(int spanCount) {
            this(spanCount, (int) (ApplicationFramework.Density * 8));
        }

        public ItemDecoration(int spanCount, int spacing) {
            mSpanCount = spanCount;
            mHorizontalSpacing = spacing / spanCount;
            mVerticalSpacing = spacing;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int position = parent.getChildPosition(view);
            int mod;

            if (!shouldFillUp(position)) {
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
    }
}