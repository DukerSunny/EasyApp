package com.harreke.utils.holders.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class RecyclerHolder<I> extends RecyclerView.ViewHolder implements IRecyclerHolder<I> {
    public RecyclerHolder(View itemView) {
        super(itemView);
    }
}