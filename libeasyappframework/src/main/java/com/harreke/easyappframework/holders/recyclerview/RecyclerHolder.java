package com.harreke.easyappframework.holders.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * RecyclerView的Holder
 *
 * @param <ITEM>
 *         项目类型
 */
public abstract class RecyclerHolder<ITEM> extends RecyclerView.ViewHolder implements IRecyclerHolder<ITEM> {
    public RecyclerHolder(View itemView) {
        super(itemView);
    }
}