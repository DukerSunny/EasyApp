package com.harreke.easyapp.widgets.pullablelayout.viewdelegates;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/15
 */
public class RecyclerViewDelegate implements IViewDelegate {
    private WeakReference<RecyclerView> mRecyclerViewRef;

    public RecyclerViewDelegate(RecyclerView recyclerView) {
        mRecyclerViewRef = new WeakReference<>(recyclerView);
    }

    @Override
    public boolean isScrollBottom() {
        RecyclerView recyclerView;
        RecyclerView.ViewHolder viewHolder;
        View itemView;

        recyclerView = mRecyclerViewRef.get();
        if (recyclerView != null) {
            viewHolder = recyclerView.findViewHolderForAdapterPosition(recyclerView.getLayoutManager().getItemCount() - 1);
            if (viewHolder != null) {
                itemView = viewHolder.itemView;

                return itemView != null && itemView.getBottom() <= recyclerView.getBottom();
            }
        }

        return false;
    }

    @Override
    public boolean isScrollTop() {
        RecyclerView recyclerView;
        RecyclerView.ViewHolder viewHolder;
        View itemView;

        recyclerView = mRecyclerViewRef.get();
        if (recyclerView != null) {
            viewHolder = recyclerView.findViewHolderForAdapterPosition(0);
            if (viewHolder != null) {
                itemView = viewHolder.itemView;

                return itemView != null && itemView.getTop() >= recyclerView.getTop();
            }
        }

        return false;
    }
}