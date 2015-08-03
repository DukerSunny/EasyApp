package com.harreke.easyapp.frameworks.recyclerview.itemdecorations;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.harreke.easyapp.R;
import com.harreke.easyapp.utils.ResourceUtil;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/02
 */
public class LinearItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider = null;
    private int mOrientation = LinearLayoutManager.HORIZONTAL;
    private int mSpacing = 0;

    public LinearItemDecoration(Context context, int orientation) {
        this(orientation, ResourceUtil.getColor(context, R.color.divider));
    }

    public LinearItemDecoration(int orientation, int color) {
        this(orientation, color, 1);
    }

    public LinearItemDecoration(int orientation, int color, int spacing) {
        mOrientation = orientation;
        mDivider = new ColorDrawable(color);
        mSpacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildLayoutPosition(view);

        if (position > 0) {
            switch (mOrientation) {
                case LinearLayoutManager.HORIZONTAL:
                    outRect.left = mSpacing;
                    break;
                case LinearLayoutManager.VERTICAL:
                    outRect.top = mSpacing;
                    break;
            }
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.LayoutParams params;
        View child;
        int left;
        int top;
        int right;
        int bottom;
        int childCount;
        int i;

        switch (mOrientation) {
            case LinearLayoutManager.HORIZONTAL:
                top = parent.getPaddingTop();
                bottom = parent.getHeight() - parent.getPaddingBottom();
                childCount = parent.getChildCount();

                for (i = 1; i < childCount; i++) {
                    child = parent.getChildAt(i);
                    params = (RecyclerView.LayoutParams) child.getLayoutParams();
                    left = child.getLeft() - params.leftMargin;
                    right = left + mSpacing;
                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(c);
                }
                break;
            case LinearLayoutManager.VERTICAL:
                left = parent.getPaddingLeft();
                right = parent.getWidth() - parent.getPaddingRight();
                childCount = parent.getChildCount();

                for (i = 1; i < childCount; i++) {
                    child = parent.getChildAt(i);
                    params = (RecyclerView.LayoutParams) child.getLayoutParams();
                    top = child.getTop() - params.topMargin;
                    bottom = top + mSpacing;
                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(c);
                }
                break;
        }
    }
}