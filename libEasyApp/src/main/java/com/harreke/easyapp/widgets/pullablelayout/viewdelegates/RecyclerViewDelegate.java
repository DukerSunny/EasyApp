package com.harreke.easyapp.widgets.pullablelayout.viewdelegates;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/15
 */
public class RecyclerViewDelegate implements IViewDelegate {
    private RecyclerView mContent;

    public RecyclerViewDelegate(RecyclerView content) {
        mContent = content;
    }

    @Override
    public boolean isScrollBottom() {
        RecyclerView.LayoutManager manager = mContent.getLayoutManager();
        View child = manager.getChildAt(manager.getChildCount() - 1);

        return child == null ||
                (mContent.getChildPosition(child) == manager.getItemCount() - 1 && child.getBottom() <= mContent.getBottom());
    }

    @Override
    public boolean isScrollTop() {
        RecyclerView.LayoutManager manager = mContent.getLayoutManager();
        View child = manager.getChildAt(0);

        return child == null || (mContent.getChildPosition(child) == 0 && child.getTop() >= mContent.getTop());
    }
}
